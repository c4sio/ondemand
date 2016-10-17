package br.com.gvt.eng.integ.dao.impl;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.PresetDAO;
import br.com.gvt.eng.integ.util.Util;
import br.com.gvt.eng.vod.model.IpvodPreset;

@Repository
@Transactional
public class PresetDAOImpl implements PresetDAO {

	static Logger logger = Logger.getLogger(PresetDAOImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public String findPresetByParameters(String som, String language,
			String video, String subtitle, String dubbedLanguage) {
		String presetName = null;
		try {
			logger.info("Busca Preset com dados de asset na base.");

			if (Util.isEmptyOrNull(language)) {
				return null;
			}

			// Altera o tipo de language para tudo que for diferente de
			// Portugues (POR)
			if (!language.toUpperCase().equalsIgnoreCase("POR")) {
				language = "OUTRAS";
			}

			IpvodPreset ipvodPreset = new IpvodPreset();
			Session session = sessionFactory.getCurrentSession();
			ipvodPreset = (IpvodPreset) session
					.createQuery(
							"FROM IpvodPreset pres WHERE pres.typeVideo =:video and pres.typeAudio =:som and pres.languages =:language and pres.subtitles =:subtitle and pres.dubbedLanguage =:dubbedLanguage ")
					.setParameter("som", som)
					.setParameter("language", language)
					.setParameter("video", video)
					.setParameter("subtitle", subtitle)
					.setParameter("dubbedLanguage", dubbedLanguage)
					.uniqueResult();
			session.flush();
			session.clear();

			if (ipvodPreset != null) {
				// Seta o preset name na variavel
				presetName = ipvodPreset.getPresetName();
			}

			// Se o valor for nulo seta o valor default
			if (Util.isEmptyOrNull(presetName)) {
				logger.info("Nao encontrou dados de Preset");
			}

			logger.info("Retornou o Preset: " + presetName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error busca Preset: ", e);
		}
		return presetName;
	}

}
