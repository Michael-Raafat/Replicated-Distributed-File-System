package data;

import java.util.ArrayList;
import java.util.List;

import request.Request;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 12, 2019
**/
public class Transaction {
	
	private List<Request> requests;
	
	public Transaction() {
		this.requests = new ArrayList<Request>();
	}
	
	public void addRequest(Request request) {
		this.requests.add(request); 
	}
}
