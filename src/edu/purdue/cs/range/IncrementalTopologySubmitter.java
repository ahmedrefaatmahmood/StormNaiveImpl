package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.range.bolt.NonIncrementalRangeFilter;

public class IncrementalTopologySubmitter {





	public static void main(String[] args) throws Exception {
         
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new RangeQueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt,
							  new IncrementalRangeFilter()).allGrouping(Constants.queryGenerator).shuffleGrouping(Constants.objectLocationGenerator);
		
        //Configuration
		Config conf = new Config();
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS,0);
		conf.put(Config.NIMBUS_HOST, Constants.mcMachinesNimbus);
		System.setProperty("storm.jar", "target/StormTestNaieve-0.0.1-SNAPSHOT.jar");
		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("IncrementalRange-Queries_toplogy", conf, builder.createTopology());
		//Thread.sleep(1000);
	//	cluster.shutdown();
	}
}
