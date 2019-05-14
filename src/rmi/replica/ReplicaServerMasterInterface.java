package rmi.replica;

import java.rmi.RemoteException;
import java.util.List;

import data.FileContent;
import data.ReplicaLoc;

public interface ReplicaServerMasterInterface extends ReplicaInterface {
	void createFile(String fileName) throws RemoteException;
	void setAsPrimary(String fileName, List<ReplicaLoc> locations) throws RemoteException;
	boolean isAlive() throws RemoteException;
}
