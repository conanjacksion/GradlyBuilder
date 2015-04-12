package com.fpt.util;

import java.io.File;

public class StringHelper {

	public static String getLastElementFromUrl(String url) {
		String result = "";
		if (url == null || url.equals("")) {
			return result;
		}
		String[] urlArray = url.trim().split("/");
		result = urlArray[urlArray.length - 1];
		return result;
	}

	public static String convertToBackFlash(String source) {
		String result = "";
		if (source != null) {
			result = source.replaceAll("\\\\", "/");
		}
		return result;
	}

	public static String convertRelativeUrlToAbsoluteUrl(String baseUrl,
			String relativeUrl) {
		baseUrl = convertToBackFlash(baseUrl);
		relativeUrl = convertToBackFlash(relativeUrl);
		String absoluteUrl = "";
		String[] baseUrlArray = baseUrl.split("/");
		String[] relativeUrlArray = relativeUrl.split("/");
		for (int i = 0; i < relativeUrlArray.length; i++) {
			if (relativeUrlArray[i].equals("..")) {
				baseUrlArray[baseUrlArray.length - 1 - i] = "";
			}
		}
		for (int i = 0; i < baseUrlArray.length; i++) {
			if (!baseUrlArray[i].equals("")) {
				absoluteUrl += baseUrlArray[i] + "/";
			}
		}
		for (int i = 0; i < relativeUrlArray.length; i++) {
			if (!relativeUrlArray[i].equals("..")) {
				absoluteUrl += relativeUrlArray[i];
				if (i < relativeUrlArray.length - 1) {
					absoluteUrl += "/";
				}
			}
		}
		return absoluteUrl;
	}

	public static String convertAbsoluteUrlToRelativeUrl(String baseUrl,
			String absoluteUrl) {
		baseUrl = convertToBackFlash(baseUrl);
		absoluteUrl = convertToBackFlash(absoluteUrl);
		String relative = "";
		if (absoluteUrl.contains(baseUrl)) {
			relative = new File(baseUrl).toURI()
					.relativize(new File(absoluteUrl).toURI()).getPath();
		} else {
			String[] baseUrlArray = baseUrl.split("/");
			String[] absoluteUrlArray = absoluteUrl.split("/");
			int indexDiff = 0;
			for (int i = 0; i < baseUrlArray.length; i++) {
				if (!baseUrlArray[i].equals(absoluteUrlArray[i])) {
					indexDiff = i;
					break;
				}
			}
			for (int i = indexDiff; i < baseUrlArray.length; i++) {
				relative += "../";
			}
			for (int i = indexDiff; i < absoluteUrlArray.length; i++) {
				relative += absoluteUrlArray[i];
				if (i < absoluteUrlArray.length - 1) {
					relative += "/";
				}
			}
		}
		return relative;
	}
}
