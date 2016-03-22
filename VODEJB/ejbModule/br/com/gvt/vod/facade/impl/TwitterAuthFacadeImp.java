package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.TwitterAuthDAO;
import br.com.gvt.eng.vod.model.IpvodTwitterAuth;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.vod.facade.TwitterAuthFacade;

@Stateless
public class TwitterAuthFacadeImp implements TwitterAuthFacade {

	@EJB
	private TwitterAuthDAO twitterAuthDAO;
	
	@Override
	public IpvodTwitterAuth save(IpvodTwitterAuth ipvodTwitterAuth) {
		twitterAuthDAO.save(ipvodTwitterAuth);
		return ipvodTwitterAuth;
	}

	@Override
	public void delete(IpvodTwitterAuth ipvodTwitterAuth) {
		twitterAuthDAO.delete(ipvodTwitterAuth);
	}

	@Override
	public IpvodTwitterAuth get(long twitterAuthId) {
		return twitterAuthDAO.find(twitterAuthId);
	}

	@Override
	public IpvodTwitterAuth getByUser(IpvodUser ipvodUser) {
		return twitterAuthDAO.findByUser(ipvodUser);
	}

	@Override
	public IpvodTwitterAuth update(IpvodTwitterAuth ipvodTwitterAuth) {
		twitterAuthDAO.update(ipvodTwitterAuth);
		return ipvodTwitterAuth;
	}

}