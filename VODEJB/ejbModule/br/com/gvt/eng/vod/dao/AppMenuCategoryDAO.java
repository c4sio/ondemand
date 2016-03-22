package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAppMenuCategory;

@Stateless
public class AppMenuCategoryDAO extends GenericDAO<IpvodAppMenuCategory> {

	public AppMenuCategoryDAO() {
		super(IpvodAppMenuCategory.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodAppMenuCategory
	 */
	public void deleteAppMenuCategory(IpvodAppMenuCategory ipvodAppMenuCategory) {
		super.delete(ipvodAppMenuCategory.getMenuCategoryId(),
				IpvodAppMenuCategory.class);
	}

	/**
	 * @param packageId
	 * @return List<IpvodAppMenuCategory>
	 */
	public List<IpvodAppMenuCategory> findAppMenuByPackageId(long packageId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("packageId", packageId);
		return super.findResultByParameter(
				IpvodAppMenuCategory.FIND_LIST_BY_PACKAGE_ID, parameters);
	}

	/**
	 * @param packageBillingId
	 * @return List<IpvodAppMenuCategory>
	 */
	public List<IpvodAppMenuCategory> getAppMenuDefault(String packageBillingId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("packageBillingId", packageBillingId);
		return super.findResultByParameter(
				IpvodAppMenuCategory.FIND_LIST_BY_PACKAGE_BILLING_ID,
				parameters);
	}

}
