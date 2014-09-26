package edu.purdue.cs.streetquery.spouts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class StreetQuerySpout implements IRichSpout
{

	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	private TopologyContext context;
	//@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) 
	{
		try {
			this.context = context;
			this.fileReader = new FileReader(conf.get("queryFile").toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error reading query file " + conf.get("inputFile"));
		}
		this.collector = collector;
	}

	//@Override
	public void nextTuple() {
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}
		String str;
		String[] arr = null;
		BufferedReader reader = new BufferedReader(fileReader);
		try 
		{
			while ((str = reader.readLine()) != null) 
			{
				arr = str.split(",");
				if(arr[0] == null || arr[1] == null || arr[2] == null)
				{
					continue;
				}
				int id = Integer.parseInt(arr[0]);
				float x = Float.parseFloat(arr[1]);
				float y = Float.parseFloat(arr[2]);
				this.collector.emit(new Values(id, x, y));
				//Thread.sleep(600);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple", e);
		} finally {
			completed = true;
		}

	}
	//@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id", "x", "y"));
	}

	//@Override
	public void close() {
		try {
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isDistributed() {
		return false;
	}
	//@Override
	public void activate() {
	}
	//@Override
	public void deactivate() {
	}
	//@Override
	public void ack(Object msgId) {
	}
	//@Override
	public void fail(Object msgId) {
	}
	//@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
