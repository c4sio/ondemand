package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodContentProvider;

@Local
public interface ContentProviderFacade {

	public abstract void save(IpvodContentProvider ipvodContentProvider);

	public abstract IpvodContentProvider update(IpvodContentProvider ipvodContentProvider);

	public abstract List<IpvodContentProvider> findAll();
	
	public abstract List<IpvodContentProvider> findProviders();

	public abstract IpvodContentProvider find(long entityID);

}
