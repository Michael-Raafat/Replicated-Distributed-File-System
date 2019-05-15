package rmi.replica;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import data.FileContent;
import data.TransactionMsg;
import exceptions.MessageNotFoundException;

public interface ReplicaServerClientInterface extends ReplicaInterface {
	/**
	 * 
	 * @param transactionMsg: the transaction to which this message relates
	 * @param msgSeqNum: the message sequence number. Each transaction starts with
	 *                   message sequence number 1.
	 * @param data: data to write in the file
	 * @return message with required info
	 * @throws IOException
	 * @throws RemoteException
	 */
	public Boolean write(TransactionMsg transactionMsg, long msgSeqNum, FileContent data)
											 throws RemoteException, IOException;
	
	public FileContent read(TransactionMsg transactionMsg, long msgSeqNum, String fileName) throws FileNotFoundException,
													IOException, RemoteException;
	/**
	 * @param transactionMsg: the transaction to which this message relates
	 * @return true for acknowledgment
	 * @throws MessageNotFoundException
	 * @throws RemoteException
	 */
	public boolean commit(TransactionMsg transactionMsg) throws MessageNotFoundException,
			IOException;
	/**
	 * @param transactionMsg: the transaction to which this message relates
	 * @return true for acknowledgment
	 * @throws RemoteException
	 */
	public boolean abort(TransactionMsg transactionMsg) throws RemoteException;
}
