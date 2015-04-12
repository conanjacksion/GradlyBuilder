package com.fpt.model;

import java.io.Serializable;

/**
 * Input data contains all input value need when building an android project
 * 
 * @author QuyPP1
 *
 */
public class AndroidInputData implements Serializable, IInputData {
	private static final long serialVersionUID = -7370407593599927796L;
	private boolean isProguard = false;
	private boolean isUsingNativeLib = false;
	private String nativeLibUrl;
	private String proguardUrl;
	private String signingName;
	private String signingKeyAlias;
	private String signingKeyPassword;
	private String projectURL;
	private String projectDirName;
	

	public String getProjectDirName() {
		return projectDirName;
	}

	public void setProjectDirName(String projectDirName) {
		this.projectDirName = projectDirName;
	}

	public String getProjectURL() {
		return projectURL;
	}

	public void setProjectURL(String projectURL) {
		this.projectURL = projectURL;
	}

	public boolean isProguard() {
		return isProguard;
	}

	public void setProguard(boolean isProguard) {
		this.isProguard = isProguard;
	}

	public boolean isUsingNativeLib() {
		return isUsingNativeLib;
	}

	public void setUsingNativeLib(boolean isUsingNativeLib) {
		this.isUsingNativeLib = isUsingNativeLib;
	}

	public String getNativeLibUrl() {
		return nativeLibUrl;
	}

	public void setNativeLibUrl(String nativeLibUrl) {
		this.nativeLibUrl = nativeLibUrl;
	}

	public String getProguardUrl() {
		return proguardUrl;
	}

	public void setProguardUrl(String proguardUrl) {
		this.proguardUrl = proguardUrl;
	}

	public String getSigningName() {
		return signingName;
	}

	public void setSigningName(String signingName) {
		this.signingName = signingName;
	}

	public String getSigningKeyAlias() {
		return signingKeyAlias;
	}

	public void setSigningKeyAlias(String signingKeyAlias) {
		this.signingKeyAlias = signingKeyAlias;
	}

	public String getSigningKeyPassword() {
		return signingKeyPassword;
	}

	public void setSigningKeyPassword(String signingKeyPassword) {
		this.signingKeyPassword = signingKeyPassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
