package com.fpt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LocalBrowser extends JFileChooser implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2371282242453670278L;
	private JPanel parentPanel;
	private JTextField resultContainer;
	private int selectMode;
	private LocalBrowserListener listener;
	private int requestCode;

	public interface LocalBrowserListener {
		void onLocalBrowserResultAlready(String resultURL,
				String resultFileName, int requestCode);
	}

	public void resgiterListener(LocalBrowserListener listener) {
		this.listener = listener;
	}

	public LocalBrowser(JPanel parentPanel, JTextField resultContainer,
			int selectMode, LocalBrowserListener listener, int requestCode) {
		this.parentPanel = parentPanel;
		this.resultContainer = resultContainer;
		this.selectMode = selectMode;
		this.requestCode = requestCode;
		resgiterListener(listener);
	}

	public void actionPerformed(ActionEvent e) {
		setFileSelectionMode(selectMode);
		if (selectMode == JFileChooser.FILES_ONLY) {
			// FileNameExtensionFilter filter = new FileNameExtensionFilter(
			// "JAR File", "jar");
			// setFileFilter(filter);
		}
		File currentDir = null;
		if (resultContainer != null && resultContainer.getText() != null && !resultContainer.getText().equals("")) {
			String currentBrowseUrl = resultContainer.getText() == null ? ""
					: resultContainer.getText();
			if (!currentBrowseUrl.equals("")) {
				currentDir = new File(currentBrowseUrl);
			}
		}else if (AndroidInputPane.getData() != null) {
			String currentBrowseUrl = AndroidInputPane.getData()
					.getProjectURL() == null ? "" : AndroidInputPane.getData()
					.getProjectURL();
			if (!currentBrowseUrl.equals("")) {
				currentDir = new File(currentBrowseUrl);
			}
		}
		if (currentDir != null) {
			setCurrentDirectory(currentDir);
		}
		int rVal = showOpenDialog(parentPanel);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			// fileChoseName = c.getSelectedFile().getName();
			// String resultURL = getCurrentDirectory().toString() + "\\"
			// + getSelectedFile().getName();
			String resultFileName = getSelectedFile().getName();
			String resultURL = getSelectedFile().getAbsolutePath();
			if (resultContainer != null) {
				resultContainer.setText(resultURL);
			}
			if (listener != null) {
				listener.onLocalBrowserResultAlready(resultURL, resultFileName,
						requestCode);
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			// Stuff to do when user click cancel button
		}
	}
}
