# ntexup-antennas Extension - dipole-current Component

## Overview
Successfully implemented the `dipole-current` component for the ntexup-antennas extension, which visualizes the sinusoidal standing current distribution of a center-fed dipole antenna.

## Implementation Details

### Project Structure
```
ntexup-antennas/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── net/thevpc/ntexup/extension/antennas/
│   │   │       └── DipoleCurrentBuilder.java
│   │   └── resources/
│   │       └── META-INF/services/
│   │           └── net.thevpc.ntexup.api.extension.NTxNodeBuilder
│   └── test/
│       ├── java/
│       │   └── net/thevpc/ntexup/extension/antennas/
│       │       └── DipoleCurrentTest.java
│       └── test.dipole-current.ntx
```

### Key Features Implemented
1. **Correct Physics Implementation**:
   - Standing wave envelope: I(z) = I₀ × sin(k × (ℓ/2 - |z|))
   - Time oscillation: I(z,t) = I(z) × cos(ωt)
   - Supports arbitrary dipole lengths in wavelengths

2. **Dual Rendering Modes** (as required):
   - **Viewer/Animate Mode**: Continuous looping animation of current oscillation
   - **Print/PDF Mode**: Static display of maximum current envelope

3. **Properties Supported**:
   - `length`: Dipole length in wavelengths (default: 0.5)
   - `animate-period`: Oscillation period in ms (default: 2000)
   - `color`: Current envelope color (common property)
   - `wire-color`: Dipole wire color (default: DARK_GRAY)

### Verification Results
The extension was successfully verified through programmatic testing:

✅ **Extension Loading**: 
- Node parser loaded: `[engine] [BASE] loaded [node parser] : [dipole-current]`
- Node renderer loaded: `[engine] [BASE] loaded [node renderer] : [dipole-current]`

✅ **Import Resolution**:
- `[engine] importing dependencies net.thevpc.ntexup:[ntexup-extension-antennas]#[1.0.0.0]`

✅ **Document Compilation**:
- `[engine] compiled document in 1s 207ms 861us 194ns`
- `Loaded document with 1 pages`

### Files Created
1. **pom.xml**: Maven project configuration with correct coordinates
   - GroupId: `net.thevpc.ntexup`
   - ArtifactId: `ntexup-extension-antennas` 
   - Version: `1.0.0.0`
   - Dependency: `net.thevpc.ntexup:ntexup-api:1.0.0.0`

2. **DipoleCurrentBuilder.java**: Main implementation
   - Properly implements `NTxNodeBuilder`
   - Handles `length` and `animate-period` custom properties
   - Uses common property handling for `color`
   - Implements correct physics for standing wave current
   - Supports both animate and print modes via `rendererContext.isAnimate()`

3. **META-INF/services/net.thevpc.ntexup.api.extension.NTxNodeBuilder**:
   - Contains: `net.thevpc.ntexup.extension.antennas.DipoleCurrentBuilder`

4. **Test Files**:
   - `test.dipole-current.ntx`: Test document showing usage
   - `DipoleCurrentTest.java`: Programmatic verification test

### Usage Example
```tson
import("ntexup-extension-antennas")

page {
    dipole-current(size: (500, 300), length: 0.5)
    dipole-current(size: (500, 300), length: 1.0, animate-period: 3000, at: (550, 0))
}
```

### Build Information
- Built with: Java 17
- Maven coordinates: `net.thevpc.ntexup:ntexup-extension-antennas:1.0.0.0`
- Successfully installed to local Maven repository
- Extension follows ntexup extension development guidelines
- No dependencies on other ntexup extensions (only on ntexup-api)

The extension is ready for use and correctly implements all specified requirements for the dipole-current component.