package demo;

import java.util.ArrayList;
import java.util.Map;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import edu.cs.purdue.edu.helpers.Constants;
import edu.cs.purdue.edu.helpers.LocationUpdate;
import edu.purdue.cs.knn.KNNQuery;
import edu.purdue.cs.range.RangeQuery;

public class RQBolt extends BaseBasicBolt {

	private InetAddress host;
	private Socket socket;
	private ObjectOutputStream oos;

	public void cleanup() {
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ArrayList<RangeQuery> queryList;

	public void execute(Tuple input, BasicOutputCollector collector) {
		filterData(input, collector);		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<RangeQuery>();
		RangeQuery rq1 = new RangeQuery(0, 10000, 10000, 18000, 18000);
		queryList.add(rq1);
		
		RangeQuery rq2 = new RangeQuery(0, -2000, 3000, 4000, 10000);
		queryList.add(rq2);

		try {
			if (DemoConstants.hostName == null) {  // which is the default. 
				host = InetAddress.getLocalHost();
				socket = new Socket(host.getHostName(), DemoConstants.portNumber);
			}
			else {
				socket = new Socket(DemoConstants.hostName, DemoConstants.portNumber);
			}
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void filterData(Tuple input, BasicOutputCollector collector) {		
		int objectId = input.getIntegerByField(Constants.objectIdField);
		double objectXCoord = input.getDoubleByField(Constants.objectXCoordField);
		double objectYCoord = input.getDoubleByField(Constants.objectYCoordField);
		
		LocationUpdate locationUpdate = new LocationUpdate(objectId, objectXCoord, objectYCoord);
		
		boolean qualifies = false;
		for (RangeQuery q : queryList) {			
			if (q.isInsideRange(locationUpdate)) {
				qualifies = true;
				break;
			}
		}
		
		try {
			if (qualifies) {
				oos.writeObject("+," + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());				
				//System.out.println("qualifies " + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());
			} else {
				oos.writeObject("-," + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());
				//System.err.println("does not qualify " + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.objectIdField,
				Constants.objectXCoordField,
				Constants.objectYCoordField));
	}
}
