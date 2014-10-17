package edu.purdue.cs.range.bolt;

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
import edu.purdue.cs.range.RangeQuery;

public class IncrementalRangeFilter extends BaseBasicBolt {

	public void cleanup() {
	}

	ArrayList<RangeQuery> queryList;

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
		queryList = new ArrayList<RangeQuery>();
	}

	void filterData(Tuple input, BasicOutputCollector collector) {
		for (RangeQuery q : queryList) {
			int objectId = input.getIntegerByField(Constants.objectIdField);

			double objectXCoord = input.getDoubleByField(Constants.objectXCoordField);
			double objectYCoord = input.getDoubleByField(Constants.objectYCoordField);

			LocationUpdate locationUpdate = new LocationUpdate(objectId,
					objectXCoord, objectYCoord);
			boolean isInside = q.isInsideRange(locationUpdate);
			if (q.objectAlreadyContained(objectId)) {
				if (isInside) {
					if (Constants.debug)
						System.out.println(" U " + " Object " + objectId
								+ " for Query " + q.getQueryID());
					collector.emit(new Values(q.getQueryID(), objectId,
							objectXCoord, objectYCoord, " U "));
				} else {
					if (Constants.debug)
						System.err.println(" - " + " Object " + objectId
								+ " for Query " + q.getQueryID());
					collector.emit(new Values(q.getQueryID(), objectId,
							objectXCoord, objectYCoord, " - "));
					q.removeObject(objectId);
				}
			} else {
				if (isInside) {
					if (Constants.debug)
						System.out.println(" + " + " Object " + objectId
								+ " for Query " + q.getQueryID());
					collector.emit(new Values(q.getQueryID(), objectId,
							objectXCoord, objectYCoord, " + "));
					q.addObject(objectId);
				}
			}
		}
	}

	void addQuery(Tuple input) {
		RangeQuery query = new RangeQuery(
				input.getIntegerByField(Constants.queryIdField),
				input.getDoubleByField(Constants.queryXMinField),
				input.getDoubleByField(Constants.queryYMinField),
				input.getDoubleByField(Constants.queryXMaxField),
				input.getDoubleByField(Constants.queryXMaxField));
		queryList.add(query);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.queryIdField,
				Constants.objectIdField, Constants.objectXCoordField,
				Constants.objectYCoordField, Constants.incrementalState));
	}
}
