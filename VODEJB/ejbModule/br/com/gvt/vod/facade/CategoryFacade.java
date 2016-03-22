package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.model.IpvodCategory;

@Local
public interface CategoryFacade {

	public abstract void save(IpvodCategory ipvodCategory);

	public abstract void delete(IpvodCategory ipvodCategory);

	public abstract IpvodCategory update(IpvodCategory ipvodCategory);

	public abstract List<IpvodCategory> findAll();

	public abstract IpvodCategory find(long entityID);

	public long findCategoryByName(String categoryName);

	public List<IpvodCategory> findResultComplexQuery(UriInfo uriInfo);

	public abstract Long countResultComplexQuery(UriInfo uriInfo);
	
}