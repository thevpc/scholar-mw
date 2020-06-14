package net.vpc.scholar.hadruplot.libraries.calc3d.engine3d;

import java.util.ArrayList;
import java.util.Collection;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Object3D;

public class Scene3D {
	  public Collection<Object3D<PrimitiveElement3D>> object3Ds = new ArrayList<Object3D<PrimitiveElement3D>>();
	  
	  public void addObject3D(Object3D<PrimitiveElement3D> obj) {
	        this.object3Ds.add(obj);
	    }
	
}
