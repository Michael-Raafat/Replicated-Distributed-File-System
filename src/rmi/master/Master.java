package rmi.master;

import data.ReplicaLoc;
import data.WriteMsg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class Master implements MasterServerClientInterface {

    @Override
    public List<ReplicaLoc> read(String fileName) throws FileNotFoundException, IOException, RemoteException {
        return null;
    }

    @Override
    public WriteMsg write(String fileName) throws RemoteException, IOException {
        return null;
    }

    @Override
    public ReplicaLoc assignPrimaryReplica(String fileName) throws FileNotFoundException, RemoteException {
        return null;
    }
}
