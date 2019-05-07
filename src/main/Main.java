package main;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;

import args.Args;
import args.MasterArgs;
import utils.SSHConnection;
import utils.SystemConfiguration;

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
		SystemConfiguration c = new SystemConfiguration("clients.properties", true);
		if (!c.isError()) {
			SSHConnection masterCon =createMaster(c);
			if (mainError) {
				System.out.println("Failed to create server, terminating process !");
				System.exit(-1);
			}
		} else {
			System.out.println("Invalid Configuration");
		}
		System.exit(0);
	}
	
	public static SSHConnection createMaster(SystemConfiguration c) {
		// Creating master server.
		SSHConnection con = new SSHConnection();
		 try {
			Args args = new MasterArgs(c.getMasterAdd(), c.getMasterPort(), c.getMasterDir());
			if (con.openConnection(c.getMasterAdd(), c.getMasterPassword(), c.getMasterUsername(), args, c.getMasterDir())) {
				System.out.println("Created !");
			}
         }catch (Exception e) {
        	mainError = true;
			System.out.println(e.getMessage());
			
		}
		return con;
	}
	
	public static void createClients(SystemConfiguration c) {
		
	}
	
	

}
