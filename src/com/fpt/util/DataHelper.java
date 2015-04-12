package com.fpt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fpt.gui.AndroidInputPane;
import com.fpt.gui.BuildManager;
import com.fpt.model.Artifact;
import com.fpt.model.BaseDependency;
import com.fpt.model.BaseDependency.Scope;
import com.fpt.model.JarLib;
import com.fpt.model.Module;

public class DataHelper {

	public static final String DOT_PROJECT_FILE_NAME = ".project";
	public static final String PROJECT_DOT_PROPERTIES_FILE_NAME = "project.properties";
	public static final String IS_ANDROID_LIBRARY_KEYWORD = "android.library=true";
	public static final String IS_ANDROID_JUMBOMODE_KEYWORD = "dex.force.jumbo=true";
	public static final String ANDROID_REFERENCE_KEYWORD = "android.library.reference.";
	public static final String SETTINGS_DOT_GRADLE_FILE_NAME = "settings.gradle";
	public static final String BUILD_DOT_GRADLE_FILE_NAME = "build.gradle";
	public static final String LOCAL_DOT_PROPERTIES_FILE_NAME = "local.properties";
	private static ArrayList<Artifact> artifactList;

	public static ArrayList<Artifact> getAllArtifacts(String remoteRep) {
		artifactList = new ArrayList<Artifact>();
		getArtifact(remoteRep);
		return artifactList;
	}

	private static void getArtifact(String urlStr) {
		try {
			Document docGroup = Jsoup.connect(urlStr).get();
			Elements links = docGroup.getElementsByTag("a");
			for (Element link : links) {
				String linkHref = link.attr("href");
				String linkText = link.text();
				if (checkUrlIsDirectory(linkHref)) {
					getArtifact(linkHref);
				} else {
					if (linkText.equals("maven-metadata.xml")) {
						Artifact artifact = new Artifact();
						artifact.setUrl(linkHref.substring(0,
								linkHref.indexOf("maven-metadata.xml") - 1));
						// get more information of artifact via reading
						// maven-metadata.xml file
						URL url = new URL(linkHref);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(url.openStream()));
						String metadataStr = "";
						String line = "";
						while ((line = br.readLine()) != null) {
							metadataStr += line;
						}
						org.w3c.dom.Document documentMetadata = Parser
								.loadXMLFromString(metadataStr);
						Node nodeRoot = documentMetadata.getFirstChild();
						NodeList nodeList = nodeRoot.getChildNodes();
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node nodeChild = nodeList.item(i);
							if (nodeChild.getNodeName().equals("groupId")) {
								artifact.setGroupId(nodeChild.getTextContent());
							}
							if (nodeChild.getNodeName().equals("artifactId")) {
								artifact.setArtifactId(nodeChild
										.getTextContent());
							}
							if (nodeChild.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nodeChildList = nodeChild
										.getChildNodes();
								for (int x = 0; x < nodeChildList.getLength(); x++) {
									Node nodeChild1 = nodeChildList.item(x);
									if (nodeChild1.getNodeName().equals(
											"versions")) {
										NodeList nodeChild1List = nodeChild1
												.getChildNodes();
										for (int n = 0; n < nodeChild1List
												.getLength(); n++) {
											Node nodeChild2 = nodeChild1List
													.item(n);
											if (nodeChild2.getNodeName()
													.equals("version")) {
												artifact.setVersion(nodeChild2
														.getTextContent());
											}
										}
									}
								}
							}
						}
						artifactList.add(artifact);
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static boolean checkUrlIsDirectory(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
			if (url != null) {
				if (urlStr.indexOf("/", urlStr.length() - 1) > -1) {
					return true;
				}
			}
			// // if (url.getProtocol().equals("file")) {
			// return (new File(url.getFile())).isDirectory();
			// // }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}

	private static ArrayList<Module> moduleList;

	public static ArrayList<Module> getAllModules(File folder) {
		moduleList = new ArrayList<Module>();
		if (checkIsGradleProject(AndroidInputPane.getData().getProjectURL())) {
			getModuleFromGradleProject(folder);
		} else {
			getModuleFromNonGradleProject(folder);
			verifyDependencies();
		}
		verifyMainModule();
		return moduleList;
	}

	public static boolean checkIsGradleProject(final String folderUrl) {
		final File folder = new File(folderUrl);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				continue;
			} else {
				if (fileEntry.getName().equals(BUILD_DOT_GRADLE_FILE_NAME)) {
					return true;
				}
			}
		}
		return false;
	}

	private static String getValueFromProperty(String fileContent,
			String property) {
		String value = null;
		if (fileContent.contains(property)) {
			String line = fileContent.substring(fileContent.indexOf(property),
					fileContent.indexOf("\n", fileContent.indexOf(property)))
					.trim();
			String[] lineArray = line.split(" ");
			value = lineArray[lineArray.length - 1].replace("\"", "");
		}
		return value;
	}

	private static void getModuleFromGradleProject(final File folder) {
		try {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					getModuleFromGradleProject(fileEntry);
				} else {
					if (fileEntry.getName().equals(BUILD_DOT_GRADLE_FILE_NAME)) {
						String buildDotGardleContent = IOHelper
								.readFromBinFile(fileEntry.getAbsolutePath())
								.trim();
						String[] buildDotGradleContentLines = buildDotGardleContent
								.split("\n");
						ArrayList<String> buildDotGradleContentLineList = new ArrayList<String>(
								Arrays.asList(buildDotGradleContentLines));
						for (int i = 0; i < buildDotGradleContentLineList
								.size(); i++) {
							if (buildDotGradleContentLineList.get(i).trim()
									.indexOf("//") == 0) {
								buildDotGradleContentLineList.remove(i);
							}
						}
						StringBuilder stringBuilder = new StringBuilder();
						for (String line : buildDotGradleContentLineList) {
							stringBuilder.append(line + "\n");
						}
						buildDotGardleContent = stringBuilder.toString();
						if (!buildDotGardleContent.contains("apply plugin")) {
							continue;
						}
						System.out.println(fileEntry.getAbsolutePath());
						Module moduleItem = new Module();
						File parent = fileEntry.getParentFile();
						moduleItem.setDirName(parent.getName());
						moduleItem.setName(parent.getName());
						moduleItem.setUrl(parent.getAbsolutePath());
						if (buildDotGardleContent
								.contains("apply plugin: 'com.android.library'")
								|| buildDotGardleContent
										.contains("apply plugin: 'android-library'")) {
							moduleItem.setProjectLibrary(true);
						} else if (buildDotGardleContent
								.contains("apply plugin: 'com.android.application'")
								|| buildDotGardleContent
										.contains("apply plugin: 'android-application'")) {
							moduleItem.setMainModule(true);
						}
						if (getValueFromProperty(buildDotGardleContent,
								"compileSdkVersion") != null) {
							moduleItem.setCompileSDKVer(Integer
									.parseInt(getValueFromProperty(
											buildDotGardleContent,
											"compileSdkVersion")));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"buildToolsVersion") != null) {
							moduleItem
									.setBuildToolsVer(getValueFromProperty(
											buildDotGardleContent,
											"buildToolsVersion"));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"minSdkVersion") != null) {
							moduleItem.setMinSDKVer(Integer
									.parseInt(getValueFromProperty(
											buildDotGardleContent,
											"minSdkVersion")));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"targetSdkVersion") != null) {
							moduleItem.setTargetSDKVer(Integer
									.parseInt(getValueFromProperty(
											buildDotGardleContent,
											"targetSdkVersion")));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"versionCode") != null) {
							moduleItem.setVersionCode(Integer
									.parseInt(getValueFromProperty(
											buildDotGardleContent,
											"versionCode")));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"versionName") != null) {
							moduleItem.setVersionName(getValueFromProperty(
									buildDotGardleContent, "versionName"));
						}
						if (getValueFromProperty(buildDotGardleContent,
								"jumboMode") != null) {
							moduleItem
									.setJumboMode(Boolean
											.parseBoolean(getValueFromProperty(
													buildDotGardleContent,
													"jumboMode")));
						}
						ArrayList<BaseDependency> dependenciesList = new ArrayList<BaseDependency>();
						String[] buildDotGardleContentLines = buildDotGardleContent
								.split("\n");
						for (String line : buildDotGardleContentLines) {
							if (line.contains("compile files")) {
								String jarUrl = line
										.substring(
												line.indexOf(
														"'",
														line.indexOf("compile files")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								JarLib jarLib = new JarLib();
								jarLib.setUrl(jarUrl);
								jarLib.setScope(Scope.COMPILE);
								jarLib.setName(StringHelper
										.getLastElementFromUrl(jarUrl));
								dependenciesList.add(jarLib);
							} else if (line.contains("provided files")) {
								String jarUrl = line
										.substring(
												line.indexOf(
														"'",
														line.indexOf("provided files")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								JarLib jarLib = new JarLib();
								jarLib.setUrl(jarUrl);
								jarLib.setScope(Scope.PROVIDED);
								jarLib.setName(StringHelper
										.getLastElementFromUrl(jarUrl));
								dependenciesList.add(jarLib);
							} else if (line.contains("compile project")) {
								String moduleDirName = line
										.substring(
												line.indexOf(
														":",
														line.indexOf("compile project")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								Module module = new Module();
								module.setScope(Scope.COMPILE);
								module.setUrl(moduleDirName);
								module.setName(moduleDirName);
								module.setDirName(moduleDirName);
								dependenciesList.add(module);
							} else if (line.contains("provided project")) {
								String moduleDirName = line
										.substring(
												line.indexOf(
														":",
														line.indexOf("provided project")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								Module module = new Module();
								module.setScope(Scope.PROVIDED);
								module.setUrl(moduleDirName);
								module.setName(moduleDirName);
								module.setDirName(moduleDirName);
								dependenciesList.add(module);
							} else if (line.contains("compile fileTree")) {
								String fileTree = line
										.substring(
												line.indexOf(
														"(",
														line.indexOf("compile fileTree")) + 1,
												line.indexOf(")",
														line.indexOf("(") + 1));
								String dir = fileTree
										.substring(
												fileTree.indexOf("'", fileTree
														.indexOf("jarLib")) + 1,
												fileTree.indexOf(
														"'",
														fileTree.indexOf("'") + 1));
								String dirUrl = StringHelper
										.convertRelativeUrlToAbsoluteUrl(
												parent.getAbsolutePath(), dir);
								File dirFile = new File(dirUrl);
								for (File file : dirFile.listFiles()) {
									JarLib jarLib = new JarLib();
									jarLib.setName(file.getName());
									jarLib.setUrl(StringHelper
											.convertAbsoluteUrlToRelativeUrl(
													parent.getAbsolutePath(),
													file.getAbsolutePath()));
									jarLib.setScope(Scope.COMPILE);
									dependenciesList.add(jarLib);
								}
							} else if (line.contains("provided fileTree")) {
								String fileTree = line
										.substring(
												line.indexOf(
														"(",
														line.indexOf("provided fileTree")) + 1,
												line.indexOf(")",
														line.indexOf("(") + 1));
								String dir = fileTree
										.substring(
												fileTree.indexOf("'", fileTree
														.indexOf("jarLib")) + 1,
												fileTree.indexOf(
														"'",
														fileTree.indexOf("'") + 1));
								String dirUrl = StringHelper
										.convertRelativeUrlToAbsoluteUrl(
												parent.getAbsolutePath(), dir);
								File dirFile = new File(dirUrl);
								for (File file : dirFile.listFiles()) {
									JarLib jarLib = new JarLib();
									jarLib.setName(file.getName());
									jarLib.setUrl(StringHelper
											.convertAbsoluteUrlToRelativeUrl(
													parent.getAbsolutePath(),
													file.getAbsolutePath()));
									jarLib.setScope(Scope.PROVIDED);
									dependenciesList.add(jarLib);
								}
							} else if (line.contains("compile '")) {
								String artifactName = line
										.substring(line.indexOf("'",
												line.indexOf("compile")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								String[] artifactNameArray = artifactName
										.split(":");
								Artifact artifact = new Artifact();
								artifact.setScope(Scope.COMPILE);
								artifact.setGroupId(artifactNameArray[0]);
								artifact.setArtifactId(artifactNameArray[1]);
								artifact.setVersion(artifactNameArray[2]);
								dependenciesList.add(artifact);
							} else if (line.contains("provided '")) {
								String artifactName = line
										.substring(line.indexOf("'",
												line.indexOf("provided")) + 1,
												line.indexOf("'",
														line.indexOf("'") + 1));
								String[] artifactNameArray = artifactName
										.split(":");
								Artifact artifact = new Artifact();
								artifact.setScope(Scope.PROVIDED);
								artifact.setGroupId(artifactNameArray[0]);
								artifact.setArtifactId(artifactNameArray[1]);
								artifact.setVersion(artifactNameArray[2]);
								dependenciesList.add(artifact);
							}
						}
						if (dependenciesList.size() > 0) {
							moduleItem.setDependenciesList(dependenciesList);
						}
						moduleList.add(moduleItem);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getModuleFromNonGradleProject(final File folder) {
		try {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					getModuleFromNonGradleProject(fileEntry);
				} else {
					if (fileEntry.getName().equals(DOT_PROJECT_FILE_NAME)) {
						Module moduleItem = new Module();
						File parent = fileEntry.getParentFile();
						moduleItem.setDirName(parent.getName());
						moduleItem.setUrl(parent.getAbsolutePath());
						String projectDesStr = IOHelper.readFromBinFile(
								fileEntry.getAbsolutePath()).trim();
						System.out.println(fileEntry.getAbsolutePath());
						try {
							org.w3c.dom.Document docPrjDes = Parser
									.loadXMLFromString(projectDesStr);
							Node nodeRoot = docPrjDes.getFirstChild();
							NodeList nodeList = nodeRoot.getChildNodes();
							for (int i = 0; i < nodeList.getLength(); i++) {
								Node nodeChild = nodeList.item(i);
								if (nodeChild.getNodeName().equals("name")) {
									moduleItem.setName(nodeChild
											.getTextContent());
									break;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						for (File fileEntry1 : parent.listFiles()) {
							if (fileEntry1.getName().equals(
									PROJECT_DOT_PROPERTIES_FILE_NAME)) {
								String prjDotProContent = IOHelper
										.readFromBinFile(fileEntry1
												.getAbsolutePath());
								if (prjDotProContent
										.indexOf(IS_ANDROID_LIBRARY_KEYWORD) > -1) {
									moduleItem.setProjectLibrary(true);
								}
								if (prjDotProContent
										.indexOf(IS_ANDROID_JUMBOMODE_KEYWORD) > -1) {
									moduleItem.setJumboMode(true);
								}
								String[] prjDotProContentArray = prjDotProContent
										.split("\n");
								ArrayList<BaseDependency> dependenciesList = null;
								for (String contentLine : prjDotProContentArray) {
									if (contentLine
											.indexOf(ANDROID_REFERENCE_KEYWORD) > -1) {
										String[] contentLineArray = contentLine
												.split("=");
										String referenceUrl = StringHelper
												.convertToBackFlash(contentLineArray[1]);
										if (referenceUrl != null
												&& !referenceUrl.equals("")) {
											if (dependenciesList == null) {
												dependenciesList = new ArrayList<BaseDependency>();
											}
											Module moduleDep = new Module();
											String[] referenceUrlArray = referenceUrl
													.split("/");
											String moduleDepName = referenceUrlArray[referenceUrlArray.length - 1];
											moduleDep.setDirName(moduleDepName);
											moduleDep.setUrl(referenceUrl);
											dependenciesList.add(moduleDep);
										}
									}
								}
								if (dependenciesList != null) {
									moduleItem
											.setDependenciesList(dependenciesList);
								}
								break;
							}
						}
						moduleItem.setCompileSDKVer(21);
						moduleItem.setBuildToolsVer("21.1.2");
						moduleItem.setMinSDKVer(14);
						moduleItem.setTargetSDKVer(21);
						moduleList.add(moduleItem);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void verifyMainModule() {
		if (moduleList.size() == 0) {
			BuildManager.updateLog("Warning: There are no android component");
		} else if (moduleList.size() == 1) {
			moduleList.get(0).setMainModule(true);
		} else if (moduleList.size() > 1) {
			int projectMainCount = 0;
			for (Module moduleItem : moduleList) {
				if (!moduleItem.isProjectLibrary()) {
					moduleItem.setMainModule(true);
					projectMainCount++;
				}
			}
			if (projectMainCount == 0) {
				BuildManager
						.updateLog("Warning: There are no android application");
			} else if (projectMainCount > 1) {
				BuildManager
						.updateLog("Warning: There are more than one android application");
			}
		}
	}

	private static void verifyDependencies() {
		for (Module moduleItem : moduleList) {
			ArrayList<BaseDependency> dependenciesList = moduleItem
					.getDependenciesList();
			if (dependenciesList != null) {
				for (int i = 0; i < dependenciesList.size(); i++) {
					if (dependenciesList.get(i) instanceof Module) {
						Module moduleItem1 = (Module) dependenciesList.get(i);
						for (Module moduleItem2 : moduleList) {
							if (moduleItem1.getDirName().equals(
									moduleItem2.getDirName())) {
								dependenciesList.remove(i);
								dependenciesList.add(i, moduleItem2);
								break;
							}
						}
					}
				}
			}
		}
	}
}
