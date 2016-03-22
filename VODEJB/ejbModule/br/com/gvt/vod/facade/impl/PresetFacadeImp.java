package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.PresetDAO;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodPreset;
import br.com.gvt.vod.facade.PresetFacade;

@Stateless
public class PresetFacadeImp implements PresetFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private PresetDAO presetDAO;

	@Override
	public IpvodPreset findPresetByParameters(IpvodAsset ipvodAsset) {
		IpvodPreset ipvodPreset = null;
		try {
			String som = ipvodAsset.getAudioType();
			String language = ipvodAsset.getLanguages();

			if (language == null || language.equals("")) {
				return null;
			}

			// Altera o tipo de language para tudo que for diferente de
			// Portugues (POR)
			if (!language.toUpperCase().equalsIgnoreCase("POR")) {
				language = "OUTRAS";
			}

			String video = ipvodAsset.isHD() == true ? "Y" : "N";
			String subtitle = ipvodAsset.getSubtitles() == null ? "null"
					: ipvodAsset.getSubtitles();
			String dubbedLanguage = ipvodAsset.getDubbedLanguage() == null ? "null"
					: ipvodAsset.getDubbedLanguage();

			ipvodPreset = new IpvodPreset();
			ipvodPreset = presetDAO.findPresetByParameters(som, language,
					video, subtitle, dubbedLanguage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ipvodPreset;
	}
}
