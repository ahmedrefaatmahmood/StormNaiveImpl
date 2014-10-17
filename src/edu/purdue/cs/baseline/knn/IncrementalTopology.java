package edu.purdue.cs.baseline.knn;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.baseline.knn.bolt.IncrementalKNNFilter;
import edu.purdue.cs.baseline.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.generator.spout.KNNQueryGenerator;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;

public class IncrementalTopology {
	public static void main(String[] args) throws InterruptedException {
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new KNNQueryGenerator(),Constants.querySpoutParallelism);
		builder.setBolt(Constants.kNNFilterBolt,
							  new IncrementalKNNFilter(),Constants.boltParallelism).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Incremental-kNN-Queries-Toplogy", conf, builder.createTopology());
//		while(true)
//			Thread.sleep(1000);		
	}
}
