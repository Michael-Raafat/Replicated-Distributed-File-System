package configurations;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
* CS 432: Distributed Systems.
* Assignment 3: Relpicated File System
* @author Michael
* May 9, 2019
**/
public class ClientConfigurations {
	
	private boolean error = false;
	
	public ClientConfigurations(String filename) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			HashMap<String, String> properties = new HashMap<String, String>();
			for (int i = 0; i < lines.size(); i++) {
				String s = lines.get(i);
				properties.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
			}
			
		} catch (Exception e) { 
			error = true;
	    	e.printStackTrace();
	    }
	}	
	
	
	public boolean isError() {
		return error;
	}
}
