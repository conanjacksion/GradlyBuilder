package com.fpt.model;

import java.io.Serializable;

import com.fpt.util.IOHelper;

public class SettingsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6101748277805163876L;
	public static final String SETTINGS_FILE_NAME = "GradlyBuilderSettings";
	public final static String ANDROID_SDK_KEY = "Android SDK = ";
	public final static String NEXUS_USERNAME_KEY = "Nexus Username = ";
	public final static String NEXUS_PASSWORD_KEY = "Nexus Password = ";
	public final static String NEXUS_KEY = "Nexus = ";
	public final static String REPOSITORY_KEY = "Repository = ";
	public final static String IS_REP_AUTHENTICATION_KEY = "IsRepAuthentication = ";
	public final static String REP_USERNAME_KEY = "Rep Username = ";
	public final static String REP_PASSWORD_KEY = "Rep Password = ";
	public final static String GRADLE_KEY = "Gradle = ";
	public final static String WORKSPACE_KEY = "Workspace = ";
	public final static String CURL_KEY = "cUrl = ";
	public final static String BREAK_SIGNAL = "\n";
	private String androidSDKURL;
	private String nexusUserName;
	private String nexusPassword;
	private String nexusUrl;
	private String repository;
	private String repUserName;
	private String repPassword;
	private boolean repAuthentication;
	private String gradleUrl;
	private String gradleBinUrl;
	private String workSpaceUrl;
	private String curlUrl;

	public String getCurlUrl() {
		return curlUrl;
	}

	public void setCurlUrl(String curlUrl) {
		this.curlUrl = curlUrl;
	}

	public String getGradleBinUrl() {
		return gradleBinUrl;
	}

	public void setGradleBinUrl(String gradleBinUrl) {
		this.gradleBinUrl = gradleBinUrl;
	}

	public String getNexusUserName() {
		return nexusUserName;
	}

	public void setNexusUserName(String nexusUserName) {
		this.nexusUserName = nexusUserName;
	}

	public String getNexusPassword() {
		return nexusPassword;
	}

	public void setNexusPassword(String nexusPassword) {
		this.nexusPassword = nexusPassword;
	}

	public String getNexusUrl() {
		return nexusUrl;
	}

	public void setNexusUrl(String nexusUrl) {
		this.nexusUrl = nexusUrl;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public boolean isRepAuthentication() {
		return repAuthentication;
	}

	public void setRepAuthentication(boolean repAuthentication) {
		this.repAuthentication = repAuthentication;
	}

	public String getGradleUrl() {
		return gradleUrl;
	}

	public void setGradleUrl(String gradleUrl) {
		this.gradleUrl = gradleUrl;
	}

	public String getWorkSpaceUrl() {
		return workSpaceUrl;
	}

	public void setWorkSpaceUrl(String workSpaceUrl) {
		this.workSpaceUrl = workSpaceUrl;
	}

	public String getRepUserName() {
		return repUserName;
	}

	public void setRepUserName(String repUserName) {
		this.repUserName = repUserName;
	}

	public String getRepPassword() {
		return repPassword;
	}

	public void setRepPassword(String repPassword) {
		this.repPassword = repPassword;
	}

	public String getAndroidSDKURL() {
		return androidSDKURL;
	}

	public void setAndroidSDKURL(String androidSDKURL) {
		this.androidSDKURL = androidSDKURL;
	}

	public void writeToFile() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ANDROID_SDK_KEY + androidSDKURL);
		stringBuilder.append(BREAK_SIGNAL + GRADLE_KEY + gradleUrl);
		stringBuilder.append(BREAK_SIGNAL + WORKSPACE_KEY + workSpaceUrl);
		stringBuilder.append(BREAK_SIGNAL + REPOSITORY_KEY + repository);
		stringBuilder.append(BREAK_SIGNAL + IS_REP_AUTHENTICATION_KEY
				+ repAuthentication);
		stringBuilder.append(BREAK_SIGNAL + REP_USERNAME_KEY + repUserName);
		stringBuilder.append(BREAK_SIGNAL + REP_PASSWORD_KEY + repPassword);
		stringBuilder.append(BREAK_SIGNAL + NEXUS_USERNAME_KEY + nexusUserName);
		stringBuilder.append(BREAK_SIGNAL + NEXUS_PASSWORD_KEY + nexusPassword);
		stringBuilder.append(BREAK_SIGNAL + NEXUS_KEY + nexusUrl);
		stringBuilder.append(BREAK_SIGNAL + CURL_KEY + curlUrl);
		stringBuilder.append(BREAK_SIGNAL);
		IOHelper.writeToBinFile(stringBuilder.toString(), SETTINGS_FILE_NAME);
	}

	public void readFromFile() {
		String textFromFile = IOHelper.readFromBinFile(SETTINGS_FILE_NAME);
		if (textFromFile.contains(ANDROID_SDK_KEY)) {
			androidSDKURL = textFromFile.substring(ANDROID_SDK_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL));
		} else {
			androidSDKURL = "";
		}
		if (textFromFile.contains(GRADLE_KEY)) {
			gradleUrl = textFromFile.substring(
					textFromFile.indexOf(GRADLE_KEY) + GRADLE_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(GRADLE_KEY)));
			gradleBinUrl = gradleUrl + "\\bin";
		} else if (IOHelper.readEnVar("GRADLE_HOME") != null
				&& !IOHelper.readEnVar("GRADLE_HOME").equals("")) {
			gradleUrl = IOHelper.readEnVar("GRADLE_HOME");
			gradleBinUrl = gradleUrl + "\\bin";
		} else {
			gradleUrl = "";
			gradleBinUrl = gradleUrl;
		}
		if (textFromFile.contains(WORKSPACE_KEY)) {
			workSpaceUrl = textFromFile.substring(
					textFromFile.indexOf(WORKSPACE_KEY)
							+ WORKSPACE_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(WORKSPACE_KEY)));
		} else {
			workSpaceUrl = "";
		}
		if (textFromFile.contains(REPOSITORY_KEY)) {
			repository = textFromFile.substring(
					textFromFile.indexOf(REPOSITORY_KEY)
							+ REPOSITORY_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(REPOSITORY_KEY)));
		} else {
			repository = "";
		}
		if (textFromFile.contains(IS_REP_AUTHENTICATION_KEY)) {
			repAuthentication = Boolean.parseBoolean(textFromFile.substring(
					textFromFile.indexOf(IS_REP_AUTHENTICATION_KEY)
							+ IS_REP_AUTHENTICATION_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(IS_REP_AUTHENTICATION_KEY))));
		}
		if (textFromFile.contains(REP_USERNAME_KEY)) {
			repUserName = textFromFile.substring(
					textFromFile.indexOf(REP_USERNAME_KEY)
							+ REP_USERNAME_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(REP_USERNAME_KEY)));
		} else {
			repUserName = "";
		}
		if (textFromFile.contains(REP_PASSWORD_KEY)) {
			repPassword = textFromFile.substring(
					textFromFile.indexOf(REP_PASSWORD_KEY)
							+ REP_PASSWORD_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(REP_PASSWORD_KEY)));
		} else {
			repPassword = "";
		}
		if (textFromFile.contains(NEXUS_USERNAME_KEY)) {
			nexusUserName = textFromFile.substring(
					textFromFile.indexOf(NEXUS_USERNAME_KEY)
							+ NEXUS_USERNAME_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(NEXUS_USERNAME_KEY)));
		} else {
			nexusUserName = "";
		}
		if (textFromFile.contains(NEXUS_PASSWORD_KEY)) {
			nexusPassword = textFromFile.substring(
					textFromFile.indexOf(NEXUS_PASSWORD_KEY)
							+ NEXUS_PASSWORD_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(NEXUS_PASSWORD_KEY)));
		} else {
			nexusPassword = "";
		}
		if (textFromFile.contains(NEXUS_KEY)) {
			nexusUrl = textFromFile.substring(
					textFromFile.indexOf(NEXUS_KEY) + NEXUS_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(NEXUS_KEY)));
		} else {
			nexusUrl = "";
		}
		if (textFromFile.contains(CURL_KEY)) {
			curlUrl = textFromFile.substring(
					textFromFile.indexOf(CURL_KEY) + CURL_KEY.length(),
					textFromFile.indexOf(BREAK_SIGNAL,
							textFromFile.indexOf(CURL_KEY)));
		} else {
			curlUrl = "";
		}
	}
}
