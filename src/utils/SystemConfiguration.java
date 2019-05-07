package utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SystemConfiguration {
	
	
	private int numberOfServers;
	private int numberOfClients;
	private String[] filePaths, serverAdds;
	private String[] clientUserNames, clientPasswords, clientAdds;
	/**
	 * server properties if false
	 * cleint properties if true
	 */
	private boolean client;
	private String masterAdd;
	private String masterPort;
	private String masterDir;
	private String masterUsername;
	private String masterPassword;
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
				if (properties.containsKey("master.server.port")) {
					masterPort = properties.get("master.server.port");
				} else {
					error = true;
			    	System.out.println("Error! missing port of main server!");
			    	return;
				}
				if (properties.containsKey("master.server.dir")) {
					masterDir = properties.get("master.server.dir");
				} else {
					error = true;
			    	System.out.println("Error! missing dir of main server!");
			    	return;
				}
				
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
				clientUserNames = new String[numberOfClients];
				clientPasswords = new String[numberOfClients];
				for (int i = 0; i < numberOfClients; i++) {
					if (properties.containsKey("client" + i + ".file")) {
						filePaths[i] = properties.get("client" + i ).trim();
					} else {
						error = true;
				    	System.out.println("Error! missing file path of client!");
				    	return;
					}
					if (properties.containsKey("client" + i)) {
			    		 String kol = properties.get("client" + i);
			    		 int indexU = kol.indexOf("@");
			    		 if (indexU == -1) {
			    			 indexU = kol.length();
			    		 }
			    		 String name = kol.substring(0, indexU);
			    		 
			    		 int indexA = kol.indexOf(" ");
			    		 if (indexA == -1) {
			    			 indexA = kol.length();
			    		 }
			    		 String add = kol.substring(indexU + 1, indexA);
			    		 clientUserNames[i] = name;
			    		 clientAdds[i] = add;
			    		 if (indexA != kol.length()) {
			    			 clientPasswords[i] = kol.substring(indexA + 1);
			    		 } else {
			    			 clientPasswords[i] = "";
			    		 }
			    	  } else {
			    		  error = true;
			    		  System.out.println("Missing client with tag 'client" + i + "'");
			    		  return;
			    	  }
				}
			} else {
				if (properties.containsKey("number.of.servers")) {
					numberOfServers = Integer.parseInt(
							properties.get("number.of.servers").trim());
				} else {
					error = true;
			    	System.out.println("Error! missing number of servers!");
			    	return;
				}
				serverAdds = new String[numberOfServers];
				for (int i = 0; i < numberOfServers; i++) {
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
			error = true;
	    	e.printStackTrace();
	    }
	}
	public int getNumberOfReplicas() {
		return numberOfServers;
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
	public int getNumberOfServers() {
		return numberOfServers;
	}
	public String getMasterPort() {
		return masterPort;
	}
	public String getMasterDir() {
		return masterDir;
	}
	public String getMasterUsername() {
		return masterUsername;
	}
	public String getMasterPassword() {
		return masterPassword;
	}
	public String[] getClientUserNames() {
		return clientUserNames;
	}
	public String[] getClientPasswords() {
		return clientPasswords;
	}
	public String[] getClientAdds() {
		return clientAdds;
	}
	
	
}
