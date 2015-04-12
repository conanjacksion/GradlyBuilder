package com.fpt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fpt.gui.BuildManager;
import com.fpt.gui.LogDialog;

public class CommandLine {

	public static void runCmd(final String[] cmd) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String executeCmd = "";
					for (int i = 0; i < cmd.length; i++) {
						executeCmd += cmd[i];
						if (i < cmd.length - 1) {
							executeCmd += " && ";
						}
					}
					ProcessBuilder builder = new ProcessBuilder("cmd.exe",
							"/c", executeCmd);
					builder.redirectErrorStream(true);
					Process p = builder.start();
					BufferedReader r = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String line;
					while (true) {
						line = r.readLine();
						if (line == null) {
							break;
						}
						System.out.println(line);
						BuildManager.updateLog(line);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					BuildManager.updateLog(ex.getMessage());
				}
			}
		});
		thread.start();
	}
}
