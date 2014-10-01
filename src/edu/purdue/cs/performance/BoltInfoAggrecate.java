package edu.purdue.cs.performance;

public class BoltInfoAggrecate {
	String id;
	Long transferred;
	Long emitted;
	Long failed;
	Long acked;
	Long executed;
	Double executeLatency;
	Double processLatency;
	int numOfExecuters;
	
	public BoltInfoAggrecate(String id){
		this.id = id;
		transferred=(long) 0;
		emitted=(long) 0;
		failed=(long) 0;
		executed=(long) 0;
		executeLatency=0.0;
		processLatency=0.0;
		numOfExecuters=0;
		acked=(long) 0;
	}

	public void addBoltStats(Long transferred,Long emitted,Long acked,	Long failed,	Long executed,Double executeLatency,	Double processLatency){
		this.numOfExecuters ++;
		this.transferred+=(transferred==null?0:transferred);
		this.emitted+=(emitted==null?0:emitted);
		this.failed += (failed==null?0:failed);
		this.executed +=(executed==null?0:executed);
		this.executeLatency +=(executeLatency==null?0:executeLatency);
		this.processLatency +=(processLatency==null?0:processLatency);
		this.acked +=(acked==null?0:acked);
	}
	public Long getAcked() {
		return acked;
	}

	public void setAcked(Long acked) {
		this.acked = acked;
	}

	@Override
	public String toString()
	{
		return "Bolt id:"+id+" , transferred:"+transferred+" , emitted:"+emitted+" , acked:"+acked+" , failed:"+failed+" , executed:"+executed+" , executeLatency:"+executeLatency/numOfExecuters+" , processLatency:"+processLatency/numOfExecuters;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getTransferred() {
		return transferred;
	}

	public void setTransferred(Long transferred) {
		this.transferred = transferred;
	}


	public Long getEmitted() {
		return emitted;
	}

	public void setEmitted(Long emitted) {
		this.emitted = emitted;
	}

	public Long getFailed() {
		return failed;
	}

	public void setFailed(Long failed) {
		this.failed = failed;
	}

	public Long getExecuted() {
		return executed;
	}

	public void setExecuted(Long executed) {
		this.executed = executed;
	}

	public Double getExecuteLatency() {
		return executeLatency;
	}

	public void setExecuteLatency(Double executeLatency) {
		this.executeLatency = executeLatency;
	}

	public Double getProcessLatency() {
		return processLatency;
	}

	public void setProcessLatency(Double processLatency) {
		this.processLatency = processLatency;
	}

	public int getNumOfExecuters() {
		return numOfExecuters;
	}

	public void setNumOfExecuters(int numOfExecuters) {
		this.numOfExecuters = numOfExecuters;
	}

}
