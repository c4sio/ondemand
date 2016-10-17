package br.com.gvt.eng.integ.dao;

import javax.ejb.Local;

@Local
public interface PresetDAO {

	/**
	 * Busca o Preset com base nos parametros de Asset
	 * 
	 * @param som
	 * @param language
	 * @param video
	 * @param subtitle
	 * @param dubbedLanguage
	 * @return Preset
	 */
	public abstract String findPresetByParameters(String som, String language,
			String video, String subtitle, String dubbedLanguage);

}
