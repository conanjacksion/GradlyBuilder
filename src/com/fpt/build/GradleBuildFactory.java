package com.fpt.build;

import com.fpt.model.AndroidDependenciesData;
import com.fpt.model.AndroidInputData;
import com.fpt.model.AndroidOutputData;
import com.fpt.model.IDependenciesData;
import com.fpt.model.IInputData;
import com.fpt.model.IOutputData;

/**
 * This class to generate a gradle build: AndroidGradleBuild,JavaGradleBuild,
 * IosGradleBuild
 * 
 * @author QuyPP1
 *
 */
public class GradleBuildFactory {
	public static final String GRADLE_ANDROID = "gradleAndroid";

	public static IGradleBuild getGradleBuild(String gradleType,
			IInputData inData, IOutputData outData, IDependenciesData depenData) {
		if (gradleType == null) {
			return null;
		}
		if (gradleType.equals(GRADLE_ANDROID)) {
			return new AndroidGradleBuild((AndroidInputData) inData,
					(AndroidOutputData) outData,
					(AndroidDependenciesData) depenData);
		}
		return null;
	}
}
