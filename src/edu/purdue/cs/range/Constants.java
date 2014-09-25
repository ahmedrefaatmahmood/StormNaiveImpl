package edu.purdue.cs.range;

public class Constants {
	public static final int generatorSeed =1000;
	public static final int dataGeneratorDelay =1;
	public static final int queryGeneratorDelay =1000;
	
	public static final int numQueries = 100;	
	public static final int numMovingObjects = 100;
	
	public static final int xMaxRange =10000;
	public static final int yMaxRange =10000;
	
	public static final int queryMaxWidth =100;
	public static final int queryMaxHeight =100;
	
	// Object's fields
	public static final String objectIdField = "objectID";
	public static final String objectXCoordField = "xCoord";
	public static final String objectYCoordField = "yCoord";
	
	// Queries' fields
	public static final String queryIdField = "queryID";
	public static final String queryXMinField = "xMin";
	public static final String queryYMinField = "yMin";
	public static final String queryXMaxField = "xMax";
	public static final String queryYMaxField = "yMax";
	
	// Topology constants:
	public static final String objectLocationGenerator = "object-location-generator";
	public static final String queryGenerator = "query-generator";
	public static final String rangeFilterBolt = "range-filter";
}
