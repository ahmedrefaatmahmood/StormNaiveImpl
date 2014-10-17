package edu.purdue.cs.generator.spout;

import java.util.Map;
import java.util.Random;

import edu.cs.purdue.edu.helpers.Constants;
import edu.cs.purdue.edu.helpers.RandomGenerator;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RangeQueryGenerator extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private RandomGenerator randomGenerator;
	int i;
	
	
	public void nextTuple() {
		// Generate queries at random
		if( i < Constants.numQueries) { // i will be the query
															// id.
			i++;
			Double xMin = randomGenerator.nextDouble(0,Constants.xMaxRange);
			Double yMin = randomGenerator.nextDouble(0,Constants.yMaxRange);

			Double width = randomGenerator.nextDouble(0,Constants.queryMaxWidth);
			Double xMax = xMin + width;
			if (xMax > Constants.xMaxRange) {
				xMax = Constants.xMaxRange;
			}

			Double height = randomGenerator.nextDouble(0,Constants.queryMaxHeight);
			Double yMax = yMin + height;
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
		i=0;
		this.collector = collector;
		randomGenerator = new RandomGenerator(Constants.generatorSeed);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.queryXMinField, Constants.queryYMinField,
				Constants.queryXMaxField, Constants.queryYMaxField));
	}
}
