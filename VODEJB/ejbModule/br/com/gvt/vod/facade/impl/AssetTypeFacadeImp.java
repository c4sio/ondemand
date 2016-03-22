package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AssetTypeDAO;
import br.com.gvt.eng.vod.model.IpvodAssetType;
import br.com.gvt.vod.facade.AssetTypeFacade;

@Stateless
public class AssetTypeFacadeImp implements AssetTypeFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private AssetTypeDAO assetTypeDAO;


	@Override
	public List<IpvodAssetType> findAll() {
		return assetTypeDAO.findAll();
	}

}
