package rmi.master;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import data.TransactionMsg;

public interface MasterServerClientInterface extends Remote {
	/**
	 * Request transactions.
	 * 
	 * @param fileName
	 * @return the addresses of its different replicas
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public TransactionMsg request_transaction(String fileName) throws FileNotFoundException,
													 IOException, RemoteException;
}