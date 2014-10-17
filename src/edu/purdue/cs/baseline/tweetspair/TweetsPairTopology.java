package edu.purdue.cs.baseline.tweetspair;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.baseline.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.baseline.tweetspair.blot.TweetsPairSpatialAndTemporalCombiner;
import edu.purdue.cs.generator.spout.DummyTextQueryGenerator;
import edu.purdue.cs.generator.spout.DummyTweetGenerator;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;

public class TweetsPairTopology {
	public static void main(String[] args) throws InterruptedException {
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.dummyTweetGenerator, new DummyTweetGenerator());
		builder.setBolt(Constants.tweeterPairIdentifierBolt,
							  new TweetsPairSpatialAndTemporalCombiner(1000,1000)).fieldsGrouping(Constants.dummyTweetGenerator,new Fields(Constants.objectTextField));
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Incremental-Range-Queries-Toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);		
	}
}
