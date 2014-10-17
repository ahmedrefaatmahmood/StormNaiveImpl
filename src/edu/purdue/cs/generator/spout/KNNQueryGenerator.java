package edu.purdue.cs.generator.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import edu.cs.purdue.edu.helpers.Constants;
import edu.cs.purdue.edu.helpers.RandomGenerator;

public class KNNQueryGenerator extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private RandomGenerator randomGenerator;
	int i ;

	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}

	public void close() {
	}

	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}

	public void nextTuple() {
		// Generate queries at random.
		if (i < Constants.numQueries) { // i will be the query
			// id.
			i++;
			Double xCoord = randomGenerator.nextDouble(0,Constants.xMaxRange);
			Double yCoord = randomGenerator.nextDouble(0,Constants.yMaxRange);

			int k = randomGenerator.nextInt(Constants.maxK) + 1; // To avoid
																	// generating
																	// k = 0, we
																	// add 1;

			this.collector.emit(new Values(i, xCoord, yCoord, k));

			try {
				if (Constants.queryGeneratorDelay != 0)
					Thread.sleep(Constants.queryGeneratorDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		i=0;
		this.collector = collector;
		randomGenerator = new RandomGenerator(Constants.generatorSeed);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.focalXCoordField, Constants.focalYCoordField,
				Constants.kField));
	}
}
