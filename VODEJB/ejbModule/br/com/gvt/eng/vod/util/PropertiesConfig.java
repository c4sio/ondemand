package br.com.gvt.eng.vod.util;

import java.util.ResourceBundle;

public class PropertiesConfig {

	public static String getString(String str) {
		return ResourceBundle.getBundle("dados").getString(str).trim();
	}
	
	public static Integer getInt(String str) {
		return Integer.parseInt(ResourceBundle.getBundle("dados").getString(str));
	}
	
	public static Long getLong(String str) {
		return Long.parseLong(ResourceBundle.getBundle("dados").getString(str));
	}
}
