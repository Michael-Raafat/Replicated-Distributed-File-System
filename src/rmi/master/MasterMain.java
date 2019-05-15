package rmi.master;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import args.Args;
import args.ReplicaArgs;
import data.ReplicaLoc;
import rmi.replica.ReplicaServerMasterInterface;
import utils.RMIUtils;
import utils.ReplicasLocManager;
import utils.SSHConnection;



/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Tuesday, 07 May 2019
 */
public class MasterMain {
	private static String USER_NAME = "admin";
	private static String PASSWORD = "admin";
	private static int port;
	private static int rmiPort = 1099;
    private static String serverAddress;
    private static String dir;
    private static Registry reg;
    private static Master controller;
    private static MasterServerClientInterface rController;
    private static Boolean masterError = false;
    public static void main(String[] args) throws InterruptedException {
    	for (int i = 0; i < args.length; i+= 2) {
			if (args[i].equals("-ip")) {
	    		serverAddress = args[i + 1];
	    	}
	    	if (args[i].equals("-port")) {
	    		port = Integer.parseInt(args[i + 1]);
	    	}
	    	if (args[i].equals("-dir")) {
	    		dir = args[i + 1];
	    	}
		}
    	System.setProperty("java.rmi.server.hostname",serverAddress);
    	try {
    		controller = new Master();
    		LocateRegistry.createRegistry(rmiPort);
    		rController = (MasterServerClientInterface) UnicastRemoteObject.exportObject(controller, port);
    		reg = LocateRegistry.getRegistry(rmiPort);
    		reg.rebind("RemoteAccessController", rController);
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("Failed to create master RMI object, terminating master !");
			System.exit(-1);
    	}
    	startReplicas();
    	if (masterError) {
			System.out.println("Failed to create all replicas, terminating master !");
			System.exit(-1);
		}

		Thread.sleep(1000);
    	startHeartBeats();
    }

    private static void startHeartBeats() {
		ReplicasLocManager repLManager = ReplicasLocManager.getInstance();
		List<ReplicaLoc> replicas = repLManager.getReplicasLocs();
		List<ReplicaServerMasterInterface> replicasServer = new ArrayList<>();
		RMIUtils rmiUtils = new RMIUtils();
		for (ReplicaLoc replica : replicas) {
			try {
				replicasServer.add((ReplicaServerMasterInterface) rmiUtils.getReplicaServer(replica));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		HeartBeats heartBeats = new HeartBeats(replicasServer);
		Thread thread = new Thread(heartBeats);
		thread.start();
	}
    
    private static SSHConnection startReplicas() {
    	SSHConnection con = new SSHConnection();
    	ReplicasLocManager repLManager = ReplicasLocManager.getInstance();
    	List<ReplicaLoc> replicas = repLManager.getReplicasLocs();
    	for (int i = 0; i < replicas.size(); i++) {
    		try {
				Args args = new ReplicaArgs(replicas.get(i));
				if (con.openConnection(replicas.get(i).getAddress(), PASSWORD, USER_NAME, args, dir)) {
					System.out.println("Replica" + i + " Created !");
				}
			} catch (Exception e) {
        		masterError = true;
				System.out.println(e.getMessage());
			}
    	}
    	return con;
    }
}
