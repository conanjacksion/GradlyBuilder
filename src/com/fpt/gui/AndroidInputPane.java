package com.fpt.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fpt.gui.BrowseDialog.BrowseDialogListener;
import com.fpt.model.AndroidInputData;
import com.fpt.model.BaseDependency;
import com.fpt.model.Module;
import com.fpt.model.SettingsData;
import com.fpt.util.IOHelper;
import com.fpt.util.StringHelper;

/**
 * @author QuyPP1
 */
public class AndroidInputPane extends JPanel implements BrowseDialogListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4104473595747047619L;
	private JTextField tfProjectURL, tfSigningName, tfKeyAlias, tfKeyPassword,
			tfNativeLibURL, tfProguardURL;
	private JButton btnBrowseProguard, btnBrowseNative, btnBrowseProject;
	private JCheckBox cbProguard, cbNativeLib;
	private static AndroidInputData data;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidInputPane frame = new AndroidInputPane();
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
	public AndroidInputPane() {
		data = new AndroidInputData();
		setBounds(100, 100, 450, 181);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 108, 70, 86, 67, 0 };
		gridBagLayout.rowHeights = new int[] { 23, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		/**
		 * Project URL
		 */
		JLabel lblProject = new JLabel("Project");
		lblProject.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_lblProject = new GridBagConstraints();
		gbc_lblProject.anchor = GridBagConstraints.WEST;
		gbc_lblProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblProject.gridx = 0;
		gbc_lblProject.gridy = 0;
		add(lblProject, gbc_lblProject);

		tfProjectURL = new JTextField();
		tfProjectURL.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setProjectURL(tfProjectURL.getText().trim());
				String[] projectUrlArray = tfProjectURL.getText().trim()
						.split("\\\\");
				data.setProjectDirName(projectUrlArray[projectUrlArray.length - 1]);
				executePreProcess(tfProjectURL.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setProjectURL(tfProjectURL.getText().trim());
				String[] projectUrlArray = tfProjectURL.getText().trim()
						.split("\\\\");
				data.setProjectDirName(projectUrlArray[projectUrlArray.length - 1]);
				executePreProcess(tfProjectURL.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setProjectURL(tfProjectURL.getText().trim());
				String[] projectUrlArray = tfProjectURL.getText().trim()
						.split("\\\\");
				data.setProjectDirName(projectUrlArray[projectUrlArray.length - 1]);
				executePreProcess(tfProjectURL.getText().trim());
			}
		});

		GridBagConstraints gbc_projectURL = new GridBagConstraints();
		gbc_projectURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_projectURL.gridwidth = 2;
		gbc_projectURL.insets = new Insets(0, 0, 5, 5);
		gbc_projectURL.gridx = 1;
		gbc_projectURL.gridy = 0;
		add(tfProjectURL, gbc_projectURL);
		tfProjectURL.setColumns(10);

		btnBrowseProject = new JButton("Browse");

		GridBagConstraints gbc_btnBrowseProjectURL = new GridBagConstraints();
		gbc_btnBrowseProjectURL.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowseProjectURL.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnBrowseProjectURL.gridx = 3;
		gbc_btnBrowseProjectURL.gridy = 0;
		add(btnBrowseProject, gbc_btnBrowseProjectURL);
		/**
		 * click on Browse Project button -set project textfield value -set data
		 * for inputdata
		 */

		btnBrowseProject.addActionListener(new LocalBrowser(this, tfProjectURL,
				JFileChooser.DIRECTORIES_ONLY, null, 0));

		JLabel lblProguard = new JLabel("Proguard");
		lblProguard.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_lblProguard = new GridBagConstraints();
		gbc_lblProguard.anchor = GridBagConstraints.WEST;
		gbc_lblProguard.insets = new Insets(0, 0, 5, 5);
		gbc_lblProguard.gridx = 0;
		gbc_lblProguard.gridy = 1;
		add(lblProguard, gbc_lblProguard);

		/**
		 * Proguard
		 */
		cbProguard = new JCheckBox("");
		GridBagConstraints gbc_isProguard = new GridBagConstraints();
		gbc_isProguard.anchor = GridBagConstraints.WEST;
		gbc_isProguard.insets = new Insets(0, 0, 5, 5);
		gbc_isProguard.gridx = 1;
		gbc_isProguard.gridy = 1;
		add(cbProguard, gbc_isProguard);

		tfProguardURL = new JTextField();
		tfProguardURL.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setProguardUrl(tfProguardURL.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setProguardUrl(tfProguardURL.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setProguardUrl(tfProguardURL.getText().trim());
			}
		});
		GridBagConstraints gbc_proguardURL = new GridBagConstraints();
		gbc_proguardURL.insets = new Insets(0, 0, 5, 5);
		gbc_proguardURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_proguardURL.gridx = 2;
		gbc_proguardURL.gridy = 1;
		add(tfProguardURL, gbc_proguardURL);
		tfProguardURL.setColumns(10);

		btnBrowseProguard = new JButton("Browse");
		GridBagConstraints gbc_btnBrowseProguard = new GridBagConstraints();
		gbc_btnBrowseProguard.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowseProguard.gridx = 3;
		gbc_btnBrowseProguard.gridy = 1;
		add(btnBrowseProguard, gbc_btnBrowseProguard);

		cbProguard.setSelected(false);
		tfProguardURL.setVisible(false);
		btnBrowseProguard.setVisible(false);

		btnBrowseProguard.addActionListener(new LocalBrowser(
				AndroidInputPane.this, tfProguardURL,
				JFileChooser.DIRECTORIES_ONLY, null, 0));
		/**
		 * disable or enable proguard text field and button when check on
		 * Proguard checkbox
		 */
		cbProguard.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					tfProguardURL.setVisible(true);
					btnBrowseProguard.setVisible(true);
					data.setProguard(true);
				} else {
					tfProguardURL.setVisible(false);
					btnBrowseProguard.setVisible(false);
					data.setProguard(false);
				}

			}
		});

		/**
		 * Signing
		 */
		JLabel lblSigningKey = new JLabel("Signing");
		lblSigningKey.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_lblSigningKey = new GridBagConstraints();
		gbc_lblSigningKey.anchor = GridBagConstraints.WEST;
		gbc_lblSigningKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblSigningKey.gridx = 0;
		gbc_lblSigningKey.gridy = 2;
		add(lblSigningKey, gbc_lblSigningKey);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 2;
		add(lblName, gbc_lblName);

		tfSigningName = new JTextField();
		tfSigningName.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setSigningName(tfSigningName.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setSigningName(tfSigningName.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setSigningName(tfSigningName.getText().trim());
			}
		});

		GridBagConstraints gbc_signingName = new GridBagConstraints();
		gbc_signingName.insets = new Insets(0, 0, 5, 5);
		gbc_signingName.fill = GridBagConstraints.HORIZONTAL;
		gbc_signingName.gridx = 2;
		gbc_signingName.gridy = 2;
		add(tfSigningName, gbc_signingName);
		tfSigningName.setColumns(10);

		JLabel lblNewLabel = new JLabel("Key Alias");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		add(lblNewLabel, gbc_lblNewLabel);

		tfKeyAlias = new JTextField();
		tfKeyAlias.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				data.setSigningKeyAlias(tfKeyAlias.getText().trim());
			}
		});
		GridBagConstraints gbc_keyAlias = new GridBagConstraints();
		gbc_keyAlias.insets = new Insets(0, 0, 5, 5);
		gbc_keyAlias.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyAlias.gridx = 2;
		gbc_keyAlias.gridy = 3;
		add(tfKeyAlias, gbc_keyAlias);
		tfKeyAlias.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Key Password");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		tfKeyPassword = new JTextField();
		tfKeyPassword.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				data.setSigningKeyPassword(tfKeyPassword.getText().trim());
			}

			public void removeUpdate(DocumentEvent e) {
				data.setSigningKeyPassword(tfKeyPassword.getText().trim());
			}

			public void insertUpdate(DocumentEvent e) {
				data.setSigningKeyPassword(tfKeyPassword.getText().trim());
			}
		});

		GridBagConstraints gbc_keyPassword = new GridBagConstraints();
		gbc_keyPassword.insets = new Insets(0, 0, 5, 5);
		gbc_keyPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyPassword.gridx = 2;
		gbc_keyPassword.gridy = 4;
		add(tfKeyPassword, gbc_keyPassword);
		tfKeyPassword.setColumns(10);

		/**
		 * Native libs
		 */
		JLabel lblNativeLibs = new JLabel("Native Libs");
		lblNativeLibs.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_lblNativeLibs = new GridBagConstraints();
		gbc_lblNativeLibs.anchor = GridBagConstraints.WEST;
		gbc_lblNativeLibs.insets = new Insets(0, 0, 0, 5);
		gbc_lblNativeLibs.gridx = 0;
		gbc_lblNativeLibs.gridy = 5;
		add(lblNativeLibs, gbc_lblNativeLibs);

		cbNativeLib = new JCheckBox("");
		GridBagConstraints gbc_isUsingNativeLib = new GridBagConstraints();
		gbc_isUsingNativeLib.anchor = GridBagConstraints.WEST;
		gbc_isUsingNativeLib.insets = new Insets(0, 0, 0, 5);
		gbc_isUsingNativeLib.gridx = 1;
		gbc_isUsingNativeLib.gridy = 5;
		add(cbNativeLib, gbc_isUsingNativeLib);

		tfNativeLibURL = new JTextField();
		tfNativeLibURL.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						data.setNativeLibUrl(tfNativeLibURL.getText().trim());
					}

					public void removeUpdate(DocumentEvent e) {
						data.setNativeLibUrl(tfNativeLibURL.getText().trim());
					}

					public void insertUpdate(DocumentEvent e) {
						data.setNativeLibUrl(tfNativeLibURL.getText().trim());
					}
				});
		GridBagConstraints gbc_nativeLibURL = new GridBagConstraints();
		gbc_nativeLibURL.insets = new Insets(0, 0, 0, 5);
		gbc_nativeLibURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_nativeLibURL.gridx = 2;
		gbc_nativeLibURL.gridy = 5;
		add(tfNativeLibURL, gbc_nativeLibURL);
		tfNativeLibURL.setColumns(10);

		btnBrowseNative = new JButton("Browse");
		GridBagConstraints gbc_btnBrowseNative = new GridBagConstraints();
		gbc_btnBrowseNative.gridx = 3;
		gbc_btnBrowseNative.gridy = 5;
		add(btnBrowseNative, gbc_btnBrowseNative);

		cbNativeLib.setSelected(false);
		tfNativeLibURL.setVisible(false);
		btnBrowseNative.setVisible(false);

		btnBrowseNative.addActionListener(new LocalBrowser(this,
				tfNativeLibURL, JFileChooser.DIRECTORIES_ONLY, null, 0));
		/**
		 * disable or enable native lib text field and button when check on
		 * Native checkbox
		 */
		cbNativeLib.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					tfNativeLibURL.setVisible(true);
					btnBrowseNative.setVisible(true);
					data.setUsingNativeLib(true);
				} else {
					tfNativeLibURL.setVisible(false);
					btnBrowseNative.setVisible(false);
					data.setUsingNativeLib(false);
				}

			}
		});

	}

	Map<Module, String> externalModuleReferences;

	private void executePreProcess(String projectUrl) {
		BuildManager.updateLog("Analyzing project...");
		AndroidPanel.getAndroidOutputPane().updateOutputLocalURL(projectUrl);
		AndroidPanel.getAndroidDependenciesPane().updateModuleList();
		externalModuleReferences = new HashMap<Module, String>();
		ArrayList<Module> moduleList = AndroidDependenciesPane.getData()
				.getModuleList();
		for (Module moduleItem : moduleList) {
			ArrayList<BaseDependency> dependenciesList = moduleItem
					.getDependenciesList();
			if (dependenciesList != null) {
				for (int i = 0; i < dependenciesList.size(); i++) {
					if (dependenciesList.get(i) instanceof Module) {
						Module moduleItem1 = (Module) dependenciesList.get(i);
						if (moduleItem1.getName() == null) {
							externalModuleReferences.put(moduleItem1,
									StringHelper
											.convertRelativeUrlToAbsoluteUrl(
													moduleItem.getUrl(),
													moduleItem1.getUrl()));
						}
					}
				}
			}
		}
		if (externalModuleReferences.size() > 0) {
			SettingsData settingsdata = new SettingsData();
			settingsdata.readFromFile();
			BrowseDialog browseDialog = new BrowseDialog(
					settingsdata.getWorkSpaceUrl() + "\\"
							+ data.getProjectDirName());
			browseDialog.addBrowseDialogListener(this);
			browseDialog.showDialog();
		}
		BuildManager.updateLog("Project was analyzed successfully.");
	}

	public static AndroidInputData getData() {
		return data;
	}

	@Override
	public void onBrowseDialogResultAlready(final String url) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				BuildManager.updateLog("Making project...");
				IOHelper.copyProjectToUrl(
						StringHelper.convertToBackFlash(data.getProjectURL()),
						StringHelper.convertToBackFlash(url + "\\"
								+ data.getProjectDirName()));
				for (Module exModulePre : externalModuleReferences.keySet()) {
					IOHelper.copyProjectToUrl(StringHelper
							.convertToBackFlash(externalModuleReferences
									.get(exModulePre)), StringHelper
							.convertToBackFlash(url + "\\"
									+ exModulePre.getDirName()));
				}
				tfProjectURL.setText(url);
			}
		});
		thread.start();
	}
}
