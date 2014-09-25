package edu.purdue.cs.incremental.range.bolt;

import java.util.ArrayList;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import edu.purdue.cs.range.Constants;
import edu.purdue.cs.range.RangeQuery;

public class IncrementalRangeFilter extends BaseBasicBolt {

	public void cleanup() {
	}

	ArrayList<RangeQuery> queryList;

	public void execute(Tuple input, BasicOutputCollector collector) {
		if (Constants.objectLocationGenerator.equals(input.getSourceComponent())) {
			filterData(input,collector);
		} else if (Constants.queryGenerator.equals(input.getSourceComponent())) {
			addQuery(input);
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<RangeQuery>();
	}

	void filterData(Tuple input, BasicOutputCollector collector) {
		for (RangeQuery q : queryList) {
			int objectId = input.getIntegerByField(Constants.objectIdField);
			int objectXCoord = input.getIntegerByField(Constants.objectXCoordField);
			int objectYCoord = input.getIntegerByField(Constants.objectYCoordField);

			boolean isInside = q.isInsideRange(objectXCoord, objectYCoord);
			
			if (q.objectAlreadyContained(objectId)) {
				if (isInside) {
					System.out.println(" U " + " Object " + objectId + " for Query " + q.getQueryID());
				} else {
					System.err.println(" - " + " Object " + objectId + " for Query " + q.getQueryID());
					q.removeObject(objectId);
				}
			}
			else {
				if (isInside) {
					System.out.println(" + " + " Object " + objectId + " for Query " + q.getQueryID());
					q.addObject(objectId);
				}
			}			
		}
	}

	void addQuery(Tuple input) {
		RangeQuery query = new RangeQuery(input.getIntegerByField(Constants.queryIdField),
				input.getIntegerByField(Constants.queryXMinField),
				input.getIntegerByField(Constants.queryYMinField),
				input.getIntegerByField(Constants.queryXMaxField),
				input.getIntegerByField(Constants.queryXMaxField));
		queryList.add(query);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.objectIdField,
				Constants.objectXCoordField,
				Constants.objectYCoordField));
	}
}
