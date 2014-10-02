package edu.purdue.cs.optimizedrange;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.optimizedrange.grouping.DataStaticGridCustomGrouping;
import edu.purdue.cs.optimizedrange.grouping.QueryStaticGridCustomGrouping;
import edu.purdue.cs.range.bolt.IncrementalRangeFilter;

public class TopologyIcrementalMain {
	public static void main(String[] args) throws InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator(),Constants.dataSpoutParallelism);
		builder.setSpout(Constants.queryGenerator, new RangeQueryGenerator(),Constants.querySpoutParallelism);
		builder.setBolt(Constants.rangeFilterBolt, new IncrementalRangeFilter(),Constants.boltParallelism)
				.customGrouping(
						Constants.queryGenerator,
						 new QueryStaticGridCustomGrouping(
								 Constants.numberOfBolts, Constants.xMaxRange,
								 Constants.yMaxRange, Constants.xCellsNum,
								 Constants.yCellsNum))
				.customGrouping(Constants.objectLocationGenerator,new DataStaticGridCustomGrouping(
						Constants.numberOfBolts, Constants.xMaxRange,
						Constants.yMaxRange, Constants.xCellsNum,
						Constants.yCellsNum));
		
		Config conf = new Config();

		conf.setDebug(false);
		// Topology run
		//conf.put(Config., 1);
		//conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		// conf.put(Config., 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("OptimizedIncrementalRange-Queries_toplogy", conf,
				builder.createTopology());
//		while (true)
//			Thread.sleep(1000);

	}
}
