package com.fpt.build;

import java.io.File;
import java.util.ArrayList;

import com.fpt.gui.BuildManager;
import com.fpt.model.AndroidDependenciesData;
import com.fpt.model.AndroidInputData;
import com.fpt.model.AndroidOutputData;
import com.fpt.model.Artifact;
import com.fpt.model.BaseDependency;
import com.fpt.model.JarLib;
import com.fpt.model.Module;
import com.fpt.model.SettingsData;
import com.fpt.util.CommandLine;
import com.fpt.util.DataHelper;
import com.fpt.util.IOHelper;
import com.fpt.util.StringHelper;

/**
 * Generate Android Gradle Build file
 * 
 * @author QuyPP1
 *
 */
public class AndroidGradleBuild implements IGradleBuild {
	public static final String REPOSITORY_PREFIX = "/content/repositories/";

	public enum Repository {
		REPOSITORY_JCENTER("jcenter()"), REPOSITORY_MAVEN("maven()");
		Repository(String text) {
			this.text = text;
		}

		private String text;

		public String getText() {
			return text;
		}
	}

	private AndroidInputData inData;
	private AndroidOutputData outData;
	private AndroidDependenciesData depData;
	private SettingsData settingsData;

	public AndroidGradleBuild(AndroidInputData inData,
			AndroidOutputData outData, AndroidDependenciesData depData) {
		this.inData = inData;
		this.outData = outData;
		this.depData = depData;
		settingsData = new SettingsData();
		settingsData.readFromFile();
	}

	private void createLocalDotPropertiesFile() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("# Location of the android SDK");
		String androidSDKUrl = settingsData.getAndroidSDKURL() == null ? ""
				: settingsData.getAndroidSDKURL();
		if (!androidSDKUrl.equals("")) {
			stringBuilder.append("\nsdk.dir="
					+ StringHelper.convertToBackFlash(androidSDKUrl));
			String fileName = inData.getProjectURL() + "/"
					+ DataHelper.LOCAL_DOT_PROPERTIES_FILE_NAME;
			IOHelper.writeToBinFile(stringBuilder.toString(), fileName);
		}
	}

	public void createSettingsDotGradleFile() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("include");
		ArrayList<Module> moduleList = depData.getModuleList();
		for (int i = 0; i < moduleList.size(); i++) {
			stringBuilder.append(" ':" + moduleList.get(i).getDirName() + "'");
			if (i < moduleList.size() - 1) {
				stringBuilder.append(",");
			}
		}
		if (!stringBuilder.toString().equals("include")) {
			String fileName = inData.getProjectURL() + "/"
					+ DataHelper.SETTINGS_DOT_GRADLE_FILE_NAME;
			IOHelper.writeToBinFile(stringBuilder.toString(), fileName);
		}
	}

	private void createBuildDotGradleFile(Module moduleItem) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(genBuildScriptPart(Repository.REPOSITORY_JCENTER
				.getText()));
		stringBuilder.append("\n" + genApplyPluginPart(moduleItem));
		stringBuilder.append("\n"
				+ genRepositoriesPart(Repository.REPOSITORY_JCENTER.getText()));
		stringBuilder.append("\n" + genDependenciesPart(moduleItem));
		stringBuilder.append("\n" + genAndroidPart(moduleItem));
		if (moduleItem.isMainModule()) {
			if (checkUploadNexus()) {
				stringBuilder.append("\n" + genUploadPart());
			}
		}
		if (!stringBuilder.toString().equals("")) {
			String fileName = moduleItem.getUrl() + "/"
					+ DataHelper.BUILD_DOT_GRADLE_FILE_NAME;
			IOHelper.writeToBinFile(stringBuilder.toString(), fileName);
		}
	}

	private void createModuleBuildDotGradleFile(Module moduleItem) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(genApplyPluginPart(moduleItem));
		stringBuilder.append("\n" + genDependenciesPart(moduleItem));
		stringBuilder.append("\n" + genAndroidPart(moduleItem));
		if (moduleItem.isMainModule()) {
			if (checkUploadNexus()) {
				stringBuilder.append("\n" + genUploadPart());
			}
		}
		if (!stringBuilder.toString().equals("")) {
			String fileName = moduleItem.getUrl() + "/"
					+ DataHelper.BUILD_DOT_GRADLE_FILE_NAME;
			IOHelper.writeToBinFile(stringBuilder.toString(), fileName);
		}
	}

	private void createRootBuildDotGradleFile() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(genBuildScriptPart(Repository.REPOSITORY_JCENTER
				.getText()));
		stringBuilder.append("\n" + "subprojects {");
		stringBuilder.append("\n"
				+ genRepositoriesPart(Repository.REPOSITORY_JCENTER.getText()));
		stringBuilder.append("\n" + "}");
		if (!stringBuilder.toString().equals("")) {
			String fileName = inData.getProjectURL() + "/"
					+ DataHelper.BUILD_DOT_GRADLE_FILE_NAME;
			IOHelper.writeToBinFile(stringBuilder.toString(), fileName);
		}
	}

	private String genApplyPluginPart(Module moduleItem) {
		StringBuilder stringBuilder = new StringBuilder();
		if (moduleItem.isProjectLibrary()
				|| outData.getBuildType() == AndroidOutputData.BUILD_TYPE_AAR) {
			stringBuilder.append("apply plugin: \'com.android.library\'");
		} else {
			stringBuilder.append("apply plugin: \'com.android.application\'");
		}
		if (moduleItem.isMainModule()) {
			if (checkUploadNexus()) {
				stringBuilder.append("\napply plugin: \'maven\'");
			}
		}
		return stringBuilder.toString();
	}

	private String genBuildScriptPart(String repository) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("buildscript {");
		stringBuilder.append("\n" + "repositories {");
		stringBuilder.append("\n" + repository);
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "dependencies {");
		stringBuilder.append("\n"
				+ "classpath \'com.android.tools.build:gradle:1.2.+\'");
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "}");
		return stringBuilder.toString();
	}

	private String genRepositoriesPart(String repository) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("repositories {");
		String remoteRep = "";
		if (!settingsData.getNexusUrl().equals("")) {
			remoteRep += settingsData.getNexusUrl() + REPOSITORY_PREFIX
					+ settingsData.getRepository();
		}
		if (remoteRep.equals("") ? false : true) {
			stringBuilder.append("\n" + "maven {");
			if (settingsData.isRepAuthentication()) {
				stringBuilder.append("\n" + "credentials {");
				stringBuilder.append("\n" + "username '"
						+ settingsData.getRepUserName() + "'");
				stringBuilder.append("\n" + "password '"
						+ settingsData.getRepPassword() + "'");
				stringBuilder.append("\n" + "}");
			}
			stringBuilder.append("\n" + "url '"
					+ StringHelper.convertToBackFlash(remoteRep) + "'");
			stringBuilder.append("\n" + "}");
		}
		if (!repository.equals("")) {
			stringBuilder.append("\n" + repository);

		}
		stringBuilder.append("\n" + "}");
		return stringBuilder.toString();
	}

	private String genDependenciesPart(Module moduleItem) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("dependencies {");
		ArrayList<BaseDependency> dependenciesList = moduleItem
				.getDependenciesList();
		if (dependenciesList != null) {
			for (BaseDependency depItem : dependenciesList) {
				if (depItem instanceof Module) {
					if (((Module) depItem).getDirName() != null
							&& !((Module) depItem).getDirName().equals("")) {
						stringBuilder.append("\n"
								+ depItem.getScope().getText() + " project(\':"
								+ ((Module) depItem).getDirName() + "\')");
					} else {
						stringBuilder.append("\n"
								+ depItem.getScope().getText() + " project(\'"
								+ ((Module) depItem).getUrl() + "\')");
					}
				} else if (depItem instanceof Artifact) {
					stringBuilder.append("\n" + depItem.getScope().getText()
							+ " \'" + ((Artifact) depItem).getArtifactName()
							+ "\'");
				} else if (depItem instanceof JarLib) {
					stringBuilder
							.append("\n" + depItem.getScope().getText()
									+ " files(\'" + ((JarLib) depItem).getUrl()
									+ "\')");
				}
			}
		}
		stringBuilder.append("\n" + "}");
		return stringBuilder.toString();
	}

	private String genUploadPart() {
		StringBuilder stringBuilder = new StringBuilder();
		String remoteRep = "";
		if (!settingsData.getNexusUrl().equals("")) {
			remoteRep += settingsData.getNexusUrl() + REPOSITORY_PREFIX
					+ settingsData.getRepository();
		}
		if (remoteRep.equals("") ? false : true) {
			stringBuilder.append("uploadArchives {");
			stringBuilder.append("\nrepositories {");
			stringBuilder.append("\nmavenDeployer {");
			stringBuilder.append("\nrepository(url: \""
					+ StringHelper.convertToBackFlash(remoteRep) + "\") {");
			stringBuilder.append("\nauthentication(userName: '"
					+ settingsData.getNexusUserName() + "', password: '"
					+ settingsData.getNexusPassword() + "')");
			stringBuilder.append("\n}");
			stringBuilder.append("\npom.version = '"
					+ outData.getNexusVersion() + "'");
			stringBuilder.append("\npom.artifactId = '"
					+ outData.getNexusArtifactId() + "'");
			stringBuilder.append("\npom.groupId = '"
					+ outData.getNexusGroupId() + "'");
			stringBuilder.append("\n}");
			stringBuilder.append("\n}");
			stringBuilder.append("\n}");
		}
		return stringBuilder.toString();
	}

	private String genAndroidPart(Module moduleItem) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("android {");
		stringBuilder.append("\n" + "compileSdkVersion "
				+ moduleItem.getCompileSDKVer());
		stringBuilder.append("\n" + "buildToolsVersion \""
				+ moduleItem.getBuildToolsVer() + "\"");
		stringBuilder.append("\n" + "dexOptions {");
		if (moduleItem.isJumboMode()) {
			stringBuilder.append("\n" + "jumboMode true");
		}
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "defaultConfig {");
		stringBuilder.append("\n" + "minSdkVersion "
				+ moduleItem.getMinSDKVer());
		stringBuilder.append("\n" + "targetSdkVersion "
				+ moduleItem.getTargetSDKVer());
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "lintOptions {");
		stringBuilder.append("\n" + "abortOnError false");
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "sourceSets {");
		stringBuilder.append("\n" + "main {");
		if (!DataHelper.checkIsGradleProject(inData.getProjectURL())) {
			stringBuilder.append("\n"
					+ "manifest.srcFile \'AndroidManifest.xml\'");
			stringBuilder.append("\n" + "java.srcDirs = [\'src\']");
			stringBuilder.append("\n" + "resources.srcDirs = [\'src\']");
			stringBuilder.append("\n" + "aidl.srcDirs = [\'src\']");
			stringBuilder.append("\n" + "renderscript.srcDirs = [\'src\']");
			stringBuilder.append("\n" + "res.srcDirs = [\'res\']");
			stringBuilder.append("\n" + "assets.srcDirs = [\'assets\']");
		}
		if (inData.isUsingNativeLib() && !moduleItem.isProjectLibrary()) {
			stringBuilder.append("\n" + "jniLibs.srcDirs = [\'"
					+ StringHelper.convertToBackFlash(inData.getNativeLibUrl())
					+ "\']");
		}
		stringBuilder.append("\n" + "}");
		if (!DataHelper.checkIsGradleProject(inData.getProjectURL())) {
			stringBuilder.append("\n" + "instrumentTest.setRoot(\'tests\')");
			stringBuilder.append("\n" + "debug.setRoot(\'build-types/debug\')");
			stringBuilder.append("\n"
					+ "release.setRoot(\'build-types/release\')");
		}
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "buildTypes {");
		stringBuilder.append("\n" + "release {");
		stringBuilder.append("\n" + "minifyEnabled false");
		if (inData.isProguard()) {
			stringBuilder
					.append("\n"
							+ "proguardFiles getDefaultProguardFile(\'proguard-android.txt\'), \'proguard-rules.pro\'");
		}
		stringBuilder.append("\n" + "}");
		stringBuilder.append("\n" + "}");
		if (checkOutputLocal() && moduleItem.isMainModule()) {
			String outputLocalUrl = outData.getOutputLocalUrl() == null ? ""
					: outData.getOutputLocalUrl();
			if (moduleItem.isProjectLibrary()
					|| outData.getBuildType() == AndroidOutputData.BUILD_TYPE_AAR) {
				stringBuilder.append("\n"
						+ "android.libraryVariants.all { variant ->");
				stringBuilder.append("\n" + "variant.outputs.each { output ->");
				stringBuilder.append("\n" + "output.outputFile = new File(\""
						+ StringHelper.convertToBackFlash(outputLocalUrl)
						+ "\",output.outputFile.name)");
				// +
				// "\",output.outputFile.name.replace(\".apk\", \"-${variant.versionName}.apk\"))");

				stringBuilder.append("\n" + "}");
				stringBuilder.append("\n" + "}");
			} else {
				stringBuilder.append("\n"
						+ "applicationVariants.all { variant ->");
				stringBuilder.append("\n" + "variant.outputs.each { output ->");
				stringBuilder.append("\n" + "output.outputFile = new File(\""
						+ StringHelper.convertToBackFlash(outputLocalUrl)
						+ "\",output.outputFile.name)");
				// +
				// "\",output.outputFile.name.replace(\".apk\", \"-${variant.versionName}.apk\"))");

				stringBuilder.append("\n" + "}");
				stringBuilder.append("\n" + "}");
			}
		}
		stringBuilder.append("\n" + "}");
		return stringBuilder.toString();
	}

	public void build() {

	}

	private boolean checkUploadNexus() {
		if (outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						+ AndroidOutputData.OUTPUT_LOCAL
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						+ AndroidOutputData.OUTPUT_SERVER
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						+ AndroidOutputData.OUTPUT_LOCAL
						+ AndroidOutputData.OUTPUT_SERVER) {
			return true;
		}
		return false;
	}

	private boolean checkOutputLocal() {
		if (outData.getOutputWay() == AndroidOutputData.OUTPUT_LOCAL
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						+ AndroidOutputData.OUTPUT_LOCAL
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_LOCAL
						+ AndroidOutputData.OUTPUT_SERVER
				|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						+ AndroidOutputData.OUTPUT_LOCAL
						+ AndroidOutputData.OUTPUT_SERVER) {
			return true;
		}
		return false;
	}

	@Override
	public void executeBuild() {
		// TODO Auto-generated method stub
		// LogDialog.getInstance("Log");
		try {
			if (inData.getProjectURL() == null
					|| inData.getProjectURL().equals("")) {
				throw new Exception("Project has not been defined.");
			}
			if (checkUploadNexus()) {
				if (outData.getNexusGroupId() == null
						|| outData.getNexusGroupId().equals("")
						|| outData.getNexusArtifactId() == null
						|| outData.getNexusArtifactId().equals("")
						|| outData.getNexusVersion() == null
						|| outData.getNexusVersion().equals("")) {
					throw new Exception(
							"Group id, artifact id and version have to be defined");
				}
			}

			// if (!DataHelper.checkIsGradleProject(inData.getProjectURL())) {
			createLocalDotPropertiesFile();
			ArrayList<Module> moduleList = depData.getModuleList();
			if (moduleList.size() > 1) {
				createSettingsDotGradleFile();
				createRootBuildDotGradleFile();
				for (Module moduleItem : moduleList) {
					createModuleBuildDotGradleFile(moduleItem);
				}
			} else if (moduleList.size() == 1) {
				createBuildDotGradleFile(moduleList.get(0));
			}
			// }
			// String[] cmd = new String[2];
			// cmd[0] = "cd "
			// + StringHelper.convertToBackFlash(inData.getProjectURL());
			// cmd[1] = "gradle clean build";
			// CommandLine.runCmd(cmd);
			if (settingsData.getAndroidSDKURL().equals("")
					|| settingsData.getGradleUrl().equals("")
					|| settingsData.getNexusUrl().equals("")
					|| settingsData.getNexusUserName().equals("")
					|| settingsData.getNexusPassword().equals("")) {
				throw new Exception(
						"Check Setting to verify all configuration were defined.");
			} else {
				ArrayList<String> cmdList = new ArrayList<String>();
				cmdList.add("cd "
						+ StringHelper.convertToBackFlash(settingsData
								.getGradleBinUrl()));
				String cmdGradle = "gradle clean build";
				if (outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
						|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
								+ AndroidOutputData.OUTPUT_LOCAL
						|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
								+ AndroidOutputData.OUTPUT_SERVER
						|| outData.getOutputWay() == AndroidOutputData.OUTPUT_NEXUS
								+ AndroidOutputData.OUTPUT_LOCAL
								+ AndroidOutputData.OUTPUT_SERVER) {
					cmdGradle += " upload";
					// if (outData.getNexusGroupId() == null
					// || outData.getNexusGroupId().equals("")
					// || outData.getNexusArtifactId() == null
					// || outData.getNexusArtifactId().equals("")
					// || outData.getNexusVersion() == null
					// || outData.getNexusVersion().equals("")) {
					// throw new Exception(
					// "Group id, artifact id and version have to be defined");
					// }
					// cmdList.add("cd "
					// + StringHelper.convertToBackFlash(System
					// .getProperty("user.dir")) + "/utils");
					// StringBuilder stringBuilder = new StringBuilder();
					// stringBuilder.append("curl -v");
					// stringBuilder.append(" -F r="
					// + settingsData.getRepository());
					// stringBuilder.append(" -F hasPom=false");
					// stringBuilder.append(" -F e=apk");
					// stringBuilder.append(" -F g=" +
					// outData.getNexusGroupId());
					// stringBuilder.append(" -F a="
					// + outData.getNexusArtifactId());
					// stringBuilder.append(" -F v=" +
					// outData.getNexusVersion());
					// stringBuilder.append(" -F p=pom");
					// String outputLocalUrl = outData.getOutputLocalUrl() ==
					// null ? ""
					// : outData.getOutputLocalUrl();
					// stringBuilder.append(" -F file=@"
					// + StringHelper.convertToBackFlash(outputLocalUrl)
					// + "/" + inData.getProjectDirName() + "-debug.apk");
					// stringBuilder.append(" -u "
					// + settingsData.getNexusUserName() + ":"
					// + settingsData.getNexusPassword());
					// stringBuilder.append(" " + settingsData.getNexusUrl()
					// + "/service/local/artifact/maven/content");
					// cmdList.add(stringBuilder.toString());
				}
				cmdGradle += " -p"
						+ StringHelper.convertToBackFlash(inData
								.getProjectURL());
				cmdList.add(cmdGradle);
				BuildManager.updateLog("Building....");
				CommandLine.runCmd(cmdList.toArray(new String[cmdList.size()]));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			BuildManager.updateLog(ex.getMessage());
		}
	}
}
