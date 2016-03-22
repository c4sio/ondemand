package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.ContentProviderDAO;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.vod.facade.ContentProviderFacade;

@Stateless
public class ContentProviderFacadeImp implements ContentProviderFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private ContentProviderDAO contentProviderDAO;

	@Override
	public void save(IpvodContentProvider ipvodContentProvider) {
		contentProviderDAO.save(ipvodContentProvider);
	}

	@Override
	public IpvodContentProvider update(IpvodContentProvider ipvodContentProvider) {
		return contentProviderDAO.update(ipvodContentProvider);
	}

	@Override
	public List<IpvodContentProvider> findAll() {
		return contentProviderDAO.findAll();
	}

	@Override
	public IpvodContentProvider find(long entityID) {
		return contentProviderDAO.find(entityID);
	}

	@Override
	public List<IpvodContentProvider> findProviders() {
		return contentProviderDAO.findProviders();
	}
	
}
