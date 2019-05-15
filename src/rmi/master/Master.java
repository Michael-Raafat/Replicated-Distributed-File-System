package rmi.master;

import data.ReplicaLoc;
import data.WriteMsg;
import utils.FilesMetaManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Master implements MasterServerClientInterface {

    private FilesMetaManager filesMetaManager;
    private AtomicInteger seq = new AtomicInteger(1);

    public Master() {
        this.filesMetaManager = FilesMetaManager.getInstance();
    }

    @Override
    public List<ReplicaLoc> read(String fileName) throws IOException, RemoteException {
        return this.filesMetaManager.getFileMeta(fileName).getReplicasLoc();
    }

    @Override
    public WriteMsg write(String fileName) throws FileNotFoundException, RemoteException, IOException {
        ReplicaLoc replicaLoc;
        try {
           replicaLoc = this.filesMetaManager.getFileMeta(fileName).getMainReplica();
        } catch (FileNotFoundException e) {
            filesMetaManager.addNewFile(fileName);
            replicaLoc = this.filesMetaManager.getFileMeta(fileName).getMainReplica();
        }

        return new WriteMsg(seq.incrementAndGet(), System.currentTimeMillis(),
                replicaLoc);
    }
}
