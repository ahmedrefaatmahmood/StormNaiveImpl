package edu.purdue.cs.range.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import edu.purdue.cs.range.Parameters;

public class QueryGenerator extends BaseRichSpout {



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

		/**
		 * The only thing that the methods will do It is emit each 
		 * file line
		 */
		public void nextTuple() {
			
			
			String str;
			//Open the reader
			
				//Read all lines
				for (int i =0;i<Parameters.numQuery;i++){
					id++;
					/**
					 * By each line emmit a new value with the line as a their
					 */
					int xmin = random.nextInt(Parameters.xRange);
					int ymin = random.nextInt(Parameters.yRange);
					int xrange = random.nextInt(Parameters.xQueryRange);
					int yrange = random.nextInt(Parameters.yQueryRange);
					int xmax = xmin+xrange;
					int ymax = ymin+yrange;
					this.collector.emit(new Values(id,xmin,ymin,xmax,ymax));
					try {
						Thread.sleep(Parameters.queryGeneratorDelay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
		}

		/**
		 * We will create the file and get the collector object
		 */
		public void open(Map conf, TopologyContext context,
				SpoutOutputCollector collector) {
			id =0;
			this.collector = collector;
			random = new Random(Parameters.seed);
		}

		/**
		 * Declare the output field "word"
		 */
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id","xmin","ymin","xmax","ymax"));
		}
	}
