package rmi.client;

import java.util.List;

import data.fileContent;
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

import master.MasterServerClientInterface;

public class Client {
	private final MasterServerClientInterface masterServer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public FileContent read(String fileName) throws FileNotFoundException,
		RemoteException, IOException, NotBoundException {

		List<ReplicaLoc> allReplicaAddresses = masterServer.read(fileName);
		ReplicaLoc primaryReplicaAddress = allReplicaAddresses.get(0);
		
		ReplicaServerClientInterface primaryReplica = 
				(ReplicaServerClientInterface) getReplicaObject(primaryReplicaAddress);
		
		FileContent file = primaryReplica.read(fileName);
		return file;
	}

}
	private Remote getReplicaObject (ReplicaLoc replicaAddress) throws RemoteException,
		NotBoundException {
		
		Registry registry = LocateRegistry.getRegistry(replicaAddress.getAddress(),
			serverAddr.getPort());

		return registry.lookup(replicaAddress.getId());
	}