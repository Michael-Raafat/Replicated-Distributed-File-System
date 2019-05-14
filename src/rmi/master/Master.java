package rmi.master;

import data.FileMeta;
import data.ReplicaLoc;
import data.TransactionMsg;
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
    public TransactionMsg request_transaction(String fileName) throws IOException, RemoteException {
        FileMeta fileMeta;
        try {
            fileMeta = this.filesMetaManager.getFileMeta(fileName);
        } catch (FileNotFoundException e) {
            filesMetaManager.addNewFile(fileName);
            fileMeta = this.filesMetaManager.getFileMeta(fileName);
        }

        return new TransactionMsg(seq.incrementAndGet(), System.currentTimeMillis(),
                fileMeta.getMainReplica(), fileMeta.getReplicasLoc());
    }
}
