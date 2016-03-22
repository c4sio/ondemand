package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAssetType;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.model.IpvodMediaType;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.eng.vod.util.PropertiesConfig;
import br.com.gvt.eng.vod.util.ValueComparator;
import br.com.gvt.eng.vod.vo.it.OnDemandContentVO;

@Stateless
public class AssetDAO extends GenericDAO<IpvodAsset> {

	@EJB
	MenuDAO menuDAO;
	
	public AssetDAO() {
		super(IpvodAsset.class);
	}

	public void deleteAsset(IpvodAsset ipvodAsset) {
		super.delete(ipvodAsset.getAssetId(), IpvodAsset.class);
	}

	public List<IpvodAsset> listAssetByCategory(long categoryId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", categoryId);
		return super.findResultByParameter(IpvodAsset.FIND_BY_CATEGORY, parameters);
	}

	public List<IpvodAsset> listAssetByInicialWord(String word) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("word", "%" + word.toUpperCase() + "%");
		return super.findResultByParameter(IpvodAsset.FIND_BY_WORD, parameters);
	}

	public List<IpvodAsset> listAssetType(long idAssetType) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idAssetType", idAssetType);

		return super.findResultByParameter(IpvodAsset.FIND_ASSET_BY_TYPE, parameters);
	}

	public List<IpvodAsset> listAssetByMenu(Long menuId, Map<String, Object> pagination) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("menuId", menuId);
		return super.findResultByParameterPagination(IpvodAsset.FIND_BY_MENU, parameters, pagination);
	}

	public List<IpvodAsset> listAssetByNewReleases(String genre) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("genre", genre);

		return super.findResultByParameter(IpvodAsset.FIND_BY_NEW_RELEASES, parameters);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getTotalNewAssets(GregorianCalendar date) {

		String sql = "" + "SELECT COUNT(0) TOTAL " + "  FROM IPVOD_ASSET " + " WHERE EXTRACT(DAY   FROM CREATION_DATE) < "
				+ (date.get(Calendar.DAY_OF_MONTH)) + " " + "   AND EXTRACT(MONTH FROM CREATION_DATE) = "
				+ (date.get(Calendar.MONTH) + 1) + " " + "   AND EXTRACT(YEAR  FROM CREATION_DATE) = "
				+ (date.get(Calendar.YEAR)) + " ";

		javax.persistence.Query x = super.getEm().createNativeQuery(sql);
		List<Object> result = x.getResultList();
		return result;

	}

	public List<IpvodAsset> findAssetsByList(List<Long> assets, long userId, boolean adult) {
		String assetWhere = " and a.asset_id in " + assets.toString().replace("[", "(").replace("]", ")") ;
		if (adult) {
			assetWhere += " and a.rating_level = 8 ";
		} else {
			assetWhere += " and a.rating_level < 8 ";
		}
		return getMyContent(userId, assetWhere);
	}

	@SuppressWarnings("unchecked")
	public List<OnDemandContentVO> findByName(String c, LikeOperator like) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("a.title as title, ");
		hql.append("a.originalTitle as originalTitle, ");
		hql.append("'UNKNOWN' as genre, ");
		hql.append("c.description as category, ");
		hql.append("a.subtitles as subtitle, ");
		hql.append("a.country as country, ");
		hql.append("sc.description as subCategory, ");
		hql.append("a.assetId as assetId, ");
		hql.append("a.creationDate as creationDate, ");
		hql.append("a.description as description, ");
		hql.append("a.director as director, ");
		hql.append("a.actors as actors, ");
		hql.append("a.episode as episode, ");
		hql.append("a.billingID as billingID, ");
		hql.append("a.episodeName as episodeName, ");
		hql.append("a.licenseWindowEnd as licenseWindowEnd, ");
		hql.append("a.licenseWindowStart as licenseWindowStart, ");
		hql.append("a.price as price, ");
		hql.append("a.releaseYear as releaseYear, ");
		hql.append("a.season as season, ");
		hql.append("a.languages as languages, ");
		hql.append("a.assetInfo as assetInfo, ");
		hql.append("r.rating as rating, ");
		hql.append("r.adult as isAdult, ");
		hql.append("a.totalTime as totalTime, ");
		hql.append("a.product as product, ");
		hql.append("a.screenFormat as screenFormat, ");
		hql.append("a.audioType as audioType, ");
		hql.append("a.canResume as canResume, ");
		hql.append("a.isHD as isHD, ");
		hql.append("a.isRevised as isRevised, ");
		hql.append("a.fileSize as fileSize, ");
		hql.append("a.checksum as checksum, ");
		hql.append("a.bitrate as bitrate, ");
		hql.append("a.titleAlternative as titleAlternative, ");
		hql.append("at.description as assetType, ");
		hql.append("cp.providerId as contentProvider ");
		hql.append("from IpvodAsset as a left join a.ipvodCategory1 c left join a.ipvodCategory2 sc left join a.ipvodAssetType at left join a.ipvodContentProvider cp left join a.rating r ");
		hql.append("where upper(a.title) like upper(:param) order by a.title asc ");

		Query query = getSession().createQuery(String.valueOf(hql));

		if (LikeOperator.CONDITION_ALL.equals(like)) {
			query.setString("param", "%" + c + "%");
		} else if (LikeOperator.CONDITION_LEFT.equals(like)){
			query.setString("param", c + "%");
		} else {
			query.setString("param", "%" + c);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(OnDemandContentVO.class));

		return query.list();
	}

	public List<OnDemandContentVO> findTop50() {

		StringBuilder aux = new StringBuilder("select a.assetId, count(*) as total from IpvodPurchase p join p.ipvodAsset a group by a.assetId order by total desc");

		Query query = getSession().createQuery(String.valueOf(aux));
		query.setFirstResult(0);
		query.setMaxResults(50);

		@SuppressWarnings("unchecked")
		List<Object[]> list = query.list();

		List<OnDemandContentVO> vos = new ArrayList<OnDemandContentVO>();

		for(Object[] obj : list){

			StringBuilder hql = new StringBuilder("select ");
			hql.append("a.title as title, ");
			hql.append("a.originalTitle as originalTitle, ");
			hql.append("'UNKNOWN' as genre, ");
			hql.append("c.description as category, ");
			hql.append("a.subtitles as subtitle, ");
			hql.append("a.country as country, ");
			hql.append("sc.description as subCategory, ");
			hql.append("a.assetId as assetId, ");
			hql.append("a.creationDate as creationDate, ");
			hql.append("a.description as description, ");
			hql.append("a.director as director, ");
			hql.append("a.actors as actors, ");
			hql.append("a.episode as episode, ");
			hql.append("a.billingID as billingID, ");
			hql.append("a.episodeName as episodeName, ");
			hql.append("a.licenseWindowEnd as licenseWindowEnd, ");
			hql.append("a.licenseWindowStart as licenseWindowStart, ");
			hql.append("a.price as price, ");
			hql.append("a.releaseYear as releaseYear, ");
			hql.append("a.season as season, ");
			hql.append("a.languages as languages, ");
			hql.append("a.assetInfo as assetInfo, ");
			hql.append("r.rating as rating, ");
			hql.append("r.adult as isAdult, ");
			hql.append("a.totalTime as totalTime, ");
			hql.append("a.product as product, ");
			hql.append("a.screenFormat as screenFormat, ");
			hql.append("a.audioType as audioType, ");
			hql.append("a.canResume as canResume, ");
			hql.append("a.isHD as isHD, ");
			hql.append("a.isRevised as isRevised, ");
			hql.append("a.fileSize as fileSize, ");
			hql.append("a.checksum as checksum, ");
			hql.append("a.bitrate as bitrate, ");
			hql.append("a.titleAlternative as titleAlternative, ");
			hql.append("at.description as assetType, ");
			hql.append("cp.providerId as contentProvider ");
			hql.append("from IpvodAsset as a left join a.ipvodCategory1 c left join a.ipvodCategory2 sc left join a.ipvodAssetType at left join a.ipvodContentProvider cp left join a.rating r ");
			hql.append("where a.assetId = :assetId order by a.title asc ");

			query = getSession().createQuery(String.valueOf(hql));
			query.setParameter("assetId", obj[0]);
			query.setResultTransformer(new AliasToBeanResultTransformer(OnDemandContentVO.class));

			OnDemandContentVO vo = (OnDemandContentVO) query.uniqueResult();
			vo.setOrder(new Long(list.lastIndexOf(obj) + 1));

			vos.add(list.lastIndexOf(obj), vo);

		}

		return vos;
	}

	@SuppressWarnings("unchecked")
	public List<OnDemandContentVO> findByGenre(Long genreId) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append("a.title as title, ");
		hql.append("a.originalTitle as originalTitle, ");
		hql.append("'UNKNOWN' as genre, ");
		hql.append("c.description as category, ");
		hql.append("a.subtitles as subtitle, ");
		hql.append("a.country as country, ");
		hql.append("sc.description as subCategory, ");
		hql.append("a.assetId as assetId, ");
		hql.append("a.creationDate as creationDate, ");
		hql.append("a.description as description, ");
		hql.append("a.director as director, ");
		hql.append("a.actors as actors, ");
		hql.append("a.episode as episode, ");
		hql.append("a.billingID as billingID, ");
		hql.append("a.episodeName as episodeName, ");
		hql.append("a.licenseWindowEnd as licenseWindowEnd, ");
		hql.append("a.licenseWindowStart as licenseWindowStart, ");
		hql.append("a.price as price, ");
		hql.append("a.releaseYear as releaseYear, ");
		hql.append("a.season as season, ");
		hql.append("a.languages as languages, ");
		hql.append("a.assetInfo as assetInfo, ");
		hql.append("r.rating as rating, ");
		hql.append("r.adult as isAdult, ");
		hql.append("a.totalTime as totalTime, ");
		hql.append("a.product as product, ");
		hql.append("a.screenFormat as screenFormat, ");
		hql.append("a.audioType as audioType, ");
		hql.append("a.canResume as canResume, ");
		hql.append("a.isHD as isHD, ");
		hql.append("a.isRevised as isRevised, ");
		hql.append("a.fileSize as fileSize, ");
		hql.append("a.checksum as checksum, ");
		hql.append("a.bitrate as bitrate, ");
		hql.append("a.titleAlternative as titleAlternative, ");
		hql.append("at.description as assetType, ");
		hql.append("cp.providerId as contentProvider ");
		hql.append("from IpvodAsset as a left join a.ipvodCategory1 c left join a.ipvodCategory2 sc left join a.ipvodAssetType at left join a.ipvodContentProvider cp left join a.rating r ");
		hql.append("where c.categoryId = :genre order by a.title asc ");
 
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("genre", genreId);
		query.setResultTransformer(new AliasToBeanResultTransformer(OnDemandContentVO.class));

		return query.list();
		
	}
	
	public Map<String, Integer> retrieveSafeSearchTermsOnDemand() {
		String where = "where ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ " and a.license_window_start <= sysdate AND a.license_window_end >= sysdate "
				+ " and a.rating_level < 8 ";
		return retrieveSearchTerms(where );
	}
	
	public Map<String, Integer> retrieveAdultSearchTermsOnDemand() {
		String where = "where ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ " and a.license_window_start <= sysdate AND a.license_window_end >= sysdate "
				+ " and a.rating_level = 8 ";
		return retrieveSearchTerms(where );
	}
	
	public Map<String, Integer> retrieveSafeSearchTermsCatchup() {
		String where = "where ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ " and a.license_window_start <= sysdate AND a.license_window_end >= sysdate "
				+ " and a.rating_level < 8 ";
		return retrieveSearchTerms(where );
	}
	
	public Map<String, Integer> retrieveAdultSearchTermsCatchup() {
		String where = "where ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ " and a.license_window_start <= sysdate AND a.license_window_end >= sysdate "
				+ " and a.rating_level = 8 ";
		return retrieveSearchTerms(where );
	}
	
	public Map<String, Integer> retrieveSearchTerms(String where) {
		// Efetuando a consulta na base
		ScrollableResults scrollableResults = getSession().createSQLQuery(
				"SELECT title, original_title, description, director, actors from IPVOD_ASSET a "
						+ "left join IPVOD_VISUAL_MENU_ASSET_INDEX ma on (ma.ASSET_ID = A.ASSET_ID) "
						+ where)
				.setFetchSize(2000)
				.setCacheable(false).setReadOnly(true)
				.scroll(ScrollMode.FORWARD_ONLY);
		
		List<String> list= new ArrayList<String>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		// Lendo o resultSet
		while (scrollableResults.next()) {
			for (int i = 0; i < 3; i++) {
				if (scrollableResults.get(i) != null && !"".equals(scrollableResults.get(i).toString()) && !"null".equals(scrollableResults.get(i).toString())) {
					list.add(scrollableResults.get(i).toString());
					map.put(scrollableResults.get(i).toString(), 0);
				}
			} 
		}
		Map<String, Integer> mp = null;
		mp = uniqueTermsCounter(list);
		
        ValueComparator bvc =  new ValueComparator(mp);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(mp);
		return sorted_map;
	}
	
	
	private Map<String, Integer> uniqueTermsCounter(List<String> list) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		
		Object[] ObjectList = list.toArray();
		String[] StringArray = Arrays.copyOf(ObjectList, ObjectList.length,
				String[].class);

		String valores = Arrays.toString(StringArray);
		valores.replaceAll("\\s*(?:(?:t|s|temp|temporada|season|e|ep|epis[o\\u00f3]dio|episode):?\\s*\\d+)", "");
		valores = valores.trim().toLowerCase();

		String str[] = valores.split("[\\s\\P{L}]+");
		int cont = 0;

		Pattern pattern = Pattern.compile(".*[^0-9].*");
		for (int j = 0; j < str.length; j++) {

			if ((str[j].length() > 1) && pattern.matcher(str[j]).matches()){
				if (map.get(str[j]) != null) {
					cont = map.get(str[j]);
				}
				map.put(str[j],cont+1);
			}

			cont = 0;
		}
		return map;
	}

	public List<IpvodAsset> findAssetsOnDemand(List<Long> assets, long userId, boolean adult) {
		String assetWhere = " and a.asset_id in " + assets.toString().replace("[", "(").replace("]", ")") + " "
				+ "and ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) + " connect by prior m.menu_id = m2.menu_sup_id) ";
		if (adult) {
			assetWhere += " and a.rating_level = 8 ";
		} else {
			assetWhere += " and a.rating_level < 8 ";
		}
		return getMyContent(userId, assetWhere );
	}
	
	public List<IpvodAsset> findAssetsCatchUp(List<Long> assets, long userId, boolean adult) {
		String assetWhere = " and a.asset_id in " + assets.toString().replace("[", "(").replace("]", ")") + " "
				+ " and ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP) + " connect by prior m.menu_id = m2.menu_sup_id) ";
		if (adult) {
			assetWhere += " and a.rating_level = 8 ";
		} else {
			assetWhere += " and a.rating_level < 8 ";
		}
		return getMyContent(userId, assetWhere );
	}
	
	@SuppressWarnings("unchecked")
	public List<OnDemandContentVO> findHighlights() {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append("h.order as order, ");
		hql.append("a.title as title, ");
		hql.append("a.originalTitle as originalTitle, ");
		hql.append("'UNKNOWN' as genre, ");
		hql.append("c.description as category, ");
		hql.append("a.subtitles as subtitle, ");
		hql.append("a.country as country, ");
		hql.append("sc.description as subCategory, ");
		hql.append("a.assetId as assetId, ");
		hql.append("a.creationDate as creationDate, ");
		hql.append("a.description as description, ");
		hql.append("a.director as director, ");
		hql.append("a.actors as actors, ");
		hql.append("a.episode as episode, ");
		hql.append("a.billingID as billingID, ");
		hql.append("a.episodeName as episodeName, ");
		hql.append("a.licenseWindowEnd as licenseWindowEnd, ");
		hql.append("a.licenseWindowStart as licenseWindowStart, ");
		hql.append("a.price as price, ");
		hql.append("a.releaseYear as releaseYear, ");
		hql.append("a.season as season, ");
		hql.append("a.languages as languages, ");
		hql.append("a.assetInfo as assetInfo, ");
		hql.append("r.rating as rating, ");
		hql.append("r.adult as isAdult, ");
		hql.append("a.totalTime as totalTime, ");
		hql.append("a.product as product, ");
		hql.append("a.screenFormat as screenFormat, ");
		hql.append("a.audioType as audioType, ");
		hql.append("a.canResume as canResume, ");
		hql.append("a.isHD as isHD, ");
		hql.append("a.isRevised as isRevised, ");
		hql.append("a.fileSize as fileSize, ");
		hql.append("a.checksum as checksum, ");
		hql.append("a.bitrate as bitrate, ");
		hql.append("a.titleAlternative as titleAlternative, ");
		hql.append("at.description as assetType, ");
		hql.append("cp.providerId as contentProvider ");
		hql.append("from IpvodHighlight as h join h.asset a left join a.ipvodCategory1 c left join a.ipvodCategory2 sc left join a.ipvodAssetType at left join a.ipvodContentProvider cp left join a.rating r ");
		hql.append("order by order asc ");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(OnDemandContentVO.class));

		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	public List<OnDemandContentVO> findReleases() {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append("ir.order as order, ");
		hql.append("a.title as title, ");
		hql.append("a.originalTitle as originalTitle, ");
		hql.append("'UNKNOWN' as genre, ");
		hql.append("c.description as category, ");
		hql.append("a.subtitles as subtitle, ");
		hql.append("a.country as country, ");
		hql.append("sc.description as subCategory, ");
		hql.append("a.assetId as assetId, ");
		hql.append("a.creationDate as creationDate, ");
		hql.append("a.description as description, ");
		hql.append("a.director as director, ");
		hql.append("a.actors as actors, ");
		hql.append("a.episode as episode, ");
		hql.append("a.billingID as billingID, ");
		hql.append("a.episodeName as episodeName, ");
		hql.append("a.licenseWindowEnd as licenseWindowEnd, ");
		hql.append("a.licenseWindowStart as licenseWindowStart, ");
		hql.append("a.price as price, ");
		hql.append("a.releaseYear as releaseYear, ");
		hql.append("a.season as season, ");
		hql.append("a.languages as languages, ");
		hql.append("a.assetInfo as assetInfo, ");
		hql.append("r.rating as rating, ");
		hql.append("r.adult as isAdult, ");
		hql.append("a.totalTime as totalTime, ");
		hql.append("a.product as product, ");
		hql.append("a.screenFormat as screenFormat, ");
		hql.append("a.audioType as audioType, ");
		hql.append("a.canResume as canResume, ");
		hql.append("a.isHD as isHD, ");
		hql.append("a.isRevised as isRevised, ");
		hql.append("a.fileSize as fileSize, ");
		hql.append("a.checksum as checksum, ");
		hql.append("a.bitrate as bitrate, ");
		hql.append("a.titleAlternative as titleAlternative, ");
		hql.append("at.description as assetType, ");
		hql.append("cp.providerId as contentProvider ");
		hql.append("from IpvodRelease as ir join ir.asset a left join a.ipvodCategory1 c left join a.ipvodCategory2 sc left join a.ipvodAssetType at left join a.ipvodContentProvider cp left join a.rating r ");
		hql.append("order by order asc ");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(OnDemandContentVO.class));

		return query.list();
		
	}

	public List<IpvodAsset> getMyContentOnDemand(long userId) {
		String assetWhere = " and ma.menu_id in "
		+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
		+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
		+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) + " connect by prior m.menu_id = m2.menu_sup_id) "
		+ "and a.rating_level < 8 "
		+ "and p.purchase_id is not null ";

		return getMyContent(userId, assetWhere );
	}

	public List<IpvodAsset> getMyContentOnDemandAdult(long userId) {
		String assetWhere = " and ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ "and a.rating_level = 8 "
				+ "and p.purchase_id is not null ";
		return getMyContent(userId, assetWhere );
	}

	public List<IpvodAsset> getMyContentCatchUp(long userId) {
		String assetWhere = " and ma.menu_id in "
				+ "(select unique m.menu_id from IPVOD_VISUAL_MENU m "
				+ "left join IPVOD_VISUAL_MENU m2 on m.menu_sup_id = m2.menu_sup_id "
				+ "start with m.menu_id = " + PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP) + " connect by prior m.menu_id = m2.menu_sup_id) "
				+ "and p.purchase_id is not null ";
		return getMyContent(userId, assetWhere );
	}

	public List<IpvodAsset> getMyContent(long userId, String assetWhere) {
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
				+ "a.release_year  "
				+ "from IPVOD_ASSET a "
				+ "left join IPVOD_VISUAL_MENU_ASSET_INDEX ma on (ma.ASSET_ID = A.ASSET_ID) "
				+ "left join IPVOD_PURCHASE p on (p.asset_id = a.asset_id and p.equipment_id in (select equipment_id from IPVOD_EQUIPMENT where user_id = :userId) and p.valid_until > sysdate) "
				+ "left join IPVOD_MEDIA_ASSET md1 on (md1.asset_id = a.asset_id and md1.media_type_id = 1) "
				+ "left join IPVOD_MEDIA_ASSET md2 on (md2.asset_id = a.asset_id and md2.media_type_id = 2) "
				+ "left join IPVOD_MEDIA_ASSET md3 on (md3.asset_id = a.asset_id and md3.media_type_id = 3) "
				+ "left join IPVOD_RATING r on (r.rating_level = a.rating_level)"
				+ "where a.asset_id = ma.asset_id and a.license_window_end >= sysdate and a.license_window_start <= sysdate "
				+ assetWhere);

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> assets = (List<Object[]>)query.list();
		
		List<IpvodAsset> ipvodAssets = new ArrayList<IpvodAsset>();
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
				ipvodAsset.getRating().setAdult(false);
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
			ipvodAssets.add(ipvodAsset);
		}
		return ipvodAssets;
	}

	public void updateMediaAssetImage(String assetId) {
		StringBuilder hql = new StringBuilder("update IPVOD_MEDIA_ASSET set url = :imgUrl where asset_id = :assetId and media_type_id = :mediaTypeId");
		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("assetId", assetId);
		query.setParameter("mediaTypeId", 1);
		query.setParameter("imgUrl", IpvodConstants.IMAGE_SERVER_URL + assetId + ".jpg");
		query.executeUpdate();
	}
}
