package com.fpt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class AndroidDependenciesData implements Serializable, IDependenciesData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6694841707074324274L;
	private ArrayList<Module> moduleList;

	public ArrayList<Module> getModuleList() {
		return moduleList;
	}

	public void setModuleList(ArrayList<Module> moduleList) {
		this.moduleList = moduleList;
	}

}
