# ntexup-extension-antennas — interactive antenna-array components (port plan)

Ports the Swing panels of the *antenna-array-simulator* app
(`FastGraphicalArrayFactorPanel`, `RadiationPatternPanel`, `WaveFieldPanel` and the shared
`AntennaArrayModel`) into reusable, fully-parametrizable ntexup components.
No Swing/AWT widget panels: everything is drawn with `NTxGraphics` (AWT `Graphics2D` wrapper).

## Components (all registered in `META-INF/services/...NTxNodeBuilder`)

| node id | ported from | what it draws |
|---|---|---|
| `array-factor` | `FastGraphicalArrayFactorPanel` | nomogram "Facteur de réseau graphique rapide": Fr(2γ) curve, zeros, semi-circle (radius βd, centre α), visibility zone, vertical projections, polar pattern inside the circle, animated scanning probe |
| `radiation-pattern` | `RadiationPatternPanel` + `AntennaArrayModel` | polar far-field pattern \|AF(θ)\| in linear or dB scale, main-beam direction, HPBW arc, theoretical null markers |
| `wave-field` | `WaveFieldPanel` + `AntennaArrayModel` | 2D near/mid-field interference raster (instantaneous E(t) or RMS intensity), wavefront crest rings, beam/null rays, element markers, overlays |
| `antenna-array` | `AntennaArrayModel` (geometry only) | schematic top view of the array axis: element markers sized by excitation amplitude, index labels, axis |
| `field-colorbar` | `WaveFieldPanel` colormaps | stand-alone legend: color ramp (configurable low/high colors + power gamma) with 0 / max / mid ticks |

## Physics (shared, pure Java, **no** ntexup-api dependency → unit-testable)

Package `net.thevpc.ntexup.extension.antennas.phys`:

- `LinearArrayModel` — port of `AntennaArrayModel` (ψ = kd·sinθ + β, |Σ aₙ e^{j(nψ+φₙ)}|/Σaₙ,
  dB floor, analytic uniform nulls at 2mπ/N, numeric nulls for tapered excitation,
  approximate HPBW 0.886λ/(Nd)/cosθ₀ for uniform, numeric otherwise; β ↔ steering sync;
  same clamps N∈[1..16], d/λ∈[0.1..2.0]).
- `ArrayTapers` — generators: `uniform`, `binomial`, `chebyshev` (Dolph, `sll` dB),
  `triangular`, `hann`, `hamming`, `blackman`. (No full Taylor synthesis; noted as a
  limitation — Dolph-Chebyshev is included instead.)
- `ElementPattern` — `isotropic`, `dipole` (half-wave, axis ∥ array), `cardioid`.
  Applied to the angular/far-field components (`radiation-pattern`, `wave-field`);
  **not** to the pure `array-factor` nomogram (it has no physical element pattern).
- `ArrayFactorMath` — Fr(2γ) = |sin(Nγ)/(N·sinγ)| and its N−1 zeros (nomogram math).
- `WaveFieldPhasorGrid` — per-pixel phasor sums A=Σaₙcos(φₙ), B=Σaₙsin(φₙ) (frame-invariant,
  cached): instantaneous E(t) = A·cos(ωt) − B·sin(ωt) and RMS intensity = A²+B² are cheap
  per frame. Includes the per-element geometric decay 1/√(r+0.35) and element pattern.
- `ColorRamp` — two-color gradient LUT with power gamma (reproduces the original
  instantaneous / intensity colormaps via properties).

## Shared property conventions

- `theme`: `dark` (default, original colors) | `light`. Every color is a property whose
  default is chosen from the theme; no hard-coded colors in rendering code.
- `n` (1..16, default per component), `d-lambda`, `lambda`, `frequency`, `steering` (°),
  `beta` (°, overrides steering when present), `taper`, `chebyshev-sll` (dB), `amplitude`
  (explicit list; shorter than N pads with 1.0, longer truncates), `phase` (per-element,
  °).
- Print mode: deterministic, driven by explicit static properties (`psi`, `time`, …).
- Live mode: activated only when the renderer has the `animate` capability AND the node's
  `animate` flag is true (default true); one timer per node stored in the render cache,
  stopped when no longer animating → no timer/thread leak.
- Invalid input is clamped (matching the source model) or produces a clear label/error,
  never a crash or blank panel.

## Unmapped source behavior (with reason)

| original | dropped / changed | reason |
|---|---|---|
| mouse scrubbing of ψ / steering | replaced by `psi` property (print) + animation (live) | no pointer input in ntexup; made a parameter |
| Swing control bar (sliders/checkbox/presets) | property set + `theme` | ntexup documents are declarative |
| `intensity-mode` toggle | `mode: instant \| intensity` property | same feature, declarative |
| running/pause, timeSpeed | `animate` flag + `speed` factor | print mode must be time-deterministic |
| per-element hue in wavefront rings | `ring-color-*` properties | let users pick colors |
| Taylor taper synthesis | Dolph-Chebyshev instead | full synthesis needs a separate roots solver; Chebyshev is the classic equal-ripple alternative |

## Testing (project convention: plain Java classes with `main()`, no JUnit)

- `AntennaPhysicsTest` — broadside uniform peak, end-fire steering, N=2 half-λ nulls at ±90°,
  grating lobes for d>λ, Fr zeros, binomial taper values, Chebyshev window sanity, wave-field
  intensity normalization.
- `AntennasRegistrationTest` — loads a `main()`-rendered `.ntx` exercising all 5 components
  with static print properties; walks `doc.pages()` (forces lazy page compilation) and asserts
  load success.