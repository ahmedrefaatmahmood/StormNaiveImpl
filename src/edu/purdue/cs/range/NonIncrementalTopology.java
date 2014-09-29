package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.range.bolt.NonIncrementalRangeFilter;

public class NonIncrementalTopology {
	public static void main(String[] args) throws InterruptedException {
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new RangeQueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt,
							  new NonIncrementalRangeFilter()).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("NonIncremental-Range-Queries-toplogy", conf, builder.createTopology());
	
	}
}
