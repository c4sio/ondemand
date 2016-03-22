package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodPreset;

@Local
public interface PresetFacade {

	/**
	 * Busca o Preset com base nos parametros de Asset
	 * 
	 * @param ipvodAsset
	 * @return IpvodPreset
	 */
	public abstract IpvodPreset findPresetByParameters(IpvodAsset ipvodAsset);

}
