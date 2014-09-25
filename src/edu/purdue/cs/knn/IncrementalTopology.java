package edu.purdue.cs.knn;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.KNNQueryGenerator;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.knn.bolt.IncrementalKNNFilter;
import edu.purdue.cs.range.bolt.IncrementalRangeFilter;


public class IncrementalTopology {
	public static void main(String[] args) throws InterruptedException {
         
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new KNNQueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt,
							  new IncrementalKNNFilter()).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
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
