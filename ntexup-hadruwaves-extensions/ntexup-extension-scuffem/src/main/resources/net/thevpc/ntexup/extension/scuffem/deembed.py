# De-embedded port: S11 read from the standing wave of the strip current on a feed extension.
#
# Appended after the solver setup, which defines Solver, freqs and the DE_* parameters (mm):
#   DE_X0, DE_X1   strip span across the feed
#   DE_Y0, DE_Y1   fit window along the feed (on the extension, clear of the port)
#   DE_DY          bin height
#   DE_YREF        reference plane (the original port position)
#   DE_EPS, DE_TAND, DE_H  substrate
#
# The edge port's own Z is not used: it carries a port reactance large enough to move the
# null by about 1 GHz on the patch deck. The strip current I(y) on a uniform line is fitted as
# a forward and a backward line wave plus a forward and a backward TM0 surface wave, which a
# thick substrate carries on the strip at 10-40 percent of the line current. Without the
# surface-wave pair the fitted eps_eff drifts to 4.1-4.5 at 7-8 GHz; with it, the stub test
# gives eps_eff within 1 percent of Kirschning-Jansen and the ideal open stub's resonance.
import numpy as np


def read_triangles(path):
    lines = open(path).read().split("\n")
    i = lines.index("$Nodes")
    nodes = {}
    for l in lines[i + 2:i + 2 + int(lines[i + 1])]:
        t = l.split()
        nodes[int(t[0])] = (float(t[1]), float(t[2]))
    i = lines.index("$Elements")
    tris = []
    for l in lines[i + 2:i + 2 + int(lines[i + 1])]:
        t = l.split()
        if t[1] == "2":
            nt = int(t[2])
            tris.append([nodes[int(v)] for v in t[3 + nt:6 + nt]])
    return tris


def clip_area(tri, ya, yb):
    """Area of the part of a triangle inside ya <= y <= yb."""
    def clip(poly, yc, above):
        out = []
        for k in range(len(poly)):
            p, q = poly[k], poly[(k + 1) % len(poly)]
            ip = p[1] >= yc if above else p[1] <= yc
            iq = q[1] >= yc if above else q[1] <= yc
            if ip:
                out.append(p)
            if ip != iq:
                t = (yc - p[1]) / (q[1] - p[1])
                out.append((p[0] + t * (q[0] - p[0]), yc))
        return out
    poly = clip(clip(list(tri), ya, True), yb, False)
    if len(poly) < 3:
        return 0.0
    return 0.5 * abs(sum(poly[k][0] * poly[(k + 1) % len(poly)][1] - poly[(k + 1) % len(poly)][0] * poly[k][1]
                         for k in range(len(poly))))


def tm0_beta(k0, er, h):
    """TM0 surface-wave propagation constant of the grounded slab, by bisection."""
    lo, hi = k0 * (1 + 1e-9), k0 * np.sqrt(er) * (1 - 1e-9)
    def F(b):
        kd = np.sqrt(er * k0 * k0 - b * b)
        return er * np.sqrt(b * b - k0 * k0) - kd * np.tan(kd * h)
    for _ in range(200):
        m = 0.5 * (lo + hi)
        if F(lo) * F(m) <= 0:
            hi = m
        else:
            lo = m
    return 0.5 * (lo + hi)


def bin_avg(g):
    # average of exp(g y) over a bin, relative to its value at the bin centre
    x = g * DE_DY / 2
    return np.sinh(x) / x


TRIS = read_triangles("/sim/mesh.msh")
TRI_CEN = np.array([[sum(v[0] for v in t) / 3, sum(v[1] for v in t) / 3] for t in TRIS])
EDGES = np.arange(DE_Y0, DE_Y1 + 1e-9, DE_DY)
YM = 0.5 * (EDGES[:-1] + EDGES[1:])
# a microstrip's eps_eff lies between (eps+1)/2 and eps; starting at (eps+1)/2 also keeps the
# line wave clear of the surface wave, which is slower than light by only a few percent
SE_GRID = np.linspace(np.sqrt((DE_EPS + 1) / 2), np.sqrt(DE_EPS), 1201)
WEIGHTS = None


def bin_weights(psd):
    """Per-panel share of each bin: the part of the panel inside the bin over the bin height."""
    w = np.zeros((psd.NR, len(YM)))
    for p in range(psd.NR):
        x, y = psd.GetEntry(p, 0).real, psd.GetEntry(p, 1).real
        if not (DE_X0 < x < DE_X1):
            continue
        tri = TRIS[int(np.argmin(((TRI_CEN - (x, y)) ** 2).sum(1)))]
        ys = [v[1] for v in tri]
        for k in range(len(YM)):
            if max(ys) > EDGES[k] and min(ys) < EDGES[k + 1]:
                w[p, k] = clip_area(tri, EDGES[k], EDGES[k + 1]) / DE_DY
    return w


def deembedded_s11(f_ghz):
    """Returns (S11, eps_eff, residual), S11 in exp(+jwt) at DE_YREF, referred to the feed line."""
    global WEIGHTS
    Solver.Solve(0, 1.0)
    psd = Solver.GetPanelSourceDensities()
    if WEIGHTS is None:
        WEIGHTS = bin_weights(psd)
    ky = np.array([psd.GetEntry(p, 6) for p in range(psd.NR)])
    I = ky @ WEIGHTS
    k0 = 2 * np.pi * f_ghz / 300.0
    bs = tm0_beta(k0, DE_EPS, DE_H)
    sw = [np.exp(1j * bs * YM) * bin_avg(1j * bs), np.exp(-1j * bs * YM) * bin_avg(-1j * bs)]
    best = None
    for se in SE_GRID:
        e = se * se
        # dielectric loss of a quasi-TEM line; the trace is a perfect conductor in scuff
        al = k0 * DE_EPS * (e - 1) / (se * (DE_EPS - 1)) * DE_TAND / 2
        g = 1j * k0 * se - al  # scuff runs exp(-iwt): exp(+i beta y) travels towards +y
        M = np.stack([np.exp(g * YM) * bin_avg(g), np.exp(-g * YM) * bin_avg(-g)] + sw, 1)
        c, *_ = np.linalg.lstsq(M, I, rcond=None)
        r = np.linalg.norm(M @ c - I) / np.linalg.norm(I)
        if best is None or r < best[0]:
            best = (r, e, g, c)
    r, e, g, c = best
    # current reflection is minus the voltage reflection; conj takes exp(-iwt) to exp(+jwt)
    gamma = np.conj(-c[1] * np.exp(-g * DE_YREF) / (c[0] * np.exp(g * DE_YREF)))
    return complex(gamma), e, r


with open("/sim/results.dat", "w") as f:
    for freq in freqs:
        f_ghz = freq / 1e9
        Solver.AssembleSystemMatrix(f_ghz * 2.0 * math.pi / 300.0)
        s11, eps_eff, resid = deembedded_s11(f_ghz)
        z11 = 50.0 * (1 + s11) / (1 - s11)
        f.write(f"{freq:.6e} {s11.real:.8e} {s11.imag:.8e} {z11.real:.8e} {z11.imag:.8e} {eps_eff:.5f} {resid:.5f}\n")
        f.flush()
