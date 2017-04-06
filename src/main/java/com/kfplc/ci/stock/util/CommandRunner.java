package com.kfplc.ci.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandRunner {

	
	public static void main(String[] args) {
		try {
			runShellCommand( "command", null);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String runShellCommand(String script, String chdirTo) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process p;

		p = Runtime.getRuntime().exec(script, null, new File(chdirTo));
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
