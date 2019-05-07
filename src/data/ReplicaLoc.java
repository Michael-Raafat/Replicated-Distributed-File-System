package data;
import java.io.Serializable;

public class ReplicaLoc implements Serializable {
	
	private long id;
	private String address;
	private String port;
	private boolean isAlive;
	public ReplicaLoc(long id, String address, String port, boolean isAlive) {
		this.id = id;
		this.address = address;
		this.port = port;
		this.isAlive = isAlive;
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	
}
