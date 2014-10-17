package edu.purdue.cs.baseline.tweetspair;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import edu.cs.purdue.edu.helpers.Constants;
import edu.cs.purdue.edu.helpers.KillTopology;
import edu.purdue.cs.baseline.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.baseline.range.bolt.NonIncrementalRangeFilter;
import edu.purdue.cs.baseline.tweetspair.blot.TweetsPairSpatialAndTemporalCombiner;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.performance.ClusterInformationExtractor;

public class TweetsPairTopologySubmitter {





	public static void main(String[] args) throws Exception {
         

	
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.dummyTweetGenerator, new ObjectLocationGenerator());
		builder.setBolt(Constants.tweeterPairIdentifierBolt,
							  new TweetsPairSpatialAndTemporalCombiner(100,100)).fieldsGrouping(Constants.dummyTweetGenerator,new Fields(Constants.objectTextField));
		String topologyName ="Tweets-pair-queries_toplogy";
		System.out.println("******************************************************************************************************");
		System.out.println(topologyName);
		Config conf = new Config();
		conf.setDebug(false);
        //Topology run
		conf.setNumWorkers(Constants.numberOfWorkers);
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS,0);
		conf.put(Config.NIMBUS_HOST, Constants.mcMachinesNimbus);
		System.setProperty("storm.jar", "target/StormTestNaieve-0.0.1-SNAPSHOT.jar");
		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
		Thread.sleep(1000 * 60 * Constants.minutesToGetSats);
		ClusterInformationExtractor.main(null);
		KillTopology.killToplogy(topologyName, Constants.mcMachinesNimbus);
		System.out.println("******************************************************************************************************");
	}
}
