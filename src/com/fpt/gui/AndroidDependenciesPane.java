package com.fpt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fpt.gui.ChooserDialog.ChooserDialogListener;
import com.fpt.gui.LocalBrowser.LocalBrowserListener;
import com.fpt.model.AndroidDependenciesData;
import com.fpt.model.Artifact;
import com.fpt.model.BaseDependency;
import com.fpt.model.BaseDependency.Scope;
import com.fpt.model.JarLib;
import com.fpt.model.Module;
import com.fpt.model.SettingsData;
import com.fpt.util.DataHelper;
import com.fpt.util.IOHelper;
import com.fpt.util.Parser;
import com.fpt.util.StringHelper;

/**
 * @author QuyPP1
 */
public class AndroidDependenciesPane extends JPanel implements
		ChooserDialogListener, LocalBrowserListener {

	private static final int LOCAL_BROWSER_JAR_REQUEST_CODE = 100;

	private JButton btnAddDep;
	private JPopupMenu popupMenuDep;
	private JList<Module> listModule;
	private JList<BaseDependency> listDependencies;
	public static AndroidDependenciesData data;
	private ArrayList<BaseDependency> dependenciesList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4971324513924531377L;

	/**
	 * Create the panel.
	 */
	public AndroidDependenciesPane() {
		data = new AndroidDependenciesData();
		dependenciesList = new ArrayList<BaseDependency>();
		setBounds(100, 100, 515, 181);
		setLayout(null);

		JLabel lblModule = new JLabel("Module");
		lblModule.setBounds(58, 17, 61, 14);
		add(lblModule);

		JLabel lblDependencies = new JLabel("Dependencies");
		lblDependencies.setBounds(280, 17, 93, 14);
		add(lblDependencies);

		btnAddDep = new JButton("Add");
		btnAddDep.setBounds(438, 13, 67, 23);
		btnAddDep.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				popupMenuDep.show(e.getComponent(), 0, 23);
			}
		});
		add(btnAddDep);

		popupMenuDep = new JPopupMenu();

		JMenuItem menuAddDepLocal = new JMenuItem("Local");

		menuAddDepLocal.addActionListener(new LocalBrowser(this, null,
				JFileChooser.FILES_ONLY, this, LOCAL_BROWSER_JAR_REQUEST_CODE));
		popupMenuDep.add(menuAddDepLocal);

		JMenuItem menuAddDepNexus = new JMenuItem("Nexus server");
		menuAddDepNexus.addActionListener(new OpenNexus(
				AndroidDependenciesPane.this));
		popupMenuDep.add(menuAddDepNexus);

		JMenuItem menuAddDepModule = new JMenuItem("Module");
		menuAddDepModule.addActionListener(new OpenModule(
				AndroidDependenciesPane.this));
		popupMenuDep.add(menuAddDepModule);

		JButton btnRemoveDep = new JButton("-");
		btnRemoveDep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listDependencies.getSelectedIndex() != -1) {
					dependenciesList.remove(listDependencies.getSelectedIndex());
					updateDependenciesList();
				}
			}
		});
		btnRemoveDep.setBounds(466, 47, 39, 23);
		add(btnRemoveDep);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 164, 128);
		add(scrollPane);

		listModule = new JList<Module>();
		scrollPane.setViewportView(listModule);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(184, 42, 273, 128);
		add(scrollPane_1);

		listDependencies = new JList<BaseDependency>();
		listDependencies.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					// Double-click detected
					String dialogTitle = "Scope - "
							+ listDependencies.getSelectedValue()
							+ " : {"
							+ listDependencies.getSelectedValue().getScope()
									.getText() + "}";

					ArrayList<Scope> listData = new ArrayList<Scope>();
					for (Scope scopeItem : Scope.values()) {
						listData.add(scopeItem);
					}
					ChooserDialog chooserDialog = new ChooserDialog(
							AndroidDependenciesPane.this, dialogTitle, listData
									.toArray(new Scope[listData.size()]));
					chooserDialog
							.addChooserDialogListener(AndroidDependenciesPane.this);
					chooserDialog.showDialog();
				}
			}
		});

		scrollPane_1.setViewportView(listDependencies);
		listModule.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				listDependencies.setListData(new BaseDependency[0]);
				dependenciesList.clear();
				if (listModule.getSelectedIndex() != -1) {
					ArrayList<BaseDependency> depenList = data.getModuleList()
							.get(listModule.getSelectedIndex())
							.getDependenciesList();
					if (depenList != null) {
						dependenciesList = new ArrayList<BaseDependency>(
								depenList);
						listDependencies.setListData(dependenciesList
								.toArray(new BaseDependency[dependenciesList
										.size()]));
					}
				}
			}
		});
	}

	public void updateModuleList() {
		ArrayList<Module> moduleList = getModuleList();
		data.setModuleList(moduleList);
		listModule
				.setListData(moduleList.toArray(new Module[moduleList.size()]));
		listModule.validate();
	}

	private ArrayList<Module> getModuleList() {
		String projectURL = AndroidInputPane.getData().getProjectURL();
		ArrayList<Module> listData = new ArrayList<Module>();
		try {
			if (projectURL != null && !projectURL.equals("")) {
				final File folder = new File(projectURL);
				listData = DataHelper.getAllModules(folder);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listData;
	}

	private void updateDependenciesList() {
		if (listModule.getSelectedIndex() != -1) {
			data.getModuleList()
					.get(listModule.getSelectedIndex())
					.setDependenciesList(
							new ArrayList<BaseDependency>(dependenciesList));
			listDependencies.setListData(dependenciesList
					.toArray(new BaseDependency[dependenciesList.size()]));
			// need to write reference module to project.properties file

		}
	}

	public static AndroidDependenciesData getData() {
		return data;
	}

	@Override
	public void onChooserDialogResultAlready(Object dependency) {
		// TODO Auto-generated method stub
		if (dependency instanceof BaseDependency) {
			dependenciesList.add((BaseDependency) dependency);
			updateDependenciesList();
		}
		if (dependency instanceof Scope) {
			dependenciesList.get(listDependencies.getSelectedIndex()).setScope(
					(Scope) dependency);
			updateDependenciesList();
		}
	}

	@Override
	public void onLocalBrowserResultAlready(String resultURL,
			String resultFileName, int requestCode) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case LOCAL_BROWSER_JAR_REQUEST_CODE:
			JarLib dependency = new JarLib();
			String resultUrlRelative = StringHelper
					.convertAbsoluteUrlToRelativeUrl(listModule
							.getSelectedValue().getUrl(), resultURL);
			dependency.setUrl(resultUrlRelative);
			dependency.setName(resultFileName);
			dependenciesList.add(dependency);
			updateDependenciesList();
			break;

		default:
			break;
		}
	}

	class OpenNexus implements ActionListener {
		private Artifact[] listData;
		private JPanel parentPanel;

		public OpenNexus(JPanel parentPanel) {
			this.parentPanel = parentPanel;
		}

		private void initialize() {
			SettingsData settingsData = new SettingsData();
			settingsData.readFromFile();
			String remoteRep = "";
			if (!settingsData.getNexusUrl().equals("")) {
				remoteRep = settingsData.getNexusUrl()
						+ "/content/repositories/"
						+ settingsData.getRepository();
			}

			// TODO Auto-generated method stub
			ArrayList<Artifact> artifactList = DataHelper
					.getAllArtifacts(remoteRep);
			listData = artifactList.toArray(new Artifact[artifactList.size()]);
		}

		public void actionPerformed(ActionEvent e) {
			initialize();
			ChooserDialog chooserDialog = new ChooserDialog(parentPanel,
					"Nexus artifact list", listData);
			chooserDialog
					.addChooserDialogListener(AndroidDependenciesPane.this);
			chooserDialog.showDialog();
		}
	}

	class OpenModule implements ActionListener {
		private JPanel parentPanel;

		public OpenModule(JPanel parentPanel) {
			this.parentPanel = parentPanel;
			initialize();
		}

		private void initialize() {

		}

		public void actionPerformed(ActionEvent e) {
			ArrayList<Module> moduleList = getModuleList();
			ChooserDialog chooserDialog = new ChooserDialog(parentPanel,
					"Module chooser", moduleList.toArray(new Module[moduleList
							.size()]));
			chooserDialog
					.addChooserDialogListener(AndroidDependenciesPane.this);
			chooserDialog.showDialog();
		}
	}
}
