package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.MenuConverter;
import br.com.gvt.eng.vod.dao.AssetDAO;
import br.com.gvt.eng.vod.dao.MenuDAO;
import br.com.gvt.eng.vod.dao.PurchaseDAO;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.util.PropertiesConfig;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.MenuFacade;

@Stateless
public class MenuFacadeImp implements MenuFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private MenuDAO menuDAO;

	@EJB
	private PurchaseDAO purchaseDAO;
	
	@EJB
	private AssetDAO assetDAO;
	
	@EJB
	private AssetFacade assetFacade;

	@Override
	public void save(IpvodVisualMenu ipvodMenu) {
		isMenuWithAllData(ipvodMenu);
		menuDAO.save(ipvodMenu);
	}

	@Override
	public void delete(IpvodVisualMenu ipvodMenu) {
		menuDAO.deleteMenu(ipvodMenu);
	}

	@Override
	public IpvodVisualMenu update(IpvodVisualMenu ipvodMenu) {
		isMenuWithAllData(ipvodMenu);
		return menuDAO.update(ipvodMenu);
	}

	public void update(List<IpvodVisualMenu> ipvodVisualMenuList) {

		for (IpvodVisualMenu ipvodMenu : ipvodVisualMenuList) {
			update(ipvodMenu);

		}
		return;

	}

	@Override
	public List<IpvodVisualMenu> findAll() {
		List<IpvodVisualMenu> listVisualMenu = new ArrayList<IpvodVisualMenu>();
		listVisualMenu = menuDAO.findAll();
		// Evitando o LazyException
		if (!listVisualMenu.isEmpty()) {
			for (IpvodVisualMenu ipvodVisualMenu : listVisualMenu) {
				ipvodVisualMenu.getIpvodVisualMenus().size();
				ipvodVisualMenu.getIpvodAssets().size();
			}
		}
		return listVisualMenu;
	}

	@Override
	public IpvodVisualMenu find(long entityID) {
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		ipvodVisualMenu = menuDAO.find(entityID);
		// Evitando o LazyException
		if (ipvodVisualMenu != null) {
			ipvodVisualMenu.getIpvodVisualMenuAsset().size();
			ipvodVisualMenu.getIpvodVisualMenus().size();
		}
		return ipvodVisualMenu;
	}

	@Override
	public IpvodVisualMenu findMenuAssetPagination(long menuId, Long userId, Map<String, Object> pagination) {
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		ipvodVisualMenu = menuDAO.find(menuId);
		// Evitando o LazyException
		if (ipvodVisualMenu != null) {
			List<IpvodAsset> assets =  null;
			
			assets = assetDAO.listAssetByMenu(menuId, pagination);

			if (assets != null) {
//				assetFacade.fillPurchases(assets, userId);
			}
			MenuConverter converter = new MenuConverter();
			ipvodVisualMenu = converter.toMenu(ipvodVisualMenu);
			ipvodVisualMenu.setIpvodAssets(assets);
		}
		return ipvodVisualMenu;
	}

	@Override
	public IpvodVisualMenu findMenuSTB(long menuId, Long userId) {
		return menuDAO.findMenuSTB(menuId, userId);
	}

	@Override
	public IpvodVisualMenu findMenuSTB(long menuId) {
		return menuDAO.findMenuSTB(menuId);
	}
	
	@Override
	public IpvodVisualMenu find(long menuId, Long userId) {
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		ipvodVisualMenu = menuDAO.find(menuId);
		// Evitando o LazyException
		if (ipvodVisualMenu != null) {
			List<IpvodAsset> assets =  ipvodVisualMenu.getIpvodAssets();
			
			for (IpvodAsset asset : assets) {
				if (userId != null) {
					asset.setIpvodPurchases(purchaseDAO.findPurchase(asset.getAssetId(), userId));
				} else {
					asset.getIpvodPurchases().size();
				}
			}
			MenuConverter converter = new MenuConverter();
			ipvodVisualMenu = converter.toMenu(ipvodVisualMenu);
			ipvodVisualMenu.setIpvodAssets(assets);
		}
		return ipvodVisualMenu;
	}
	
	@Override
	public List<IpvodVisualMenu> findMenuHierarchy() {
		return menuDAO.findMenuHierarchy();
	}
	
	@Override
	public List<IpvodVisualMenu> findMenuActive() {
		List<IpvodVisualMenu> listVisualMenu = new ArrayList<IpvodVisualMenu>();
		listVisualMenu = menuDAO.findMenuActive();
		// Evitando o LazyException
		if (!listVisualMenu.isEmpty()) {
			for (IpvodVisualMenu ipvodVisualMenu : listVisualMenu) {
				ipvodVisualMenu.getIpvodVisualMenus().size();
			}
		}
		return listVisualMenu;
	}

	@Override
	public void toEnableMenu(IpvodVisualMenu ipvodMenu) {
		ipvodMenu.setActive(1);
		menuDAO.update(ipvodMenu);

		if (ipvodMenu.getIpvodVisualMenus() != null
				&& !ipvodMenu.getIpvodVisualMenus().isEmpty()) {
			toEnableToDisableSubMenus(ipvodMenu.getIpvodVisualMenus(), 1);
		}
	}

	@Override
	public void toDisableMenu(IpvodVisualMenu ipvodMenu) {
		menuDAO.disableEnableMenus(ipvodMenu, 0);
	}

	/**
	 * Busca e executa update nos submenus
	 * 
	 * @param ipvodVisualMenus
	 * @param i
	 *            - 1 Enable / 0 Disable
	 */
	private void toEnableToDisableSubMenus(
			List<IpvodVisualMenu> ipvodVisualMenus, int i) {
		for (IpvodVisualMenu menu : ipvodVisualMenus) {
			menu.setActive(i);
			menuDAO.update(menu);
			if (menu.getIpvodVisualMenus() != null && !menu.getIpvodVisualMenus().isEmpty()) {
				toEnableToDisableSubMenus(menu.getIpvodVisualMenus(), i);
			}
		}
	}

	/**
	 * Valida��o de campos - Save/Update
	 * 
	 * @param IpvodVisualMenu
	 */
	private void isMenuWithAllData(IpvodVisualMenu ipvodMenu) {
		boolean hasError = false;
		if (ipvodMenu == null)
			hasError = true;

		if (ipvodMenu.getName() == null
				|| "".equals(ipvodMenu.getName().trim()))
			hasError = true;

		if (hasError)
			throw new IllegalArgumentException(
					"The menu is missing data. Check, they should have value.");
	}

	@Override
	public List<IpvodVisualMenu> findByAssetId(long assetId) {
		return menuDAO.findByAssetId(assetId);
	}

	@Override
	public IpvodVisualMenu getOnDemand(Long userId) {
		return menuDAO.getMenuHierarchy(PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND), userId);
	}

	@Override
	public IpvodVisualMenu getCatchup(Long userId) {
		return menuDAO.getMenuHierarchy(PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP), userId);
	}

	@Override
	public List<IpvodVisualMenu> findMenuCategorize() {
		return menuDAO.findMenuCategorize();
	}

}