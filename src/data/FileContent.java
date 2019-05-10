package data;
import java.io.Serializable;

public class FileContent implements Serializable {
	
	private String filename;
	private String data;
	
	public FileContent(String filename, String data) {
		this.data = data;
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
