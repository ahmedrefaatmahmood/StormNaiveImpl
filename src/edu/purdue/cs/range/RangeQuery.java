package edu.purdue.cs.range;

import java.util.HashSet;

public class RangeQuery {
	private int queryID; 
	private int xMin, yMin, xMax, yMax;
	
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
	
	public int getXMin() {
		return xMin;
	}
	
	public void setXMin(int xMin) {
		this.xMin = xMin;
	}
	
	public int getYMin() {
		return yMin;
	}
	
	public void setYMin(int yMin) {
		this.yMin = yMin;
	}
	
	public int getXMax() {
		return xMax;
	}
	
	public void setXmax(int xMax) {
		this.xMax = xMax;
	}
	
	public int getYMax() {
		return yMax;
	}
	
	public void setYMax(int yMax) {
		this.yMax = yMax;
	}
	
	public RangeQuery(int id, int xMin, int yMin, int xMax, int yMax) {
		this.currentObjects = new HashSet<Integer>();
		
		this.queryID = id;
	
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	public boolean isInsideRange(int xCoord, int yCoord){
		return (xCoord >= xMin && 
					xCoord <= xMax &&
					yCoord >= yMin &&
					yCoord <= yMax);		
	}
}
