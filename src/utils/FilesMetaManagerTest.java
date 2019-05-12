package utils;

import data.FileMeta;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Sunday, 12 May 2019
 */

class FilesMetaManagerTest {

    @Test
    void addNewFile() throws IOException {
        String fileName = "helloWorld";
        FilesMetaManager filesMetaManager = FilesMetaManager.getInstance();
        filesMetaManager.addNewFile(fileName);
        FileMeta fileMeta = filesMetaManager.getFileMeta(fileName);
        assertNotNull(fileMeta);
        assertNotNull(fileMeta.getReplicasLoc());
        assertNotNull(fileMeta.getMainReplica());
        System.out.println(fileMeta.toString());
    }
}
