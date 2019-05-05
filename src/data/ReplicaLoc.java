package data;
import java.io.Serializable;

public class ReplicaLoc implements Serializable {
	
	private long id;
	private String address;
	
	public ReplicaLoc(long id, String address) {
		this.id = id;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

}
