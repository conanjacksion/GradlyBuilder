package com.fpt.model;

import java.io.Serializable;

public class AndroidOutputData implements Serializable, IOutputData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int OUTPUT_NEXUS = 0x1;
	public final static int OUTPUT_SERVER = 0xA;
	public final static int OUTPUT_LOCAL = 0x14;

	public final static int BUILD_TYPE_APK = 1;
	public final static int BUILD_TYPE_AAR = 2;

	private int outputWay;
	private String nexusGroupId;
	private String nexusArtifactId;
	private String nexusVersion;
	private String outputServerUrl;
	private String outputLocalUrl;
	private int buildType = BUILD_TYPE_APK;

	public int getOutputWay() {
		return outputWay;
	}

	public void setOutputWay(int outputWay) {
		this.outputWay = outputWay;
	}
	

	public String getNexusGroupId() {
		return nexusGroupId;
	}

	public void setNexusGroupId(String nexusGroupId) {
		this.nexusGroupId = nexusGroupId;
	}

	public String getNexusArtifactId() {
		return nexusArtifactId;
	}

	public void setNexusArtifactId(String nexusArtifactId) {
		this.nexusArtifactId = nexusArtifactId;
	}

	public String getNexusVersion() {
		return nexusVersion;
	}

	public void setNexusVersion(String nexusVersion) {
		this.nexusVersion = nexusVersion;
	}

	public String getOutputServerUrl() {
		return outputServerUrl;
	}

	public void setOutputServerUrl(String outputServerUrl) {
		this.outputServerUrl = outputServerUrl;
	}

	public String getOutputLocalUrl() {
		return outputLocalUrl;
	}

	public void setOutputLocalUrl(String outputLocalUrl) {
		this.outputLocalUrl = outputLocalUrl;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

}
