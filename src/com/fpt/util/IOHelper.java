package com.fpt.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fpt.model.BaseDependency;
import com.fpt.model.Module;

public class IOHelper {

	public static void copyProjectToUrl(String sourceUrl, String desUrl) {
		File source = new File(sourceUrl);
		File dest = new File(desUrl);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readEnVar(String enVarKey) {
		String value = System.getenv(enVarKey);
		return value;
	}

	public static void writeToTextFile(String textToWrite, String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(textToWrite);
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeToBinFile(String textToWrite, String fileName) {
		try {
			byte dataToWrite[] = textToWrite.getBytes();
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(dataToWrite);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String readFromBinFile(String fileName) {
		String result = "";
		try {
			byte[] buffer = new byte[1000];

			FileInputStream inputStream = new FileInputStream(fileName);
			int nRead = 0;
			while ((nRead = inputStream.read(buffer)) != -1) {
				result += new String(buffer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
