package br.com.gvt.vod.facade.impl;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AuthenticationSystemDAO;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.vod.facade.AuthenticationSystemFacade;

@Stateless
public class AuthenticationSystemFacadeImp implements
		AuthenticationSystemFacade {

	@EJB
	private AuthenticationSystemDAO authenticationSystemDAO;

	@Override
	public IpvodAuthenticationSystem verifyToken(String token, long userID) {
		return authenticationSystemDAO.verifyToken(token, userID);
	}

	@Override
	public IpvodAuthenticationSystem findByToken(String token) {
		return authenticationSystemDAO.getAuthByToken(token);
	}

	@Override
	public IpvodAuthenticationSystem login(IpvodUserSystem user) {
		IpvodAuthenticationSystem auth = null;
		try {
			auth = authenticationSystemDAO.getAuthByUser(user.getUserSysId());
		} catch (Exception e) {
			System.err.println("NAO ACHOU O TOKEN+USER");
		}
		if (auth == null) {
			auth = new IpvodAuthenticationSystem();
			auth.setIpvodUserSys(user);
			auth.setToken(generateLoginToken(user.getUsername()));
			auth.setAuthDate(new Date());
			authenticationSystemDAO.save(auth);
			return auth;
		} else {
			auth.setToken(generateLoginToken(user.getUsername()));
			auth.setAuthDate(new Date());
			return authenticationSystemDAO.update(auth);
		}

	}

	@Override
	public void logout(IpvodAuthenticationSystem auth) {
		auth = authenticationSystemDAO.getAuthByToken(auth.getToken());

		authenticationSystemDAO.delete(auth.getAuthId(),
				IpvodAuthenticationSystem.class);
		return;
	}

	@Override
	public IpvodAuthenticationSystem verifyExpiration(String token) {
		return authenticationSystemDAO.verifyExpiration(token);
	}

	private String generateLoginToken(String userName) {
		MessageDigest md;
		StringBuffer sb = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			String timestamp = new SimpleDateFormat("dd/MM/yyyy hh:mm:SS.sssss")
					.format(new Date());
			String text = userName + "KEY-GVT" + timestamp;
			md.update(text.getBytes("UTF-8"));
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
				sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
