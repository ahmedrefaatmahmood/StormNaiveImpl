package demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import edu.cs.purdue.edu.helpers.Constants;

public class BerlinSocketReaderSpout extends BaseRichSpout {
	
	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private BufferedReader reader;
	
	private static final long serialVersionUID = 1L;
	
	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}

	public void close() {
	}

	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}

	@Override
	public void nextTuple() {
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (line == null) {
			try {
				System.err.println("End of file reached");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			String[] parts = line.split(",");
			
			// ID is Parts 0
			int id = Integer.parseInt(parts[0]);
			// Time is Parts 1
			// X is Parts 2
			double xCoord = Double.parseDouble(parts[2]);
			// Y is Parts 3
			double yCoord = Double.parseDouble(parts[3]);
						
			this.collector.emit(new Values(id, xCoord, yCoord));
		}
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		try {
			
		  Socket socket =new Socket("127.0.0.1",2013);
          socket.setSoTimeout(60000);
			//this.fileReader = new FileReader(DemoConstants.dataPath);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			throw new RuntimeException("Error reading file " + DemoConstants.dataPath);
		}
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.objectIdField,
				Constants.objectXCoordField, Constants.objectYCoordField));
	}
}
