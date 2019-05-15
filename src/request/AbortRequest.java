package request;

import data.RequestType;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 12, 2019
**/
public class AbortRequest implements Request {

	private String fileName;
	private int transactionNum;
	
	public AbortRequest(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public RequestType getType() {
		return RequestType.ABORT;
	}

	public String getFileName() {
		return fileName;
	}

	public int getTransactionNum() {
		return transactionNum;
	}
	

}
