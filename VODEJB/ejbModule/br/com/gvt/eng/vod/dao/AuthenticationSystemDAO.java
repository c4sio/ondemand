package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;

@Stateless
public class AuthenticationSystemDAO extends GenericDAO<IpvodAuthenticationSystem> {

	public AuthenticationSystemDAO() {
		super(IpvodAuthenticationSystem.class);
	}

	/**
	 * @param token
	 * @param userID
	 * @return
	 */
	public IpvodAuthenticationSystem verifyToken(String token, long userID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);
		parameters.put("user", userID);

		return super
				.findOneResult(IpvodAuthenticationSystem.VERIFY_TOKEN, parameters);
	}

	/**
	 * @param userID
	 * @return
	 */
	public IpvodAuthenticationSystem getAuthByUser(long userID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", userID);

		return super.findOneResult(IpvodAuthenticationSystem.FIND_AUTH_BY_USER,
				parameters);
	}

	/**
	 * @param token
	 * @return
	 */
	public IpvodAuthenticationSystem getAuthByToken(String token) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);

		return super.findOneResult(IpvodAuthenticationSystem.FIND_AUTH_BY_TOKEN,
				parameters);
	}
	/**
	 * @param token
	 * @return
	 */
	public IpvodAuthenticationSystem verifyExpiration(String token) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("token", token);

		return super.findOneResult(IpvodAuthenticationSystem.VERIFY_AUTH_EXPIRATION,
				parameters);
	}
	
	public void delete(Object id, Class<IpvodAuthenticationSystem> classe) {
		super.delete(id, classe);
	}

}
