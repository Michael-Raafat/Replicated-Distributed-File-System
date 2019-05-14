package rmi.master;

import java.awt.image.ReplicateScaleFilter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import args.Args;
import args.ClientArgs;
import args.ReplicaArgs;
import data.ReplicaLoc;
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
	private static String USER_NAME = "USERNAME";
	private static String PASSWORD = "PASSWORD";
	private static int port;
	private static int rmiPort = 1099;
    private static String serverAddress;
    private static String dir;
    private static Registry reg;
    private static Master controller;
    private static MasterServerClientInterface rController;
    private static Boolean masterError = false;
    public static void main(String[] args) {
    	if (args[0] == "-ip") {
    		serverAddress = args[1];
    	}
    	if (args[2] == "-port") {
    		port = Integer.parseInt(args[3]);
    	}
    	if (args[4] == "-dir") {
    		dir = args[5];
    	}
    	System.setProperty("java.rmi.server.hostname",serverAddress);
    	try {
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
    	
    }
    
    private static SSHConnection startReplicas() {
    	SSHConnection con = new SSHConnection();
    	ReplicasLocManager repLManager = ReplicasLocManager.getInstance();
    	List<ReplicaLoc> replicas = repLManager.getReplicasLocs();
    	for (int i = 0; i < replicas.size(); i++) {
    		try {
				Args args = new ReplicaArgs();
				if (con.openConnection(replicas.get(i).getAddress(), PASSWORD, USER_NAME, args, dir)) {
					System.out.println("Clients Created !");
				}
			} catch (Exception e) {
        		masterError = true;
				System.out.println(e.getMessage());
			}
    	}
    	return con;
    }
}
