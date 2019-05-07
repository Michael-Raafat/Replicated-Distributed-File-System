package utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SystemConfiguration {
	
	
	private int numberOfReplicas;
	private int numberOfClients;
	private String[] filePaths, serverAdds;
	/**
	 * server properties if false
	 * client properties if true
	 */
	private boolean client;
	private String masterAdd;
	private boolean error = false;
	public SystemConfiguration (String filename, boolean client) {
		this.client = client;
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			HashMap<String, String> properties = new HashMap<String, String>();
			for (int i = 0; i < lines.size(); i++) {
				String s = lines.get(i);
				properties.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
			}
			if (client) {
				if (properties.containsKey("master.server.ip")) {
					masterAdd = properties.get("master.server.ip");
				} else {
					error = true;
			    	System.out.println("Error! missing address of main server!");
			    	return;
				}
				if (properties.containsKey("number.of.clients")) {
					numberOfClients = Integer.parseInt(
							properties.get("number.of.clients").trim());
				} else {
					error = true;
			    	System.out.println("Error! missing number of clients!");
			    	return;
				}
				filePaths = new String[numberOfClients];
				for (int i = 0; i < numberOfClients; i++) {
					if (properties.containsKey("client" + i)) {
						filePaths[i] = properties.get("client" + i ).trim();
					} else {
						error = true;
				    	System.out.println("Error! missing file path of client!");
				    	return;
					}
				}
			} else {
				if (properties.containsKey("number.of.replicas")) {
					numberOfReplicas = Integer.parseInt(
							properties.get("number.of.replicas").trim());
				} else {
					error = true;
			    	System.out.println("Error! missing number of replicas!");
			    	return;
				}
				serverAdds = new String[numberOfReplicas];
				for (int i = 0; i < numberOfReplicas; i++) {
					if (properties.containsKey("server" + i)) {
						serverAdds[i] = properties.get("server" + i ).trim();
					} else {
						error = true;
				    	System.out.println("Error! missing ip address of server");
				    	return;
					}
				}
			}
		} catch (Exception e) { 
	    	e.printStackTrace();
	    }
	}
	public int getNumberOfReplicas() {
		return numberOfReplicas;
	}
	public int getNumberOfClients() {
		return numberOfClients;
	}
	public String[] getFilePaths() {
		return filePaths;
	}
	public String[] getServerAdds() {
		return serverAdds;
	}
	public boolean isClient() {
		return client;
	}
	public String getMasterAdd() {
		return masterAdd;
	}
	public boolean isError() {
		return error;
	}
}
