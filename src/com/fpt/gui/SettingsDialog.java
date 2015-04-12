package com.fpt.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fpt.model.Module;
import com.fpt.model.SettingsData;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3822659508101627521L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfAndroidSDKUrl;
	private SettingsData data;
	private JTextField tfRepositoryId;
	private JTextField tfGradleUrl;
	private JTextField tfRepUsername;
	private JTextField tfRepPassword;
	private JTextField tfWorkspaceUrl;
	private JTextField tfNexusUserName;
	private JTextField tfNexusPassword;
	private JTextField tfNexusUrl;

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		initialize();
	}

	private void initialize() {
		data = new SettingsData();
		data.readFromFile();
		setTitle("Settings");
		setBounds(100, 100, 450, 332);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblAndroidSDKUrl = new JLabel("SDK Path");
		lblAndroidSDKUrl.setBounds(10, 11, 72, 14);
		contentPanel.add(lblAndroidSDKUrl);

		JLabel lblRepositoryUrl = new JLabel("Repository");
		lblRepositoryUrl.setBounds(10, 156, 72, 14);
		contentPanel.add(lblRepositoryUrl);

		tfAndroidSDKUrl = new JTextField();
		tfAndroidSDKUrl.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setAndroidSDKURL(tfAndroidSDKUrl.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setAndroidSDKURL(tfAndroidSDKUrl.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setAndroidSDKURL(tfAndroidSDKUrl.getText().trim());
					}
				});
		tfAndroidSDKUrl.setBounds(84, 8, 251, 20);
		contentPanel.add(tfAndroidSDKUrl);
		tfAndroidSDKUrl.setColumns(10);
		tfAndroidSDKUrl.setText(data.getAndroidSDKURL());

		JButton btnBrowseAndroidSDKUrl = new JButton("Browse");
		btnBrowseAndroidSDKUrl.setBounds(345, 7, 89, 23);
		btnBrowseAndroidSDKUrl.addActionListener(new LocalBrowser(contentPanel,
				tfAndroidSDKUrl, JFileChooser.DIRECTORIES_ONLY, null, 0));
		contentPanel.add(btnBrowseAndroidSDKUrl);

		tfRepositoryId = new JTextField();
		tfRepositoryId.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setRepository(tfRepositoryId.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setRepository(tfRepositoryId.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setRepository(tfRepositoryId.getText().trim());
			}
		});
		tfRepositoryId.setBounds(84, 153, 251, 20);
		contentPanel.add(tfRepositoryId);
		tfRepositoryId.setColumns(10);
		tfRepositoryId.setText(data.getRepository());

		JLabel lblGradleUrl = new JLabel("Gradle Path");
		lblGradleUrl.setBounds(10, 37, 72, 14);
		contentPanel.add(lblGradleUrl);

		tfGradleUrl = new JTextField();
		tfGradleUrl.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setGradleUrl(tfGradleUrl.getText().trim());
				data.setGradleBinUrl(tfGradleUrl.getText().trim() + "\\bin");
			}

			public void removeUpdate(DocumentEvent e) {
				data.setGradleUrl(tfGradleUrl.getText().trim());
				data.setGradleBinUrl(tfGradleUrl.getText().trim() + "\\bin");
			}

			public void insertUpdate(DocumentEvent e) {
				data.setGradleUrl(tfGradleUrl.getText().trim());
				data.setGradleBinUrl(tfGradleUrl.getText().trim() + "\\bin");
			}
		});
		tfGradleUrl.setBounds(84, 34, 251, 20);
		contentPanel.add(tfGradleUrl);
		tfGradleUrl.setColumns(10);
		tfGradleUrl.setText(data.getGradleUrl());

		JButton btnBrowseGradleUrl = new JButton("Browse");
		btnBrowseGradleUrl.addActionListener(new LocalBrowser(contentPanel,
				tfGradleUrl, JFileChooser.DIRECTORIES_ONLY, null, 0));
		btnBrowseGradleUrl.setBounds(345, 33, 89, 23);
		contentPanel.add(btnBrowseGradleUrl);

		tfRepUsername = new JTextField();
		tfRepUsername.setToolTipText("Repository UserName");
		tfRepUsername.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setRepUserName(tfRepUsername.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setRepUserName(tfRepUsername.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setRepUserName(tfRepUsername.getText().trim());
					}
				});
		tfRepUsername.setBounds(84, 207, 130, 20);
		contentPanel.add(tfRepUsername);
		tfRepUsername.setColumns(10);
		tfRepUsername.setText(data.getRepUserName());

		tfRepPassword = new JTextField();
		tfRepPassword.setToolTipText("Repository Password");
		tfRepPassword.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setRepPassword(tfRepPassword.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setRepPassword(tfRepPassword.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setRepPassword(tfRepPassword.getText().trim());
					}
				});
		tfRepPassword.setBounds(224, 207, 130, 20);
		contentPanel.add(tfRepPassword);
		tfRepPassword.setColumns(10);
		tfRepPassword.setText(data.getRepPassword());

		final JCheckBox cbRepAuth = new JCheckBox("Authentication");
		cbRepAuth.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				data.setRepAuthentication(cbRepAuth.isSelected());
			}
		});
		cbRepAuth.setBounds(84, 177, 136, 23);
		contentPanel.add(cbRepAuth);
		cbRepAuth.setSelected(data.isRepAuthentication());

		JLabel lblWorkspaceUrl = new JLabel("Workspace");
		lblWorkspaceUrl.setBounds(10, 66, 72, 14);
		contentPanel.add(lblWorkspaceUrl);

		tfWorkspaceUrl = new JTextField();
		tfWorkspaceUrl.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setWorkSpaceUrl(tfWorkspaceUrl.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setWorkSpaceUrl(tfWorkspaceUrl.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setWorkSpaceUrl(tfWorkspaceUrl.getText().trim());
					}
				});
		tfWorkspaceUrl.setColumns(10);
		tfWorkspaceUrl.setBounds(84, 63, 251, 20);
		contentPanel.add(tfWorkspaceUrl);
		tfWorkspaceUrl.setText(data.getWorkSpaceUrl());

		JButton btnBrowseWorkspaceUrl = new JButton("Browse");
		btnBrowseWorkspaceUrl.addActionListener(new LocalBrowser(contentPanel,
				tfWorkspaceUrl, JFileChooser.DIRECTORIES_ONLY, null, 0));
		btnBrowseWorkspaceUrl.setBounds(345, 62, 89, 23);
		contentPanel.add(btnBrowseWorkspaceUrl);
		
		tfNexusUserName = new JTextField();
		tfNexusUserName.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setNexusUserName(tfNexusUserName.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setNexusUserName(tfNexusUserName.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setNexusUserName(tfNexusUserName.getText().trim());
					}
				});
		tfNexusUserName.setToolTipText("Nexus UserName");
		tfNexusUserName.setText((String) null);
		tfNexusUserName.setColumns(10);
		tfNexusUserName.setBounds(84, 91, 130, 20);
		contentPanel.add(tfNexusUserName);
		tfNexusUserName.setText(data.getNexusUserName());
		
		tfNexusPassword = new JTextField();
		tfNexusPassword.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setNexusPassword(tfNexusPassword.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setNexusPassword(tfNexusPassword.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setNexusPassword(tfNexusPassword.getText().trim());
					}
				});
		tfNexusPassword.setToolTipText("Nexus Password");
		tfNexusPassword.setText((String) null);
		tfNexusPassword.setColumns(10);
		tfNexusPassword.setBounds(224, 91, 130, 20);
		contentPanel.add(tfNexusPassword);
		tfNexusPassword.setText(data.getNexusPassword());
		
		JLabel lblNexusAuth = new JLabel("Nexus Auth");
		lblNexusAuth.setBounds(10, 94, 72, 14);
		contentPanel.add(lblNexusAuth);
		
		tfNexusUrl = new JTextField();
		tfNexusUrl.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setNexusUrl(tfNexusUrl.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setNexusUrl(tfNexusUrl.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setNexusUrl(tfNexusUrl.getText().trim());
					}
				});
		tfNexusUrl.setText((String) null);
		tfNexusUrl.setColumns(10);
		tfNexusUrl.setBounds(84, 122, 251, 20);
		contentPanel.add(tfNexusUrl);
		tfNexusUrl.setText(data.getNexusUrl());
		
		JLabel lblNexusUrl = new JLabel("Nexus Url");
		lblNexusUrl.setBounds(10, 125, 72, 14);
		contentPanel.add(lblNexusUrl);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						data.writeToFile();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setModal(true);
		setResizable(false);
		setVisible(true);
	}
}
