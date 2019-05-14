package rmi.replica;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import data.FileContent;
import data.WriteMsg;
import exceptions.MessageNotFoundException;

public interface ReplicaServerClientInterface extends ReplicaInterface {
	/**
	 * 
	 * @param transactionId: the ID of the transaction to which this message relates
	 * @param msgSeqNum: the message sequence number. Each transaction starts with
	 *                   message sequence number 1.
	 * @param data: data to write in the file
	 * @return message with required info
	 * @throws IOException
	 * @throws RemoteException
	 */
	public Boolean write(long transactionId, long msgSeqNum, FileContent data)
											 throws RemoteException, IOException;
	
	public FileContent read(long transactionId, String fileName) throws FileNotFoundException,
													IOException, RemoteException;
	/**
	 * @param transactionId: the ID of the transaction to which this message relates
	 * @param numOfMsgs: Number of messages sent to the server
	 * @return true for acknowledgment
	 * @throws MessageNotFoundException
	 * @throws RemoteException
	 */
	public boolean commit(long transactionId, long numOfMsgs) throws MessageNotFoundException,
			IOException;
	/**
	 * @param transactionId: the ID of the transaction to which this message relates
	 * @return true for acknowledgment
	 * @throws RemoteException
	 */
	public boolean abort(long transactionId) throws RemoteException;
}
