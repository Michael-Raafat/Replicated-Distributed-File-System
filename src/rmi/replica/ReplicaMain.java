package rmi.replica;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class ReplicaMain {
	private static ReplicaServerGeneralInterface rController;
	private static Registry reg;
	private static int rmiPort = 1099;
	
	public static void main(String[] args) {
		String serverAddress = "127.0.0.1";
		int port = 8080;
		String dir = "";
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
		ReplicaServerGeneralInterface controller = new ReplicaServer(dir);
		try {
    		System.setProperty("java.rmi.server.hostname", serverAddress);
    		LocateRegistry.createRegistry(rmiPort);
			rController = (ReplicaServerGeneralInterface) UnicastRemoteObject.exportObject(controller, port);
			reg = LocateRegistry.getRegistry(rmiPort);
			reg.rebind(dir, rController);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	System.out.println("Server registered RMI successfully!");
	}

}
