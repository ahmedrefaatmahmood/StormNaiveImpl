package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.purdue.cs.incremental.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.range.spout.ObjectLocationGenerator;
import edu.purdue.cs.range.spout.QueryGenerator;


public class IncrementalTopology {
	public static void main(String[] args) throws InterruptedException {
         
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new QueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt,
							  new IncrementalRangeFilter()).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Incremental-Range-Queries-toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);		
	}
}
