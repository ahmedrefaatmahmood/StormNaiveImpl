package edu.purdue.cs.generator.spout;

import java.util.Map;
import java.util.Random;

import edu.cs.purdue.edu.helpers.Constants;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RangeQueryGenerator extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private Random randomGenerator;

	
	
	public void nextTuple() {
		// Generate queries at random
		for (int i = 0; i < Constants.numQueries; i++) { // i will be the query
															// id.
			int xMin = randomGenerator.nextInt(Constants.xMaxRange);
			int yMin = randomGenerator.nextInt(Constants.yMaxRange);

			int width = randomGenerator.nextInt(Constants.queryMaxWidth);
			int xMax = xMin + width;
			if (xMax > Constants.xMaxRange) {
				xMax = Constants.xMaxRange;
			}

			int height = randomGenerator.nextInt(Constants.queryMaxHeight);
			int yMax = yMin + height;
			if (yMax > Constants.yMaxRange) {
				yMax = Constants.yMaxRange;
			}

			this.collector.emit(new Values(i, xMin, yMin, xMax, yMax));

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
		this.collector = collector;
		randomGenerator = new Random(Constants.generatorSeed);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.queryXMinField, Constants.queryYMinField,
				Constants.queryXMaxField, Constants.queryYMaxField));
	}
}
