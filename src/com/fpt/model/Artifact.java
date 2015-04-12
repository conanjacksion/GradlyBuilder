package com.fpt.model;

import java.io.Serializable;

public class Artifact extends BaseDependency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7634779157276825800L;
	private String url;
	private String groupId;
	private String artifactId;
	private String version;

	public String getArtifactName() {
		return groupId + ":" + artifactId + ":" + version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String toString() {
		return getArtifactName();
	}
}
