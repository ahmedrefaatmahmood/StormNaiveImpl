package edu.cs.purdue.edu.helpers;

public class Constants {
	public static final int generatorSeed =1000;
	public static final int dataGeneratorDelay =0; //setting this to zero to achieve the highest data rate possible
	public static final int queryGeneratorDelay =0; //setting this to zero to achieve to send queries to bolts as soon as possible
	
	public static final int numQueries = 100;	
	public static final int numMovingObjects = 100;
	
	public static final int xMaxRange =10000;
	public static final int yMaxRange =10000;
	
	public static final int maxK = 10;  // for kNN queries.
	
	public static final int queryMaxWidth =100;
	public static final int queryMaxHeight =100;
	
	// Object's fields
	public static final String objectIdField = "objectID";
	public static final String objectXCoordField = "xCoord";
	public static final String objectYCoordField = "yCoord";

	public static final String queryIdField = "queryID";
	// Range Query fields
	public static final String queryXMinField = "xMin";
	public static final String queryYMinField = "yMin";
	public static final String queryXMaxField = "xMax";
	public static final String queryYMaxField = "yMax";
	
	// kNN Query fields
	public static final String focalXCoordField = "focalXCoord";
	public static final String focalYCoordField = "focalYCoord";
	public static final String kField = "k";
	
	// Topology constants
	public static final String objectLocationGenerator = "object-location-generator";
	public static final String queryGenerator = "query-generator";
	public static final String rangeFilterBolt = "range-filter";
	public static final String kNNFilterBolt = "kNN-filter";
	
	// Cluster constants
	public static final String mcMachinesNimbus = "mc07.cs.purdue.edu";
	public static final String mcMachinesUI = "mc07.cs.purdue.edu";
	public static final String localHostNimbus = "127.0.0.1";
	public static final int dataSpoutParallelism = 100;
	public static final int querySpoutParallelism = 1;
	public static final int boltParallelism = 20;
	public static final int numberOfWorkers = 10;
}