package edu.cs.purdue.edu.helpers;

public class Constants {
	public static final Integer generatorSeed = 1000;
	public static final Integer dataGeneratorDelay = 10; // setting this to zero to
													// achieve the highest data
													// rate possible
	public static final Integer queryGeneratorDelay = 0; // setting this to zero to
														// achieve to send
														// queries to bolts as
														// soon as possible

	public static final Integer numQueries = 1000;
	public static final Integer numMovingObjects = 100;

	public static final Double xMaxRange = 10000.0;
	public static final Double yMaxRange = 10000.0;

	public static final Integer maxK = 10; // for kNN queries.

	public static final Double queryMaxWidth = 100.0;
	public static final Double queryMaxHeight = 100.0;

	// Object's fields
	public static final String objectIdField = "objectID";
	public static final String objectXCoordField = "xCoord";
	public static final String objectYCoordField = "yCoord";
	public static final String objectTextField = "textContent";
	public static final String incrementalState = "incrementalState";
	public static final String timeStamp = "timeStamp";

	public static final String queryIdField = "queryID";

	// Text Query field
	public static final String queryTextField = "queryText";

	// Range Query fields
	public static final String queryXMinField = "xMin";
	public static final String queryYMinField = "yMin";
	public static final String queryXMaxField = "xMax";
	public static final String queryYMaxField = "yMax";

	// kNN Query fields
	public static final String focalXCoordField = "focalXCoord";
	public static final String focalYCoordField = "focalYCoord";
	public static final String kField = "k";
	public static final String updateStatus = "updateStatus";

	// Topology constants
	public static final String objectLocationGenerator = "object-location-generator";
	public static final String queryGenerator = "query-generator";
	public static final String rangeFilterBolt = "range-filter";
	public static final String kNNFilterBolt = "kNN-filter";
	public static final String dummyTweetGenerator = "dummy-tweet-generator";
	public static final String dummyTextQueryGenerator = "dummy-text-query-generator";
	public static final String textFilterBolt = "text-filter-bolt";
	public static final String tweeterPairIdentifierBolt = "tweeter-pair-identifier-bolt";
	
	
	// Grid Constants
	public static final Integer xCellsNum =5;
	public static final Integer yCellsNum =5;
	public static final Integer numberOfBolts = xCellsNum * yCellsNum;

	
	// Cluster constants
	public static final String mcMachinesNimbus = "mc07.cs.purdue.edu";
	public static final String mcMachinesUI = "mc07.cs.purdue.edu";
	public static final String localHostNimbus = "127.0.0.1";
	public static final Integer dataSpoutParallelism =1;
	public static final Integer dataSpoutExecuters = 1;
	public static final Integer querySpoutParallelism = 1;
	public static final Integer boltParallelism =numberOfBolts;
	public static final Integer numberOfWorkers = 10;

	
	
	
	
	
	//Simulation constansts
	public static final Integer minutesToGetSats = 4;
	public static final boolean debug= true;
}