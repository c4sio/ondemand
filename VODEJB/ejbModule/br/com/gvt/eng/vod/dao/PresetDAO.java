package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodPreset;

@Stateless
public class PresetDAO extends GenericDAO<IpvodPreset> {

	public PresetDAO() {
		super(IpvodPreset.class);
	}

	/**
	 * Busca o Preset para o encoder no 4Balancer
	 * 
	 * @param som
	 * @param language
	 * @param video
	 * @param subtitle
	 * @param dubbedLanguage
	 * @return IpvodPreset
	 */
	public IpvodPreset findPresetByParameters(String som, String language,
			String video, String subtitle, String dubbedLanguage) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("som", som);
		parameters.put("language", language);
		parameters.put("video", video);
		parameters.put("subtitle", subtitle);
		parameters.put("dubbedLanguage", dubbedLanguage);
		return super.findOneResult(IpvodPreset.SEARCH_PRESET_BY_PARAMETERS,
				parameters);
	}

}
