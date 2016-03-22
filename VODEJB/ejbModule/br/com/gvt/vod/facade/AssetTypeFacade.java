package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAssetType;

@Local
public interface AssetTypeFacade {

	public abstract List<IpvodAssetType> findAll();

}
