package br.com.gvt.eng.vod.converter;

import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodUser;

public class AuthenticationConverter {

	public IpvodAuthentication toAuthentication(
			IpvodAuthentication dataAuthentication) {
		IpvodAuthentication ipvodAuthentication = null;
		try {
			ipvodAuthentication = new IpvodAuthentication();
			ipvodAuthentication.setAuthenticationId(dataAuthentication
					.getAuthenticationId());
			ipvodAuthentication.setAuthDate(dataAuthentication.getAuthDate());
			ipvodAuthentication.setIpAddress(dataAuthentication.getIpAddress());
			// ipvodAuthentication.setToken(dataAuthentication.getToken());

			if (dataAuthentication.getIpvodUser() != null) {
				IpvodUser ipvodUser = new IpvodUser();
				// ipvodUser.setUserId(dataAuthentication.getIpvodUser()
				// .getUserId());
				ipvodUser.setCrmCustomerId(dataAuthentication.getIpvodUser()
						.getCrmCustomerId());
				ipvodUser.setActive(dataAuthentication.getIpvodUser()
						.getActive());
				ipvodAuthentication.setIpvodUser(ipvodUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodAuthentication;
	}

}
