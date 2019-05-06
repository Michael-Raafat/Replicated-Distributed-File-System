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
	private String[] filePaths, writersAdd;
	/**
	 * server properties if false
	 * cleint properties if true
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
				for (int i = 0; i < numberOfClients; i++) {
					if (properties.containsKey("client" + i)) {
						
					}
				}
			}
		} catch (Exception e) { 
	    	e.printStackTrace();
	    }
	}
}
