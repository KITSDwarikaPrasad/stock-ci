package com.kfplc.ci.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CommandRunner {

	
	public static void main(String[] args) {
		try {
			runShellCommand( "command", null, null);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param script
	 * @param envVar
	 * @param chdirTo
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String runShellCommand(String script, String[] envVar, String chdirTo) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process p;
		if(chdirTo == null) {
			p = Runtime.getRuntime().exec(script, envVar, null);
		} else {
			p = Runtime.getRuntime().exec(script, envVar, new File(chdirTo));
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
			}
		}).start();

		p.waitFor();
		return null;
	}

	public static void runShellCommandPB( Map<String, String> envVarMap, String... command) throws IOException {
		// TODO Auto-generated method stub
		ProcessBuilder pb = new ProcessBuilder(command);
		Map<String, String> pbEnvMap = System.getenv();

		envVarMap.put("Path", pbEnvMap.get("Path").concat("C:\\Users\\prasad01\\tools\\python\\WinPython-64bit-3.6.0.1\\scripts"));
		pb.start();
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
