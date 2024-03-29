package request;

import data.RequestType;

/**
* CS 432: Distributed Systems.
* Assignment 3: Relpicated File System
* @author Michael
* May 15, 2019
**/
public class BeginTransactionRequest implements Request {

	private String fileName;
	private int transactionNum;

	public BeginTransactionRequest(String fileName, int transactionNum) {
		this.transactionNum = transactionNum;
		this.fileName = fileName;
	}
	
	public BeginTransactionRequest(int transactionNum) {
		this.transactionNum = transactionNum;
	}
	@Override
	public RequestType getType() {
		return RequestType.BEGIN;
	}

	@Override
	public int getTransactionNum() {
		return transactionNum;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
