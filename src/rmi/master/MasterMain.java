package rmi.master;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Tuesday, 07 May 2019
 */
public class MasterMain {
	private static int port;
	private static int rmiPort = 1099;
    private static String serverAddress;
    private static String dir;
    private static Registry reg;
    private static Master controller;
    private static MasterServerClientInterface rController;
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
    	}
    	
    	
    }
}
