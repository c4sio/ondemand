package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.SQLQuery;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;

@Stateless
public class MenuAssetDAO extends GenericDAO<IpvodVisualMenuAsset> {

	public MenuAssetDAO() {
		super(IpvodVisualMenuAsset.class);
	}

	public void deleteAssetMenu(IpvodVisualMenuAsset ipvodVisualMenuAsset) {
		super.delete(ipvodVisualMenuAsset.getId(), IpvodVisualMenuAsset.class);
	}
	
	public List<IpvodVisualMenuAsset> findAll() {
		
		return super.findAll();
	}
	
	public void clean() {
		
	    Query query = getEm().createQuery("DELETE FROM IpvodVisualMenuAsset");
	    query.executeUpdate();
	}

	public void clean(long menuId) {
		
	    Query query = getEm().createQuery("DELETE FROM IpvodVisualMenuAsset vma WHERE vma.ipvodVisualMenu.menuId = " + menuId);
	    query.executeUpdate();
	}

	public List<IpvodVisualMenuAsset> findByMenuId(long menuId) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("menuId", menuId);
		
		return super.findResultByParameter(IpvodVisualMenuAsset.FIND_BY_MENU_ID,
				parameters);
	}

	
	public List lab() {
		
		String sql = "" +
		
		"    SELECT IPVOD_VISUAL_MENU.NAME, 																							" +
		"           IPVOD_VISUAL_MENU.MENU_ID,	 																						" +
		"           IPVOD_VISUAL_MENU.MENU_SUP_ID, 																						" +
		"           IPVOD_ASSET.ASSET_ID, 																								" +
		"           IPVOD_VISUAL_MENU_ASSET_INDEX.ZINDEX  AS ASSET_ZINDEX, 																" +
		"           IPVOD_VISUAL_MENU.ZINDEX              AS MENU_ZINDEX, 																" +
		"           IPVOD_ASSET.TITLE, 																									" +
		"			IPVOD_VISUAL_MENU.ACTIVE,																							" +
		"			IPVOD_VISUAL_MENU.RATING_LEVEL,																						" +
		"			IPVOD_ASSET.HD_CONTENT, 																							" +
		"			IPVOD_ASSET.AUDIO_TYPE, 																							" +
		"			IPVOD_ASSET.LANGUAGES, 																								" +
		"			IPVOD_ASSET.SUBTITLES, 																								" +
		"			IPVOD_ASSET.PRICE, 																									" +
		"			IPVOD_ASSET.BILLING_ID,																								" +
		"			IPVOD_VISUAL_MENU.AVALIABLE_SINCE,																					" +
		"			IPVOD_VISUAL_MENU.AVALIABLE_UNTIL									  											    " +
//		"			IPVOD_VISUAL_MENU.AVALIABLE_UNTIL,									  											    " +
//		"			IPVOD_PACKAGE_MENU.PACKAGE_ID																					    " +
		"      FROM IPVOD_VISUAL_MENU 																									" + 
		" LEFT JOIN IPVOD_VISUAL_MENU_ASSET_INDEX ON IPVOD_VISUAL_MENU.MENU_ID = IPVOD_VISUAL_MENU_ASSET_INDEX.MENU_ID 					" +
		" LEFT JOIN IPVOD_ASSET ON IPVOD_ASSET.ASSET_ID = IPVOD_VISUAL_MENU_ASSET_INDEX.ASSET_ID 										" +
//		" LEFT JOIN IPVOD_PACKAGE_MENU ON IPVOD_PACKAGE_MENU.MENU_ID = IPVOD_VISUAL_MENU.MENU_ID										" +
		"  ORDER BY MENU_ZINDEX ASC, 																									" +
		"           ASSET_ZINDEX ASC																									";
//		"           ASSET_ZINDEX ASC,																									" +
//        "  			IPVOD_PACKAGE_MENU.PACKAGE_ID ASC																					";
		
		Query query = super.getEm().createNativeQuery(sql);
		return query.getResultList();
		
	}

	public void delete(List<IpvodVisualMenuAsset> menuAssetList) {
	    String hql = "";
	    for (IpvodVisualMenuAsset menuAsset : menuAssetList) {
	    	if (!hql.equals("")) {
	    		hql += " or ";	
	    	}
	    	hql += "(" +
	    			" vma.ipvodVisualMenu.menuId = "+ menuAsset.getIpvodVisualMenu().getMenuId() +
	    			" and " +
	    			" vma.ipvodAsset.assetId = "+ menuAsset.getIpvodAsset().getAssetId() +
	    			")";
	    }
		Query query = getEm().createQuery("DELETE FROM IpvodVisualMenuAsset vma WHERE " + hql );
	    query.executeUpdate();
	}

	public List<IpvodVisualMenu> findByAssetId(long assetId) {
		final int MENU_ID = 0;
		final int ZINDEX = 1;
		final int NAME = 2;
		final int BREADCRUMBS = 3;
		
		StringBuilder hql = new StringBuilder("select vma.menu_id, vma.zindex, m.name, "
				+ "concat(concat(pai.name, '/'),m.name) as breadcrumbs "
				+ "from IPVOD_VISUAL_MENU_ASSET_INDEX vma, IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU pai ON pai.MENU_ID = m.MENU_SUP_ID "
				+ "where vma.MENU_ID = m.MENU_ID and asset_id = " 
				+ assetId 
				+ " order by breadcrumbs");

		SQLQuery query = getSession().createSQLQuery(String.valueOf(hql));

		@SuppressWarnings("unchecked")
		List<Object[]> assets = (List<Object[]>)query.list();
		
		List<IpvodVisualMenu> list = new ArrayList<IpvodVisualMenu>();
		for (Object[] obj : assets) {
			IpvodVisualMenu visualMenuAsset = new IpvodVisualMenu();
			
			visualMenuAsset.setMenuId(((BigDecimal) obj[MENU_ID]).longValue());
			visualMenuAsset.setName((String) obj[NAME]);
			visualMenuAsset.setBreadcrumbs((String) obj[BREADCRUMBS]);
			list.add(visualMenuAsset);
		}
		return list;
	}
}
