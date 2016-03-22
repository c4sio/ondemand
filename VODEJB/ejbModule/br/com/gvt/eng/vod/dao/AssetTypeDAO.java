package br.com.gvt.eng.vod.dao;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAssetType;

@Stateless
public class AssetTypeDAO extends GenericDAO<IpvodAssetType> {

	public AssetTypeDAO() {
		super(IpvodAssetType.class);
	}

}
