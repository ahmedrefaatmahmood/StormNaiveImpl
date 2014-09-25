package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.purdue.cs.nonincremental.range.bolt.NonIncrementalRangeFilter;
import edu.purdue.cs.range.spout.ObjectLocationGenerator;
import edu.purdue.cs.range.spout.QueryGenerator;


public class NonIncrementalTopology {
	public static void main(String[] args) throws InterruptedException {
         
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new QueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt,
							  new NonIncrementalRangeFilter()).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Range-Queries-toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);		
	}
}
