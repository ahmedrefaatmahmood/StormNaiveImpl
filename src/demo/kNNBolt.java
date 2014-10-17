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
import edu.purdue.cs.baseline.knn.KNNQuery;
import edu.purdue.cs.baseline.range.RangeQuery;

public class kNNBolt extends BaseBasicBolt {

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

	private ArrayList<KNNQuery> queryList;

	private int i = 0;
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		if (i++ % 2000 == 0) {
			System.err.println("REFRESHED");
			for (KNNQuery q : queryList) {
				q.reset();
			}
		}
		filterData(input, collector);		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<KNNQuery>();
		KNNQuery q1 = new KNNQuery(0, 14000.0, 14000.0, 2);
		queryList.add(q1);
		
		KNNQuery q2 = new KNNQuery(0, 1000.0, 6500.0, 2);
		queryList.add(q2);

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
		for (KNNQuery q : queryList) {			
			if (q.processLocationUpdate(locationUpdate) != null) {
				qualifies = true;
				break;
			}
		}
		
		try {
			if (qualifies) {
				oos.writeObject("+," + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());
			} else {
				oos.writeObject("-," + locationUpdate.getNewLocationXCoord() + "," + locationUpdate.getNewLocationYCoord());
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
