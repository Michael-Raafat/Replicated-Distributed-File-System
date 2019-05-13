package configurations;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
* CS 432: Distributed Systems.
* Assignment 3: Replicated File System
* @author Michael
* May 9, 2019
**/
public class MainConfigurations {
	private int numberOfClients;
	private String masterAdd;
	private String masterPort;
	private String masterDir;
	private String masterUsername;
	private String masterPassword;
	private String[] clientUserNames, clientPasswords, clientAdds;
	private String[] filePaths;
	private boolean error = false;
	
	public MainConfigurations(String filename) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			HashMap<String, String> properties = new HashMap<String, String>();
			for (int i = 0; i < lines.size(); i++) {
				String s = lines.get(i);
				properties.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
			}
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
		} catch (Exception e) { 
			error = true;
	    	e.printStackTrace();
	    }
	}
	
	public int getNumberOfClients() {
		return numberOfClients;
	}
	public String[] getFilePaths() {
		return filePaths;
	}

	public String getMasterAdd() {
		return masterAdd;
	}
	public boolean isError() {
		return error;
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
