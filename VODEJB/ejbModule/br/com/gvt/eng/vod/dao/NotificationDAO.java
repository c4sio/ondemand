package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodNotification;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Stateless
public class NotificationDAO extends GenericDAO<IpvodNotification> {

	public NotificationDAO() {
		super(IpvodNotification.class);
	}

	public void delete(IpvodNotification ipvodNotification) {
		super.delete(ipvodNotification.getNotificationId(), IpvodNotification.class);
	}

	public List<IpvodNotification> findByContentProvider(IpvodUserSystem ipvodUserSystem) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("providerId", ipvodUserSystem.getContentProvider().getContentProviderId());

		return super.findResultByParameter(IpvodNotification.FIND_BY_CONTENT_PROVIDER,
				parameters);
	}
	
	public List<IpvodNotification> findByRoleGroup(String role) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("role", role);

		return super.findResultByParameter(IpvodNotification.FIND_BY_ROLE_GROUP,
				parameters);
	
	}
}
