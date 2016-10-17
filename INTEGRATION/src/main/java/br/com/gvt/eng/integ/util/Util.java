package br.com.gvt.eng.integ.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import br.com.gvt.eng.integ.properties.PropertiesConfig;

public class Util {

	/**
	 * @return Preset Name default
	 * @throws IOException
	 */
	public static String getPresetDefault() {
		// Busca arquivo de properties
		String presetName = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			presetName = prop.getProperty("preset.default");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return presetName;
	}

	/**
	 * @return Preset trailer default
	 * @throws IOException
	 */
	public static String getPresetTrailer() {
		// Busca arquivo de properties
		String presetTrailerName = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			presetTrailerName = prop.getProperty("preset.trailer");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return presetTrailerName;
	}

	/**
	 * @return Preset audio plus
	 * @throws IOException
	 */
	public static String getPresetAudioPlus() {
		// Busca arquivo de properties
		String presetAudioPlusName = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			presetAudioPlusName = prop.getProperty("preset.audio.plus");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return presetAudioPlusName;
	}

	/**
	 * @return Input Path for files
	 * @throws IOException
	 */
	public static String getInFilesPath() {
		// Busca arquivo de properties
		String inFilesPath = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			inFilesPath = prop.getProperty("url.in.files");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inFilesPath;
	}

	/**
	 * @return Output Path for files
	 * @throws IOException
	 */
	public static String getOutFilesPath() {
		// Busca arquivo de properties
		String outFilesPath = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			outFilesPath = prop.getProperty("url.out.files");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outFilesPath;
	}

	/**
	 * @return Path for Legend files
	 * @throws IOException
	 */
	public static String getLegendFilePath() {
		// Busca arquivo de properties
		String legendFilesPath = null;
		Properties prop = null;
		try {
			prop = PropertiesConfig.getProp();
			legendFilesPath = prop.getProperty("url.legend.files");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return legendFilesPath;
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
