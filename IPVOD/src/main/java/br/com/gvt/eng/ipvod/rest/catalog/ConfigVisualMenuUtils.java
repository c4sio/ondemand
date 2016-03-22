package br.com.gvt.eng.ipvod.rest.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.vod.facade.MenuAssetFacade;

public class ConfigVisualMenuUtils {
	
	
	private static int MENU_NAME = 0;
	private static int MENU_ID = 1;
	private static int MENU_SUP_ID = 2;
	private static int ASSET_ID = 3;
	private static int ASSET_ZINDEX = 4;
	private static int MENU_ZINDEX = 5;
	private static int ASSET_TITLE = 6;
	private static int ACTIVE = 7;
	private static int RATING_LEVEL = 8;
	
	private static int HD_CONTENT = 9;
	private static int AUDIO_TYPE = 10;
	private static int LANGUAGES = 11;
	private static int SUBTITLES = 12;
	private static int PRICE = 13;
	private static int BILLING_ID = 14;
	
	private static int AVALIABLE_SINCE = 15;
	private static int AVALIABLE_UNITL = 16;
	
	private static int PACKAGE_ID = 17;

	public static List<IpvodVisualMenu> filterMenus(List<Object[]> mainmenu) {
		
		List<IpvodVisualMenu> allMenus = new ArrayList<IpvodVisualMenu>();
		ConfigVisualMenuUtils.menus = new HashMap<Integer, IpvodVisualMenu>();
		
		for(Object[] y : mainmenu) {
			
			if( y[MENU_SUP_ID] == null && !hasRootMenu((BigDecimal)y[MENU_ID], allMenus)) {
				IpvodVisualMenu menu = getMenuByObject(MENU_ID, y);
				allMenus.add(menu);
			}
		}
		
		for(Object[] x : mainmenu) {
			
			IpvodVisualMenu menu = getMenuByObject(MENU_ID, x);
			
			if( x[MENU_SUP_ID] == null ) {
				
				/*allMenus.add(menu);
				this.menus.put(x[MENU_ID], menu);*/
				
			} else {
				//System.out.println("ELSE PARA: " + x[MENU_NAME]);
				IpvodVisualMenu supMenu = getMenuByObject(MENU_SUP_ID, x);
				boolean hasInSupMenu = hasInSupMenu(menu, supMenu);
				//System.out.println("hasInSupMenu: " + hasInSupMenu);
				if(!hasInSupMenu) {
					
					List<IpvodVisualMenu> menus;
					
					if(supMenu.getIpvodVisualMenus() == null) {
						menus = new ArrayList<IpvodVisualMenu>();	
					} else {
						menus = supMenu.getIpvodVisualMenus();
					}
					menus.add(menu);
					supMenu.setIpvodVisualMenus(menus);
					ConfigVisualMenuUtils.menus.put(x[MENU_SUP_ID], supMenu);
				}
				
			}
			
			menu = getMenuByObject(MENU_ID, x);
			
			if(x[ASSET_ID] != null) {
				IpvodAsset asset = new IpvodAsset();
				asset.setAssetId(((BigDecimal)x[ASSET_ID]).intValue());
				asset.setTitle((String)x[ASSET_TITLE]);
				asset.setHD(((BigDecimal)x[ASSET_ID]).intValue() == 1);
				asset.setAudioType((String)x[AUDIO_TYPE]);
				asset.setLanguages((String)x[LANGUAGES]);
				asset.setSubtitles((String)x[SUBTITLES]);
				asset.setPrice(((BigDecimal)x[ASSET_ID]).doubleValue());
				asset.setBillingID((String)x[BILLING_ID]);
				
				List<IpvodAsset> assets = menu.getIpvodAssets();
				if(assets == null) {
					assets = new ArrayList<IpvodAsset>();
				}
				assets.add(asset);
				menu.setIpvodAssets(assets);
				menus.put(x[MENU_ID], menu);
			}
//			if(x[PACKAGE_ID] != null) {
//				IpvodPackage ipvodPackage = new IpvodPackage();
//				ipvodPackage.setPackageId(((BigDecimal)x[PACKAGE_ID]).intValue());
//				
//				List<IpvodPackage> packages = menu.getIpvodPackages();
//				if(packages == null) {
//					packages = new ArrayList<IpvodPackage>();
//				}
//				packages.add(ipvodPackage);
//				menu.setIpvodPackages(packages);
//				menus.put(x[MENU_ID], menu);
//			}
		}
		
		return allMenus;
	}
	
	public static boolean hasInSupMenu(IpvodVisualMenu menu, IpvodVisualMenu supMenu) {
		
		if(supMenu.getIpvodVisualMenus() == null) {
			return false;
		}
		
		for(IpvodVisualMenu subMenu : supMenu.getIpvodVisualMenus()) {
			if(subMenu.getMenuId() == menu.getMenuId()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public static boolean hasRootMenu(BigDecimal id, List<IpvodVisualMenu> allMenus) {
		
		for(IpvodVisualMenu menu : allMenus) {
			if(menu.getMenuId() == id.longValue()) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public static HashMap menus = new HashMap<Integer, IpvodVisualMenu>();
	
	public static IpvodVisualMenu getMenuByObject(int id_, Object [] x) {
		
		BigDecimal id = (BigDecimal)x[id_];
		
		
		return getMenuById(id, x);
	}
	
	public static IpvodVisualMenu getMenuById(BigDecimal id, Object [] x) {
		
		String menuName = x[MENU_NAME].toString();
		BigDecimal menuIndex = (BigDecimal) x[MENU_ZINDEX];
		Integer active = ((BigDecimal) x[ACTIVE]).intValue();
		Date avaliableSince = (Date) x[AVALIABLE_SINCE];
		Date avaliableUntil = (Date) x[AVALIABLE_UNITL];
		
		Long rating = null;
		if(x[RATING_LEVEL] != null) {
			rating = ((BigDecimal) x[RATING_LEVEL]).longValue();
		}
		Iterator it = menus.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        if(pair.getKey().toString().equals(id.toString())) {
	        	return (IpvodVisualMenu) pair.getValue();
	        }
	        //it.remove();
	    }
	    IpvodVisualMenu menu = new IpvodVisualMenu();
	    menu.setMenuId(id.intValue());
	    menu.setName(menuName);
	    menu.setZindex(menuIndex.longValue());
	    menu.setActive(active);
	    menu.setAvaliableSince(avaliableSince);
	    menu.setAvaliableUntil(avaliableUntil);
	    
	    if(rating != null) {
	    	IpvodRating ipvodRating = new IpvodRating();
	    	ipvodRating.setRatingLevel(rating);
	    	ipvodRating.setAdult(false);
	    	menu.setIpvodRating(ipvodRating);
	    }
	   
	    menus.put(id, menu);
		return menu;
	}
	
	
	public static void sortAssetsByMenu(IpvodVisualMenu ipvodVisualMenu, MenuAssetFacade menuAssetFacade) {
		
		List<IpvodVisualMenu> ipvodVisualMenus = ipvodVisualMenu.getIpvodVisualMenus();
		if(ipvodVisualMenus != null && ipvodVisualMenus.size() > 0) {
			for(IpvodVisualMenu _ipvodVisualMenu : ipvodVisualMenus) {
				sortAssetsByMenu(_ipvodVisualMenu, menuAssetFacade);
			}
		}
		
		List<IpvodAsset> assets = new ArrayList<IpvodAsset>();
		List<IpvodVisualMenuAsset> ipvodVisualMenuAssets = menuAssetFacade.findByMenuId(ipvodVisualMenu.getMenuId());
		
		if(ipvodVisualMenuAssets != null) {
			Collections.sort(ipvodVisualMenuAssets, new Comparator<IpvodVisualMenuAsset>() {
		        @Override public int compare(IpvodVisualMenuAsset p1, IpvodVisualMenuAsset p2) {
		        	if(p1.getZindex() != null && p2.getZindex() != null) {
		        		return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
		        	}
		        	return 0;
		        }
	
		    });
			
			for(IpvodVisualMenuAsset ipvodVisualMenuAsset : ipvodVisualMenuAssets) {
				assets.add(ipvodVisualMenuAsset.getIpvodAsset());
			}
		}
		
		ipvodVisualMenu.setIpvodAssets(assets);
		
	}

	public static void sortMenusAsset(IpvodVisualMenu ipvodVisualMenu, MenuAssetFacade menuAssetFacade) {
		
		List<IpvodAsset> assets = new ArrayList<IpvodAsset>();
		List<IpvodVisualMenuAsset> ipvodVisualMenuAssets = menuAssetFacade.findByMenuId(ipvodVisualMenu.getMenuId());
		
		if(ipvodVisualMenuAssets != null) {
			Collections.sort(ipvodVisualMenuAssets, new Comparator<IpvodVisualMenuAsset>() {
		        @Override public int compare(IpvodVisualMenuAsset p1, IpvodVisualMenuAsset p2) {
		        	if(p1.getZindex() != null && p2.getZindex() != null) {
		        		return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
		        	}
		        	return 0;
		        }
	
		    });
			
			Map<Long, IpvodAsset> map = new HashMap<Long, IpvodAsset>();
			for (IpvodAsset asset : ipvodVisualMenu.getIpvodAssets()) {
				map.put(asset.getAssetId(), asset);
			}
			for(IpvodVisualMenuAsset ipvodVisualMenuAsset : ipvodVisualMenuAssets) {
				if (map.containsKey(ipvodVisualMenuAsset.getIpvodAsset().getAssetId())) {
					assets.add(map.get(ipvodVisualMenuAsset.getIpvodAsset().getAssetId()));
				}
			}
		}
		
		ipvodVisualMenu.setIpvodAssets(assets);
		
	}
}
