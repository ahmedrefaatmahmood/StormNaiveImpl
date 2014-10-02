package edu.purdue.cs.streetquery;


import edu.purdue.cs.streetquery.bolts.StreetMovingObjectQueryBolt;
import edu.purdue.cs.streetquery.bolts.StreetQueryBolt;
import edu.purdue.cs.streetquery.spouts.StreetMovingObjectQuerySpout;
import edu.purdue.cs.streetquery.spouts.StreetQuerySpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class StreetMovingObjectQueryDriver 
{
	public static void main(String[] args) throws Exception{
		Config config = new Config();
		config.put("queryFile", args[0]);
		config.put("dataFile", args[1]);
		config.setDebug(false);
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("query-moving-reader-spout", new StreetMovingObjectQuerySpout());
		builder.setBolt("street-moving-query-bolt", new StreetMovingObjectQueryBolt()).shuffleGrouping("query-moving-reader-spout");
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("StreetMovingObjectQueryTopology", config, builder.createTopology());
		Thread.sleep(10000);
		
		cluster.shutdown();
	}
}
