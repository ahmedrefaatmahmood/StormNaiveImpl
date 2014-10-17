package demo;

import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.range.bolt.IncrementalRangeFilter;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class kNNTopology {
	public static void main(String[] args) throws InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("berlin-reader-spout", new BerlinReaderSpout(),1);
		builder.setBolt(Constants.kNNFilterBolt,
							  new kNNBolt(),1).shuffleGrouping("berlin-reader-spout");
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("kNN-Demo-Toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);	
	}
}
