package edu.purdue.cs.range.bolt;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import edu.purdue.cs.range.RangeQuery;

public class RangeFilter extends BaseBasicBolt {

	public void cleanup() {
	}

	ArrayList<RangeQuery> queryList;

	public void execute(Tuple input, BasicOutputCollector collector) {
		//System.out.println(input.getSourceComponent());
		if ("data-generator".equals(input.getSourceComponent())) {
			filterData(input,collector);
		} else if ("query-generator".equals(input.getSourceComponent())) {
			addQuery(input);
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		queryList = new ArrayList<RangeQuery>();
	}

	void filterData(Tuple input, BasicOutputCollector collector) {
		boolean qualified =false;
		for(RangeQuery q: queryList){
			if (q.filterPoint(input.getIntegerByField("x"), input.getIntegerByField("y"))){
				System.out.println("Tuple "+input.getIntegerByField("id")+" Qualifies for query "+q.getId());
				collector.emit(new Values(q.getId(),input.getIntegerByField("id"),input.getIntegerByField("x"),input.getIntegerByField("y")));
				qualified = true;
			}
		}
//		if(!qualified)
//			System.out.println("Tuple "+input.getIntegerByField("id")+" not qualified");
	}

	void addQuery(Tuple input) {
		RangeQuery query = new RangeQuery(input.getIntegerByField("id"),
				input.getIntegerByField("xmin"),
				input.getIntegerByField("ymin"),
				input.getIntegerByField("xmax"),
				input.getIntegerByField("ymax"));
		queryList.add(query);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("queryid", "objectId", "x", "y"));
	}
}
