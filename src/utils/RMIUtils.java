package utils;

import data.ReplicaLoc;
import rmi.replica.ReplicaServerGeneralInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtils {
	public static final int RMI_PORT = 1099;
	
    public ReplicaServerGeneralInterface getReplicaServer(ReplicaLoc replicaLoc) throws RemoteException {
        ReplicaServerGeneralInterface replicaServer = null;
        try {
        	String nameRMI = "Replica" + String.valueOf(replicaLoc.getId());
        	System.out.println("Trying to connect to : " + nameRMI);
            replicaServer = (ReplicaServerGeneralInterface) getRegistry(replicaLoc).lookup(nameRMI);
            System.out.println("Connected to: " + nameRMI);
        } catch (NotBoundException e) {
            System.out.println("NotBoundException for Registry Variable");
        }
        return replicaServer;
    }

    private Registry getRegistry(ReplicaLoc loc) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(loc.getAddress(), RMI_PORT);
        } catch (RemoteException e) {
            System.out.println("Unable to get Registry");
        }
        return registry;
    }
}
