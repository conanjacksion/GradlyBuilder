package com.fpt.gui;

import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author QuyPP1
 */
public class AndroidPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1672104627333304140L;
	private static AndroidInputPane inputPane;
	private static AndroidOutputPane outputPane;
	private static AndroidDependenciesPane dependenciesPane;
	private JTabbedPane androidPane;

	/**
	 * Create the panel.
	 */
	public AndroidPanel() {
		setBounds(26, 42, 547, 211);
		setLayout(null);
		//init 3 frame tab
		inputPane = new AndroidInputPane();
		outputPane = new AndroidOutputPane();
		dependenciesPane = new AndroidDependenciesPane();
		//main android frame
		androidPane = new JTabbedPane();
		androidPane.setBounds(new Rectangle(10, 5, 520, 208));
		androidPane.addTab("Input", inputPane);
		androidPane.addTab("Output", outputPane);
		androidPane.addTab("Dependencies", dependenciesPane);
		this.add(androidPane);
	}
	
	public static AndroidDependenciesPane getAndroidDependenciesPane(){
		return dependenciesPane;
	}
	
	public static AndroidInputPane getAndroidInputPane(){
		return inputPane;
	}
	
	public static AndroidOutputPane getAndroidOutputPane(){
		return outputPane;
	}

}
