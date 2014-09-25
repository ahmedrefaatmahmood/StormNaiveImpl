package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.range.bolt.NonIncrementalRangeFilter;

public class TopologySubmitter {





	public static void main(String[] args) throws Exception {
         
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("data-generator",new ObjectLocationGenerator());
		builder.setSpout("query-generator",new RangeQueryGenerator());
		builder.setBolt("range-filter", new NonIncrementalRangeFilter())
			.allGrouping("query-generator").shuffleGrouping("data-generator");
		
        //Configuration
		Config conf = new Config();
		conf.put("wordsFile", "words.txt");
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		conf.put(Config.NIMBUS_HOST, "127.0.0.1");
		System.setProperty("storm.jar", "target/StormTestNaieve-0.0.1-SNAPSHOT.jar");
		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("Range-Queries_toplogy", conf, builder.createTopology());
		Thread.sleep(1000);
	//	cluster.shutdown();
	}
}
