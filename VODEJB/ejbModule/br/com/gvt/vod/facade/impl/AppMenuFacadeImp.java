package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AppMenuDAO;
import br.com.gvt.eng.vod.model.IpvodAppMenu;
import br.com.gvt.vod.facade.AppMenuFacade;

@Stateless
public class AppMenuFacadeImp implements AppMenuFacade {

	@EJB
	private AppMenuDAO appMenuDAO;

	@Override
	public IpvodAppMenu findByID(Long entityID) {
		IpvodAppMenu ipvodAppMenu = new IpvodAppMenu();
		ipvodAppMenu = this.appMenuDAO.find(entityID);
		return ipvodAppMenu;
	}

	@Override
	public List<IpvodAppMenu> findAll() {
		List<IpvodAppMenu> listApps = new ArrayList<IpvodAppMenu>();
		listApps = this.appMenuDAO.findAll();
		return listApps;
	}

	@Override
	public void save(IpvodAppMenu ipvodAppMenu) {
		this.appMenuDAO.save(ipvodAppMenu);
	}

	@Override
	public void delete(IpvodAppMenu ipvodAppMenu) {
		this.appMenuDAO.deleteAppMenu(ipvodAppMenu);
	}

	@Override
	public IpvodAppMenu update(IpvodAppMenu ipvodAppMenu) {
		return this.appMenuDAO.update(ipvodAppMenu);
	}

}
