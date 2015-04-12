package com.fpt.build;

import com.fpt.model.IDependenciesData;
import com.fpt.model.IInputData;
import com.fpt.model.IOutputData;

/**
 * This class use to process a gradle build project
 * 
 * @author QuyPP1
 *
 */
public class GradleBuildManager {

	public void build(String gradleType, IInputData inData,
			IOutputData outData, IDependenciesData depenData) {
		IGradleBuild gradleBuild = GradleBuildFactory.getGradleBuild(
				gradleType, inData, outData, depenData);
		gradleBuild.executeBuild();
	}
}
