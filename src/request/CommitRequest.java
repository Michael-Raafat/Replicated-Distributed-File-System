package request;

import data.RequestType;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 12, 2019
**/
public class CommitRequest implements Request {

	@Override
	public RequestType getType() {
		return RequestType.COMMIT;
	}

}
