package com.fpt.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fpt.model.BaseDependency;

public class ChooserDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6210220294367153334L;
	private JButton btnOK;
	private JButton btnCancel;
	private JList<Object> listBox;
	private JScrollPane listScrollPane;
	private JPanel nexusPanel;
	private JPanel parentPanel;
	private String dialogTitle;
	private Object[] listData;
	private ChooserDialogListener listener;

	public interface ChooserDialogListener {
		void onChooserDialogResultAlready(Object dependency);
	}

	public void addChooserDialogListener(ChooserDialogListener listener) {
		this.listener = listener;
	}

	public ChooserDialog(JPanel parentPanel, String dialogTitle,
			Object[] listData) {
		this.parentPanel = parentPanel;
		this.dialogTitle = dialogTitle;
		this.listData = listData;
		initialize();
	}

	public void setListData() {

	}

	private void initialize() {
		// Create a new listbox control
		listBox = new JList<Object>();
		listBox.setVisibleRowCount(5);
		listBox.setListData(listData);
		listScrollPane = new JScrollPane();
		listScrollPane.setBounds(1, 1, 483, 223);
		listScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setViewportView(listBox);
		listBox.validate();
		listScrollPane.validate();
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(382, 227, 81, 23);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOK = new JButton("OK");
		btnOK.setBounds(291, 227, 81, 23);
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					listener.onChooserDialogResultAlready(listBox.getSelectedValue());
					dispose();
				}
			}
		});
		nexusPanel = new JPanel();
		nexusPanel.setLayout(null);
		nexusPanel.add(listScrollPane);
		nexusPanel.add(btnCancel);
		nexusPanel.add(btnOK);
		setTitle(dialogTitle);
		setSize(500, 292);
		setBackground(Color.gray);
		setModal(true);
		setContentPane(nexusPanel);
		setLocationRelativeTo(parentPanel);
		setResizable(false);
	}
	
	public void showDialog(){
		setVisible(true);
	}
}
