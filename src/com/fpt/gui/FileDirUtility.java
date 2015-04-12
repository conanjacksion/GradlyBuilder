package com.fpt.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Basic class to show a file choose for user.
 * 
 * @author QuyPP1
 *
 */
public class FileDirUtility extends JFileChooser {
	public FileDirUtility() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method use to open chooser for user to choose and return url of
	 * directory
	 * 
	 * @return -Directory path
	 */
	public String openDirectoryChooser() {
		this.setCurrentDirectory(new File("."));
		this.setDialogTitle("Choose your path");
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.setAcceptAllFileFilterUsed(false);
		int returnValue = this.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return this.getSelectedFile().getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * This method use to open chooser for user to choose and return url of file
	 * selected
	 * 
	 * @return -File selected path
	 */
	public String openFileChooser() {
		this.setCurrentDirectory(new File("."));
		this.setDialogTitle("Choose your file");
		int returnValue = this.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return this.getSelectedFile().getAbsolutePath();
		} else {
			return null;
		}
	}

	public void validateDir(String dir) {
		File directory = new File(dir);
		if (!directory.exists()) {
			JOptionPane.showMessageDialog(null,
					"Error: Please choose correct directory for your project",
					"Wrong Directory", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}
}
