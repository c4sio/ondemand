package br.com.gvt.eng.vod.dao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.it.UserVO;

@Stateless
public class UserDAO extends GenericDAO<IpvodUser> {

	public UserDAO() {
		super(IpvodUser.class);
	}

	public void deleteUser(IpvodUser ipvodUser) {
		super.delete(ipvodUser.getUserId(), IpvodUser.class);
	}

	/**
	 * @param active
	 * @return data of user
	 */
	public List<IpvodUser> findUserActive() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("active", true);

		return super.findResultByParameter(IpvodUser.FIND_BY_USER_ACTIVE,
				parameters);
	}

	/**
	 * @param subscriberID
	 * @return IpvodUser
	 */
	public IpvodUser findUserBySubscriberID(String subscriberID) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("subscriberID", subscriberID);
		IpvodUser user = null;

		List<IpvodUser> l = super.findResultByParameter(
				IpvodUser.FIND_BY_USER_BY_SUBSCRIBER_ID, parameters);

		if (l != null && !l.isEmpty()) {
			user = l.get(0);
		}

		return user;
	}

	public IpvodUser getUserByAuthInfo(String authInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("authInfo", authInfo);

		return super
				.findOneResult(IpvodUser.FIND_BY_USER_AUTH_INFO, parameters);
	}

	/**
	 * @param date
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getLogonByRegion(GregorianCalendar date) {

		String sql = "SELECT DISTINCT((SELECT i.SERVICE_REGION FROM IPVOD_USER i WHERE i.USER_ID = x.USER_ID)) REGION, "
				+ " 	(SELECT COUNT(DISTINCT(ip_a.USER_ID)) FROM IPVOD_AUTHENTICATION ip_a, IPVOD_USER ip_u WHERE ip_a.USER_ID = ip_u.USER_ID "
				+ "   AND ip_u.SERVICE_REGION = yy.SERVICE_REGION) COUNTT "
				+ "  FROM IPVOD_AUTHENTICATION x, IPVOD_USER yy "
				+ " WHERE x.USER_ID = yy.USER_ID "
				+ "   AND EXTRACT(DAY   FROM AUTH_DATE) < "
				+ (date.get(Calendar.DAY_OF_MONTH))
				+ " "
				+ "   AND EXTRACT(MONTH FROM AUTH_DATE) = "
				+ (date.get(Calendar.MONTH) + 1)
				+ " "
				+ "   AND EXTRACT(YEAR  FROM AUTH_DATE) = "
				+ (date.get(Calendar.YEAR)) + " ";

		Query x = super.getEm().createNativeQuery(sql);
		List<Object> result = x.getResultList();
		return result;
	}

	/**
	 * @param date
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTotalLogonByRegion(GregorianCalendar date) {

		String sql = "SELECT DISTINCT((SELECT i.SERVICE_REGION FROM IPVOD_USER i WHERE i.USER_ID = x.USER_ID)) REGION, "
				+ "(SELECT COUNT(ip_a.USER_ID) FROM IPVOD_AUTHENTICATION ip_a, IPVOD_USER ip_u WHERE ip_a.USER_ID = ip_u.USER_ID "
				+ "AND ip_u.SERVICE_REGION = yy.SERVICE_REGION) COUNTT "
				+ "FROM IPVOD_AUTHENTICATION x, IPVOD_USER yy "
				+ "WHERE x.USER_ID = yy.USER_ID "
				+ "  AND EXTRACT(DAY   FROM AUTH_DATE) < "
				+ (date.get(Calendar.DAY_OF_MONTH))
				+ " "
				+ "  AND EXTRACT(MONTH FROM AUTH_DATE) = "
				+ (date.get(Calendar.MONTH) + 1)
				+ " "
				+ "  AND EXTRACT(YEAR  FROM AUTH_DATE) = "
				+ (date.get(Calendar.YEAR)) + " ";

		Query x = super.getEm().createNativeQuery(sql);
		List<Object> result = x.getResultList();
		return result;
	}

	/**
	 * @param date
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTotalNewUsers(GregorianCalendar date) {

		String sql = ""
				+ "SELECT DISTINCT(SERVICE_REGION) REGION, "
				+ "       (SELECT COUNT(0) FROM IPVOD_USER i WHERE i.SERVICE_REGION = o.SERVICE_REGION) PERCENT "
				+ "  FROM IPVOD_USER o "
				+ " WHERE EXTRACT(DAY   FROM CREATED_AT) < "
				+ (date.get(Calendar.DAY_OF_MONTH)) + " "
				+ "   AND EXTRACT(MONTH FROM CREATED_AT) = "
				+ (date.get(Calendar.MONTH) + 1) + " "
				+ "   AND EXTRACT(YEAR  FROM CREATED_AT) = "
				+ (date.get(Calendar.YEAR)) + " ";

		Query x = super.getEm().createNativeQuery(sql);
		List<Object> result = x.getResultList();
		return result;
	}

	/**
	 * @param date
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getMinutesPlayedByRegion(GregorianCalendar date) {

		String sql = ""
				+ "SELECT DISTINCT((SELECT i.SERVICE_REGION FROM IPVOD_USER i WHERE i.USER_ID = eq.USER_ID)) REGION, "
				+

				"(SELECT SUM(ip_a.DURATION) FROM IPVOD_SESSION ip_a, IPVOD_USER ip_u, IPVOD_EQUIPMENT ip_q "
				+ "WHERE ip_u.USER_ID = ip_q.USER_ID "
				+ "AND ip_a.EQUIPMENT_ID = ip_q.EQUIPMENT_ID "
				+ "AND ip_a.EQUIPMENT_ID = eq.EQUIPMENT_ID "
				+ "AND ip_u.SERVICE_REGION = yy.SERVICE_REGION) COUNTT " +

				"FROM IPVOD_SESSION x, IPVOD_USER yy, IPVOD_EQUIPMENT eq "
				+ "WHERE yy.USER_ID = eq.USER_ID "
				+ "AND x.EQUIPMENT_ID = eq.EQUIPMENT_ID "
				+ "AND EXTRACT(DAY   FROM EVENT_DATE) < "
				+ (date.get(Calendar.DAY_OF_MONTH)) + " "
				+ "AND EXTRACT(MONTH FROM EVENT_DATE) = "
				+ (date.get(Calendar.MONTH) + 1) + " "
				+ "AND EXTRACT(YEAR  FROM EVENT_DATE) = "
				+ (date.get(Calendar.YEAR)) + " ";

		Query x = super.getEm().createNativeQuery(sql);
		List<Object> result = x.getResultList();
		return result;
	}

	/**
	 * @param id
	 * @return UserVO
	 */
	public UserVO findVO(Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("u.active as active, ");
		hql.append("u.crmCustomerId as crmCustomerId, ");
		hql.append("u.serviceRegion as serviceRegion, ");
		hql.append("u.authInfo as authInfo, ");
		hql.append("u.createdAt as createdAt ");
		hql.append("from ");
		hql.append("IpvodUser u ");
		hql.append("where u.userId = :userId");

		org.hibernate.Query query = getSession().createQuery(
				String.valueOf(hql));
		query.setParameter("userId", id);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				UserVO.class));

		return (UserVO) query.uniqueResult();
	}
}