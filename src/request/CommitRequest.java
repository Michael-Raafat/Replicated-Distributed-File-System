package request;

import data.RequestType;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 12, 2019
**/
public class CommitRequest implements Request {
	
	private String fileName;
	private int transactionNum;
	
	public CommitRequest(String fileName, int transactionNum) {
		this.fileName = fileName;
		this.transactionNum = transactionNum;
	}

	@Override
	public RequestType getType() {
		return RequestType.COMMIT;
	}

	public String getFileName() {
		return fileName;
	}
	@Override
	public int getTransactionNum() {
		return transactionNum;
	}
}
