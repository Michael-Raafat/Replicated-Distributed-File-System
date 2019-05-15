package utils;

import data.FileMeta;
import data.ReplicaLoc;

import java.io.*;
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
    private ReplicasLocManager replicasLocManager;
    private FilesMetaManager(){
        this.filesMeta = new Hashtable<>();
        this.replicasLocManager = ReplicasLocManager.getInstance();
        readMetasFromDisk();
    }
    private static String META_FILE = "files_meta.txt";
    /**.
     * Build metadata of all files from database text file.
     * Data is on the following format: - no spaces -
     * file_name \t replica_data1 \t replica_data2
     * replica_data => id:address:port
     */
    private void readMetasFromDisk() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(META_FILE));
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
            ReplicaLoc replicaLoc = new ReplicaLoc(Long.valueOf(replicaData[0]), replicaData[1], Integer.parseInt(replicaData[2]),true);
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

    public void addNewFile(String fileName) throws IOException {
        List<ReplicaLoc> replicaLocList = replicasLocManager.getRandomReplicas(3);
        FileMeta fileMeta = new FileMeta(fileName, replicaLocList, replicaLocList.get(0));
        this.filesMeta.put(fileName, fileMeta);
        this.addMetaToDisk(fileMeta);
    }

    private void addMetaToDisk(FileMeta fileMeta) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fileMeta.getFileName());
        for(int i = 0; i < fileMeta.getReplicasLoc().size(); i++) {
            stringBuilder.append('\t');
            ReplicaLoc replicaLoc = fileMeta.getReplicasLoc().get(i);
            stringBuilder.append(replicaLoc.getId()).append(':').append(replicaLoc.getAddress())
                    .append(':').append(replicaLoc.getPort());
        }

        FileWriter writer = new FileWriter(META_FILE, true);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(stringBuilder.append('\n').toString());
        buffer.close();
    }
}
