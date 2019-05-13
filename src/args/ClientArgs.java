package args;

public class ClientArgs implements Args {
	private boolean client;
	private String filePath;
	
	
	public ClientArgs(String filePath) {
		client = true;
		this.filePath = filePath;
	}
	
	public boolean isClient() {
		return client;
	}

	public String getFilePath() {
		return filePath;
	}

	public String toString() {
		return filePath;
	}
}
