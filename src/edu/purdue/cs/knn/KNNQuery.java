package edu.purdue.cs.knn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import edu.cs.purdue.edu.helpers.LocationUpdate;

public class KNNQuery {
	private int queryID;
	private double focalXCoord, focalYCoord;
	private int k;
	
	public void reset() {
		Comparator<LocationUpdate> maxHeap = new MaxHeap(focalXCoord, focalYCoord);
		this.kNNQueue = new PriorityQueue<LocationUpdate>(50, maxHeap);
		this.currentRanks = new HashMap<Integer, Integer>();
	}
	
	public PriorityQueue<LocationUpdate> kNNQueue;  // Priority queue (max-heap).
	HashMap<Integer, Integer> currentRanks;  // Records the current rank of each object in the top-k list.
		
	// Retrieves the distance of the farthest object in the current top-k.
	public double getFarthestDistance() {
		LocationUpdate farthest = kNNQueue.peek();
		return Math.sqrt(Math.pow((farthest.getNewLocationXCoord() - this.focalXCoord), 2) +
				                  Math.pow((farthest.getNewLocationYCoord() - this.focalYCoord), 2));
	}
	
	public int getK() {
		return this.k;
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public int getQueryID() {
		return this.queryID;
	}
	
	public void setQueryID(int id) {
		this.queryID = id;
	}
	
	public double getFocalXCoord() {
		return this.focalXCoord;
	}
	
	public void setFocalXCoord(int focalXCoord) {
		this.focalXCoord = focalXCoord;
	}
	
	public double getFocalYCoord() {
		return this.focalYCoord;
	}
	
	public void setFocalYCoord(int focalYCoord) {
		this.focalYCoord = focalYCoord;
	}
	
	public KNNQuery(Integer id, Double focalXCoord, Double focalYCoord, Integer k) {
		Comparator<LocationUpdate> maxHeap = new MaxHeap(focalXCoord, focalYCoord);
		this.kNNQueue = new PriorityQueue<LocationUpdate>(50, maxHeap);
		this.currentRanks = new HashMap<Integer, Integer>();
		
		this.queryID = id;
	
		this.focalXCoord = focalXCoord;
		this.focalYCoord = focalYCoord;
		this.k = k;
	}
	
	// Returns a representation of the changes in the top-k list (if any).
	public ArrayList<String> processLocationUpdate(LocationUpdate incomingUpdate) {		
		boolean topkMayHaveChanged = false;
		// If the new location update corresponds to an object that is already in the top-k list.
		if (this.currentRanks.containsKey(incomingUpdate.getObjectId())) {
			topkMayHaveChanged = true;
			LocationUpdate toBeUpdatedInHeap = null;
			// Locate that object in the priority queue.
			for (LocationUpdate l : kNNQueue) {
				if (l.getObjectId() == incomingUpdate.getObjectId()) {
					toBeUpdatedInHeap = l;
				}
			}
			// Heapify.
			kNNQueue.remove(toBeUpdatedInHeap);			
			kNNQueue.add(incomingUpdate);
		} else {
			// If the current list is small, i.e., has less than k objects, take that object anyway and add it to the topk list.
			if (currentRanks.size() < this.k) {
				topkMayHaveChanged = true;
				this.kNNQueue.add(incomingUpdate);				
			} else {
				// Calculate the distance corresponding to new location.
				double distanceOfObject = Math.sqrt(Math.pow((incomingUpdate.getNewLocationXCoord() - this.focalXCoord), 2) +
						  								   Math.pow((incomingUpdate.getNewLocationYCoord() - this.focalYCoord), 2));
				// New location is closer than the current farthest.
				if (this.getFarthestDistance() > distanceOfObject) {
					topkMayHaveChanged = true;
					// Remove the farthest.
					this.kNNQueue.remove();
					// Add the new object.
					this.kNNQueue.add(incomingUpdate);
				}
			}
		}
		
		if (topkMayHaveChanged)
			return getTopkUpdates();
		else
			return null;
	}
	
	// Returns a string representation of the updates that happened to the top-k list, e.g., removal of an object, addition of
	// an object, or the change of a rank of an object
	private ArrayList<String> getTopkUpdates() {
		// Calculate the new rank of each object in the top-k list.
		HashMap<Integer, Integer> newRanks = new HashMap<Integer, Integer>();
		Comparator<LocationUpdate> maxHeap = new MaxHeap(focalXCoord, focalYCoord);
		PriorityQueue<LocationUpdate> temp = new PriorityQueue<LocationUpdate>(50, maxHeap);
		int rank = 1;		
		while (!this.kNNQueue.isEmpty()) {
			LocationUpdate l = this.kNNQueue.remove();
			temp.add(l);
			newRanks.put(l.getObjectId(), rank);
			rank++;
		}
		this.kNNQueue = temp;

		// Compute the change list.
		ArrayList<String> changes = new ArrayList<String>();
		// Compare the new ranks with the existing (i.e., old) ranks.
		for (Integer objectId : newRanks.keySet()) {
			if (!this.currentRanks.containsKey(objectId)) {
				changes.add("+ Object " + objectId + " with Rank " + newRanks.get(objectId) +" for Query " + this.queryID );
			} else if (this.currentRanks.get(objectId) != newRanks.get(objectId)) {
				changes.add("U Object " + objectId + " with Rank " + newRanks.get(objectId) +" for Query " + this.queryID );
			}
		}
		for (Integer objectId : this.currentRanks.keySet()) {
			if (!newRanks.containsKey(objectId)) {
				changes.add("- Object " + objectId +" for Query " + this.queryID );
			}
		}
		
		// Finally, update the current ranks to reflect the new ranks.
		this.currentRanks = newRanks;
		// Return the change list as a string. Maybe we need to change this later to something more solid.
		return changes;
	}
	
	// This is an internal class that is used to order the objects according to the Euclidean distance using a priority queue.
	public static class MaxHeap implements Comparator<LocationUpdate>{
		private double focalXCoord, focalYCoord;
		
		public MaxHeap(double focalXCoord, double focalYCoord) {
			this.focalXCoord = focalXCoord;
			this.focalYCoord = focalYCoord;
		}
		
		@Override
		public int compare(LocationUpdate a, LocationUpdate b) {
			double distanceOfA = Math.sqrt(Math.pow((a.getNewLocationXCoord() - this.focalXCoord), 2) +
															  Math.pow((a.getNewLocationYCoord() - this.focalYCoord), 2));
			
			double distanceOfB = Math.sqrt(Math.pow((b.getNewLocationXCoord() - this.focalXCoord), 2) +
															  Math.pow((b.getNewLocationYCoord() - this.focalYCoord), 2));
			
			if (distanceOfA < distanceOfB)
				return 1;
			if (distanceOfA > distanceOfB)
				return -1;

			return 0;
		}
	}
}
