package rmi.replica;

import data.FileContent;
import data.ReplicaLoc;
import data.WriteMsg;
import exceptions.MessageNotFoundException;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ReplicaServer implements ReplicaInterface, ReplicaServerClientInterface,
                                      ReplicaServerInterface, ReplicaServerMasterInterface {
    // TODO: To be changed to configuration or arguments.
    public static final String REGISTRY_ADDRESS = "Test";
    public static final int REGISTRY_PORT = 2020;

    private String path;
    private ReplicaLoc loc;
    private Registry registry;
    private Map<String, List<ReplicaServer>> sameFileReplicas;

    public ReplicaServer(String path, ReplicaLoc loc) {
        this.path = path;
        this.loc = loc;
        this.registry = getRegistry();
        this.sameFileReplicas = new Hashtable<>();
    }

    private Registry getRegistry() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(REGISTRY_ADDRESS, REGISTRY_PORT);
        } catch (RemoteException e) {
            System.out.println("Unable to get Registry");
        }
        return registry;
    }

    @Override
    public boolean acquireLock(String fileName) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateReplicas(long transactionId, String fileName, List<byte[]> sentWrites) throws RemoteException, IOException {
        return false;
    }

    @Override
    public boolean releaseLock(String fileName) throws RemoteException {
        return false;
    }

    @Override
    public void createFile(FileContent data) throws RemoteException {
        try {
            File file = new File(path + "/" + data.getFilename());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(data.getData());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAsPrimary(String fileName, List<ReplicaLoc> locations) throws RemoteException {
        List<ReplicaServer> replicaServers = new ArrayList<>();
        for (ReplicaLoc replicaLoc : locations) {
            if (replicaLoc.getId() == this.loc.getId()) {
                continue;
            }
            replicaServers.add(getReplicaServer(replicaLoc));
        }
        sameFileReplicas.put(fileName, replicaServers);
    }

    private ReplicaServer getReplicaServer(ReplicaLoc replicaLoc) {
        ReplicaServer replicaServer = null;
        try {
            // TODO: Modify Name to make it consistent.
            replicaServer = (ReplicaServer) registry.lookup("Replica" + replicaLoc.getId());
        } catch (RemoteException e) {
            System.out.println("Invalid Name for Registry Variable");
        } catch (NotBoundException e) {
            System.out.println("NotBoundException for Registry Variable");
        }
        return replicaServer;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

    @Override
    public WriteMsg write(long transactionId, long msgSeqNum, FileContent data) throws RemoteException, IOException {
        return null;
    }

    @Override
    public FileContent read(String fileName) throws FileNotFoundException, IOException, RemoteException {
        return null;
    }

    @Override
    public boolean commit(long transactionId, long numOfMessages) throws MessageNotFoundException, RemoteException {
        return false;
    }

    @Override
    public boolean abort(long transactionId) throws RemoteException {
        return false;
    }
}
