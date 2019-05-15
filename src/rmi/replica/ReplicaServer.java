package rmi.replica;

import data.FileContent;
import data.ReplicaLoc;
import data.TransactionMsg;
import exceptions.MessageNotFoundException;
import utils.RMIUtils;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReplicaServer implements ReplicaServerGeneralInterface {
    private String path;
    private Map<String, List<ReplicaServer>> sameFileReplicas;
    private Map<String, ReentrantReadWriteLock> locks;
    private Map<Long, String> transactionFiles;
    private Map<String, Set<Long>> runningTransactions;
    private Map<Long, Map<Long, String>> transactionWrites;
    private Lock metaLock;
    private RMIUtils rmiUtils;

    public ReplicaServer(String path) {
        this.path = path;
        this.sameFileReplicas = new ConcurrentHashMap<>();
        this.locks = new ConcurrentHashMap<>();
        this.transactionFiles = new HashMap<>();
        this.transactionWrites = new TreeMap<>();
        this.runningTransactions = new HashMap<>();
        this.metaLock = new ReentrantLock(true);
        this.rmiUtils = new RMIUtils();
        createDirectory();
    }

    private void createDirectory() {
        File file = new File(this.path);
        if (!file.exists()){
            boolean flag = file.mkdir();
            System.out.println("Directory created: " + flag);
        }
    }

    @Override
    public boolean updateReplicas(String fileName, List<String> writes) throws RemoteException, IOException {
        locks.get(fileName).writeLock().lock();
        File file = new File(path + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        for (String write : writes) {
            fileWriter.append(write);
        }
        fileWriter.close();
        locks.get(fileName).writeLock().unlock();
        return true;
    }

    private void initializeFileLock(String fileName) throws RemoteException {
        synchronized (locks) {
            if (!locks.containsKey(fileName)) {
                locks.put(fileName, new ReentrantReadWriteLock());
            }
        }
    }

    private boolean addTransactionToFile(String filename, Long transactionId) {
    	Boolean allowed = true;
    	this.metaLock.lock();
    	if (!runningTransactions.containsKey(filename)) {
    		runningTransactions.put(filename, new HashSet<Long>());
    	} else {
    		allowed = runningTransactions.get(filename).size() == 1 && runningTransactions.get(filename).contains(transactionId);
    	}
    	if (allowed) {
    		runningTransactions.get(filename).add(transactionId);
    	}
    	this.metaLock.unlock();
    	return allowed;
    }
    
    private boolean checkConccurencyToFile(String filename, Long transactionId) {
    	Boolean allowed = true;
    	this.metaLock.lock();
    	if (runningTransactions.containsKey(filename)) {
    		allowed = runningTransactions.get(filename).size() == 1 && runningTransactions.get(filename).contains(transactionId);
    	}
    	this.metaLock.unlock();
    	return allowed;
    }
    
    private void removeTransactionToFile(String filename, Long transactionId) {
    	this.metaLock.lock();
    	runningTransactions.get(filename).remove(transactionId);
    	this.metaLock.unlock();
    }
    
    private void setAsPrimary(String fileName, List<ReplicaLoc> locations) throws RemoteException {
        List<ReplicaServer> replicaServers = new ArrayList<>();
        for (ReplicaLoc replicaLoc : locations) {
            replicaServers.add(rmiUtils.getReplicaServer(replicaLoc));
        }
        sameFileReplicas.put(fileName, replicaServers);
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

    private String getUnCommittedContent(Map<Long, String> transactionWrites) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Long, String> entry : transactionWrites.entrySet()) {
            builder.append(entry.getValue());
        }
        return builder.toString();
    }

    private String getFileContent(String fileName) throws IOException {
        File file = new File(path + "/" + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        locks.get(fileName).readLock().lock();
        String content = getContentFromFile(file);
        locks.get(fileName).readLock().unlock();
        return content;
    }

    private String getContentFromFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        FileReader fileStream = new FileReader(file);
        
        BufferedReader bufferedReader = new BufferedReader(fileStream);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line + System.lineSeparator());
        }
        bufferedReader.close();
        fileStream.close();
        return builder.toString();
    }

    private List<String> getListFromMapValues(Map<Long, String> map) {
        List<String> writes = new ArrayList<>();
        for (Map.Entry<Long, String> entry : map.entrySet()) {
            writes.add(entry.getValue());
        }
        return writes;
    }

    @Override
    public Boolean write(TransactionMsg transactionMsg, long msgSeqNum, FileContent data) throws RemoteException, IOException {
        initializeFileLock(data.getFilename());
        initializeSameFileReplicas(transactionMsg, data.getFilename());
        String fileName = data.getFilename();
        Long transactionId = transactionMsg.getTransactionId();
        boolean allowed = addTransactionToFile(fileName, transactionId);
        if (!allowed) {
        	return false;
        }
        if (!transactionFiles.containsKey(transactionId)) {
            transactionFiles.put(transactionId, fileName);
            transactionWrites.put(transactionId, new TreeMap<Long, String>());
        }
        transactionWrites.get(transactionId).put(msgSeqNum, data.getData());
        return true;
    }

    @Override
    public FileContent read(TransactionMsg transactionMsg, long msgSeqNum, String fileName) throws FileNotFoundException, IOException, RemoteException {
        initializeFileLock(fileName);
        initializeSameFileReplicas(transactionMsg, fileName);
        Long transactionId = transactionMsg.getTransactionId();
        boolean allowed = checkConccurencyToFile(fileName, transactionId);
        if (!allowed) {
        	return new FileContent(true);
        }
        File file = new File(path + "/" + fileName);
        if (transactionWrites.containsKey(transactionId)) {
            String initialContent = getFileContent(fileName);
            String unCommittedContent = getUnCommittedContent(transactionWrites.get(transactionId));
            return new FileContent(fileName, initialContent + unCommittedContent);
        }

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        String data = getFileContent(fileName);
        return new FileContent(fileName, data);
    }

    private void initializeSameFileReplicas(TransactionMsg transactionMsg, String fileName) throws RemoteException {
        synchronized (sameFileReplicas) {
            if (!sameFileReplicas.containsKey(fileName)) {
                setAsPrimary(fileName, transactionMsg.getReplicas());
            }
        }
    }

    @Override
    public boolean commit(TransactionMsg transactionMsg) throws MessageNotFoundException, IOException {
        Long transactionId = transactionMsg.getTransactionId();
        removeTransactionToFile(transactionFiles.get(transactionId), transactionId);
        if (transactionFiles.containsKey(transactionId)) {
            Map<Long, String> writes = transactionWrites.get(transactionId);
            String fileName = transactionFiles.get(transactionId);
            List<ReplicaServer> replicas = sameFileReplicas.get(fileName);
            for (ReplicaServer replica : replicas) {
                List<String> writeMessages = getListFromMapValues(writes);
                replica.updateReplicas(fileName, writeMessages);
            }
            transactionWrites.remove(transactionId);
            transactionFiles.remove(transactionId);
        }
        return true;
    }

    @Override
    public boolean abort(TransactionMsg transactionMsg) throws RemoteException {
        Long transactionId = transactionMsg.getTransactionId();
        removeTransactionToFile(transactionFiles.get(transactionId), transactionId);
        transactionWrites.remove(transactionId);
        transactionFiles.remove(transactionId);
        return true;
    }
}
