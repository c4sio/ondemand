package br.com.gvt.eng.convoy.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.properties.PropertiesConfig;

public class Util {

	static Logger logger = Logger.getLogger(Util.class.getName());

	/**
	 * @return URI Convoy
	 * @throws IOException
	 */
	public static String getURIConvoy() {
		// Busca arquivo de properties
		String inFilesPath = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			inFilesPath = prop.getProperty("uri.convoy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inFilesPath;
	}

	/**
	 * @return URI IPVOD
	 * @throws IOException
	 */
	public static String getApiKeyConvoy() {
		// Busca arquivo de properties
		String apiKeyConvoy = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			apiKeyConvoy = prop.getProperty("key.convoy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apiKeyConvoy;
	}

	/**
	 * @return IP Convoy
	 * @throws IOException
	 */
	public static String getIPConvoy() {
		// Busca arquivo de properties
		String ipConvoy = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			ipConvoy = prop.getProperty("ip.uri.convoy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ipConvoy;
	}

	/**
	 * @return Mime Type files
	 * @throws IOException
	 */
	public static String getMimeTypeConvoy() {
		// Busca arquivo de properties
		String mimeTypeConvoy = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			mimeTypeConvoy = prop.getProperty("mime.type.convoy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mimeTypeConvoy;
	}

	/**
	 * @return URI IPVOD
	 * @throws IOException
	 */
	public static String getURIIpvod() {
		// Busca arquivo de properties
		String uriIpvod = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			uriIpvod = prop.getProperty("uri.ipvod");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uriIpvod;
	}

	/**
	 * @return In Files Path
	 * @throws IOException
	 */
	public static String getInFilesPath() {
		// Busca arquivo de properties
		String inFilesPath = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			inFilesPath = prop.getProperty("in.files.convoy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inFilesPath;
	}

	/**
	 * @return FTP Host
	 * @throws IOException
	 */
	public static String getFtpHost() {
		// Busca arquivo de properties
		String ftpHost = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			ftpHost = prop.getProperty("ftp.host");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpHost;
	}

	/**
	 * @return FTP User
	 * @throws IOException
	 */
	public static String getFtpUser() {
		// Busca arquivo de properties
		String ftpUser = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			ftpUser = prop.getProperty("ftp.user");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpUser;
	}

	/**
	 * @return FTP User
	 * @throws IOException
	 */
	public static String getFtpPassWord() {
		// Busca arquivo de properties
		String ftpPassWord = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			ftpPassWord = prop.getProperty("ftp.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpPassWord;
	}

	/**
	 * @return FTP User
	 * @throws IOException
	 */
	public static String getFtpWorkDirectory() {
		// Busca arquivo de properties
		String ftpWorkDirectory = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			ftpWorkDirectory = prop.getProperty("ftp.working.directory");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpWorkDirectory;
	}

	/**
	 * Retorna a data atual
	 * 
	 * @return Date
	 * @throws ParseException
	 */
	public static Date getCurrentDate() throws ParseException {
		// Setando a data atual
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = df.parse(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(new Date()));
		return date;
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

}
