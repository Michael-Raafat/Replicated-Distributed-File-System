package data;
import java.io.Serializable;

public class ReplicaLoc implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3946648948561068054L;
	
	private long id;
	private String address;
	private int port;
	private boolean isAlive;

	public ReplicaLoc(long id, String address, int port, boolean isAlive) {
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	@Override
	public String toString() {
		return "ReplicaLoc{" +
				"id=" + id +
				", address='" + address + '\'' +
				", port='" + port + '\'' +
				", isAlive=" + isAlive +
				'}';
	}
}
