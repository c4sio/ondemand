package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.gvt.eng.vod.dao.AssetDAO;
import br.com.gvt.eng.vod.dao.MenuAssetDAO;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.vod.facade.MenuAssetFacade;

@Stateless
public class MenuAssetFacadeImp implements MenuAssetFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private MenuAssetDAO menuAssetDAO;

	@EJB
	private AssetDAO assetDAO;

	@Override
	public void save(IpvodVisualMenuAsset ipvodMenu) {
		menuAssetDAO.save(ipvodMenu);
	}

	@Override
	public void delete(IpvodVisualMenuAsset ipvodMenu) {
		menuAssetDAO.deleteAssetMenu(ipvodMenu);
	}

	@Override
	public List<IpvodVisualMenuAsset> findByMenuId(long menuId) {
		return menuAssetDAO.findByMenuId(menuId);
	}
	
	@Override
	public List lab() {
		return menuAssetDAO.lab();
	}
	
	@Override
	public void clean() {
		menuAssetDAO.clean();
	}
	
	@Override
	public void clean(long menuId) {
		menuAssetDAO.clean(menuId);
	}
	
	@Override
	public List<IpvodVisualMenuAsset> findAll() {
		return menuAssetDAO.findAll();
	}

	@Override
	public void delete(List<IpvodVisualMenuAsset> menuAssetList) {
		menuAssetDAO.delete(menuAssetList);
	}


}