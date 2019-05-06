package rmi.replica;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReplicaServerInterface extends ReplicaInf {
	
	public boolean aqcuireLock(String fileName) throws RemoteException;
	
	public boolean updateReplicas(long txnID, String fileName,
			List<byte[]> sentWrites) throws RemoteException, IOException; 
	
	public boolean releaseLock(String fileName) throws RemoteException;
}
