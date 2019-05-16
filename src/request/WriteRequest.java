package request;

import java.util.List;

import data.RequestType;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 12, 2019
**/
public class WriteRequest implements Request {

	private String fileName;
	private List<String> data;
	private int transactionNum;
	
	public WriteRequest(String fileName, List<String> data, int transactionNum) {
		this.fileName = fileName;
		this.data = data;
		this.transactionNum = transactionNum;
	}
	
	@Override
	public RequestType getType() {
		return RequestType.WRITE;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getData() {
		return data;
	}
	
	@Override
	public int getTransactionNum() {
		return transactionNum;
	}
}
