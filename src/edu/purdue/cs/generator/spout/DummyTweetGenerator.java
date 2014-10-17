package edu.purdue.cs.generator.spout;

import java.util.Date;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import edu.cs.purdue.edu.helpers.Constants;
import edu.cs.purdue.edu.helpers.RandomGenerator;

public class DummyTweetGenerator extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private RandomGenerator randomGenerator;
	int i = 0;

	String hashtags[] = { "android", "apple", "art", "business", "canada",
			"cdnpoli", "china", "climate", "cloud", "cnn", "debt", "eco",
			"economy", "edchat", "edtech", "education", "egypt", "energy",
			"environment	", "facebook", "fail", "fashion", "fb", "ff", "food",
			"football", "free", "google", "gop", "green", "health",
			"humanrights", "ididntknowthat", "in", "innovation", "ipad",
			"iphone", "jan25", "job", "jobs", "lgbt", "marketing", "media",
			"mlb", "mobile", "music", "nba", "news", "nfl", "notw", "occupy",
			"occupywallstreet", "ocra", "ows", "p2", "photo", "photography",
			"politics", "pr", "rt", "sgp", "sm", "smochat", "social",
			"socialmedia", "startups", "startupchat", "tcot", "tchat",
			"teamfollowback", "teaparty", "tech", "technology",
			"thingslongerthankimkardashiansmarriage", "tigerblood", "tlot",
			"toolschat", "travel", "tweet", "twitter", "video", "win",
			"winning", "women", "writing" };

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

		int id = randomGenerator.nextInt(Constants.numMovingObjects);
		Double xCoord = randomGenerator.nextDouble(0, Constants.xMaxRange);
		Double yCoord = randomGenerator.nextDouble(0, Constants.yMaxRange);
		String textContent = hashtags[randomGenerator.nextInt(hashtags.length-1)];
		Date date = new Date();

		this.collector.emit(new Values(id, xCoord, yCoord, textContent,date.getTime()));
		try {
			Thread.sleep(Constants.dataGeneratorDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		randomGenerator = new RandomGenerator(Constants.generatorSeed);
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.objectIdField,
				Constants.objectXCoordField, Constants.objectYCoordField,
				Constants.objectTextField,
				Constants.timeStamp));
	}

}
