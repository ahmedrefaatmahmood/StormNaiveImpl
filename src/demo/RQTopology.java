package demo;

import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.baseline.range.bolt.IncrementalRangeFilter;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class RQTopology {
	public static void main(String[] args) throws InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("berlin-reader-spout", new BerlinReaderSpout());
		builder.setBolt(Constants.rangeFilterBolt,
							  new RQBolt()).shuffleGrouping("berlin-reader-spout");
		
        //Configuration
		Config conf = new Config();
		
		conf.setDebug(false);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("RQ-Demo-Toplogy", conf, builder.createTopology());
		while(true)
			Thread.sleep(1000);	
	}
}
