package rmi.replica;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public interface ReplicaServerInterface extends ReplicaInterface {
	boolean updateReplicas(String fileName, List<String> writes) throws RemoteException, IOException;
}
