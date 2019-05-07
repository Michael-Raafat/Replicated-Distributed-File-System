package data;

public class Args {
	private boolean client;
	private String filePath;
	private String masterAdd;
	private String masterPort;
	private String dirPath;
	
	public Args(String masterAdd, String masterPort, String dirPath) {
		client = false;
		this.masterAdd = masterAdd;
		this.masterPort = masterPort;
		this.dirPath = dirPath;
	}
	
	public Args(String filePath) {
		client = true;
		this.filePath = filePath;
	}
	
	
	
	public boolean isClient() {
		return client;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getMasterAdd() {
		return masterAdd;
	}

	public String getMasterPort() {
		return masterPort;
	}

	public String getDirPath() {
		return dirPath;
	}

	public String toString() {
		if (client) {
			return filePath;
		} else {
			return "-ip " + masterAdd +  "-port " + masterPort + "-dir " +  dirPath;
		}
	}
}
