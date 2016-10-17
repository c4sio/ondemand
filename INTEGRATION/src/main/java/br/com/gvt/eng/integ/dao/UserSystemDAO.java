package br.com.gvt.eng.integ.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Local
public interface UserSystemDAO {

	/**
	 * Busca e-mail para envio de erro na IpvodUserSystem
	 * 
	 * @param contentProviderId
	 */
	public abstract List<IpvodUserSystem> findEmail(long contentProviderId);

}
