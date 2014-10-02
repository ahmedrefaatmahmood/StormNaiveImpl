package edu.purdue.cs.streetquery.bolts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import edu.cs.purdue.edu.helpers.LineSegment;
import edu.cs.purdue.edu.helpers.Point;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class StreetMovingObjectQueryBolt implements IRichBolt
{
	private OutputCollector collector;
	private FileReader fileReader;
	private ArrayList<LineSegment> segments;
	private float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE, yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE;
	//@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		segments = new ArrayList<LineSegment>();
		try 
		{
			this.fileReader = new FileReader(stormConf.get("dataFile").toString());
			BufferedReader reader = new BufferedReader(fileReader);
			String str = null;
			String[] arr = null;
			int lineID = 1;
			while ((str = reader.readLine()) != null) 
			{
				arr = str.split("\t");
				if(arr[0] == null || arr[1] == null || arr[2] == null || arr[3] == null)
				{
					continue;
				}
				//int streetID = Integer.parseInt(arr[0]);
				int streetID = lineID++;
				float x1 = Float.parseFloat(arr[0]);
				float y1 = Float.parseFloat(arr[1]);
				float x2 = Float.parseFloat(arr[2]);
				float y2 = Float.parseFloat(arr[3]);
				this.segments.add(new LineSegment(x1, y1, x2, y2, streetID));
				if (x1 < xMin)
				{
					xMin = x1;
				}
				if (x1 > xMax)
				{
					xMax = x1;
				}
				if (x2 < xMin)
				{
					xMin = x2;
				}
				if (x2 > xMax)
				{
					xMax = x2;
				}
				if (y1 < yMin)
				{
					yMin = y1;
				}
				if (y1 > yMax)
				{
					yMax = y1;
				}
				if (y2 < yMin)
				{
					yMin = y2;
				}
				if (y2 > yMax)
				{
					yMax = y2;
				}
			}
		}	
		catch (FileNotFoundException e) 
		{
			throw new RuntimeException("Error reading data file " + stormConf.get("dataFile"));
		}
		catch( NumberFormatException e)
		{
			throw new RuntimeException(e);
		}
		catch( IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	//@Override
	public void execute(Tuple input) 
	{
		int id = input.getIntegerByField("id");
		long timeStamp = input.getLongByField("time");
		float x = input.getFloatByField("x");
		float y = input.getFloatByField("y");
		Point point = new Point(x, y);
		int streetID = getStreetID(point);
		DateTime now = DateTime.now();
		System.out.println("MovingObjectID: " + String.valueOf(id) + ", QueryX: " + String.valueOf(x) + ", QueryY: " + String.valueOf(y) + ", TimeStamp: " + String.valueOf(timeStamp) + ", StreetID = " + streetID);
		collector.emit(new Values(id, x, y, now, streetID));			
		collector.ack(input);
	}
	//@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) 
	{
		declarer.declare(new Fields("id", "x", "y", "time", "streetID"));
	}

	public int getStreetID(Point point)
	{
		int streetID = -1;
		for(LineSegment line : segments)
		{
			if(line.contains(point))
			{
				streetID = line.SegmentID;
				break;
			}
		}
		return streetID;
	}
	
	//@Override
	public void cleanup() 
	{
		System.out.println("xMin: " + String.valueOf(xMin) + ", xMax: " + String.valueOf(xMax) + ", yMin: " + String.valueOf(yMin) + ", yMax: " + String.valueOf(yMax));
	}
	//@Override
	public Map<String, Object> getComponentConfiguration() 
	{
		return null;
	}
}
