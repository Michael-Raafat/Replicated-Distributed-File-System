package data;
import java.io.Serializable;

public class FileContent implements Serializable {
	
	private String filename;
	private byte[] data;
	
	public FileContent(String filename, byte[] data) {
		this.data = data;
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
