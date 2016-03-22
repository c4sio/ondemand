package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodCategory;

@Stateless
public class CategoryDAO extends GenericDAO<IpvodCategory> {

	public CategoryDAO() {
		super(IpvodCategory.class);
	}

	public void deleteCategory(IpvodCategory ipvodCategory) {
		super.delete(ipvodCategory.getCategoryId(), IpvodCategory.class);
	}

	/**
	 * @param assetTypeName
	 * @return
	 */
	public long findCategoryByName(String categoryName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("description", categoryName);

		IpvodCategory ipvodCategory = super.findOneResult(
				IpvodCategory.FIND_BY_DESCRIPTION, parameters);

		return ipvodCategory.getCategoryId();
	}

}
