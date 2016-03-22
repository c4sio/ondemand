package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodTwitterAuth;
import br.com.gvt.eng.vod.model.IpvodUser;

@Stateless
public class TwitterAuthDAO extends GenericDAO<IpvodTwitterAuth> {

	public TwitterAuthDAO() {
		super(IpvodTwitterAuth.class);
	}

	public IpvodTwitterAuth findByUser(IpvodUser ipvodUser) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", ipvodUser.getUserId());
		List<IpvodTwitterAuth> list = super.findResultByParameter(IpvodTwitterAuth.FIND_BY_USER,
				parameters);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void delete(IpvodTwitterAuth ipvodTwitterAuth) {
		super.delete(ipvodTwitterAuth.getTwitterAuthId(), IpvodTwitterAuth.class);
	}
}
