package edu.purdue.cs.text;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.DummyTextQueryGenerator;
import edu.purdue.cs.generator.spout.DummyTweetGenerator;
import edu.purdue.cs.text.bolt.InMemoryFullTextSearchBolt;

public class InMemoryFullTextSearchTopology {
	public static void main(String[] args) throws InterruptedException {
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.dummyTweetGenerator, new DummyTweetGenerator());
		builder.setSpout(Constants.dummyTextQueryGenerator, new DummyTextQueryGenerator());
		builder.setBolt(Constants.textFilterBolt,
							  new InMemoryFullTextSearchBolt()).
							  allGrouping(Constants.dummyTextQueryGenerator).shuffleGrouping(Constants.dummyTweetGenerator);
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("plain-text-tilter-toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);		
	}
}
