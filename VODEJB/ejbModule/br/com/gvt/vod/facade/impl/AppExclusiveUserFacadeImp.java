package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AppExclusiveUserDAO;
import br.com.gvt.eng.vod.model.IpvodAppExclusiveUser;
import br.com.gvt.vod.facade.AppExclusiveUserFacade;

@Stateless
public class AppExclusiveUserFacadeImp implements AppExclusiveUserFacade {

	@EJB
	private AppExclusiveUserDAO appExclusiveUserDAO;

	@Override
	public IpvodAppExclusiveUser findByID(Long entityID) {
		return this.appExclusiveUserDAO.find(entityID);
	}

	@Override
	public List<IpvodAppExclusiveUser> findAll() {
		return this.appExclusiveUserDAO.findAll();
	}

	@Override
	public IpvodAppExclusiveUser findByKeyValue(String keyValue) {
		return appExclusiveUserDAO.getExclusiveUserByKeyValue(keyValue);
	}

}
