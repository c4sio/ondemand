package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodTwitterAuth;
import br.com.gvt.eng.vod.model.IpvodUser;

@Local
public interface TwitterAuthFacade {

	public abstract IpvodTwitterAuth save(IpvodTwitterAuth ipvodTwitterAuth);

	public abstract void delete(IpvodTwitterAuth ipvodTwitterAuth);
	
	public abstract IpvodTwitterAuth get(long twitterAuthId);

	public abstract IpvodTwitterAuth getByUser(IpvodUser ipvodUser);
	
	public abstract IpvodTwitterAuth update(IpvodTwitterAuth ipvodTwitterAuth);
	
}
