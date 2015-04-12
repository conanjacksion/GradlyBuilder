package com.fpt.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Module extends BaseDependency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5028690706583943962L;
	private String url;
	private String name;
	private String dirName;
	private ArrayList<BaseDependency> dependenciesList;
	private boolean projectLibrary;
	private int minSDKVer;
	private int targetSDKVer;
	private int compileSDKVer;
	private boolean jumboMode;
	private boolean mainModule;
	private String buildToolsVer;
	private int versionCode;
	private String versionName;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getBuildToolsVer() {
		return buildToolsVer;
	}

	public void setBuildToolsVer(String buildToolsVer) {
		this.buildToolsVer = buildToolsVer;
	}

	public boolean isMainModule() {
		return mainModule;
	}

	public void setMainModule(boolean mainModule) {
		this.mainModule = mainModule;
	}

	public boolean isJumboMode() {
		return jumboMode;
	}

	public void setJumboMode(boolean jumboMode) {
		this.jumboMode = jumboMode;
	}

	public int getMinSDKVer() {
		return minSDKVer;
	}

	public void setMinSDKVer(int minSDKVer) {
		this.minSDKVer = minSDKVer;
	}

	public int getTargetSDKVer() {
		return targetSDKVer;
	}

	public void setTargetSDKVer(int targetSDKVer) {
		this.targetSDKVer = targetSDKVer;
	}

	public int getCompileSDKVer() {
		return compileSDKVer;
	}

	public void setCompileSDKVer(int compileSDKVer) {
		this.compileSDKVer = compileSDKVer;
	}

	public boolean isProjectLibrary() {
		return projectLibrary;
	}

	public void setProjectLibrary(boolean projectLibrary) {
		this.projectLibrary = projectLibrary;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<BaseDependency> getDependenciesList() {
		return dependenciesList;
	}

	public void setDependenciesList(ArrayList<BaseDependency> dependenciesList) {
		this.dependenciesList = dependenciesList;
	}

	public String toString() {
		if (name == null || name.equals("")) {
			return url;
		}
		return ":" + name;
	}

}
