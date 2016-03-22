package br.com.gvt.eng.vod.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Query;

import br.com.gvt.eng.vod.model.IpvodAppCategory;
import br.com.gvt.eng.vod.model.IpvodAppImage;
import br.com.gvt.eng.vod.model.IpvodAppInfo;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodEquipmentType;
import br.com.gvt.eng.vod.util.Utils;

@Stateless
public class AppDAO extends GenericDAO<IpvodAppCategory> {

	@EJB
	EquipmentDAO equipmentDAO;

	public AppDAO() {
		super(IpvodAppCategory.class);
	}

	@SuppressWarnings("unchecked")
	public List<IpvodAppCategory> findAppsByValue(String keyValue) {

		String patternString = ".*TV-.*"; // ".*TV-GVT-.*"
		Pattern pattern = Pattern.compile(patternString,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(keyValue);
		boolean matches = matcher.matches();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("	G.NAME, A.APP_INFO_ID, A.APP_NAME, B.ICON, B.ICON_SELECTED, NVL(A.MODULE_ID, ' ') MODULE_ID, ");
		hql.append("	NVL(A.FIT_ADDRESS, ' ') FIT_ADDRESS, A.APP_ORDER, A.APP_VERSION, A.APP_ACTIVE, E.EQUIPMENT_TYPE_ID, E.DESCRIPTION ");
		hql.append("FROM IPVOD_APP_INFO A ");
		hql.append("	INNER JOIN IPVOD_APP_IMAGE B ON B.APP_IMAGE_ID = A.APP_IMAGE_ID ");

		if (matches) {
			hql.append("	INNER JOIN IPVOD_APP_EQUIP_HYBRID C ON C.APP_INFO_ID = A.APP_INFO_ID ");
			hql.append("	INNER JOIN IPVOD_EQUIPMENT_HYBRID D ON D.EQUIPMENT_HYBRID_ID = C.EQUIPMENT_HYBRID_ID ");
		} else {
			hql.append("	INNER JOIN IPVOD_APP_EQUIP C ON C.APP_INFO_ID = A.APP_INFO_ID ");
			hql.append("	INNER JOIN IPVOD_EQUIPMENT D ON D.EQUIPMENT_ID = C.EQUIPMENT_ID ");
		}

		hql.append("	INNER JOIN IPVOD_EQUIPMENT_TYPE E ON E.EQUIPMENT_TYPE_ID = A.EQUIPMENT_TYPE_ID ");
		hql.append("	INNER JOIN IPVOD_APP_CAT_INFO F ON F.APP_INFO_ID = A.APP_INFO_ID ");
		hql.append("	INNER JOIN IPVOD_APP_CATEGORY G ON G.APP_CAT_ID = F.APP_CAT_ID ");

		if (matches) {
			// Se STB for Hibrido
			hql.append("WHERE D.CRM_CUSTOMER_ID =:keyValue ");
		} else {
			// Se STB for DTH
			hql.append("WHERE D.CA_ID =:keyValue ");
		}

		hql.append("	AND A.APP_ACTIVE = 1 ");
		hql.append("ORDER BY G.NAME, A.APP_ORDER");

		Query query = getSession().createSQLQuery(String.valueOf(hql));
		query.setParameter("keyValue", keyValue);

		List<Object[]> dataValue = (List<Object[]>) query.list();

		List<IpvodAppCategory> listAppCategorys = new ArrayList<IpvodAppCategory>();

		// Se encontrou algum registro
		if (!dataValue.isEmpty()) {
			List<IpvodAppInfo> listAppInfos = new ArrayList<IpvodAppInfo>();
			IpvodAppCategory ipvodAppCategory = null;
			IpvodAppInfo ipvodAppInfo = null;
			IpvodAppImage ipvodAppImage = null;
			IpvodEquipmentType ipvodEquipmentType = null;

			Set<Object> set = new HashSet<>();
			for (Object[] values : dataValue) {
				set.add(values[0]);
			}

			for (Object object : set) {
				ipvodAppCategory = new IpvodAppCategory();
				ipvodAppCategory.setName(object.toString());

				for (Iterator<Object[]> iter = dataValue.iterator(); iter
						.hasNext();) {
					Object[] values = iter.next();
					if (object.toString()
							.equalsIgnoreCase(values[0].toString())) {
						if (values[1] != null) {
							ipvodAppInfo = new IpvodAppInfo();
							ipvodAppInfo.setId(new Long(values[1].toString()));
							ipvodAppInfo.setAppName(values[2].toString());

							ipvodAppImage = new IpvodAppImage();
							ipvodAppImage.setIcon(values[3].toString());
							ipvodAppImage.setIconSelected(values[4].toString());
							ipvodAppInfo.setIpvodAppImage(ipvodAppImage);

							if (!Utils.isEmptyOrNull(values[5].toString())) {
								ipvodAppInfo.setModuleId(values[5].toString());
							}

							if (!Utils.isEmptyOrNull(values[6].toString())) {
								ipvodAppInfo
										.setFitAddress(values[6].toString());
							}

							ipvodAppInfo
									.setOrder(new Long(values[7].toString()));
							ipvodAppInfo.setVersion(new Long(values[8]
									.toString()));
							ipvodAppInfo.setActive(new Long(values[9]
									.toString()) == 1 ? true : false);

							ipvodEquipmentType = new IpvodEquipmentType();
							ipvodEquipmentType.setEquipmentTypeId(new Long(
									values[10].toString()));
							ipvodEquipmentType.setDescription(values[11]
									.toString());
							ipvodAppInfo
									.setIpvodEquipmentType(ipvodEquipmentType);

							listAppInfos.add(ipvodAppInfo);
							iter.remove();
						}
					}
				}
				ipvodAppCategory.setIpvodAppInfos(listAppInfos);
				listAppCategorys.add(ipvodAppCategory);
				listAppInfos = new ArrayList<IpvodAppInfo>();
			}
		} else {
			// Busca menu APP default - 6 Hybrid, 1 DTH
			listAppCategorys = matches == true ? listAppByParameters(6)
					: verifyTypeEquipment(keyValue);
		}

		// Orderna o menu
		Collections.sort(listAppCategorys, new Comparator<IpvodAppCategory>() {
			public int compare(IpvodAppCategory appCat, IpvodAppCategory appCate) {
				return appCat.getName().compareTo(appCate.getName());
			}
		});

		return listAppCategorys;
	}

	/**
	 * @param keyValue
	 * @return List<IpvodAppCategory>
	 */
	private List<IpvodAppCategory> verifyTypeEquipment(String keyValue) {
		long typeEquipment = 0;
		try {
			IpvodEquipment ipvodEquipment = equipmentDAO
					.findEquipmentByCAId(keyValue);

			if (ipvodEquipment == null) {
				typeEquipment = 1;
			} else {
				typeEquipment = ipvodEquipment.getIpvodEquipmentType()
						.getEquipmentTypeId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppByParameters(typeEquipment);
	}

	/**
	 * @param equipmentType
	 * @return List<IpvodAppCategory>
	 */
	@SuppressWarnings("unchecked")
	public List<IpvodAppCategory> listAppByParameters(long equipmentType) {
		List<IpvodAppCategory> listAppCategorys = new ArrayList<IpvodAppCategory>();
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ");
			hql.append("		cat.NAME, info.APP_INFO_ID, info.APP_NAME, img.ICON, img.ICON_SELECTED, info.MODULE_ID, ");
			hql.append("		info.FIT_ADDRESS, info.APP_ORDER, info.APP_VERSION, info.APP_ACTIVE ");
			hql.append("	FROM IPVOD_APP_CATEGORY cat ");
			hql.append("		INNER JOIN IPVOD_APP_CAT_INFO catinfo ON catinfo.APP_CAT_ID = cat.APP_CAT_ID ");
			hql.append("		INNER JOIN IPVOD_APP_INFO info ON info.APP_INFO_ID = catinfo.APP_INFO_ID ");
			hql.append("		INNER JOIN IPVOD_APP_IMAGE img ON img.APP_IMAGE_ID = info.APP_IMAGE_ID ");
			hql.append("	WHERE info.EQUIPMENT_TYPE_ID =:equipmentType ");
			hql.append("		AND info.APP_ACTIVE = 1 ");
			hql.append("		AND info.APP_DEFAULT = 1 ");
			hql.append(" ORDER BY cat.NAME, info.APP_ORDER ");

			Query query = getSession().createSQLQuery(String.valueOf(hql));
			query.setParameter("equipmentType", equipmentType);

			List<Object[]> dataValue = (List<Object[]>) query.list();

			// Se encontrou algum registro
			if (!dataValue.isEmpty()) {
				List<IpvodAppInfo> listAppInfos = new ArrayList<IpvodAppInfo>();
				IpvodAppCategory ipvodAppCategory = null;
				IpvodAppInfo ipvodAppInfo = null;
				IpvodAppImage ipvodAppImage = null;

				Set<Object> set = new HashSet<>();
				for (Object[] values : dataValue) {
					set.add(values[0]);
				}

				for (Object object : set) {
					ipvodAppCategory = new IpvodAppCategory();
					ipvodAppCategory.setName(object.toString());

					for (Iterator<Object[]> iter = dataValue.iterator(); iter
							.hasNext();) {
						Object[] values = iter.next();
						if (object.toString().equalsIgnoreCase(
								values[0].toString())) {
							if (values[1] != null) {
								ipvodAppInfo = new IpvodAppInfo();
								ipvodAppInfo.setId(new Long(values[1]
										.toString()));
								ipvodAppInfo.setAppName(values[2].toString());

								ipvodAppImage = new IpvodAppImage();
								ipvodAppImage.setIcon(values[3].toString());
								ipvodAppImage.setIconSelected(values[4]
										.toString());
								ipvodAppInfo.setIpvodAppImage(ipvodAppImage);

								if (!Utils.isEmptyOrNull(values[5].toString())) {
									ipvodAppInfo.setModuleId(values[5]
											.toString());
								}

								if (!Utils.isEmptyOrNull(values[6].toString())) {
									ipvodAppInfo.setFitAddress(values[6]
											.toString());
								}

								ipvodAppInfo.setOrder(new Long(values[7]
										.toString()));
								ipvodAppInfo.setVersion(new Long(values[8]
										.toString()));
								ipvodAppInfo.setActive(new Long(values[9]
										.toString()) == 1 ? true : false);

								ipvodAppInfo
										.setIpvodEquipmentType(new IpvodEquipmentType());

								listAppInfos.add(ipvodAppInfo);
								iter.remove();
							}
						}
					}
					ipvodAppCategory.setIpvodAppInfos(listAppInfos);
					listAppCategorys.add(ipvodAppCategory);
					listAppInfos = new ArrayList<IpvodAppInfo>();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listAppCategorys;
	}
}