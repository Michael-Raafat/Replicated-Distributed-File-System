package utils;

import data.FileMeta;
import data.ReplicaLoc;
import exceptions.MessageNotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * CS 432: Distributed Systems.
 * Assignment 3: Relpicated File System
 *
 * @author Marc Magdi
 * Tuesday, 07 May 2019
 */
public class FilesMetaManager {
    private static FilesMetaManager filesMetaManager;
    private Hashtable<String, FileMeta> filesMeta;

    private FilesMetaManager(){
        this.filesMeta = new Hashtable<>();
        readMetasFromDisk();
    }

    private void readMetasFromDisk() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("files_meta.txt"));
            String line = reader.readLine();
            while(line != null) {
                String[] tokens = line.split("\t");
                FileMeta fileMeta = buildFileMeta(tokens);
                this.filesMeta.put(fileMeta.getFileName(), fileMeta);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileMeta buildFileMeta(String[] tokens) {
        String fileName = tokens[0];
        List<ReplicaLoc> replicaLocs = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            String[] replicaData = tokens[i].split(":");
            ReplicaLoc replicaLoc = new ReplicaLoc(Long.valueOf(replicaData[0]), replicaData[1]);
            replicaLocs.add(replicaLoc);
        }

        return new FileMeta(fileName, replicaLocs, replicaLocs.get(0));
    }

    public static FilesMetaManager getInstance() {
        if (filesMetaManager == null) filesMetaManager = new FilesMetaManager();
        return filesMetaManager;
    }

    public FileMeta getFileMeta(String fileName) throws FileNotFoundException {
        if (!filesMeta.containsKey(fileName)) throw new FileNotFoundException();
        return filesMeta.get(fileName);
    }

    public void addNewFile(String fileName) {

    }
}
