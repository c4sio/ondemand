package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAppCategory;

@Local
public interface AppFacade {

	/**
	 * @return IpvodAppCategory
	 */
	public abstract IpvodAppCategory find(Long entityID);

	/**
	 * List all Apps
	 * 
	 * @return List<IpvodAppCategory>
	 */
	public abstract List<IpvodAppCategory> findAll();

	/**
	 * List all Apps for equipament
	 * 
	 * @param caId
	 * @return List<IpvodAppCategory>
	 */
	public abstract List<IpvodAppCategory> findAppsByValue(String keyValue);

}
