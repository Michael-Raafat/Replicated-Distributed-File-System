package rmi.master;

import rmi.replica.ReplicaServer;
import rmi.replica.ReplicaServerMasterInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.util.List;

/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Wednesday, 15 May 2019
 */
public class HeartBeats implements Runnable {
    private List<ReplicaServerMasterInterface> replicas;

    public HeartBeats(List<ReplicaServerMasterInterface> replicas) {
        this.replicas = replicas;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            for (ReplicaServerMasterInterface replica : replicas) {
                i++;
                try {
                    replica.isAlive();
                    System.out.println("Replica" + i + " is alive!");
                } catch (RemoteException e) {
                    // TODO implement error handling
                    e.printStackTrace();
                }
            }
            System.out.println("=========================================================");
        }
    }
}
