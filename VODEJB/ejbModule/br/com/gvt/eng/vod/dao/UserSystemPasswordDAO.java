package br.com.gvt.eng.vod.dao;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodSystemPasswordRecover;

@Stateless
public class UserSystemPasswordDAO extends GenericDAO<IpvodSystemPasswordRecover> {

	public UserSystemPasswordDAO() {
		super(IpvodSystemPasswordRecover.class);
	}

	public void deletePassRecover(IpvodSystemPasswordRecover recover) {
		super.delete(recover.getPasswordRecoverCode(), IpvodSystemPasswordRecover.class);
	}

}
