package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Stateless
public class UserSystemDAO extends GenericDAO<IpvodUserSystem> {

	public UserSystemDAO() {
		super(IpvodUserSystem.class);
	}

	public void deleteUserSystem(IpvodUserSystem IpvodUserSystem) {
		super.delete(IpvodUserSystem.getUserSysId(), IpvodUserSystem.class);
	}

	public IpvodUserSystem login(String username) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", username);
		return super.findOneResult(IpvodUserSystem.GET_USER_BY_USERNAME,
				parameters);
	}

	public IpvodUserSystem getUserByEmail(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		return super.findOneResult(IpvodUserSystem.GET_USER_BY_EMAIL,
				parameters);
	}

	public IpvodUserSystem getUserByPasswordRecoverCode(String pwRecId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("passRecover", pwRecId);
		return super
				.findOneResult(IpvodUserSystem.RECOVER_PASSWORD, parameters);
	}

	public List<IpvodUserSystem> findByRole(String role) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("role", role);
		return super.findResultByParameter(IpvodUserSystem.GET_USER_BY_ROLE,
				parameters);
	}

	/**
	 * Lista de usuarios do sistema pelo contentProviderId
	 * 
	 * @param ipvodContentProvider
	 * @return List<IpvodUserSystem>
	 */
	public List<IpvodUserSystem> findByContentProvider(
			IpvodContentProvider ipvodContentProvider) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("providerId",
				ipvodContentProvider.getContentProviderId());
		return super.findResultByParameter(
				IpvodUserSystem.GET_USER_BY_CONTENT_PROVIDER, parameters);
	}
}
