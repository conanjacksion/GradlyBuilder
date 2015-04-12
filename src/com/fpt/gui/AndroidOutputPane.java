package com.fpt.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fpt.model.AndroidInputData;
import com.fpt.model.AndroidOutputData;

/**
 * @author QuyPP1
 */
public class AndroidOutputPane extends JPanel {
	private static final long serialVersionUID = -2815940357873196069L;
	private JTextField tfOuputServerURL;
	private JCheckBox isOutputNexus;
	private JCheckBox isOutputServer;
	private JCheckBox isOutputLocal;
	private JRadioButton isAPK, isAAR;
	private JTextField tfOutputLocalURL;
	private JLabel lblLocalPath;
	private JButton btnBrowseOutputLocal;
	private ButtonGroup bgBuildType;
	private static AndroidOutputData data;
	private JTextField tfGroupId;
	private JTextField tfArtifactId;
	private JTextField tfVersion;
	private JLabel lblArtifact;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidOutputPane frame = new AndroidOutputPane();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AndroidOutputPane() {
		data = new AndroidOutputData();
		setBounds(100, 100, 450, 181);
		setLayout(null);

		JLabel lblProject = new JLabel("Output");
		lblProject.setBounds(0, 4, 71, 14);
		lblProject.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblProject);

		isOutputNexus = new JCheckBox("Nexus");
		isOutputNexus.addItemListener(new OutputWay());
		isOutputNexus.setBounds(108, 0, 71, 23);
		add(isOutputNexus);

		isOutputServer = new JCheckBox("Server");
		isOutputServer.addItemListener(new OutputWay());
		isOutputServer.setBounds(181, 0, 71, 23);
		add(isOutputServer);

		isOutputLocal = new JCheckBox("Local");
		isOutputLocal.addItemListener(new OutputWay());
		isOutputLocal.setBounds(254, 0, 71, 23);
		add(isOutputLocal);

		JLabel lblProguard = new JLabel("Server URL");
		lblProguard.setBounds(0, 65, 71, 14);
		lblProguard.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblProguard);

		tfOuputServerURL = new JTextField();
		tfOuputServerURL.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setOutputServerUrl(tfOuputServerURL.getText()
								.trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setOutputServerUrl(tfOuputServerURL.getText()
								.trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setOutputServerUrl(tfOuputServerURL.getText()
								.trim());
					}
				});

		tfOuputServerURL.setBounds(108, 62, 211, 20);
		add(tfOuputServerURL);
		tfOuputServerURL.setColumns(10);

		lblLocalPath = new JLabel("Local path");
		lblLocalPath.setBounds(0, 97, 71, 14);
		add(lblLocalPath);

		tfOutputLocalURL = new JTextField();
		tfOutputLocalURL.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setOutputLocalUrl(tfOutputLocalURL.getText()
								.trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setOutputLocalUrl(tfOutputLocalURL.getText()
								.trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setOutputLocalUrl(tfOutputLocalURL.getText()
								.trim());
					}
				});
		tfOutputLocalURL.setBounds(108, 94, 211, 20);
		add(tfOutputLocalURL);
		tfOutputLocalURL.setColumns(10);

		JLabel lblBuildType = new JLabel("Build Type");
		lblBuildType.setBounds(0, 127, 71, 14);
		add(lblBuildType);

		isAPK = new JRadioButton("APK");
		isAPK.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (isAPK.isSelected()) {
					data.setBuildType(AndroidOutputData.BUILD_TYPE_APK);
				}
			}
		});
		isAPK.setSelected(true);
		isAPK.setBounds(108, 123, 71, 23);
		add(isAPK);

		isAAR = new JRadioButton("AAR");
		isAAR.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (isAAR.isSelected()) {
					data.setBuildType(AndroidOutputData.BUILD_TYPE_AAR);
				}
			}
		});
		isAAR.setBounds(248, 123, 71, 23);
		add(isAAR);

		btnBrowseOutputLocal = new JButton("Browse");
		
		btnBrowseOutputLocal.addActionListener(new LocalBrowser(
				AndroidOutputPane.this, tfOutputLocalURL,
				JFileChooser.DIRECTORIES_ONLY, null, 0));
		btnBrowseOutputLocal.setBounds(329, 93, 89, 23);
		add(btnBrowseOutputLocal);

		bgBuildType = new ButtonGroup();
		bgBuildType.add(isAPK);
		bgBuildType.add(isAAR);
		
		tfGroupId = new JTextField();
		tfGroupId.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setNexusGroupId(tfGroupId.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setNexusGroupId(tfGroupId.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setNexusGroupId(tfGroupId.getText().trim());
			}
		});
		tfGroupId.setToolTipText("Group");
		tfGroupId.setColumns(10);
		tfGroupId.setBounds(74, 30, 124, 20);
		add(tfGroupId);
		
		tfArtifactId = new JTextField();
		tfArtifactId.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setNexusArtifactId(tfArtifactId.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setNexusArtifactId(tfArtifactId.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setNexusArtifactId(tfArtifactId.getText().trim());
			}
		});
		tfArtifactId.setToolTipText("Artifact");
		tfArtifactId.setColumns(10);
		tfArtifactId.setBounds(208, 30, 97, 20);
		add(tfArtifactId);
		
		tfVersion = new JTextField();
		tfVersion.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setNexusVersion(tfVersion.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setNexusVersion(tfVersion.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setNexusVersion(tfVersion.getText().trim());
			}
		});
		tfVersion.setToolTipText("Version");
		tfVersion.setColumns(10);
		tfVersion.setBounds(314, 30, 71, 20);
		add(tfVersion);
		
		lblArtifact = new JLabel("Artifact");
		lblArtifact.setAlignmentX(0.5f);
		lblArtifact.setBounds(0, 32, 71, 14);
		add(lblArtifact);
	}

	class OutputWay implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			int outputWay = 0;
			if (isOutputNexus.isSelected()) {
				outputWay += AndroidOutputData.OUTPUT_NEXUS;
			}
			if (isOutputLocal.isSelected()) {
				outputWay += AndroidOutputData.OUTPUT_LOCAL;
			}
			if (isOutputServer.isSelected()) {
				outputWay += AndroidOutputData.OUTPUT_SERVER;
			}
			data.setOutputWay(outputWay);
		}

	}

	public static AndroidOutputData getData() {
		return data;
	}

	public void updateOutputLocalURL(String outputLocalURL) {
		tfOutputLocalURL.setText(outputLocalURL);
	}
}
