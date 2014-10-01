package edu.purdue.cs.optimizedrange.grouping;

import java.util.ArrayList;
import java.util.List;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.task.WorkerTopologyContext;

public class QueryStaticGridCustomGrouping implements CustomStreamGrouping {

	Integer numberOfPartitions;
	Integer  xrange;
	Integer  yrange;
	Integer  xStep;
	Integer  yStep;
	Integer xCellsNum;
	Integer yCellsNum;
	List<Integer> _targets; // this is the list of target bolt tasks
	WorkerTopologyContext context;

	public List<Integer> mapToPartitions(Integer  xmin, Integer  ymin, Integer  xmax,
			Integer  ymax) {

		ArrayList<Integer> partitions = new ArrayList<Integer>();
		Integer xMinCell = (Integer) (xmin / xStep);
		Integer yMinCell = (Integer) (ymin / yStep);
		Integer xMaxCell = (Integer) (xmax / xStep);
		Integer yMaxCell = (Integer) (ymax / yStep);
		for (Integer xCell = xMinCell; xCell <= xMaxCell; xCell++)
			for (Integer yCell = yMinCell; yCell <= yMaxCell; yCell++) {
				Integer partitionNum = xCell * yCellsNum + yCell;
				if (partitionNum >= _targets.size())
					System.out.println("error in query " + xmin + " , " + ymin
							+ " , " + xmax + " , " + ymax + "  index is "
							+ partitionNum + " while partitions "
							+ _targets.size());
				else {
					// System.out.println("Query "+xmin+" , "+ymin+" , "+xmax+" , "+ymax+" is mapped to xcell:"+xCell+"ycell:"+yCell+" index is "+
					// partitionNum+" to partitions "+_targets.get(partitionNum));
					partitions.add(_targets.get(partitionNum));
				}

			}

		return partitions;
	}

	public QueryStaticGridCustomGrouping(Integer numberOfPartitions, Integer  xrange,
			Integer  yrange, Integer xCellsNum, Integer yCellsNum) {
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
	@Override
	public List<Integer> chooseTasks(int fromTask, List<Object> values) {
		return mapToPartitions((Integer ) values.get(1), (Integer ) values.get(2),
				(Integer ) values.get(3), (Integer ) values.get(4));

	}


	
}
