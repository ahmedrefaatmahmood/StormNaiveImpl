package edu.purdue.cs.performance.simulationRunner;

public class SimulationRunner {
	public static void main(String[] args) throws Exception {
		edu.purdue.cs.optimizedrange.TobologyIncrementalSubmitter.main(null);
		edu.purdue.cs.optimizedrange.TobologyNonIncrementalSubmitter.main(null);
		edu.purdue.cs.range.IncrementalTopologySubmitter.main(null);
		edu.purdue.cs.range.NonIncrementalTopologySubmitter.main(null);
	}
}
