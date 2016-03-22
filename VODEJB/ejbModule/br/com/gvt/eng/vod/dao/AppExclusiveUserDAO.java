package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAppExclusiveUser;

@Stateless
public class AppExclusiveUserDAO extends GenericDAO<IpvodAppExclusiveUser> {

	public AppExclusiveUserDAO() {
		super(IpvodAppExclusiveUser.class);
	}

	/**
	 * @param keyUser
	 * @return IpvodAppExclusiveUser
	 */
	public IpvodAppExclusiveUser getExclusiveUserByKeyValue(String keyUser) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyUser", keyUser);
		return super.findOneResult(
				IpvodAppExclusiveUser.FIND_DATA_BY_KEY_VALUE, parameters);
	}

}
