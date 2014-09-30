package edu.purdue.cs.text.bolt;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Collector;

import edu.cs.purdue.edu.helpers.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class InMemoryFullTextSearchBolt implements IRichBolt {
	private InMemoryFullTextFilter filter;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		try {
			filter = new InMemoryFullTextFilter();
			this.collector = collector;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple input) {
		if(input.getSourceComponent().equals(Constants.dummyTextQueryGenerator))
			registerQuery(input);
		else if(input.getSourceComponent().equals(Constants.dummyTweetGenerator)) 
			filterData(input);

	}

	private void registerQuery(Tuple input) {
		int id = input.getIntegerByField(Constants.queryIdField);
		String q = input.getStringByField(Constants.queryTextField);
		filter.registerQuery(id+"", q);
		System.out.println("Registering Query_"+id + ": "+q);
		
	}

	private void filterData(Tuple input) {
		int objectID = input.getIntegerByField(Constants.objectIdField);
		String textContent = input.getStringByField(Constants.objectTextField);
		Collection<String> matchingQueries = filter.search(textContent);
		collector.emit(new Values(objectID,textContent,matchingQueries));
		System.out.println("Tweet_"+ objectID + ": " + textContent + "matched queries: " + matchingQueries.toString());
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
