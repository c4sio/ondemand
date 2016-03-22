package br.com.gvt.eng.vod.dao;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAppMenu;

@Stateless
public class AppMenuDAO extends GenericDAO<IpvodAppMenu> {

	public AppMenuDAO() {
		super(IpvodAppMenu.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodAppMenu
	 */
	public void deleteAppMenu(IpvodAppMenu ipvodAppMenu) {
		super.delete(ipvodAppMenu.getAppMenuId(), IpvodAppMenu.class);
	}

}
