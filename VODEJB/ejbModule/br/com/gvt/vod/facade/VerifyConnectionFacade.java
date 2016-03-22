package br.com.gvt.vod.facade;

import javax.ejb.Local;

@Local
public interface VerifyConnectionFacade {

	public abstract String verifyConnection();

}
