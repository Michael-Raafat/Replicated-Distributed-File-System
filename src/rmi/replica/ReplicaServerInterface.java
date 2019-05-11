package rmi.replica;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public interface ReplicaServerInterface extends ReplicaInterface {
	boolean acquireLock(String fileName) throws RemoteException;
	boolean updateReplicas(long txnID, String fileName, List<byte[]> sentWrites) throws RemoteException, IOException;
	boolean releaseLock(String fileName) throws RemoteException;
}
