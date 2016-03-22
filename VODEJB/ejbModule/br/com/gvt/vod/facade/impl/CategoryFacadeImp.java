package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.dao.CategoryDAO;
import br.com.gvt.eng.vod.model.IpvodCategory;
import br.com.gvt.vod.facade.CategoryFacade;

@Stateless
public class CategoryFacadeImp implements CategoryFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private CategoryDAO categoryDAO;
	
	@Override
	public void save(IpvodCategory ipvodCategory) {
		isCategoryWithAllData(ipvodCategory);
		categoryDAO.save(ipvodCategory);
	}

	@Override
	public void delete(IpvodCategory ipvodCategory) {
		categoryDAO.deleteCategory(ipvodCategory);
	}

	@Override
	public IpvodCategory update(IpvodCategory ipvodCategory) {
		isCategoryWithAllData(ipvodCategory);
		return categoryDAO.update(ipvodCategory);
	}

	@Override
	public List<IpvodCategory> findAll() {
		return categoryDAO.findAll();
	}

	@Override
	public IpvodCategory find(long entityID) {
		return categoryDAO.find(entityID);
	}

	@Override
	public long findCategoryByName(String categoryName) {
		return categoryDAO.findCategoryByName(categoryName);
	}

	@Override
	public List<IpvodCategory> findResultComplexQuery(UriInfo uriInfo) {
		return categoryDAO.findResultComplexQuery(uriInfo);
	}
	
	@Override
	public Long countResultComplexQuery(UriInfo uriInfo) {
		return categoryDAO.countResultComplexQuery(uriInfo);
	}
	
	/**
	 * Validação de campos - Save/Update
	 * 
	 * @param ipvodAsset
	 */
	private void isCategoryWithAllData(IpvodCategory ipvodCategory) {
		boolean hasError = false;
		if (ipvodCategory == null)
			hasError = true;
		
		if (ipvodCategory.getDescription() == null
				|| "".equals(ipvodCategory.getDescription().trim()))
			hasError = true;
		
		if (hasError)
			throw new IllegalArgumentException(
					"The category is missing data. Check, they should have value.");
	}
}
