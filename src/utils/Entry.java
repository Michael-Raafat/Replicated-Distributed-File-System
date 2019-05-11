package utils;

import rmi.replica.ReplicaServerClientInterface;

public final class Entry <ReplicaServerClientInterface, Long>{
	
	private final ReplicaServerClientInterface primaryReplica;
	private final long transactionId;

	public Entry(ReplicaServerClientInterface primaryReplica, long transactionId) {
		this.primaryReplica = primaryReplica;
		this.transactionId = transactionId;
	}
		
	public ReplicaServerClientInterface getPrimaryReplica() {
		return primaryReplica;
	}
	
	public long getTransactionId() {
		return transactionId;
	}
}
