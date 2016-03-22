package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.util.PropertiesConfig;
import br.com.gvt.eng.vod.vo.PriceVO;
import br.com.gvt.eng.vod.vo.PurchaseOnlyVO;
import br.com.gvt.eng.vod.vo.PurchasePriceVO;
import br.com.gvt.eng.vod.vo.PurchaseReportVO;
import br.com.gvt.eng.vod.vo.PurchaseVO;
import br.com.gvt.eng.vod.vo.VDRPurchaseReportVO;

@Stateless
public class PurchaseDAO extends GenericDAO<IpvodPurchase> {

	public PurchaseDAO() {
		super(IpvodPurchase.class);
	}

	public void deletePurchase(IpvodPurchase ipvodPurchase) {
		super.delete(ipvodPurchase.getPurchaseId(), IpvodPurchase.class);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseVO> findPurchaseExecutions(String subscriberId, Date start, Date end) {
		
		StringBuilder hql = new StringBuilder("select p.purchaseDate as buyTime, a.title as title, c.description as genre, cp.providerName as provider, e.mac as mac, p.purchaseId as purchaseId, a.assetId as assetId, p.amountPaid as offeringPrice from IpvodPurchase as p join p.ipvodAsset as a join a.ipvodCategory1 as c join a.ipvodContentProvider cp join p.ipvodEquipment as e join e.ipvodUser u where u.crmCustomerId = :subscriberId ");
		
		if(start != null){
			hql.append("and p.purchaseDate >= :start ");
		}
		
		if(end != null){
			hql.append("and p.purchaseDate <= :end ");
		}
		
		hql.append("order by p.purchaseDate");
		
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("subscriberId", subscriberId);
		
		if(start != null){
			query.setParameter("start", start);
		}
		
		if(end != null){
			query.setParameter("end", end);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(PurchaseVO.class));
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseReportVO> findPurchasesByDate(Date start, Date end) {
		
		StringBuilder hql = new StringBuilder("select p.purchaseDate as purchaseDate, a.title as title, "
				+ "p.purchaseId as purchaseId, a.assetId as assetId, p.amountPaid as amountPaid, "
				+ "p.billed as billed, u.crmCustomerId as crmCustomerId "
				+ "from IpvodPurchase as p "
				+ "join p.ipvodAsset as a "
				+ "join p.ipvodEquipment as e "
				+ "join e.ipvodUser u "
				+ "where p.purchaseDate >= :start and p.purchaseDate <= :end "
				+ "and p.amountPaid > 0 and p.billed = true "
				+ "order by p.purchaseDate, u.crmCustomerId");
		
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("start", start);
		query.setParameter("end", end);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(PurchaseReportVO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	public List<VDRPurchaseReportVO> findPurchasesByDateVDR(Date start, Date end) {
		
		StringBuilder hql = new StringBuilder("select p.purchaseDate as purchaseDate, a.title as title, "
				+ "p.purchaseId as purchaseId, a.assetId as assetId, p.amountPaid as amountPaid, "
				+ "p.billed as billed, u.crmCustomerId as crmCustomerId, "
				+ "u.serviceRegion as serviceRegion, p.validUntil as validUntil, "
				+ "c.providerName as providerName, c.providerId as providerId, "
				+ "e.mac as mac, a.product as product "
				+ "from IpvodPurchase as p "
				+ "join p.ipvodAsset as a "
				+ "join p.ipvodEquipment as e "
				+ "join e.ipvodUser u "
				+ "join a.ipvodContentProvider c "
				+ "where p.purchaseDate >= :start and p.purchaseDate <= :end "
				+ "and p.amountPaid > 0 and p.billed = true "
				+ "order by p.purchaseDate, u.crmCustomerId");
		
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("start", start);
		query.setParameter("end", end);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VDRPurchaseReportVO.class));
		
		return query.list();
		
	}

	public List<IpvodPurchase> findPurchase(Long assetId, Long userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetId", assetId);
		parameters.put("userId", userId);
		return super.findResultByParameter(IpvodPurchase.CURRENT_PURCHASE_BY_USER_ASSET,
				parameters);
	}
	 
	public List<IpvodPurchase> findPurchaseByAssetList(List<Long> assetId, Long userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assets", assetId);
		parameters.put("userId", userId);
		return super.findResultByParameter(IpvodPurchase.CURRENT_PURCHASE_BY_ASSET_LIST,
				parameters);
	}
	
	public List<IpvodPurchase> findPurchaseComplex(UriInfo uriInfo) {
		return super.findResultComplexQuery(uriInfo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getPurchaseByRegion(GregorianCalendar date) {
	    	
			 String sql = "" +
			 "SELECT DISTINCT((SELECT i.SERVICE_REGION FROM IPVOD_USER i WHERE i.USER_ID = eq.USER_ID)) REGION, " +
			     "(SELECT COUNT(DISTINCT(ip_q.USER_ID)) FROM IPVOD_PURCHASE ip_a, IPVOD_USER ip_u, IPVOD_EQUIPMENT ip_q " +
			     "WHERE ip_u.USER_ID = ip_q.USER_ID " +
			     "  AND ip_a.EQUIPMENT_ID = ip_q.EQUIPMENT_ID " +
			     "  AND ip_u.SERVICE_REGION = yy.SERVICE_REGION) COUNTT " +
			 " FROM IPVOD_PURCHASE x, IPVOD_USER yy, IPVOD_EQUIPMENT eq " +
			 "WHERE yy.USER_ID = eq.USER_ID " +
			 "  AND x.EQUIPMENT_ID = eq.EQUIPMENT_ID " +
			 "  AND EXTRACT(DAY   FROM PURCHASE_DATE) < "+(date.get(Calendar.DAY_OF_MONTH))+" " +
			 "  AND EXTRACT(MONTH FROM PURCHASE_DATE) = "+(date.get(Calendar.MONTH)+1)+" " +
			 "  AND EXTRACT(YEAR  FROM PURCHASE_DATE) = "+(date.get(Calendar.YEAR))+" "
			;
	    	
	    	javax.persistence.Query x = super.getEm().createNativeQuery(sql);
	    	List<Object> result = x.getResultList();
	    	return result;
	    }

	public double getLowerPrice(IpvodUser ipvodUser, IpvodAsset ipvodAsset) {
		 String sql = "select least(price, coalesce(price_package, 9999)) "
		 		+ "from (select price from IPVOD_ASSET where asset_id = " + ipvodAsset.getAssetId() + "), "
 				+ "(select min(price) as price_package from IPVOD_ASSET_PACKAGE "
 				+ "where asset_id = " + ipvodAsset.getAssetId() 
 				+ " and package_id in(select package_id from IPVOD_PACKAGE_SUBSCRIPTION where user_id = " + ipvodUser.getUserId() + "))"
		;
    	
    	javax.persistence.Query query = super.getEm().createNativeQuery(sql);
    	BigDecimal result = (BigDecimal) query.getSingleResult();
    	return result.doubleValue();
    }

	public Map<Long, Object> findAllMyPurchases(IpvodUser ipvodUser) {
		final int ASSET_ID = 0;
		final int PURCHASE_ID = 1;
		final int VALID_UNTIL = 2;
		final int PURCHASE_DATE = 3;
		final int LOWER_PRICE = 4;
		final int PACKAGE_PRICE = 5;

		String sql = ""
				+ "select a.asset_id, p.purchase_id, p.valid_until, p.purchase_date, 																				"
				//RECUPERA O MENOR PREÇO ENTRE O PREÇO DO ASSET E O PREÇO ENTRE OS PACOTES DO USUARIO
				+ "			least(																																	"
				+ "				(select price from IPVOD_ASSET x where x.asset_id = a.asset_id), 																	"
				+ "				coalesce(																															"
				+ "					(select min(price) from IPVOD_ASSET_PACKAGE ap where ap.asset_id = a.asset_id 													"
				+ "					and ap.package_id in (select package_id from IPVOD_PACKAGE_SUBSCRIPTION ps where ps.user_id = " + ipvodUser.getUserId() + ")	"
				+ "				),9999)																																"
				+ "			) as price, 																															"
				+ "			(select min(price) from IPVOD_ASSET_PACKAGE ap where ap.asset_id = a.asset_id 															"
				+ "				and ap.package_id in (select package_id from IPVOD_PACKAGE_SUBSCRIPTION ps where ps.user_id = " + ipvodUser.getUserId() + ")		"
				+ "			) as package_price																								"
				//RETORNA OS PURCHASES QUANDO EXISTENTES 
				+ "from IPVOD_ASSET a left join IPVOD_PURCHASE p on (																								"
				+ "				p.asset_id = a.asset_id 																											"
				//PARA OS EQUIPAMENTOS DO USUARIO
				+ "				and EQUIPMENT_ID in (select EQUIPMENT_ID from IPVOD_EQUIPMENT where USER_ID = " + ipvodUser.getUserId() + ") 						"
				//E QUE AS COMPRAS AINDA ESTEJAM VALIDAS
				+ "and valid_until > sysdate) 																														"
				//DOS ASSETS QUE CONSTAM NOS MENUS VALIDOS PARA O USUARIO
				+ "where a.asset_id in (select unique(asset_id) from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id in (												"
				+ "				select m.menu_id from IPVOD_VISUAL_MENU m 																							"
				+ "				left join IPVOD_PACKAGE_MENU pm on (pm.menu_id = m.menu_id) 																		"
				//ONDE OS MENUS ESTEJAM NOS PACOTES QUE O USUARIO POSSUI
				+ "				where package_id in (select package_id from ipvod_package_subscription where user_id = " + ipvodUser.getUserId() + ")				"
				+ ")																									"
				//E TODAS AS PURCHASES VALIDAS
				+ ") or (p.purchase_id is not null and p.EQUIPMENT_ID in (select EQUIPMENT_ID from IPVOD_EQUIPMENT where USER_ID = " + ipvodUser.getUserId() + ") and valid_until > sysdate)";

	    javax.persistence.Query query = super.getEm().createNativeQuery(sql);
	    @SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
	    Map<Long, Object> map = new HashMap<Long, Object>();
	    for (Object[] res :result) {
	    	Object vo = null;
			if (res[PURCHASE_ID] != null) {
				if (res[PACKAGE_PRICE] == null) {
					vo = new PurchaseOnlyVO();
					((PurchaseOnlyVO)vo).setPurchaseId(((BigDecimal) res[PURCHASE_ID]).longValue());
					((PurchaseOnlyVO)vo).setValidUntil((Date) res[VALID_UNTIL]);
					((PurchaseOnlyVO)vo).setPurchaseDate((Date) res[PURCHASE_DATE]);
				} else {
					vo = new PurchasePriceVO();
					((PurchasePriceVO)vo).setPurchaseId(((BigDecimal) res[PURCHASE_ID]).longValue());
					((PurchasePriceVO)vo).setValidUntil((Date) res[VALID_UNTIL]);
					((PurchasePriceVO)vo).setPurchaseDate((Date) res[PURCHASE_DATE]);
					((PurchasePriceVO)vo).setPrice(((BigDecimal) res[LOWER_PRICE]).doubleValue());
				}
			} else {
				if (res[LOWER_PRICE] != null) {
					vo = new PriceVO();
					((PriceVO)vo).setPrice(((BigDecimal) res[LOWER_PRICE]).doubleValue());
				}
			}
			map.put(((BigDecimal) res[ASSET_ID]).longValue(), vo);

	    }
	    return map;
    }

	public Map<Long, Map<String, Object>> findMyPurchasesByMenu(long menuId, IpvodUser ipvodUser) {
		String assetsWhere = "where a.asset_id in (select unique(asset_id) from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id = " + menuId + ")";
	    return findMyPurchases(ipvodUser, assetsWhere);
    }

	public Map<Long, Map<String, Object>> findMyPurchases(IpvodUser ipvodUser, String assetsWhere) {
		final int ASSET_ID = 0;
		final int PURCHASE_ID = 1;
		final int VALID_UNTIL = 2;
		final int PURCHASE_DATE = 3;
		final int LOWER_PRICE = 4;
		final int PACKAGE_PRICE = 5;

		String sql = ""
				+ "select a.asset_id, p.purchase_id, to_char(p.valid_until,'dd/MM/yyyy HH24:mi:SS'), p.purchase_date, 																				"
				//RECUPERA O MENOR PREÇO ENTRE O PREÇO DO ASSET E O PREÇO ENTRE OS PACOTES DO USUARIO
				+ "			least(																																	"
				+ "				(select price from IPVOD_ASSET x where x.asset_id = a.asset_id), 																	"
				+ "				coalesce(																															"
				+ "					(select min(price) from IPVOD_ASSET_PACKAGE ap where ap.asset_id = a.asset_id 													"
				+ "					and ap.package_id in (select package_id from IPVOD_PACKAGE_SUBSCRIPTION ps where ps.user_id = " + ipvodUser.getUserId() + ")	"
				+ "				),9999)																																"
				+ "			) as price, 																															"
				+ "			(select min(price) from IPVOD_ASSET_PACKAGE ap where ap.asset_id = a.asset_id 															"
				+ "				and ap.package_id in (select package_id from IPVOD_PACKAGE_SUBSCRIPTION ps where ps.user_id = " + ipvodUser.getUserId() + ")		"
				+ "			) as package_price,p.valid_until																								"
				//RETORNA OS PURCHASES QUANDO EXISTENTES 
				+ "from IPVOD_ASSET a left join IPVOD_PURCHASE p on (																								"
				+ "				p.asset_id = a.asset_id 																											"
				//PARA OS EQUIPAMENTOS DO USUARIO
				+ "				and EQUIPMENT_ID in (select EQUIPMENT_ID from IPVOD_EQUIPMENT where USER_ID = " + ipvodUser.getUserId() + ") 						"
				//E QUE AS COMPRAS AINDA ESTEJAM VALIDAS
				+ "and valid_until > sysdate) 																														"
				+ assetsWhere; 

	    javax.persistence.Query query = super.getEm().createNativeQuery(sql);
	    @SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
	    Map<Long, Map<String, Object>> map = new HashMap<Long, Map<String, Object>>();
	    for (Object[] res :result) {
	    	Map<String, Object> purchasePrice = new HashMap<String, Object>();
			if (res[PURCHASE_ID] != null) {
				System.out.println("DATE-TEST - to_char - " + (String)res[VALID_UNTIL]);
				System.out.println("DATE-TEST - to_char as date - " + toDate((String)res[VALID_UNTIL]));
				System.out.println("DATE-TEST - original - " + res[6]);
				System.out.println("DATE-TEST - timezone - " + TimeZone.getDefault());
				PurchaseOnlyVO vo = new PurchaseOnlyVO();
				if (res[PACKAGE_PRICE] == null) {
					vo.setPurchaseId(((BigDecimal) res[PURCHASE_ID]).longValue());
					vo.setValidUntil(toDate((String)res[VALID_UNTIL]));
					vo.setPurchaseDate((Date)res[PURCHASE_DATE]);
				} else {
					vo.setPurchaseId(((BigDecimal) res[PURCHASE_ID]).longValue());
					vo.setValidUntil(toDate((String)res[VALID_UNTIL]));
					vo.setPurchaseDate((Date) res[PURCHASE_DATE]);
					purchasePrice.put("price", res[LOWER_PRICE]);
				}
				purchasePrice.put("purchase", vo);
				map.put(((BigDecimal) res[ASSET_ID]).longValue(), purchasePrice);
			} else {
				if (res[PACKAGE_PRICE] != null && res[LOWER_PRICE] != null) {
					purchasePrice.put("price", res[LOWER_PRICE]);
					map.put(((BigDecimal) res[ASSET_ID]).longValue(), purchasePrice);
				}
			}
	    }
	    return map;
    }
	
	private Date toDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object findMyPurchasesCatchup(IpvodUser ipvodUser) {
		String assetsWhere = "where a.asset_id in (select unique(asset_id) from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id in "
				+ "(select unique m.menu_id " 
				+ "from IPVOD_VISUAL_MENU m " 
				+ "left join ipvod_visual_menu m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = "
				+ PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP)
				+ " connect by prior m.menu_id = m2.menu_sup_id)"
				+ ")";
		return findMyPurchases(ipvodUser, assetsWhere);
	}
	
	public Object findMyPurchasesOnDemandAdult(IpvodUser ipvodUser) {
		String assetsWhere = "where a.asset_id in (select unique(asset_id) from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id in "
				+ "(select unique m.menu_id " 
				+ "from IPVOD_VISUAL_MENU m " 
				+ "left join ipvod_visual_menu m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = "
				+ PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND)
				+ " connect by prior m.menu_id = m2.menu_sup_id) "
				+ ") and a.rating_level = 8";
		return findMyPurchases(ipvodUser, assetsWhere);
	}

	public Object findMyPurchasesOnDemand(IpvodUser ipvodUser) {
		String assetsWhere = "where a.asset_id in (select unique(asset_id) from IPVOD_VISUAL_MENU_ASSET_INDEX where menu_id in "
				+ "(select unique m.menu_id " 
				+ "from IPVOD_VISUAL_MENU m " 
				+ "left join ipvod_visual_menu m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = "
				+ PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND)
				+ " connect by prior m.menu_id = m2.menu_sup_id)"
				+ ") and a.rating_level < 8";
		return findMyPurchases(ipvodUser, assetsWhere);
	}

}
