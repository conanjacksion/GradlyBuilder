package com.fpt.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import com.fpt.gui.LocalBrowser.LocalBrowserListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BrowseDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432339156423377712L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfUrl;
	private BrowseDialogListener listener;
	private String defaultUrl;

	public interface BrowseDialogListener {
		void onBrowseDialogResultAlready(String url);
	}

	public void addBrowseDialogListener(BrowseDialogListener listener) {
		this.listener = listener;
	}

	/**
	 * Create the dialog.
	 */
	public BrowseDialog(String defaultUrl) {
		this.defaultUrl = defaultUrl;
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 450, 169);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			tfUrl = new JTextField();
			tfUrl.setBounds(57, 41, 268, 20);
			contentPanel.add(tfUrl);
			tfUrl.setColumns(10);
			tfUrl.setText(defaultUrl);
		}

		JLabel lblUrl = new JLabel("Path");
		lblUrl.setBounds(22, 44, 36, 14);
		contentPanel.add(lblUrl);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new LocalBrowser(contentPanel, tfUrl, JFileChooser.DIRECTORIES_ONLY,
				null, 0));
		btnBrowse.setBounds(335, 40, 89, 23);
		contentPanel.add(btnBrowse);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						listener.onBrowseDialogResultAlready(tfUrl.getText()
								.trim());
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void showDialog() {
		setVisible(true);
	}
}
