package rmi.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import configurations.ClientConfigurations;

import data.FileContent;
import data.ReplicaLoc;
import data.Transaction;
import data.TransactionMsg;
import exceptions.MessageNotFoundException;
import request.AbortRequest;
import request.CommitRequest;
import request.ReadRequest;
import request.Request;
import request.WriteRequest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import data.RequestType;
import rmi.master.MasterServerClientInterface;
import rmi.replica.ReplicaServerClientInterface;
import utils.Entry;

public class Client {
	// null for now till i get the info
	private MasterServerClientInterface masterServer = null;
	private ClientConfigurations cc;
	public static void main(String[] args) throws NotBoundException, 
		FileNotFoundException, IOException, MessageNotFoundException {
		// TODO Auto-generated method stub
		Client c = new Client (args[0]);
		c.start();
		System.out.println("Client has done all his transactions");
		System.exit(0);
	}
	public Client (String fileName) throws RemoteException, NotBoundException {
		cc = new ClientConfigurations (fileName);
		masterServer = (MasterServerClientInterface) getMasterObject (cc.getMasterAdd(), cc.getMasterPort());
	}
	private void start () throws FileNotFoundException, RemoteException,
		IOException, NotBoundException, MessageNotFoundException {
		HashMap<String, ArrayList<FileContent>> modifiedFiles = new HashMap<String, ArrayList<FileContent>>();
		HashMap<String, Entry<ReplicaServerClientInterface, Long>> transComOrAb = 
				new HashMap<String, Entry <ReplicaServerClientInterface, Long>>();
		
		List<Transaction> trans = cc.getTransactions();
		for (int i = 0; i < trans.size(); i++) {
			
			Transaction t = trans.get(i);
			List<Request> requests = t.getRequests();
			System.out.println("Transaction Number : " + (i+1));
			for (int j= 0; j < requests.size(); j++) {
				System.out.println("Request Number : " + (j+1));
				switch (requests.get(j).getType()) {
				case READ :
					ReadRequest r = (ReadRequest) requests.get(j);
					System.out.println("Request Type : Read ... Reading file" + r.getFileName());
					FileContent file = read(r.getFileName());
					System.out.println("File Content");
					System.out.println(file.toString());
					break;
				case WRITE :
					WriteRequest w = (WriteRequest) requests.get(j);
					ArrayList<FileContent> oldData = modifiedFiles.get(w.getFileName());
					if (oldData == null) {
						oldData = new ArrayList<FileContent>();
					}
					List<String> dataLines = w.getData();
					for (int k = 0; k < dataLines.size(); k++) {
						oldData.add(new FileContent(w.getFileName(), dataLines.get(k)));
					}
					System.out.println("Request Type : Write ... Writing in file" + w.getFileName());
					Entry <ReplicaServerClientInterface, Long> toComOrAb = write(
							w.getFileName(), oldData);
					transComOrAb.put(w.getFileName(), toComOrAb);
					modifiedFiles.put(w.getFileName(), oldData);
					
					break;
				case COMMIT :
					CommitRequest c = (CommitRequest) requests.get(j);
					System.out.println("Request Type : Commit ... Commiting file" + c.getFileName());
					commit(c.getFileName(), transComOrAb.get(c.getFileName()), 
							modifiedFiles.get(c.getFileName()).size());
					modifiedFiles.remove(c.getFileName());
					transComOrAb.remove(c.getFileName());
					break;
				case ABORT :
					AbortRequest a = (AbortRequest) requests.get(j);
					System.out.println("Request Type : Abort ... Aborting file" + a.getFileName());
					abort(transComOrAb.get(a.getFileName()));
					modifiedFiles.remove(a.getFileName());
					transComOrAb.remove(a.getFileName());
					break;
					
				}
			}
			
			
		}
	}
	
	public FileContent read(String fileName) throws FileNotFoundException,
		RemoteException, IOException, NotBoundException {

		List<ReplicaLoc> allReplicaAddresses = masterServer.read(fileName);
		ReplicaLoc primaryReplicaAddress = allReplicaAddresses.get(0);
		
		ReplicaServerClientInterface primaryReplica = 
				(ReplicaServerClientInterface) getReplicaObject(primaryReplicaAddress);
		// TODO: change transaction id to be dynamic from master server.
		FileContent file = primaryReplica.read(1, fileName);
		return file;
	}
	
	public Entry <ReplicaServerClientInterface, Long> write(String fileName,
			ArrayList<FileContent> data) throws FileNotFoundException,
			RemoteException, IOException, NotBoundException,
			MessageNotFoundException {
		System.out.println("In Client");
		System.out.println("Writing In : " + fileName);

		System.out.println("Send Write Request to Master Server");
		
		TransactionMsg masterResponse = masterServer.write(fileName);

		System.out.println("Get the Primary Replica : ( "
				+ masterResponse.getLoc().toString() + " )");

		ReplicaServerClientInterface primaryReplica = (ReplicaServerClientInterface) getReplicaObject(masterResponse.getLoc());

		for (int i = 0; i < data.size(); i++) {
			System.out
					.println("Send Write Request to PrimaryReplica : transactionID : "
							+ masterResponse.getTransactionId()
							+ ", msgSequenceNum : "
							+ i);
			primaryReplica.write(masterResponse.getTransactionId(), i, data.get(i));
		}
		System.out.println("Finished .. Return .. Ready to Commit ");
		return new Entry <ReplicaServerClientInterface, Long>(primaryReplica,
				masterResponse.getTransactionId());

	}
	
	private void commit(String fileName,
			Entry <ReplicaServerClientInterface, Long> transactionInfo,
			int dataSize) throws RemoteException, IOException,
			NotBoundException, MessageNotFoundException {
		System.out
				.println("Send Commit Request to Primary Replica : transactionID : "
						+ transactionInfo.getTransactionId() );
		boolean success = transactionInfo.getPrimaryReplica().commit(transactionInfo.getTransactionId(),
				dataSize);
		System.out.println(" Commit Response : "
				+ success);
	}
	private void abort (
			Entry <ReplicaServerClientInterface, Long> transactionInfo
			) throws RemoteException, IOException,
			NotBoundException, MessageNotFoundException {
		System.out
				.println("Send Abort Request to Primary Replica : transactionID : "
						+ transactionInfo.getTransactionId());
		boolean success = transactionInfo.getPrimaryReplica().abort(transactionInfo.getTransactionId());
		System.out.println(" Abort Response : "
				+ success);
	}
	
	private Remote getReplicaObject (ReplicaLoc Address) throws RemoteException,
	NotBoundException {
		
		
		Registry registry = LocateRegistry.getRegistry(Address.getAddress(), Integer.parseInt(Address.getPort()));
//////////////>>>>>>>>>>>>>>>>>>>>>>. check what's the Name !!!
		return registry.lookup(String.valueOf(Address.getId()));
	}
	
	private Remote getMasterObject (String ipAddr, String port) throws RemoteException,
	NotBoundException {
		
		
		Registry registry = LocateRegistry.getRegistry(ipAddr, Integer.parseInt(port));
//////////////>>>>>>>>>>>>>>>>>>>>>>. check what's the Name !!!
		return registry.lookup("Const");
	}


}
	