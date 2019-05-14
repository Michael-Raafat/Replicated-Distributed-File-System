package data;

import java.util.List;

public class TransactionMsg {

	private long transactionId;
	private long timeStamp;
	/**
	 * location of the primary replica of that file.
	 */
	private ReplicaLoc loc;
	/**
	 * List of replicas locations.
	 */
	List<ReplicaLoc> replicas;
	
	public TransactionMsg(long transactionId, long timeStamp, ReplicaLoc loc, List<ReplicaLoc> replicas) {
		this.transactionId = transactionId;
		this.timeStamp = timeStamp;
		this.loc = loc;
		this.replicas = replicas;
	}

	public List<ReplicaLoc> getReplicas() {
		return replicas;
	}

	public void setReplicas(List<ReplicaLoc> replicas) {
		this.replicas = replicas;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ReplicaLoc getLoc() {
		return loc;
	}

	public void setLoc(ReplicaLoc loc) {
		this.loc = loc;
	}
	
}
