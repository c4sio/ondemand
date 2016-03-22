package br.com.gvt.eng.ipvod.rest.util;

import br.com.gvt.eng.vod.util.PropertiesConfig;

public class Util {

	public static String generatePasswordRecoverCode() {
		String pwRecCd = String
				.valueOf(Math.round(Math.random() * 1000000000l));
		return pwRecCd;
	}

	/**
	 * Verifica se o campo e nulo ou vazio
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isEmptyOrNull(String s) {
		return s == null || s.equals("");
	}

	/**
	 * @return getServicePaytv
	 */
	public static String getServicePaytv() {
		// Busca arquivo de properties
		return PropertiesConfig.getString("paytv.service");
	}

}
