package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAppMenuCategory;

@Local
public interface AppMenuCategoryFacade {

	/**
	 * @return IpvodAppMenuCategory
	 */
	public abstract IpvodAppMenuCategory findByID(Long entityID);

	/**
	 * List all Apps
	 * 
	 * @return List<IpvodAppMenuCategory>
	 */
	public abstract List<IpvodAppMenuCategory> findAll();

	/**
	 * List all Apps for user
	 * 
	 * @param caId
	 * @return List<IpvodAppMenuCategory>
	 */
	public abstract List<IpvodAppMenuCategory> findMenuAppsDthByCaId(String caId);

	/**
	 * @param listPackadgeIds
	 * @return List<IpvodAppMenuCategory>
	 */
	public abstract List<IpvodAppMenuCategory> findMenuAppsHybridByPackage(
			List<String> listPackadgeIds);

	/**
	 * @param packageValue
	 * @return List<IpvodAppMenuCategory>
	 */
	public abstract List<IpvodAppMenuCategory> findMenuAppsHybridByPackage(
			String packageValue);

	/**
	 * @param ipvodAppMenuCategory
	 */
	public abstract void save(IpvodAppMenuCategory ipvodAppMenuCategory);

	/**
	 * @param ipvodAppMenuCategory
	 */
	public abstract void delete(IpvodAppMenuCategory ipvodAppMenuCategory);

	/**
	 * @param ipvodAppMenu
	 * @return IpvodAppMenuCategory
	 */
	public abstract IpvodAppMenuCategory update(
			IpvodAppMenuCategory ipvodAppMenuCategory);

}
