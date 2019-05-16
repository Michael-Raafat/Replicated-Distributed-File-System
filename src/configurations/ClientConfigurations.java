package configurations;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import data.RequestType;
import data.Transaction;
import request.AbortRequest;
import request.BeginTransactionRequest;
import request.CommitRequest;
import request.ReadRequest;
import request.Request;
import request.WriteRequest;
/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 9, 2019
**/
public class ClientConfigurations {
	
	private boolean error = false;
	private String masterAdd;
	private String masterPort;
	private int numberOfTransactions;
	private List<Transaction> transactions;
	private List<Request> requests;
	public ClientConfigurations(String filename) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			HashMap<String, String> properties = new HashMap<String, String>();
			requests = new ArrayList<Request>();
			for (int i = 0; i < lines.size(); i++) {
				String s = lines.get(i);
				int indexU = s.indexOf(" ");
	    		if (indexU == -1) {
	    			error = true;
				    System.out.println("Error! wrong command format!");
				    return;
	    		}
				if (s.substring(0, s.indexOf(" ")).trim().equals("master.port")) {
					masterPort = s.substring(s.indexOf(" ") + 1).trim();
				} else if (s.substring(0, s.indexOf(" ")).trim().equals("master.ip")) {
					masterAdd = s.substring(s.indexOf(" ") + 1).trim();
				} else if (s.substring(0, s.indexOf(" ")).trim().equals("TransactionsNum")) {
					numberOfTransactions = Integer.valueOf(s.substring(s.indexOf(" ") + 1).trim());
				} else if (s.substring(0, s.indexOf(" ")).trim().contains("T")) {
					String request = s.substring(s.indexOf(" ") + 1);
					int indexA = request.indexOf("_");
					String tInfo = s.substring(0, s.indexOf(" ")).trim();
					int transactionNum = Integer.parseInt(tInfo.substring(tInfo.indexOf("T") + 1));
					String requestType;
		    		if (indexA == -1) {
		    			indexA = request.length();
		    			requestType = request.substring(0, indexA).trim();
		    			if (RequestType.toRequestType(requestType) == RequestType.COMMIT) {
			    			requests.add(new CommitRequest(transactionNum));
			    		} else if (RequestType.toRequestType(requestType) == RequestType.ABORT){ 
			    			requests.add(new AbortRequest(transactionNum));
			    		} else if (RequestType.toRequestType(requestType) == RequestType.BEGIN) {
		    				requests.add(new BeginTransactionRequest(transactionNum));
		    			} else {
			    			error = true;
						    System.out.println("Error! transaction wrong format!");
						    return;
			    		}
		    		} else {
		    			requestType = request.substring(0, indexA).trim();
		    			int indexB = request.indexOf("_");
	    				if (indexB == -1) {
	    					error = true;
					    	System.out.println("Error! request wrong format!");
					    	return;
	    				}
	    				String fileName = request.substring(request.indexOf("_") + 1).trim();
						if (RequestType.toRequestType(requestType) == RequestType.COMMIT) {
		    				requests.add(new CommitRequest(fileName, transactionNum));
		    			} else if (RequestType.toRequestType(requestType) == RequestType.ABORT){ 
		    				requests.add(new AbortRequest(fileName, transactionNum));
		    			} else if (RequestType.toRequestType(requestType) == RequestType.WRITE) {
		    				int linesPassed = 1;
		    				String str = lines.get(i + linesPassed);
		    				int numOfData = Integer.parseInt(str);
		    				linesPassed += numOfData;
		    				List<String> data = new ArrayList<>();
		    				for (int j = i+2; j < numOfData + i+2; j++) {
		    					String dataI = lines.get(j);
		    					data.add(dataI);
		    				}
			    			requests.add(new WriteRequest(fileName, data,transactionNum));
			    			i += linesPassed;
		    			} else if (RequestType.toRequestType(requestType) == RequestType.READ) {
		    				requests.add(new ReadRequest(fileName, transactionNum));
		    			} else if (RequestType.toRequestType(requestType) == RequestType.BEGIN) {
		    				requests.add(new BeginTransactionRequest(fileName, transactionNum));
		    			} else {
		    				error = true;
					    	System.out.println("Error! missing request format!");
					    	return;
		    			}
		    		}
		    	} else {
		    		error = true;
				    System.out.println("Error! wrong command format!");
				    return;
				}
			}
			/*if (properties.containsKey("master.server.port")) {
				masterPort = properties.get("master.server.port");
			} else {
				error = true;
		    	System.out.println("Error! missing port of main server!");
		    	return;
			}
			if (properties.containsKey("master.server.ip")) {
				masterAdd = properties.get("master.server.ip");
			} else {
				error = true;
		    	System.out.println("Error! missing address of main server!");
		    	return;
			}
			if (properties.containsKey("number.of.transactions")) {
				numberOfTransactions = Integer.parseInt(
						properties.get("number.of.transactions").trim());
			} else {
				error = true;
		    	System.out.println("Error! missing number of transactions!");
		    	return;
			}
			for (int i = 0; i < numberOfTransactions; i++) {
				int numberOfRequests;
				if (properties.containsKey("number.of.requests.t" + (i+1))) {
					numberOfRequests = Integer.parseInt(
							properties.get("number.of.requests.t" + (i+1)).trim());
				} else {
					error = true;
			    	System.out.println("Error! missing number of transactions!");
			    	return;
				}
				Transaction transaction = new Transaction();
				for (int j = 0; j < numberOfRequests; j++) {
					if (properties.containsKey("transaction." +(i+1) + "." + (j+1) )) {
						
						String request = properties.get("transaction." +(i+1) + "." + (j+1)).trim();
						int indexU = request.indexOf("_");
			    		if (indexU == -1) {
			    			indexU = request.length();
			    		}
			    		String requestType = request.substring(0, indexU);
			    		//////////////////////////////////////////////////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			    		int indexA = request.indexOf(" ");
		    			if (indexA == -1) {
		    				indexA = request.length();
		    			}
		    			String fileName = request.substring(indexU + 1, indexA);
		    			////////////////////////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			    		if (RequestType.toRequestType(requestType) == RequestType.COMMIT) {
			    			transaction.addRequest(new CommitRequest(fileName));
			    		} else if (RequestType.toRequestType(requestType) == RequestType.ABORT){ 
			    			transaction.addRequest(new AbortRequest(fileName));
			    		} else {
//			    			int indexA = request.indexOf(" ");
//			    			if (indexA == -1) {
//			    				indexA = request.length();
//			    			}
//			    			String fileName = request.substring(indexU + 1, indexA);
			    			if (RequestType.toRequestType(requestType) == RequestType.WRITE) {
			    				int indexB = request.indexOf(" ");
				    			if (indexB == -1) {
				    				indexB = request.length();
				    			}
				    			int numberOfData = Integer.parseInt(request.substring(indexA + 1, indexB));
				    			List<String> data = new ArrayList<>(); 
				    			for (int k = 0; k < numberOfData; k++) {
				    				if (properties.containsKey("transaction." +(i+1) + "." + (j+1) + ".data" + (k+1))) {
				    					data.add(properties.get("transaction." +(i+1) + "." + (j+1) + ".data" + (k+1)).trim());
				    				}
				    			}
				    			transaction.addRequest(new WriteRequest(fileName, data));
			    			} else if (RequestType.toRequestType(requestType) == RequestType.READ) {
			    				transaction.addRequest(new ReadRequest(fileName));
			    			} else {
			    				error = true;
						    	System.out.println("Error! missing request format!");
						    	return;
			    			}
			    		}
					} else {
						error = true;
				    	System.out.println("Error! missing number of transactions!");
				    	return;
					}
				}
				
			}*/
		} catch (Exception e) { 
			error = true;
	    	e.printStackTrace();
	    }
	}	
	
	
	public boolean isError() {
		return error;
	}


	public String getMasterAdd() {
		return masterAdd;
	}


	public String getMasterPort() {
		return masterPort;
	}


	public int getNumberOfTransactions() {
		return numberOfTransactions;
	}


	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public List<Request> getRequest() {
		return requests;
	}
	
}
