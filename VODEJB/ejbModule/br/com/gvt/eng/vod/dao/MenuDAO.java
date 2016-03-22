package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAssetType;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.model.IpvodMediaType;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.util.PropertiesConfig;

@Stateless
public class MenuDAO extends GenericDAO<IpvodVisualMenu> {

	public MenuDAO() {
		super(IpvodVisualMenu.class);
	}

	public void deleteMenu(IpvodVisualMenu ipvodVisualMenu) {
		super.delete(ipvodVisualMenu.getMenuId(), IpvodVisualMenu.class);
	}

	public List<IpvodVisualMenu> findMenuHierarchy() {
		return super.findResultByParameter(IpvodVisualMenu.FIND_MAIN_MENU,
				null);
	}

	public List<IpvodVisualMenu> findMenuActive() {
		return super.findResultByParameter(IpvodVisualMenu.FIND_MAIN_MENU_ACTIVE,
				null);
	}

	public List<IpvodVisualMenu> findByAssetId(long assetId) {
		Map<String, Object> param =  new HashMap<String, Object>();
		param.put("assetId", assetId);
		return super.findResultByParameter(IpvodVisualMenu.FIND_MENU_BY_ASSET,
				param );
	}
	
	public List<Long> getMenusOnDemand() {
		return getMenusByType(IpvodConstants.MENU_ID_ONDEMAND);
	}
	
	public List<Long> getMenusCatchUp() {
		return getMenusByType(IpvodConstants.MENU_ID_CATCHUP);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getMenusByType(String type) {
		List<BigDecimal> menus = super.getSession().createSQLQuery("SELECT UNIQUE m.MENU_ID " 
				+ "FROM IPVOD_VISUAL_MENU m " 
				+ "LEFT JOIN IPVOD_VISUAL_MENU m2 ON m.MENU_SUP_ID = m2.MENU_SUP_ID "
				+ "START WITH m.MENU_ID = "
				+ PropertiesConfig.getLong(type)
				+ " CONNECT BY PRIOR m.MENU_ID = m2.MENU_SUP_ID")
				.list();
				
		List<Long> menuIds = new ArrayList<Long>();
		for (BigDecimal menu : menus) {
			menuIds.add(new Long(menu.toString()));
		}
		return menuIds;
	}
	
	public List<IpvodVisualMenu> findMenusWithPackages() {
		return super.findResultByParameter(IpvodVisualMenu.FIND_MENU_WITH_PACKAGE,
				null);
	}
	
	public void cleanAssetPackage() {
		StringBuilder sql = new StringBuilder(
				"	delete from IPVOD_ASSET_PACKAGE where asset_package_id in (		                      " +
				"		select asset_package_id from IPVOD_ASSET_PACKAGE where asset_id not in (          " +
				"			select asset_id from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id in           " +
				"				(select menu_id from IPVOD_VISUAL_MENU where menu_id in                   " +
				"					(select menu_id from IPVOD_PACKAGE_MENU)                              " +
				"			)                                                                             " +
				"		)                                                                                 " +
				"	)                                                                                     "
			);
		super.getSession().createSQLQuery(sql.toString()).executeUpdate();
		return;
	}

	public IpvodVisualMenu findMenuSTB(long menuId, Long userId) {
		final int ASSET_ID = 0;
		final int TITLE = 1;
		final int DESCRIPTION = 2;
		final int PRICE = 3;
		final int RATING = 4;
		final int RATING_LEVEL = 5;
		final int LANGUAGES = 6;
		final int SUBTITLES = 7;
		final int AUDIO_TYPE = 8;
		final int HD_CONTENT = 9;
		final int PURCHASE_ID = 10;
		final int PURCHASE_DATE = 11;
		final int VALID_UNTIL = 12;
		final int MD1_URL = 13;
		final int MD2_URL = 14;
		final int MD3_URL = 15;
		final int COUNTRY = 16;
		final int TOTAL_TIME = 17;
		final int RELEASE_YEAR = 18;

		StringBuilder hql = new StringBuilder("select "
				+ "unique a.asset_id,  "
				+ "a.title, "
				+ "a.description,  "
				+ "least((select price from IPVOD_ASSET x where x.asset_id = a.asset_id),coalesce((select min(price) from IPVOD_ASSET_PACKAGE ap where ap.asset_id = a.asset_id and ap.package_id in (select package_id from IPVOD_PACKAGE_SUBSCRIPTION ps where ps.user_id = :userId)),9999)) as price, "
				+ "r.rating,  "
				+ "r.rating_level,  "
				+ "a.languages,  "
				+ "a.subtitles,  "
				+ "a.audio_type,  "
				+ "a.hd_content,  "
				+ "p.purchase_id,  "
				+ "p.PURCHASE_DATE, "
				+ "p.VALID_UNTIL, "
				+ "md1.URL as md1URL, "
				+ "md2.URL as md2URL, "
				+ "md3.URL as md3URL, "
				+ "a.country,  "
				+ "a.total_time,  "
				+ "a.release_year,  "
				+ "ma.zindex "
				+ "from IPVOD_ASSET a "
				+ "left join IPVOD_VISUAL_MENU_ASSET_INDEX ma on (ma.ASSET_ID = A.ASSET_ID) "
				+ "left join IPVOD_PURCHASE p on (p.asset_id = a.asset_id and p.equipment_id in (select equipment_id from IPVOD_EQUIPMENT where user_id = :userId) and p.valid_until > sysdate) "
				+ "left join IPVOD_MEDIA_ASSET md1 on (md1.asset_id = a.asset_id and md1.media_type_id = 1) "
				+ "left join IPVOD_MEDIA_ASSET md2 on (md2.asset_id = a.asset_id and md2.media_type_id = 2) "
				+ "left join IPVOD_MEDIA_ASSET md3 on (md3.asset_id = a.asset_id and md3.media_type_id = 3) "
				+ "left join IPVOD_RATING r on (r.rating_level = a.rating_level)"
				+ "where a.asset_id = ma.asset_id and a.license_window_end >= sysdate and a.license_window_start <= sysdate "
				+ "and ma.menu_id = :menuId order by ma.zindex ");

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("menuId", menuId);
		query.setParameter("userId", userId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> assets = (List<Object[]>)query.list();
		
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		ipvodVisualMenu.setIpvodAssets(new ArrayList<IpvodAsset>());
		for (Object[] asset : assets) {
			IpvodAsset ipvodAsset = new IpvodAsset();
			ipvodAsset.setAssetId(((BigDecimal) asset[ASSET_ID]).longValue());
			ipvodAsset.setTitle((String) asset[TITLE]);
			ipvodAsset.setDescription((String) asset[DESCRIPTION]);
			ipvodAsset.setPrice(((BigDecimal) asset[PRICE]).doubleValue());
			
			ipvodAsset.setRating(new IpvodRating());
			if (asset[RATING_LEVEL] != null) {
				ipvodAsset.getRating().setRatingLevel(((BigDecimal) asset[RATING_LEVEL]).longValue());
				ipvodAsset.getRating().setRating((String) asset[RATING]);
			}
			
			ipvodAsset.setLanguages((String) asset[LANGUAGES]);
			ipvodAsset.setSubtitles((String) asset[SUBTITLES]);
			ipvodAsset.setAudioType((String) asset[AUDIO_TYPE]);
			if (((BigDecimal) asset[HD_CONTENT]).longValue() == 1) {
				ipvodAsset.setHD(true);	
			} else {
				ipvodAsset.setHD(false);
			}
			if (asset[PURCHASE_ID] != null) {
				 IpvodPurchase ipvodPurchase = new IpvodPurchase();
				 ipvodPurchase.setPurchaseId(((BigDecimal) asset[PURCHASE_ID]).longValue());
				 ipvodPurchase.setPurchaseDate((Date) asset[PURCHASE_DATE]);
				 ipvodPurchase.setValidUntil((Date) asset[VALID_UNTIL]);
				 ipvodAsset.setIpvodPurchases(new ArrayList<IpvodPurchase>());
				 ipvodAsset.getIpvodPurchases().add(ipvodPurchase);
			}
			ArrayList<IpvodMediaAsset> ipvodMediaAssets = new ArrayList<IpvodMediaAsset>();
			if (asset[MD1_URL] != null) {
				IpvodMediaAsset mediaAsset =  new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD1_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(1l);
				ipvodMediaAssets.add(mediaAsset);
			}
			if (asset[MD2_URL] != null) {
				IpvodMediaAsset mediaAsset = new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD2_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(2l);
				ipvodMediaAssets.add(mediaAsset);
			}
			if (asset[MD3_URL] != null) {
				IpvodMediaAsset mediaAsset = new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD3_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(3l);
				ipvodMediaAssets.add(mediaAsset);
			}
			ipvodAsset.setIpvodMediaAssets(ipvodMediaAssets);
			ipvodAsset.setCountry((String) asset[COUNTRY]);
			ipvodAsset.setTotalTime(((BigDecimal) asset[TOTAL_TIME]).longValue());
			if (asset[RELEASE_YEAR] != null) {
				ipvodAsset.setReleaseYear(((BigDecimal) asset[RELEASE_YEAR]).intValue());
			}
			ipvodAsset.setIpvodAssetType(new IpvodAssetType());
			ipvodVisualMenu.getIpvodAssets().add(ipvodAsset);
		}
		return ipvodVisualMenu ;
	}
	
	public IpvodVisualMenu findMenuSTB(long menuId) {
		final int ASSET_ID = 0;
		final int TITLE = 1;
		final int DESCRIPTION = 2;
		final int PRICE = 3;
		final int RATING = 4;
		final int RATING_LEVEL = 5;
		final int LANGUAGES = 6;
		final int SUBTITLES = 7;
		final int AUDIO_TYPE = 8;
		final int HD_CONTENT = 9;
		final int MD1_URL = 10;
		final int MD2_URL = 11;
		final int MD3_URL = 12;
		final int COUNTRY = 13;
		final int TOTAL_TIME = 14;
		final int RELEASE_YEAR = 15;
		final int DIRECTOR = 17;
		final int ACTORS = 18;

		StringBuilder hql = new StringBuilder("select "
				+ "unique a.asset_id,  "
				+ "a.title, "
				+ "a.description,  "
				+ "a.price,  "
				+ "r.rating, "
				+ "r.rating_level,  "
				+ "a.languages,  "
				+ "a.subtitles,  "
				+ "a.audio_type,  "
				+ "a.hd_content,  "
				+ "md1.URL as md1URL, "
				+ "md2.URL as md2URL, "
				+ "md3.URL as md3URL, "
				+ "a.country,  "
				+ "a.total_time,  "
				+ "a.release_year, "
				+ "ma.zindex, "
				+ "a.director, "
				+ "a.actors "
				+ "from IPVOD_ASSET a "
				+ "left join IPVOD_VISUAL_MENU_ASSET_INDEX ma on (ma.ASSET_ID = A.ASSET_ID) "
				+ "left join IPVOD_MEDIA_ASSET md1 on (md1.asset_id = a.asset_id and md1.media_type_id = 1) "
				+ "left join IPVOD_MEDIA_ASSET md2 on (md2.asset_id = a.asset_id and md2.media_type_id = 2) "
				+ "left join IPVOD_MEDIA_ASSET md3 on (md3.asset_id = a.asset_id and md3.media_type_id = 3) "
				+ "left join IPVOD_RATING r on (r.rating_level = a.rating_level)"
				+ "where a.asset_id = ma.asset_id and a.license_window_end >= sysdate and a.license_window_start <= sysdate "
				+ "and ma.menu_id = :menuId order by ma.zindex ");

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("menuId", menuId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> assets = (List<Object[]>)query.list();
		
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		ipvodVisualMenu.setIpvodAssets(new ArrayList<IpvodAsset>());
		for (Object[] asset : assets) {
			IpvodAsset ipvodAsset = new IpvodAsset();
			ipvodAsset.setAssetId(((BigDecimal) asset[ASSET_ID]).longValue());
			ipvodAsset.setTitle((String) asset[TITLE]);
			ipvodAsset.setDescription((String) asset[DESCRIPTION]);
			ipvodAsset.setDirector((String) asset[DIRECTOR]);
			ipvodAsset.setActors((String) asset[ACTORS]);
			ipvodAsset.setDescription((String) asset[DESCRIPTION]);
			ipvodAsset.setPrice(((BigDecimal) asset[PRICE]).doubleValue());
			
			ipvodAsset.setRating(new IpvodRating());
			if (asset[RATING_LEVEL] != null) {
				ipvodAsset.getRating().setRatingLevel(((BigDecimal) asset[RATING_LEVEL]).longValue());
				ipvodAsset.getRating().setRating((String) asset[RATING]);
			}
			
			ipvodAsset.setLanguages((String) asset[LANGUAGES]);
			ipvodAsset.setSubtitles((String) asset[SUBTITLES]);
			ipvodAsset.setAudioType((String) asset[AUDIO_TYPE]);
			if (((BigDecimal) asset[HD_CONTENT]).longValue() == 1) {
				ipvodAsset.setHD(true);	
			} else {
				ipvodAsset.setHD(false);
			}
			ArrayList<IpvodMediaAsset> ipvodMediaAssets = new ArrayList<IpvodMediaAsset>();
			if (asset[MD1_URL] != null) {
				IpvodMediaAsset mediaAsset =  new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD1_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(1l);
				ipvodMediaAssets.add(mediaAsset);
			}
			if (asset[MD2_URL] != null) {
				IpvodMediaAsset mediaAsset = new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD2_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(2l);
				ipvodMediaAssets.add(mediaAsset);
			}
			if (asset[MD3_URL] != null) {
				IpvodMediaAsset mediaAsset = new IpvodMediaAsset();
				mediaAsset.setUrl((String) asset[MD3_URL]);
				mediaAsset.setIpvodMediaType(new IpvodMediaType());
				mediaAsset.getIpvodMediaType().setMediaTypeId(3l);
				ipvodMediaAssets.add(mediaAsset);
			}
			ipvodAsset.setIpvodMediaAssets(ipvodMediaAssets);
			ipvodAsset.setCountry((String) asset[COUNTRY]);
			ipvodAsset.setTotalTime(((BigDecimal) asset[TOTAL_TIME]).longValue());
			if (asset[RELEASE_YEAR] != null) {
				ipvodAsset.setReleaseYear(((BigDecimal) asset[RELEASE_YEAR]).intValue());
			}
			ipvodAsset.setIpvodAssetType(new IpvodAssetType());
			ipvodVisualMenu.getIpvodAssets().add(ipvodAsset);
		}
		return ipvodVisualMenu ;
	}

	public IpvodVisualMenu getMenuHierarchy(Long parentMenuId, Long userId) {
		final int MENU_ID = 0;
		final int NAME = 1;
		final int MENU_SUP = 2;
		final int RATING_LEVEL = 3;
		final int ADULT = 4;
		final int ZINDEX = 5;
		final int PACKAGE_ID = 6;

		StringBuilder hql = new StringBuilder("select "
				+ "m.menu_id, "
				+ "m.name, "
				+ "m.menu_sup_id, "
				+ "m.rating_level, "
				+ "m.zindex, "
				+ "r.adult, "
				+ "p.package_id "
				+ "from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_RATING r on (r.rating_level = m.rating_level) "
				+ "left join ipvod_package_menu p on (p.MENU_ID = m.MENU_ID) "
				+ "where m.active = 1 "
				+ "and (m.avaliable_since < sysdate or m.avaliable_since is null) "
				+ "and (m.avaliable_until > sysdate or m.avaliable_until is null) "
				+ "and m.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = :parentMenuId connect by prior m.menu_id = m2.menu_sup_id) "
//				+ "and m.menu_id in "
//				+ "(select unique menu_id from IPVOD_VISUAL_MENU_ASSET_INDEX)"
				+ " and ("
				+ "m.menu_id in ( select unique menu_id from IPVOD_VISUAL_MENU_ASSET_INDEX) "
				+ "or "
				+ "m.menu_id in (select unique menu_sup_id from ipvod_visual_menu)"
				+ ")"
				+ "order by m.menu_sup_id desc, m.zindex, m.menu_id, p.package_id");

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("parentMenuId", parentMenuId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> menus = (List<Object[]>)query.list();
		
		IpvodVisualMenu ipvodVisualMenuList = new IpvodVisualMenu();
		Map<Long, IpvodVisualMenu> map = new HashMap<Long, IpvodVisualMenu>();
		Map<Long, IpvodVisualMenu> parentNotFoundMap = new LinkedHashMap<Long, IpvodVisualMenu>();
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		for (Object[] menu : menus) {
			if (ipvodVisualMenu.getMenuId() != ((BigDecimal) menu[MENU_ID]).longValue()) {
				ipvodVisualMenu = new IpvodVisualMenu();
				ipvodVisualMenu.setMenuId(((BigDecimal) menu[MENU_ID]).longValue());
				ipvodVisualMenu.setName((String) menu[NAME]);
				ipvodVisualMenu.setContentURL("/menu/"+ipvodVisualMenu.getMenuId());
				if (menu[ZINDEX] != null) {
					ipvodVisualMenu.setZindex(((BigDecimal) menu[ZINDEX]).longValue());
				}
				IpvodRating ipvodRating = new IpvodRating();
				if (menu[RATING_LEVEL] != null) {
					ipvodRating.setRatingLevel(((BigDecimal) menu[RATING_LEVEL]).longValue());
					if (((BigDecimal) menu[ADULT]).longValue() == 1) {
						ipvodRating.setAdult(true);
					} else {
						ipvodRating.setAdult(false);
					}
				}
				ipvodVisualMenu.setIpvodRating(ipvodRating );
				if (ipvodVisualMenu.getMenuId() == parentMenuId) {
					ipvodVisualMenuList = ipvodVisualMenu;
				}
				ipvodVisualMenu.setIpvodVisualMenus(new ArrayList<IpvodVisualMenu>());
				map.put(ipvodVisualMenu.getMenuId(), ipvodVisualMenu);
				if (menu[MENU_SUP] != null) {
					IpvodVisualMenu menuParent = map.get(((BigDecimal) menu[MENU_SUP]).longValue());
					if (menuParent != null) {
						menuParent.getIpvodVisualMenus().add(ipvodVisualMenu);
					} else {
						menuParent = new IpvodVisualMenu();
						menuParent.setMenuId(((BigDecimal) menu[MENU_SUP]).longValue());
						ipvodVisualMenu.setIpvodVisualMenu(menuParent);
						parentNotFoundMap.put(ipvodVisualMenu.getMenuId(), ipvodVisualMenu);
					}
				}
				ipvodVisualMenu.setIpvodPackages(new ArrayList<IpvodPackage>());
				if (menu[PACKAGE_ID] != null) {
					IpvodPackage ipvodPackage = new IpvodPackage();
					ipvodPackage.setPackageId(((BigDecimal) menu[PACKAGE_ID]).longValue());
					ipvodVisualMenu.getIpvodPackages().add(ipvodPackage );
				}
			} else {
				IpvodPackage ipvodPackage = new IpvodPackage();
				ipvodPackage.setPackageId(((BigDecimal) menu[PACKAGE_ID]).longValue());
				ipvodVisualMenu.getIpvodPackages().add(ipvodPackage );
			}
			
		}
		
		while (!parentNotFoundMap.isEmpty()) {
			ipvodVisualMenu = parentNotFoundMap.remove(parentNotFoundMap.keySet().toArray()[0]);
			long parentId = ipvodVisualMenu.getIpvodVisualMenu().getMenuId(); 
			IpvodVisualMenu menuParent = map.get(parentId);
			if (menuParent != null) {
				menuParent.getIpvodVisualMenus().add(ipvodVisualMenu);
			} else {
				menuParent = new IpvodVisualMenu();
				menuParent.setMenuId(parentId);
				if (map.get(parentId) != null || parentNotFoundMap.get(parentId) != null) {
					parentNotFoundMap.put(ipvodVisualMenu.getMenuId(), ipvodVisualMenu);
				}
			}
		}
		return ipvodVisualMenuList ;
	}

	public List<IpvodVisualMenu> findMenuCategorize() {
		final int MENU_ID = 0;
		final int NAME = 1;
		final int BREADCRUMBS = 2;
		StringBuilder hql = new StringBuilder("select "
				+ "m.menu_id, "
				+ "m.name, "
				+ "concat(concat(pai.name, '/'),m.name) as breadcrumbs "
				+ "from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU pai ON pai.MENU_ID = m.MENU_SUP_ID " 
				+ "order by m.name");

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		
		@SuppressWarnings("unchecked")
		List<Object[]> menus = (List<Object[]>)query.list();
		
		List<IpvodVisualMenu> ipvodVisualMenuList = new ArrayList<IpvodVisualMenu>();
		IpvodVisualMenu ipvodVisualMenu = new IpvodVisualMenu();
		for (Object[] menu : menus) {
			ipvodVisualMenu = new IpvodVisualMenu();
			ipvodVisualMenu.setMenuId(((BigDecimal) menu[MENU_ID]).longValue());
			ipvodVisualMenu.setName((String) menu[NAME]);
			ipvodVisualMenu.setBreadcrumbs((String) menu[BREADCRUMBS]);
			ipvodVisualMenuList.add(ipvodVisualMenu);
		}
		
		return ipvodVisualMenuList ;
	}

	public void disableEnableMenus(IpvodVisualMenu parentMenu, Integer  active) {
		StringBuilder sql = new StringBuilder(
				"update IPVOD_VISUAL_MENU set ACTIVE = :active where menu_id in ( " +
						"	select unique m.menu_id from IPVOD_VISUAL_MENU m " +
						"	left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id " +
						"	start with m.menu_id = 503548 connect by prior m.menu_id = m2.menu_sup_id) "
			);
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		query.setParameter("active", active);
		query.executeUpdate();
	}

}