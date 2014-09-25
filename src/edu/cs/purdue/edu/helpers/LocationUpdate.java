package edu.cs.purdue.edu.helpers;

public class LocationUpdate {

	private int objectId;
	private int newLocationXCoord;
	private int newLocationYCoord;
	
	public LocationUpdate(int objectId, int newLocationXCoord, int newLocationYCoord) {
		this.objectId = objectId;
		this.newLocationXCoord = newLocationXCoord;
		this.newLocationYCoord = newLocationYCoord;
	}
	
	public int getObjectId() {
		return this.objectId;
	}
	
	public int getNewLocationXCoord() {
		return this.newLocationXCoord;
	}
	
	public int getNewLocationYCoord() {
		return this.newLocationYCoord;
	}
	
	public void setObjectID(int id) {
		this.objectId = id;
	}
	
	public void setNewLocationXCoord(int newLocationXCoord) {
		this.newLocationXCoord = newLocationXCoord;
	}
	
	public void setNewLocationYCoord(int newLocationYCoord) {
		this.newLocationYCoord = newLocationYCoord;
	}
}
