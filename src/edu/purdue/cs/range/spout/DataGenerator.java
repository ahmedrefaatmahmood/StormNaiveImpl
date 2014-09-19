package edu.purdue.cs.range.spout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Random;

import edu.purdue.cs.range.Parameters;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class DataGenerator extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private Random random;
	private int id ;
	public void ack(Object msgId) {
		System.out.println("OK:"+msgId);
	}
	public void close() {}
	public void fail(Object msgId) {
		System.out.println("FAIL:"+msgId);
	}

	
	public void nextTuple() {
		
		
		String str;
		//Open the reader
		
			//Read all lines
			while(true){
				id++;
				/**
				 * By each line emmit a new value with the line as a their
				 */
				int x = random.nextInt(Parameters.xRange);
				int y = random.nextInt(Parameters.yRange);
				this.collector.emit(new Values(id,x,y));
			//	System.out.println("tuple  generated");
				try {
					
					Thread.sleep(Parameters.dataGeneratorDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
	}

	
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		id =0;
		this.collector = collector;
		random = new Random(Parameters.seed);
	}

	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id","x","y"));
	}
}
