package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class PrimitiveElement3DRuler extends PrimitiveElement3DArrow {

	/*Ruler's Values*/
	private double minValue,maxValue;
	/*offset to be left before ticks are drown*/
	private double offset=0;
	/*If values are to be displayed*/
	private boolean drawValues=true;
	
	private PrimitiveElement3DRuler parent;
	/*Ruler's Divisions*/
	private int divisions=1,subdivisions=0;
	/**Vector along which tricks are to be drawn
	 * choose null if ticks are always to be displyed perpendicular to the ruler*/
	private Vector3D tickDir;
	public PrimitiveElement3DRuler(Vector3D p1, Vector3D p2, Vector3D tickDir, double minValue, double maxValue, int divisions, int subdivisions) {
		super(p1, p2);
		this.setTickDir(tickDir);
		this.setDivisions(divisions);
		this.setSubdivisions(Math.max(1,subdivisions));
		this.minValue=minValue;
		this.maxValue=maxValue;
		parent=null;
	}

	public PrimitiveElement3DRuler(Vector3D p1, Vector3D p2, PrimitiveElement3DRuler parent) {
		super(p1, p2);
     	setParent(parent);
	}
	/*
	public boolean getSplitScale(Vector3D p,PrimitiveElement3DRuler e1,PrimitiveElement3DRuler e2){
		Vector3D v1,v2;
		v1=new Vector3D(p,p1);
		v2=new Vector3D(p,p2);
		
		//if p1 lies beyong p1 and p2 scale is not split
		if (Vector3D.calculateAngle(v1,v2)>=0)return false;
		div=divisions(1-v1.getLength())
		e1=new PrimitiveElement3DRuler(p1,p,tickDir,0,0,0,0);
		
		return false;
		
	}
	*/
	public int getDivisions() {
		return divisions;
	}

	public void setDivisions(int divisions) {
		this.divisions = Math.max(1,divisions);
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = Math.max(1,subdivisions);
	}
	
	/** may return null if defualt ticking is enabled*/
	public Vector3D getTickDir() {
		return tickDir;
	}

	public void setTickDir(Vector3D tickDir) {
		this.tickDir = tickDir;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isValuesShown() {
		return drawValues;
	}

	public void setValuesShown(boolean drawValues) {
		this.drawValues = drawValues;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

	public PrimitiveElement3DRuler getParent() {
		return parent;
	}

	public void setParent(PrimitiveElement3DRuler parent) {
		this.parent = parent;
		this.setTickDir(parent.tickDir);
		this.setDivisions(parent.divisions);
		this.setSubdivisions(Math.max(1,parent.subdivisions));
		this.minValue=parent.minValue;
		this.maxValue=parent.maxValue;
	}
	
}
