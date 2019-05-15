package args;

import data.ReplicaLoc;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 14, 2019
**/
public class ReplicaArgs implements Args{
 
	private String address;
	private int port;
	private String dir;
	
	public ReplicaArgs(ReplicaLoc loc) {
		address = loc.getAddress();
		port = loc.getPort();
		dir = "Replica" + loc.getId();
	}
	
	@Override
	public String toString() {
		return "-ip " + address + " -port " + port + " -dir " + dir;
	}
}
