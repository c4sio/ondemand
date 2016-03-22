package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;

@Local
public interface AssetPackageFacade {

	public abstract List<IpvodAssetPackage> findComplexQuery(UriInfo uriInfo);
	
	public abstract void insertAssetPackages(IpvodVisualMenu ipvodMenu);

	public abstract void cleanAssetPackage();

	void insertAssetPackages(IpvodVisualMenu ipvodMenu, IpvodAssetPackage ipvodAssetPackage);

}
