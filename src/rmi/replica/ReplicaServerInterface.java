package rmi.replica;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public interface ReplicaServerInterface extends ReplicaInterface {
	void acquireLock(String fileName) throws RemoteException;
	boolean updateReplicas(String fileName, Map<Long, String> writes) throws RemoteException, IOException;
	void releaseLock(String fileName) throws RemoteException;
}
