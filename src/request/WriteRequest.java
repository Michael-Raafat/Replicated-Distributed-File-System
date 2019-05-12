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
	
	public WriteRequest(String fileName, List<String> data) {
		this.fileName = fileName;
		this.data = data;
	}
	
	@Override
	public RequestType getType() {
		return RequestType.READ;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getData() {
		return data;
	}
	
	
}