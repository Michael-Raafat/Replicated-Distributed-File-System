package main;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;

import args.Args;
import args.ClientArgs;
import args.MasterArgs;
import configurations.MainConfigurations;
import utils.SSHConnection;

public class Main {
	static boolean mainError = false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean rmi = false;
		if (args.length > 0 && args[0].equals("rmi")) {
			System.out.println("Running in RMI mode");
			rmi = true;
		} else {
			System.out.println("Running in socket mode");
		}
		MainConfigurations c = new MainConfigurations("main.properties");
		if (!c.isError()) {
			SSHConnection masterCon =createMaster(c);
			if (mainError) {
				System.out.println("Failed to create server, terminating process !");
				System.exit(-1);
			}
			SSHConnection clientsCon =createClients(c);
			if (mainError) {
				System.out.println("Failed to create server, terminating process !");
				System.exit(-1);
			}
		} else {
			System.out.println("Invalid Configuration");
		}
		System.exit(0);
	}
	
	public static SSHConnection createMaster(MainConfigurations c) {
		// Creating master server.
		SSHConnection con = new SSHConnection();
		 try {
			Args args = new MasterArgs(c.getMasterAdd(), c.getMasterPort(), c.getMasterDir());
			String path = System.getProperty("user.dir");
			if (con.openConnection(c.getMasterAdd(), c.getMasterPassword(), c.getMasterUsername(), args, path)) {
				System.out.println("Master Created !");
			}
         }catch (Exception e) {
        	mainError = true;
			System.out.println(e.getMessage());
			
		}
		return con;
	}
	
	public static SSHConnection createClients(MainConfigurations c) {
		SSHConnection con = new SSHConnection();
		for (int i = 0; i < c.getNumberOfClients(); i++) {
			try {
				Args args = new ClientArgs(c.getFilePaths()[i]);
				String path = System.getProperty("user.dir");
				if (con.openConnection(c.getClientAdds()[i], c.getClientPasswords()[i], c.getClientUserNames()[i], args, path)) {
					System.out.println("Clients Created !");
				}
			} catch (Exception e) {
        		mainError = true;
				System.out.println(e.getMessage());
			}
		}
		return con;
	}
	
	

}
