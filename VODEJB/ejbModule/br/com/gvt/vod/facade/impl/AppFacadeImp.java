package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AppDAO;
import br.com.gvt.eng.vod.model.IpvodAppCategory;
import br.com.gvt.vod.facade.AppFacade;

@Stateless
public class AppFacadeImp implements AppFacade {

	@EJB
	private AppDAO appDAO;

	@Override
	public IpvodAppCategory find(Long entityID) {
		IpvodAppCategory ipvodAppCategory = new IpvodAppCategory();
		ipvodAppCategory = appDAO.find(entityID);
		// Evita o Lazy Exception
		if (ipvodAppCategory != null) {
			ipvodAppCategory.getIpvodAppInfos().size();
		}
		return ipvodAppCategory;
	}

	@Override
	public List<IpvodAppCategory> findAll() {
		List<IpvodAppCategory> listApps = new ArrayList<IpvodAppCategory>();
		listApps = appDAO.findAll();
		// Evita o Lazy Exception
		for (IpvodAppCategory ipvodAppCategory : listApps) {
			ipvodAppCategory.getIpvodAppInfos().size();
		}
		return listApps;
	}

	@Override
	public List<IpvodAppCategory> findAppsByValue(String keyValue) {
		List<IpvodAppCategory> listApps = new ArrayList<IpvodAppCategory>();
		listApps = appDAO.findAppsByValue(keyValue);
		return listApps;
	}

}
