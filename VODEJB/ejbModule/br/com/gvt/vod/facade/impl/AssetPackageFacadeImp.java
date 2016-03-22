package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.dao.AssetPackageDAO;
import br.com.gvt.eng.vod.dao.MenuDAO;
import br.com.gvt.eng.vod.dao.PackageDAO;
import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.vod.facade.AssetPackageFacade;

@Stateless
public class AssetPackageFacadeImp implements AssetPackageFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private AssetPackageDAO assetPackageDAO;
	
	@EJB
	private PackageDAO packageDAO;
	
	@EJB
	private MenuDAO menuDAO; 

	@Override
	public List<IpvodAssetPackage> findComplexQuery(UriInfo uriInfo) {
		return assetPackageDAO.findResultComplexQuery(uriInfo);
	}

	@Override
	public void cleanAssetPackage() {
		menuDAO.cleanAssetPackage();
	}
	
	@Override
	public void insertAssetPackages(IpvodVisualMenu ipvodMenu) {
		//REMOVE TODOS OS NÃO VÁLIDOS
		menuDAO.cleanAssetPackage();
		 
		List<IpvodAssetPackage> assetPackageList =  new ArrayList<IpvodAssetPackage>();
		for (IpvodPackage ipvodPackage : ipvodMenu.getIpvodPackages()) {
			Map<Long, Long> map = new HashMap<Long, Long>();
			for (IpvodAssetPackage pckage : assetPackageDAO.listByPackage(ipvodPackage.getPackageId())) {
				map.put(pckage.getIpvodAsset().getAssetId(), pckage.getIpvodAsset().getAssetId());
			}
			for (IpvodVisualMenuAsset ipvodMenuAsset : ipvodMenu.getIpvodVisualMenuAsset()) {
				if (!map.containsKey(ipvodMenuAsset.getIpvodAsset().getAssetId())) {
					IpvodAssetPackage assetPackage = new IpvodAssetPackage();
					assetPackage.setIpvodAsset(ipvodMenuAsset.getIpvodAsset());
					assetPackage.setIpvodPackage(ipvodPackage);
					assetPackageList.add(assetPackage);
				}
			}
		}
		//INSERE NOVOS
		assetPackageDAO.insertAssetPackage(assetPackageList); 
		
	}
	
	@Override
	public void insertAssetPackages(IpvodVisualMenu ipvodMenu, IpvodAssetPackage ipvodAssetPackage) {
		//REMOVE TODOS OS NÃO VÁLIDOS
		menuDAO.cleanAssetPackage();
		 
		List<IpvodAssetPackage> assetPackageList =  new ArrayList<IpvodAssetPackage>();
		for (IpvodPackage ipvodPackage : ipvodMenu.getIpvodPackages()) {
			Map<Long, Long> map = new HashMap<Long, Long>();
			for (IpvodAssetPackage pckage : assetPackageDAO.listByPackage(ipvodPackage.getPackageId())) {
				map.put(pckage.getIpvodAsset().getAssetId(), pckage.getIpvodAsset().getAssetId());
			}
			for (IpvodVisualMenuAsset ipvodMenuAsset : ipvodMenu.getIpvodVisualMenuAsset()) {
				if (!map.containsKey(ipvodMenuAsset.getIpvodAsset().getAssetId())) {
					IpvodAssetPackage assetPackage = new IpvodAssetPackage();
					assetPackage.setIpvodAsset(ipvodMenuAsset.getIpvodAsset());
					assetPackage.setIpvodPackage(ipvodPackage);
					assetPackageList.add(assetPackage);
				}
			}
		}
		assetPackageList.add(ipvodAssetPackage);
		
		//INSERE NOVOS
		assetPackageDAO.insertAssetPackage(assetPackageList); 
		
	}

}
