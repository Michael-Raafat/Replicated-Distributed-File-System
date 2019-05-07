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
    public List<ReplicaLoc> read(String fileName) throws FileNotFoundException, IOException, RemoteException {
        return this.filesMetaManager.getFileMeta(fileName).getReplicasLoc();
    }

    @Override
    public WriteMsg write(String fileName) throws FileNotFoundException, RemoteException, IOException {
        // TODO handle new file
        WriteMsg writeMsg = new WriteMsg(seq.incrementAndGet(), System.currentTimeMillis(),
                this.filesMetaManager.getFileMeta(fileName).getMainReplica());

        return writeMsg;
    }
}
