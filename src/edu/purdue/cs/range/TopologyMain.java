package edu.purdue.cs.range;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.purdue.cs.range.bolt.RangeFilter;
import edu.purdue.cs.range.spout.DataGenerator;
import edu.purdue.cs.range.spout.QueryGenerator;


public class TopologyMain {
	public static void main(String[] args) throws InterruptedException {
         
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("data-generator",new DataGenerator());
		builder.setSpout("query-generator",new QueryGenerator());
		builder.setBolt("range-filter", new RangeFilter())
			.allGrouping("query-generator").shuffleGrouping("data-generator");
		
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
	//	conf.put(Config., 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Range-Queries_toplogy", conf, builder.createTopology());
		while(true)
		Thread.sleep(1000);
		
	}
}
