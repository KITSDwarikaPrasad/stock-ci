package com.kfplc.ci.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

public class CommandRunner {

	public static String output = null;
	public static void main(String[] args) {
		try {
			runShellCommand( "command",  null);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * The method helps in runn the shell command - the implementation used the Runtime.exec()
	 * @param chdirTo - The path where  the script is placed- analogous to cd command
	 * @param command - the actual command to be executed  which arguments like 'sh myscript.sh arg1 arg2' 
	 * @return - console output
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String runShellCommand(String chdirTo, String command) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process p;
		
		if(chdirTo == null) {
			p = Runtime.getRuntime().exec(command);
		} else {
			p = Runtime.getRuntime().exec(command, null, new File(chdirTo));
		}
		new Thread(new Runnable() {
			
			public void run() {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null; 

				try {
					while ((line = input.readLine()) != null)
						System.out.println(line);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = line;
			}
		}).start();

		p.waitFor();
		return output;
	}

	/**
	 * The method helps in runn the shell command - the implementation used the ProcessBuilder and is specific to java8
	 * @param chdirTo - The path where  the script is placed- analogous to cd command
	 * @param command - the actual command to be executed  which arguments like 'sh myscript.sh arg1 arg2' 
	 * @return - console output
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void runShellCommandPB( String chdirTo,  String command) throws IOException {
		// TODO Auto-generated method stub
		ProcessBuilder pb = new ProcessBuilder(command.split(" "));
		Map<String, String> pbEnvMap = System.getenv();
		//System.out.println("pbEnvMap ----> "+pbEnvMap);
		//pbEnvMap.put("PATH", pbEnvMap.get("PATH").concat("C:\\Users\\prasad01\\tools\\python\\WinPython-64bit-3.6.0.1\\scripts;"));
		if(chdirTo != null) {
			pb.directory(new File(chdirTo));
		} 
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		System.out.println("chdirTo: "+chdirTo );
		Process proces = pb.start();
		
	}
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		Process p;
		try  {
			p = Runtime.getRuntime().exec("ipconfig");
			new Thread(new Runnable() {
				public void run() {
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null; 

					try {
						while ((line = input.readLine()) != null)
							System.out.println(line);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

			p.waitFor();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}*/

}
