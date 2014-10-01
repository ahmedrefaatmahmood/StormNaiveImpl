package edu.purdue.cs.optimizedrange;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import edu.cs.purdue.edu.helpers.Constants;
import edu.purdue.cs.generator.spout.ObjectLocationGenerator;
import edu.purdue.cs.generator.spout.RangeQueryGenerator;
import edu.purdue.cs.optimizedrange.grouping.DataStaticGridCustomGrouping;
import edu.purdue.cs.optimizedrange.grouping.QueryStaticGridCustomGrouping;
import edu.purdue.cs.performance.ClusterInformationExtractor;
import edu.purdue.cs.range.bolt.NonIncrementalRangeFilter;

public class TobologyNonIncrementalSubmitter {





	public static void main(String[] args) throws Exception {
         
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.objectLocationGenerator, new ObjectLocationGenerator());
		builder.setSpout(Constants.queryGenerator, new RangeQueryGenerator());
		builder.setBolt(Constants.rangeFilterBolt, new NonIncrementalRangeFilter(),Constants.numberOfBolts)
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

		

		//Configuration
		Config conf = new Config();
		conf.setDebug(false);
        //Topology run
		conf.setNumWorkers(Constants.numberOfWorkers);
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS,0);
		conf.put(Config.NIMBUS_HOST, Constants.mcMachinesNimbus);
		System.setProperty("storm.jar", "target/StormTestNaieve-0.0.1-SNAPSHOT.jar");
		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("Optimized_Non-IncrementalRange-Queries_toplogy", conf, builder.createTopology());
		Thread.sleep(1000*60*Constants.minutesToGetSats);
		ClusterInformationExtractor.main(null);
	//	cluster.shutdown();
	}
}
