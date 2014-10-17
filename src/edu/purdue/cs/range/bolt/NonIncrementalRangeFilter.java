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

public class NonIncrementalRangeFilter extends BaseBasicBolt {

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
			if (q.isInsideRange(locationUpdate)) {
				if (Constants.debug) {
					System.out.println("Object " + objectId
							+ " qualifies Query " + q.getQueryID());
					System.out.println("    Object's coords: " + objectXCoord
							+ ", " + objectYCoord);
					System.out.println("    Queries's dimensions: xMin="
							+ q.getXMin() + ", yMin=" + q.getYMin() + ", xMax="
							+ q.getXMax() + ", " + q.getYMax());
				}
				collector.emit(new Values(q.getQueryID(), objectId,
						objectXCoord, objectYCoord));
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
				Constants.objectYCoordField));
	}
}
