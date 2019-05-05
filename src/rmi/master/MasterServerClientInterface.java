package rmi.master;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import data.ReplicaLoc;
import data.WriteMsg;

public interface MasterServerClientInterface extends Remote {

	/**
	 * Read file from server
	 * 
	 * @param fileName
	 * @return the addresses of its different replicas
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public List<ReplicaLoc> read(String fileName) throws FileNotFoundException,
													 IOException, RemoteException;

	/**
	 * Start a new write transaction
	 * 
	 * @param fileName
	 * @return the required info
	 * @throws RemoteException
	 * @throws IOException
	 */
	public WriteMsg write(String fileName) throws RemoteException, IOException;
	
	/**
	 * locating the primary replica
	 * @param fileName
	 * @return replica location
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	public ReplicaLoc assignPrimaryReplica(String fileName) throws FileNotFoundException,RemoteException; 
}