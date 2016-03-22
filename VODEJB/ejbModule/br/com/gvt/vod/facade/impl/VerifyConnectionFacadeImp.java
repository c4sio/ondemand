package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.VerifyConnectionDAO;
import br.com.gvt.vod.facade.VerifyConnectionFacade;

@Stateless
public class VerifyConnectionFacadeImp implements VerifyConnectionFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private VerifyConnectionDAO verifyConnectionDAO;

	@Override
	public String verifyConnection() {
		return verifyConnectionDAO.verifyConnection();
	}

}
