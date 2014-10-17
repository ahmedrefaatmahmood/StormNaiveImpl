package edu.purdue.cs.baseline.range;

import java.util.HashSet;

import edu.cs.purdue.edu.helpers.LocationUpdate;

public class RangeQuery {
	private int queryID; 
	private double xMin, yMin, xMax, yMax;
	
	private HashSet<Integer> currentObjects; 
	
	public boolean objectAlreadyContained(int objectID) {
		return currentObjects.contains(objectID);
	}
	
	public void addObject(int objectID) {
		this.currentObjects.add(objectID);
	}
	
	public void removeObject(int objectID) {
		this.currentObjects.remove(objectID);
	}
	
	public int getQueryID() {
		return queryID;
	}
	
	public void setQueryID(int id) {
		this.queryID = id;
	}
	
	public double getXMin() {
		return xMin;
	}
	
	public void setXMin(int xMin) {
		this.xMin = xMin;
	}
	
	public double getYMin() {
		return yMin;
	}
	
	public void setYMin(int yMin) {
		this.yMin = yMin;
	}
	
	public double getXMax() {
		return xMax;
	}
	
	public void setXmax(int xMax) {
		this.xMax = xMax;
	}
	
	public double getYMax() {
		return yMax;
	}
	
	public void setYMax(int yMax) {
		this.yMax = yMax;
	}
	
	public RangeQuery(int id, double xMin, double yMin, double xMax, double yMax) {
		this.currentObjects = new HashSet<Integer>();
		
		this.queryID = id;
	
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	public boolean isInsideRange(LocationUpdate locationUpdate) {	
		return ((locationUpdate.getNewLocationXCoord() >= this.xMin) && 
					(locationUpdate.getNewLocationXCoord() <= this.xMax) &&
					(locationUpdate.getNewLocationYCoord() >= this.yMin) &&
					(locationUpdate.getNewLocationYCoord() <= this.yMax));
	}
}