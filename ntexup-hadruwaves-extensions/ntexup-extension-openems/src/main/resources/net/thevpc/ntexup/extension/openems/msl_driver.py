# openEMS's own microstrip port, driven by the ntexup OpenEMS extension. Nothing here models the
# antenna: geometry and mesh come from geometry.xml, written by the extension from the ntx scene;
# the port, its excitation and S11 are openEMS's (AddMSLPort, CalcPort). Parameters: driver.json.
# Writes s11.dat: f, S11 referred to the reference plane and normalized to ref_impedance, and the
# line impedance openEMS reads at its measurement plane.
import json, sys, numpy as np
from CSXCAD import ContinuousStructure
from openEMS import openEMS

p = json.load(open('driver.json'))
CSX = ContinuousStructure()
CSX.ReadFromXML('geometry.xml')
FDTD = openEMS(NrTS=p['timesteps'], EndCriteria=p['endcriteria'])
FDTD.SetGaussExcite(p['f0'], p['fc'])
FDTD.SetBoundaryCond(p['boundaries'])
FDTD.SetCSX(CSX)

# the strip enters from the domain edge, runs through the PML and ends at the reference plane
axis = 'xyz'.index(p['prop_dir'])
lines = np.sort(np.array(CSX.GetGrid().GetLines(p['prop_dir'])))
sign, ref = p['sign'], p['reference']
edge = lines[0] if sign > 0 else lines[-1]
inner = lines if sign > 0 else lines[::-1]
feed_shift = abs(inner[p.get('feed_cell', 12)] - edge)   # excitation 4 cells clear of the (8-cell) PML
port_len = abs(ref - edge)
meas_shift = port_len - p['measure_before']
if meas_shift <= feed_shift:
    sys.exit(f'msl port: measurement plane ({meas_shift:.2f} mm from the edge) must lie past the excitation ({feed_shift:.2f} mm)')
start, stop = [0.0, 0.0, 0.0], [0.0, 0.0, 0.0]
cross = 1 - axis if axis < 2 else 0
start[axis], stop[axis] = edge, ref
start[cross], stop[cross] = p['w1'], p['w2']
start[2], stop[2] = p['z_strip'], p['z_ground']
# the port strip gets its own PEC property: properties read from XML cannot take new primitives in python
metal = CSX.AddMetal('port_strip')
port = FDTD.AddMSLPort(1, metal, start, stop, p['prop_dir'], p['exc_dir'], excite=-1,
                       FeedShift=feed_shift, MeasPlaneShift=meas_shift, priority=10)
print(f'msl port: {start} -> {stop}, feed shift {feed_shift:.3f}, measurement plane shift {meas_shift:.3f}')

if '--no-run' not in sys.argv:
    FDTD.Run('.', cleanup=False, numThreads=p['threads'], verbose=1)

f = np.linspace(p['f_min'], p['f_max'], p['f_count'])
port.ReadUIData('.', f)
z_line = np.array(port.Z_ref)   # read before CalcPort, which replaces Z_ref with ref_impedance
port.CalcPort('.', f, ref_impedance=p['ref_impedance'], ref_plane_shift=port_len)
s11 = port.uf_ref / port.uf_inc
with open('s11.dat', 'w') as out:
    out.write('# f_Hz re_S11 im_S11 re_Zline im_Zline\n')
    for k in range(len(f)):
        out.write(f'{f[k]:.6e} {s11[k].real:.10e} {s11[k].imag:.10e} {z_line[k].real:.6f} {z_line[k].imag:.6f}\n')
