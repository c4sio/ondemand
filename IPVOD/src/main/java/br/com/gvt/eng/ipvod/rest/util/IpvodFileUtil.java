package br.com.gvt.eng.ipvod.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.gvt.eng.vod.util.PropertiesConfig;

public class IpvodFileUtil {

	private static List<String> error = new ArrayList<String>();

	/**
	 * Lista de erros na leitura do XML de entrada
	 * 
	 * @return List
	 */
	public static List<String> getError() {
		return error;
	}

	public static void setError(List<String> error) {
		IpvodFileUtil.error = error;
	}

	/**
	 * @param strHor
	 * @return Hours in minutes
	 * @throws ParseException
	 */
	public static long changeHourToSecond(String strHor) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(strHor));
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		long value = (hours * 3600) + minute * 60 + second;
		return value;
	}

	/**
	 * @param strData
	 * @return date
	 * @throws Exception
	 */
	public static Date stringToData(String strData) throws Exception {
		if (strData == null || strData.equals(""))
			return null;

		Date data = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			data = formatter.parse(strData);
			System.out.println(data);
			System.out.println(formatter.format(data));
		} catch (ParseException e) {
			throw e;
		}
		return data;
	}

	/**
	 * Verifica se o campo e nulo ou vazio
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isEmptyOrNull(String s) {
		return s == null || s.equals("") || s.equals("null");
	}

	/**
	 * @return pathImportImg
	 */
	public static String getPathImportImg() {
		// Busca arquivo de properties
		return PropertiesConfig.getString("out.import.img");
	}

	/**
	 * @return pathClusterImg
	 */
	public static String getPathClusterImg() {
		// Busca arquivo de properties
		return PropertiesConfig.getString("out.cluster.img");
	}

	/**
	 * @return scpPort
	 */
	public static int getScpPort() {
		// Busca arquivo de properties
		return PropertiesConfig.getInt("scp.port");
	}

	/**
	 * @return scpUser
	 */
	public static String getScpUser() {
		// Busca arquivo de properties
		return PropertiesConfig.getString("scp.user");
	}

	/**
	 * @return scpPassword
	 */
	public static String getScpPassword() {
		// Busca arquivo de properties
		return PropertiesConfig.getString("scp.password");
	}

	/**
	 * @return listHost
	 */
	public static List<String> getScpHost() {
		// Busca arquivo de properties
		List<String> listHost = new ArrayList<String>();
		String[] tokens = null;
		tokens = PropertiesConfig.getString("scp.host").split(";");
		for (String token : tokens) {
			listHost.add(token);
		}
		return listHost;
	}

}
