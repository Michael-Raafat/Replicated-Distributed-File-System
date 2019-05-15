package utils;

import data.ReplicaLoc;
import rmi.replica.ReplicaServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtils {
	private static int rmiPort = 1099;
	
    public ReplicaServer getReplicaServer(ReplicaLoc replicaLoc) throws RemoteException {
        ReplicaServer replicaServer = null;
        try {
            replicaServer = (ReplicaServer) getRegistry(replicaLoc).lookup("Replica" + replicaLoc.getId());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException for Registry Variable");
        }
        return replicaServer;
    }

    private Registry getRegistry(ReplicaLoc loc) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(loc.getAddress(), rmiPort);
        } catch (RemoteException e) {
            System.out.println("Unable to get Registry");
        }
        return registry;
    }
}
