package args;

public class MasterArgs implements Args {
	private String masterAdd;
	private String masterPort;
	private String dirPath;
	
	public MasterArgs(String masterAdd, String masterPort, String dirPath) {
		this.masterAdd = masterAdd;
		this.masterPort = masterPort;
		this.dirPath = dirPath;
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
		return "-ip " + masterAdd +  " -port " + masterPort + " -dir " +  dirPath;
	}
}
