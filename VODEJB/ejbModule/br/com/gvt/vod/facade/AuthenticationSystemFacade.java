package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Local
public interface AuthenticationSystemFacade {

	public IpvodAuthenticationSystem verifyToken(String token, long userID);
	
	public IpvodAuthenticationSystem findByToken(String token);
	
	public IpvodAuthenticationSystem login(IpvodUserSystem user);

	public void logout(IpvodAuthenticationSystem ipvodUserSystem);

	public IpvodAuthenticationSystem verifyExpiration(String token);

}
