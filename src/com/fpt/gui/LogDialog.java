package com.fpt.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LogDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 525367793092409413L;
	private final JPanel contentPanel = new JPanel();
	private static JTextArea taLog;
	private String dialogTitle;
	private static JScrollPane listScrollPane;
	private static LogDialog logDialog;
	private JButton btnClose;
	
	public static LogDialog getInstance(String dialogTitle){
		if(logDialog==null){
			logDialog = new LogDialog(dialogTitle);
		}
		return logDialog;
	}

	/**
	 * Create the dialog.
	 */
	private LogDialog(String dialogTitle) {
		this.dialogTitle = dialogTitle;
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		taLog = new JTextArea();
		//taLog.setBounds(10, 11, 414, 240);
		//taLog.setSize(500, 500);
		listScrollPane = new JScrollPane(taLog);
		listScrollPane.setBounds(1, 1, 483, 223);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(listScrollPane);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logDialog = null;
				dispose();
			}
		});
		btnClose.setBounds(326, 235, 89, 23);
		contentPanel.add(btnClose);
		setTitle(dialogTitle);
		//setSize(500, 292);
		//pack();
		setBackground(Color.gray);
		//setModal(true);
		//setLocationRelativeTo(parentPanel);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public static void updateLog(String text) {
		if (taLog != null) {
			taLog.append(text + "\n");
		}
	}
}
