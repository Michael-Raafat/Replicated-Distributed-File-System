package rmi.client;

import java.util.ArrayList;

import java.util.List;
import java.util.List;

import data.FileContent;
import data.ReplicaLoc;
import data.WriteMsg;
import exceptions.MessageNotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import data.ReplicaLoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.master.MasterServerClientInterface;
import rmi.replica.ReplicaServerClientInterface;
import utils.Entry;

public class Client {
	// null for now till i get the info
	private final MasterServerClientInterface masterServer = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public FileContent read(String fileName) throws FileNotFoundException,
		RemoteException, IOException, NotBoundException {

		List<ReplicaLoc> allReplicaAddresses = masterServer.read(fileName);
		ReplicaLoc primaryReplicaAddress = allReplicaAddresses.get(0);
		
		ReplicaServerClientInterface primaryReplica = 
				(ReplicaServerClientInterface) getReplicaObject(primaryReplicaAddress);
		//>>>>>>>>>>>> transaction id removed from replica
		FileContent file = primaryReplica.read(fileName);
		return file;
	}
	
	public Entry <ReplicaServerClientInterface, Long> write(String fileName,
			ArrayList<FileContent> data) throws FileNotFoundException,
			RemoteException, IOException, NotBoundException,
			MessageNotFoundException {
		System.out.println("In Client");
		System.out.println("Writing In : " + fileName);

		System.out.println("Send Write Request to Master Server");
		
		WriteMsg masterResponse = masterServer.write(fileName);

		System.out.println("Get the Primary Replica : ( "
				+ masterResponse.getLoc().toString() + " )");

		ReplicaServerClientInterface primaryReplica = (ReplicaServerClientInterface) getReplicaObject(masterResponse.getLoc());

		for (int i = 0; i < data.size(); i++) {
			System.out
					.println("Send Write Requst to PrimaryReplica : transactionID : "
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
	
	private Remote getReplicaObject (ReplicaLoc replicaAddress) throws RemoteException,
	NotBoundException {
		
		
		Registry registry = LocateRegistry.getRegistry(replicaAddress.getAddress(), Integer.parseInt(replicaAddress.getPort()));
//////////////>>>>>>>>>>>>>>>>>>>>>>. check what's the Name !!!
		return registry.lookup(String.valueOf(replicaAddress.getId()));
	}


}
	