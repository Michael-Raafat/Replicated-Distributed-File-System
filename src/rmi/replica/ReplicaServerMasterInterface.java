package rmi.replica;

import java.rmi.RemoteException;
import java.util.List;

import data.FileContent;
import data.ReplicaLoc;

public interface ReplicaServerMasterInterface extends ReplicaInterface {
	boolean isAlive() throws RemoteException;
}
