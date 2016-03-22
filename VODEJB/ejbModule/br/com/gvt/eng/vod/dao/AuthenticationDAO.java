package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.vo.AuthVO;

@Stateless
public class AuthenticationDAO extends GenericDAO<IpvodAuthentication> {

	public AuthenticationDAO() {
		super(IpvodAuthentication.class);
	}

	/**
	 * @param token
	 * @param userID
	 * @return IpvodAuthentication
	 */
	public IpvodAuthentication verifyToken(String token, long userID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);
		parameters.put("user", userID);
		return super
				.findOneResult(IpvodAuthentication.VERIFY_TOKEN, parameters);
	}

	/**
	 * @param token
	 * @return IpvodAuthentication
	 */
	public IpvodAuthentication getAuthByToken(String token) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);
		return super.findOneResult(IpvodAuthentication.FIND_AUTH_BY_TOKEN,
				parameters);
	}

	/**
	 * @param userID
	 * @return IpvodAuthentication
	 */
	public IpvodAuthentication getAuthByUser(long userID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", userID);
		return super.findOneResult(IpvodAuthentication.FIND_AUTH_BY_USER,
				parameters);
	}

	/**
	 * @param token
	 * @return IpvodAuthentication
	 */
	public IpvodAuthentication verifyExpiration(String token) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);
		return super.findOneResult(IpvodAuthentication.VERIFY_AUTH_EXPIRATION,
				parameters);
	}

	/**
	 * @param token
	 * @return IpvodAuthentication
	 */
	public IpvodAuthentication getValidAuthByEquipment(Long equipmentId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("equipId", equipmentId);
		List<IpvodAuthentication> auth = super.findResultByParameter(
				IpvodAuthentication.FIND_VALID_AUTH_BY_EQUIPMENT, parameters);
		if (auth == null || auth.isEmpty()) {
			return null;
		}
		return auth.get(0);
	}

	public AuthVO getLastAuthBySerialEquipment(String serial) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("a.authenticationId as id, ");
		hql.append("a.authDate as requestDate, ");
		hql.append("a.ipAddress as ip, ");
		hql.append("a.token as token, ");
		hql.append("a.connection as connection, ");
		hql.append("e.cas as equipament, ");
		hql.append("e.authInfo as authInfo, ");
		hql.append("e.mac as mac, ");
		hql.append("e.cas as caId ");
		hql.append("from ");
		hql.append("IpvodAuthentication as a ");
		hql.append("join a.equipment as e ");
		hql.append("where e.serial = :serial ");
		hql.append("order by a.authDate desc");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("serial", serial);
		query.setFirstResult(0);
		query.setMaxResults(1);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				AuthVO.class));
		return (AuthVO) query.uniqueResult();
	}

	/**
	 * @param ipvodAuthentications
	 */
	public void delete(List<IpvodAuthentication> ipvodAuthentications) {
		for (IpvodAuthentication authentication : ipvodAuthentications) {
			delete(authentication);
		}
	}

	/**
	 * @param authentication
	 */
	public void delete(IpvodAuthentication authentication) {
		super.delete(authentication.getAuthenticationId(),
				IpvodAuthentication.class);
	}
}