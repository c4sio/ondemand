package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAppMenu;

@Local
public interface AppMenuFacade {

	/**
	 * @return IpvodAppMenu
	 */
	public abstract IpvodAppMenu findByID(Long entityID);

	/**
	 * List all Apps
	 * 
	 * @return List<IpvodAppMenu>
	 */
	public abstract List<IpvodAppMenu> findAll();

	/**
	 * @param ipvodAppMenu
	 */
	public abstract void save(IpvodAppMenu ipvodAppMenu);

	/**
	 * @param ipvodAppMenu
	 */
	public abstract void delete(IpvodAppMenu ipvodAppMenu);

	/**
	 * @param ipvodAppMenu
	 * @return IpvodAppMenu
	 */
	public abstract IpvodAppMenu update(IpvodAppMenu ipvodAppMenu);

}
