package com.fpt.model;

import java.io.Serializable;

public class JarLib extends BaseDependency implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7753597393253250770L;
	private String url;
	private String name;

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

	public String toString() {
		if (name != null && !name.equals("")) {
			return name;
		}
		return url;
	}

}
