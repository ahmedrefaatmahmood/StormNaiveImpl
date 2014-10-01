/**
 * author Ahmed Mahmood
 */
package edu.purdue.cs.optimizedrange.grouping;

import java.util.Arrays;
import java.util.List;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.task.WorkerTopologyContext;

public class DataStaticGridCustomGrouping implements CustomStreamGrouping {
	/**
	 * here we are assuming point objects every point can go only to a single query node 
	 */

	Integer numberOfPartitions;
	Integer xrange;
	Integer yrange;
	Integer xStep;
	Integer yStep;
	Integer xCellsNum;
	Integer yCellsNum;
	List<Integer> _targets; // this is the list of target bolt tasks
	WorkerTopologyContext context;
	
	
	public int mapToPartition(Integer x, Integer y) {
		Integer xCell =(int) (x/xStep);
		Integer yCell =(int) (y/yStep);
		int partitionNum =xCell*yCellsNum +yCell;
	//	System.out.println("Point "+x+" , "+y+" is mapped to xcell:"+xCell+"ycell:"+yCell+" index is "+ partitionNum+" to partitions "+_targets.get(partitionNum));
		return partitionNum;
	}

	

	public DataStaticGridCustomGrouping(int numberOfPartitions, Integer xrange,
			Integer yrange, Integer xCellsNum,Integer yCellsNum) {
		this.numberOfPartitions = numberOfPartitions;
		this.xrange = xrange;
		this.yrange = yrange;
		this.xCellsNum = xCellsNum;
		this.yCellsNum = yCellsNum;
		this.xStep = (xrange+1)/xCellsNum;
		this.yStep = (yrange+1)/yCellsNum;
	}

	public void prepare(WorkerTopologyContext context, GlobalStreamId stream,
			List<Integer> targetTasks) {
		_targets = targetTasks;
		this.context = context;  //TODO this may cause problems 
	}

	public List<Integer> chooseTasks(int fromTask, List<Object> values) {
		int i = mapToPartition((Integer)values.get(1), (Integer)values.get(2));
		return Arrays.asList(_targets.get(i));
	}
}
