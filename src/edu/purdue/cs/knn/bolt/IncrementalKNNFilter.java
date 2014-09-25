package edu.purdue.cs.knn.bolt;

import java.util.ArrayList;
import java.util.Map;


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

public class IncrementalKNNFilter extends BaseBasicBolt {

	public void cleanup() {
	}

	ArrayList<KNNQuery> queryList;

	public void execute(Tuple input, BasicOutputCollector collector) {
		if (Constants.objectLocationGenerator.equals(input.getSourceComponent())) {
			filterData(input,collector);
		} else if (Constants.queryGenerator.equals(input.getSourceComponent())) {
			addQuery(input);
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<KNNQuery>();
	}

	void filterData(Tuple input, BasicOutputCollector collector) {
		for (KNNQuery q : queryList) {
			int objectId = input.getIntegerByField(Constants.objectIdField);
			int objectXCoord = input.getIntegerByField(Constants.objectXCoordField);
			int objectYCoord = input.getIntegerByField(Constants.objectYCoordField);

			LocationUpdate locationUpdate = new LocationUpdate(objectId, objectXCoord, objectYCoord);
			ArrayList<String> changes = q.processLocationUpdate(locationUpdate);
			for (String str : changes) {
				if (str.charAt(0) == '-')
					System.err.println(str);
				else
					System.out.println(str);
			}
		}
	}

	void addQuery(Tuple input) {
		KNNQuery query = new KNNQuery(input.getIntegerByField(Constants.queryIdField),
				input.getIntegerByField(Constants.focalXCoordField),
				input.getIntegerByField(Constants.focalYCoordField),
				input.getIntegerByField(Constants.kField));
		queryList.add(query);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.objectIdField,
				Constants.objectXCoordField,
				Constants.objectYCoordField));
	}
}
