package data;
import java.io.Serializable;

public class FileContent implements Serializable {
	
	private String filename;
	private String data;
	private boolean error;
	
	public FileContent(String filename, String data) {
		this.data = data;
		this.filename = filename;
		this.error = false;
	}
	
	public FileContent(boolean error) {
		this.data = null;
		this.filename = null;
		this.error = error;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
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
