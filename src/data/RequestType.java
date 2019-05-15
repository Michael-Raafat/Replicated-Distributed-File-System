package data;
/**
* CS 432: Distributed Systems.
* Assignment 3: Relpicated File System
* @author Michael
* May 12, 2019
**/
public enum RequestType {
	WRITE, READ, COMMIT, ABORT, BEGIN;
	
	public static RequestType toRequestType(String s) {
		if (s.equals("R")) {
			return READ;
		} else if (s.equals("W")) {
			return WRITE;
		} else if (s.equals("C")) {
			return COMMIT;
		} else if (s.equals("A")) {
			return ABORT;
		} else if (s.equals("B")) {
			return BEGIN;
		}
		return null;
	}
}
