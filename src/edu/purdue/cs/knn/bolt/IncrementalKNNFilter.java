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
		if (Constants.objectLocationGenerator
				.equals(input.getSourceComponent())) {
			filterData(input, collector);
		} else if (Constants.queryGenerator.equals(input.getSourceComponent())) {
			addQuery(input);
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<KNNQuery>();
	}

	void filterData(Tuple input, BasicOutputCollector collector) {
		int objectId = input.getIntegerByField(Constants.objectIdField);
		double objectXCoord = input
				.getDoubleByField(Constants.objectXCoordField);
		double objectYCoord = input
				.getDoubleByField(Constants.objectYCoordField);

		String changesOverall="";
		for (KNNQuery q : queryList) {
			
			LocationUpdate locationUpdate = new LocationUpdate(objectId,
					objectXCoord, objectYCoord);
			ArrayList<String> changes = q.processLocationUpdate(locationUpdate);
			if (changes != null)
				for (String str : changes) {
					changesOverall=changesOverall+"("+q.getQueryID()+","+str.charAt(0)+"),";
//					 if (str.charAt(0) == '-'){
//					 System.err.println(str);
//					
//					 }else{
//					 System.out.println(str);
//					 collector.emit(new Values(q.getQueryID(), objectId,
//					 objectXCoord, objectYCoord,"-"));
//					 }

				}
		
		}
		collector.emit(new Values(changesOverall, objectId, objectXCoord,
				objectYCoord));
	}

	void addQuery(Tuple input) {
		KNNQuery query = new KNNQuery(
				input.getIntegerByField(Constants.queryIdField),
				input.getDoubleByField(Constants.focalXCoordField),
				input.getDoubleByField(Constants.focalYCoordField),
				input.getIntegerByField(Constants.kField));
		queryList.add(query);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.objectIdField, Constants.objectXCoordField));
	}
}
