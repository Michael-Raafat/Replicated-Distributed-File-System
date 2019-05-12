package utils;

import data.ReplicaLoc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Sunday, 12 May 2019
 */
class ReplicaLocManagerTest {
    private ReplicasLocManager replicasLocManager = ReplicasLocManager.getInstance();

    @Test
    void loadingReplicasInfo() {
        String[][] replicasInfo = {{"1", "127.0.0.1", "1144"},
                {"2", "127.0.0.1", "1145"}, {"3", "127.0.0.1", "1146"},
                {"4", "127.0.0.1", "1147"}, {"5", "127.0.0.1", "1148"},
                {"6", "127.0.0.1", "1149"}, {"7", "127.0.0.1", "1150"},
                {"8", "127.0.0.1", "1151"}};
        List<ReplicaLoc> replicaLocs = replicasLocManager.replicasLocs;
        for (int i = 0; i < replicaLocs.size(); i++) {
            String[] tokens = replicasInfo[i];
            ReplicaLoc replicaLoc = replicaLocs.get(i);
            Assertions.assertEquals((long) Long.valueOf(tokens[0]), replicaLoc.getId());
            Assertions.assertEquals(tokens[1], replicaLoc.getAddress());
            Assertions.assertEquals(tokens[2], replicaLoc.getPort());
        }
    }

    @Test
    void testGet3Replicas() {
        List<ReplicaLoc> replicasLoc = replicasLocManager.getRandomReplicas(3);
        Assertions.assertEquals(replicasLoc.size(), 3);
        for (ReplicaLoc replicaLoc : replicasLoc) {
            Assertions.assertNotNull(replicaLoc.getAddress());
            Assertions.assertNotNull(replicaLoc.getPort());
        }
    }

    @Test
    void testGet5Replicas() {
        List<ReplicaLoc> replicasLoc = replicasLocManager.getRandomReplicas(5);
        Assertions.assertEquals(replicasLoc.size(), 5);
        for (ReplicaLoc replicaLoc : replicasLoc) {
            Assertions.assertNotNull(replicaLoc.getAddress());
            Assertions.assertNotNull(replicaLoc.getPort());
        }
    }
}
