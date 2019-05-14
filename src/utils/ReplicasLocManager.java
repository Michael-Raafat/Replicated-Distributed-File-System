package utils;

import data.ReplicaLoc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Saturday, 11 May 2019
 */
public class ReplicasLocManager {
    List<ReplicaLoc> replicasLocs;
    private Random rand = new Random();

    private static ReplicasLocManager ourInstance = new ReplicasLocManager();

    public static ReplicasLocManager getInstance() {
        return ourInstance;
    }

    private ReplicasLocManager() {
        this.replicasLocs = new ArrayList<>();
        this.buildReplicasInfo();
    }

    private void buildReplicasInfo() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("babies.info"));
            String line = reader.readLine();
            while(line != null) {
                String[] tokens = line.split("\\s+");
                this.replicasLocs.add( buildReplicaLoc(tokens));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ReplicaLoc buildReplicaLoc(String[] tokens) {
        return new ReplicaLoc(Long.valueOf(tokens[0]), tokens[1], tokens[2], true);
    }

    List<ReplicaLoc> getRandomReplicas(int count) {
        Set<ReplicaLoc> replicaLocSet = new HashSet<>();
        while (replicaLocSet.size() != count) {
            replicaLocSet.add(this.replicasLocs.get(rand.nextInt(count)));
        }

        return new ArrayList<>(replicaLocSet);
    }

    public List<ReplicaLoc> getReplicasLocs() {
        return replicasLocs;
    }
}
