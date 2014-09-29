package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.range.bolt.NonIncrementalRangeFilter;

public class NonIncrementalTopologySubmitter {





	public static void main(String[] args) throws Exception {
         
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator(),Constants.dataSpoutParallelism);
		builder.setSpout(Constants.queryGenerator, new RangeQueryGenerator(), Constants.querySpoutParallelism);
		builder.setBolt(Constants.rangeFilterBolt,
							  new NonIncrementalRangeFilter(),Constants.boltParallelism).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
		//Configuration
		Config conf = new Config();
		conf.setDebug(false);
        //Topology run
		conf.setNumWorkers(Constants.numberOfWorkers);
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS,0);
		conf.put(Config.NIMBUS_HOST, Constants.mcMachinesNimbus);
		System.setProperty("storm.jar", "target/StormTestNaieve-0.0.1-SNAPSHOT.jar");
		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("Non-IncrementalRange-Queries_toplogy", conf, builder.createTopology());
		//Thread.sleep(1000);
	//	cluster.shutdown();
	}
}
