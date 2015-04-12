package com.fpt.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import com.fpt.build.GradleBuildFactory;
import com.fpt.build.GradleBuildManager;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * @author QuyPP1
 */
public class BuildManager {

	private AndroidPanel androidPanel;
	private JFrame mainFrame;
	private JButton btnBuild;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuView;
	private JMenu menuHelp;
	private JMenuItem menuItemFileSetting;
	private JCheckBoxMenuItem miLog;
	private JLabel buildType;
	private JMenuItem menuNewAndroid;
	private static String gradleType;
	private GradleBuildManager gradleBuildManager;
	private static JTextArea taLog;

	public static String getGradleType() {
		return gradleType;
	}

	public static void setGradleType(String gradleType) {
		BuildManager.gradleType = gradleType;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuildManager window = new BuildManager();
					window.mainFrame.setVisible(true);
					System.out.println("Working Directory = "
							+ System.getProperty("user.dir"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BuildManager() {
		gradleBuildManager = new GradleBuildManager();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		initMenuBar();
		/**
		 * Init Main Frame Main frame: contains Label, Build Frame and Build
		 * footer
		 */
		mainFrame.setTitle("Gradly Build Tool");
		mainFrame.setBounds(100, 100, 602, 619);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setResizable(false);
		/**
		 * Build Frame: this can be Android Frame, Ios Frame or Java Frame
		 */
		// mainPanel = new AndroidPanel();
		//
		// mainFrame.getContentPane().add( (JPanel) mainPanel);

		/**
		 * Build Footer: contain Build button
		 */
		btnBuild = new JButton("Build");
		btnBuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (androidPanel != null) {
					gradleBuildManager.build(GradleBuildFactory.GRADLE_ANDROID,
							AndroidInputPane.getData(),
							AndroidOutputPane.getData(),
							AndroidDependenciesPane.getData());
				}
			}
		});
		btnBuild.setBounds(485, 269, 89, 23);

		mainFrame.getContentPane().add(btnBuild);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 319, 576, 227);
		mainFrame.getContentPane().add(scrollPane);

		taLog = new JTextArea();
		DefaultCaret defaultCaret = (DefaultCaret) taLog.getCaret();
		defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		taLog.setEditable(false);
		scrollPane.setViewportView(taLog);

		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(10, 294, 46, 14);
		mainFrame.getContentPane().add(lblLog);

	}

	/**
	 * Init menu bar item
	 */
	private void initMenuBar() {
		menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		menuFile = new JMenu("File");
		menuBar.add(menuFile);

		JMenu menuItemFileNew = new JMenu("New");
		menuFile.add(menuItemFileNew);

		menuNewAndroid = new JMenuItem("Android");
		menuNewAndroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setGradleType(GradleBuildFactory.GRADLE_ANDROID);
				androidPanel = new AndroidPanel();
				buildType = new JLabel("ANDROID BUILD");
				buildType.setFont(new Font("Tahoma", Font.PLAIN, 15));
				buildType.setBounds(227, 11, 150, 31);
				buildType.setHorizontalAlignment(SwingConstants.CENTER);
				mainFrame.getContentPane().add(buildType);
				mainFrame.getContentPane().add(androidPanel);
				mainFrame.repaint();
			}
		});
		menuItemFileNew.add(menuNewAndroid);

		menuItemFileSetting = new JMenuItem("Setting");
		menuItemFileSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsDialog settingsDialog = new SettingsDialog();
			}
		});
		menuFile.add(menuItemFileSetting);

		menuView = new JMenu("View");
		menuBar.add(menuView);

		miLog = new JCheckBoxMenuItem("Log");
		miLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogDialog logDialog = LogDialog.getInstance("Log");
			}
		});
		menuView.add(miLog);
		
		JMenuItem miClearLog = new JMenuItem("Clear Log");
		miClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taLog.setText("");
			}
		});
		menuView.add(miClearLog);

		menuHelp = new JMenu("Help");
		Component horizontalGlue = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue);
		menuBar.add(menuHelp);
	}

	public static void updateLog(String text) {
		if (taLog != null) {
			taLog.append(text + "\n");
			taLog.setCaretPosition(taLog.getDocument().getLength());
		}
	}
}
