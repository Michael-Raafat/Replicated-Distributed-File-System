package data;

import java.util.List;

/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Tuesday, 07 May 2019
 */
public class FileMeta {
    private String fileName;
    private List<ReplicaLoc> replicasLoc;
    private ReplicaLoc mainReplica;

    public FileMeta(String fileName, List<ReplicaLoc> replicasLoc, ReplicaLoc mainReplica) {
        this.fileName = fileName;
        this.replicasLoc = replicasLoc;
        this.mainReplica = mainReplica;
    }

    public String getFileName() {
        return fileName;
    }

    public List<ReplicaLoc> getReplicasLoc() {
        return replicasLoc;
    }

    public ReplicaLoc getMainReplica() {
        return mainReplica;
    }
}
