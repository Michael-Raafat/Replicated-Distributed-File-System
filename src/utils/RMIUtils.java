package utils;

import data.ReplicaLoc;
import rmi.replica.ReplicaServer;
import rmi.replica.ReplicaServerGeneralInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtils {
    public ReplicaServerGeneralInterface getReplicaServer(ReplicaLoc replicaLoc) throws RemoteException {
        ReplicaServerGeneralInterface replicaServer = null;
        try {
            replicaServer = (ReplicaServerGeneralInterface) getRegistry(replicaLoc).lookup("Replica" + replicaLoc.getId());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException for Registry Variable");
        }
        return replicaServer;
    }

    private Registry getRegistry(ReplicaLoc loc) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(loc.getAddress(), loc.getPort());
        } catch (RemoteException e) {
            System.out.println("Unable to get Registry");
        }
        return registry;
    }
}
