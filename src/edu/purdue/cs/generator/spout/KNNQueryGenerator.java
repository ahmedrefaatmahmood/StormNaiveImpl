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

public class KNNQueryGenerator extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private Random randomGenerator;
	
	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}
	
	public void close() {}
	
	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}
	
	
	public void nextTuple() {
		// Generate queries at random.
		for (int i = 0; i < Constants.numQueries; i++) {  // i will be the query id.
			int xCoord = randomGenerator.nextInt(Constants.xMaxRange);
			int yCoord = randomGenerator.nextInt(Constants.yMaxRange);
			
			int k = randomGenerator.nextInt(Constants.maxK) + 1;  // To avoid generating k = 0, we add 1; 
			
			this.collector.emit(new Values(i, xCoord, yCoord, k));
			
			try {
				if(Constants.queryGeneratorDelay!=0)
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
												  Constants.focalXCoordField,
												  Constants.focalYCoordField,
												  Constants.kField));
	}
}
