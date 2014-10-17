/**
 * author Ahmed Mahmood
 */
package edu.purdue.cs.optimized.range.grouping;

import java.util.Arrays;
import java.util.List;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.task.WorkerTopologyContext;

public class DataStaticGridCustomGrouping implements CustomStreamGrouping {
	/**
	 * here we are assuming point objects every point can go only to a single
	 * query node
	 */

	Integer numberOfPartitions;
	Double xrange;
	Double yrange;
	Double xStep;
	Double yStep;
	Integer xCellsNum;
	Integer yCellsNum;
	List<Integer> _targets; // this is the list of target bolt tasks
	WorkerTopologyContext context;

	public int mapToPartition(Double x, Double y) {
		Integer xCell = (int) (x / xStep);
		Integer yCell = (int) (y / yStep);
		Integer partitionNum = xCell * yCellsNum + yCell;
		if (partitionNum >= _targets.size()) {
			System.out.println("error in data " + x + " , " + y + "  index is "
					+ partitionNum + " while partitions " + _targets.size());
			return 0;
		} else {
			// System.out.println("Point "+x+" , "+y+" is mapped to xcell:"+xCell+"ycell:"+yCell+" index is "+
			// partitionNum+" to partitions "+_targets.get(partitionNum));
			return partitionNum;
		}
	}

	public DataStaticGridCustomGrouping(Integer numberOfPartitions, Double xrange,
			Double yrange, Integer xCellsNum, Integer yCellsNum) {
		this.numberOfPartitions = numberOfPartitions;
		this.xrange = xrange;
		this.yrange = yrange;
		this.xCellsNum = xCellsNum;
		this.yCellsNum = yCellsNum;
		this.xStep = (xrange + 1) / xCellsNum;
		this.yStep = (yrange + 1) / yCellsNum;
	}

	public void prepare(WorkerTopologyContext context, GlobalStreamId stream,
			List<Integer> targetTasks) {
		_targets = targetTasks;
		this.context = context; // TODO this may cause problems
	}

	public List<Integer> chooseTasks(int fromTask, List<Object> values) {
		int i = mapToPartition((Double) values.get(1), (Double) values.get(2));
		return Arrays.asList(_targets.get(i));
	}
}
