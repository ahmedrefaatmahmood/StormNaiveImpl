package edu.purdue.cs.baseline.tweetspair.blot;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
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
import edu.purdue.cs.baseline.range.RangeQuery;
import edu.purdue.cs.baseline.tweetspair.Tweet;

public class TweetsPairSpatialAndTemporalCombiner extends BaseBasicBolt {
	HashMap<String, ArrayList<Tweet>> localTweetsIndex1;
	HashMap<String, ArrayList<Tweet>> localTweetsIndex2;
	HashMap<String, ArrayList<Tweet>> localTweetsIndex3;
	int temporalWindow;
	long index1Start, index1End, index2Start, index2End, index3Start,
			index3End;
	double distance;

	public TweetsPairSpatialAndTemporalCombiner(int temporalWindow,
			double distance) {
		this.temporalWindow = temporalWindow;
		this.distance = distance;
	}

	public void cleanup() {
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		Tweet tweet = new Tweet(
				input.getIntegerByField(Constants.objectIdField),
				input.getDoubleByField(Constants.objectXCoordField),
				input.getDoubleByField(Constants.objectYCoordField),
				input.getStringByField(Constants.objectTextField),
				input.getLongByField(Constants.timeStamp));

		while (tweet.getTime() > index3End) {
			shiftIndexes();
		}
		findMatchingTweets(tweet, collector);
		insertTweet(tweet);

	}

	private void insertTweet(Tweet tweet) {
		if (tweet.getTime() < index1Start)
			System.err.println("Delayed tweet");
		else if (tweet.getTime() < index2Start) {
			if (localTweetsIndex1.containsKey(tweet.getText()))
				localTweetsIndex1.get(tweet.getText()).add(tweet);
			else {
				localTweetsIndex1.put(tweet.getText(), new ArrayList<Tweet>());
				localTweetsIndex1.get(tweet.getText()).add(tweet);
			}
		} else if (tweet.getTime() < index3Start) {
			if (localTweetsIndex2.containsKey(tweet.getText()))
				localTweetsIndex2.get(tweet.getText()).add(tweet);
			else {
				localTweetsIndex2.put(tweet.getText(), new ArrayList<Tweet>());
				localTweetsIndex2.get(tweet.getText()).add(tweet);
			}
		} else {

			if (localTweetsIndex3.containsKey(tweet.getText()))
				localTweetsIndex3.get(tweet.getText()).add(tweet);
			else {
				localTweetsIndex3.put(tweet.getText(), new ArrayList<Tweet>());
				localTweetsIndex3.get(tweet.getText()).add(tweet);
			}

		}
	}

	private void findMatchingTweets(Tweet tweet, BasicOutputCollector collector) {
		ArrayList<Tweet> tweetList = localTweetsIndex1.get(tweet.getText());
		if (tweetList != null && tweetList.size() != 0) {
			for (Tweet t : tweetList) {
				if (getDistance(t, tweet) <= distance
						&& Math.abs(t.getTime() - tweet.getTime()) <= temporalWindow) {
					System.out.print("tweet : (" + tweet.getId() + ",x:"
							+ tweet.getX() + ",y:" + tweet.getY() + ",text"
							+ tweet.getText() + ",time" + tweet.getTime());
					System.out.println(")  matchs with tweet : (" + t.getId()
							+ ",x:" + t.getX() + ",y:" + t.getY() + ",text"
							+ t.getText() + ",time" + t.getTime() + ")");
					collector.emit(new Values(tweet.getId(), tweet.getX(),
							tweet.getY(), tweet.getText(), tweet.getTime(), t
									.getId(), t.getX(), t.getY(), t.getText(),
							t.getTime()));
				}

			}
		}
		tweetList = localTweetsIndex2.get(tweet.getText());
		if (tweetList != null && tweetList.size() != 0) {
			for (Tweet t : tweetList) {
				if (getDistance(t, tweet) <= distance
						&& Math.abs(t.getTime() - tweet.getTime()) <= temporalWindow) {
					System.out.print("tweet : (" + tweet.getId() + ",x:"
							+ tweet.getX() + ",y:" + tweet.getY() + ",text"
							+ tweet.getText() + ",time" + tweet.getTime());
					System.out.println(")  matchs with tweet : (" + t.getId()
							+ ",x:" + t.getX() + ",y:" + t.getY() + ",text"
							+ t.getText() + ",time" + t.getTime() + ")");
					collector.emit(new Values(tweet.getId(), tweet.getX(),
							tweet.getY(), tweet.getText(), tweet.getTime(), t
									.getId(), t.getX(), t.getY(), t.getText(),
							t.getTime()));
				}

			}
		}
		tweetList = localTweetsIndex3.get(tweet.getText());
		if (tweetList != null && tweetList.size() != 0) {
			for (Tweet t : tweetList) {
				if (getDistance(t, tweet) <= distance
						&& Math.abs(t.getTime() - tweet.getTime()) <= temporalWindow) {
					System.out.print("tweet : (" + tweet.getId() + ",x:"
							+ tweet.getX() + ",y:" + tweet.getY() + ",text"
							+ tweet.getText() + ",time" + tweet.getTime());
					System.out.println(")  matchs with tweet : (" + t.getId()
							+ ",x:" + t.getX() + ",y:" + t.getY() + ",text"
							+ t.getText() + ",time" + t.getTime() + ")");
					collector.emit(new Values(tweet.getId(), tweet.getX(),
							tweet.getY(), tweet.getText(), tweet.getTime(), t
									.getId(), t.getX(), t.getY(), t.getText(),
							t.getTime()));
				}

			}
		}
	}

	double getDistance(Tweet t1, Tweet t2) {
		double distance = Math.sqrt(Math.pow(t1.getX() - t2.getX(), 2)
				+ Math.pow(t1.getY() - t2.getY(), 2));
		return distance;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		localTweetsIndex1 = new HashMap<String, ArrayList<Tweet>>();
		localTweetsIndex2 = new HashMap<String, ArrayList<Tweet>>();
		localTweetsIndex3 = new HashMap<String, ArrayList<Tweet>>();
		index1Start = 0;
		index1End = temporalWindow;
		index2Start = temporalWindow;
		index2End = 2 * temporalWindow;
		index3Start = 2 * temporalWindow;
		index3End = 3 * temporalWindow;
	}

	private void shiftIndexes() {
		index1Start = index1End;
		index1End = index2Start;
		index2Start = index2End;
		index2End = index3Start;
		index3Start = index3End;
		index3End = index3End + temporalWindow;
		localTweetsIndex1 = localTweetsIndex2;
		localTweetsIndex2 = localTweetsIndex3;
		localTweetsIndex3 = new HashMap<String, ArrayList<Tweet>>();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(
				"1"+Constants.objectIdField, "1"+Constants.objectXCoordField,
				"1"+Constants.objectYCoordField, "1S"+Constants.objectTextField, "1"+Constants.timeStamp,
				"2"+Constants.objectIdField,
				"2"+Constants.objectXCoordField, "2"+Constants.objectTextField, "2"+Constants.objectYCoordField,
				"2"+Constants.timeStamp));
	}
}
