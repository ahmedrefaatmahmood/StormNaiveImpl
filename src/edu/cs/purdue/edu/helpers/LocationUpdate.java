package edu.cs.purdue.edu.helpers;

public class LocationUpdate {

	private int objectId;
	private double newLocationXCoord;
	private double newLocationYCoord;
	
	public LocationUpdate(int objectId, double newLocationXCoord, double newLocationYCoord) {
		this.objectId = objectId;
		this.newLocationXCoord = newLocationXCoord;
		this.newLocationYCoord = newLocationYCoord;
	}
	
	public int getObjectId() {
		return this.objectId;
	}
	
	public double getNewLocationXCoord() {
		return this.newLocationXCoord;
	}
	
	public double getNewLocationYCoord() {
		return this.newLocationYCoord;
	}
	
	public void setObjectID(int id) {
		this.objectId = id;
	}
	
	public void setNewLocationXCoord(double newLocationXCoord) {
		this.newLocationXCoord = newLocationXCoord;
	}
	
	public void setNewLocationYCoord(double newLocationYCoord) {
		this.newLocationYCoord = newLocationYCoord;
	}
}
