package br.com.gvt.vod.facade.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.dao.UserSystemDAO;
import br.com.gvt.eng.vod.dao.UserSystemPasswordDAO;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodSystemPasswordRecover;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.vod.facade.UserSystemFacade;

@Stateless
public class UserSystemFacadeImp implements UserSystemFacade {

	@EJB
	private UserSystemDAO userSystemDAO;

	@EJB
	private UserSystemPasswordDAO systemPasswordDAO;

	@Override
	public IpvodUserSystem login(String username, String password) {

		IpvodUserSystem userSystem = userSystemDAO.login(username);

		if (userSystem == null) {
			throw new IllegalArgumentException("Erro: username incorreto!");
		}

		if (userSystem.getContentProvider() != null) {
			IpvodContentProvider cp = new IpvodContentProvider();
			cp.setContentProviderId(userSystem.getContentProvider()
					.getContentProviderId());
			cp.setProviderId(userSystem.getContentProvider().getProviderId());
			cp.setProviderName(userSystem.getContentProvider()
					.getProviderName());
			cp.setProviderRating(userSystem.getContentProvider()
					.getProviderRating());
			userSystem.setContentProvider(cp);
		}

		if (!userSystem.isActive()) {
			throw new IllegalArgumentException("Erro: usuario desativado!");
		}
		// USUARIOS GVT -> ADM/MKT/VOC
		if (userSystem.getRole().equals(IpvodConstants.ROLE_MARKETING)
				|| userSystem.getRole().equals(IpvodConstants.ROLE_VOC)) {
			// if(!Ldap.authenticate(username, password)) {
			// throw new IllegalArgumentException("Senha inválida");
			// }
		} else if (userSystem.getRole().equals(IpvodConstants.ROLE_ADMIN)
				|| userSystem.getRole().equals(IpvodConstants.ROLE_OPERADORA)) {
			if (!userSystem.getPassword().equals(encryptMd5(password))) {
				throw new IllegalArgumentException("Senha inválida");
			}

		} else {
			throw new IllegalArgumentException("Erro: nenhum role cadastrado!");
		}

		return userSystem;
	}

	@Override
	public void save(IpvodUserSystem ipvodUserSystem) {
		if (ipvodUserSystem.getPassword() != null && !"".equals(ipvodUserSystem.getPassword())) {
			ipvodUserSystem.setPassword(encryptMd5(ipvodUserSystem
					.getPassword()));
		}
		userSystemDAO.save(ipvodUserSystem);
	}

	@Override
	public void delete(IpvodUserSystem ipvodUserSystem) {
		userSystemDAO.deleteUserSystem(ipvodUserSystem);
	}

	@Override
	public IpvodUserSystem update(IpvodUserSystem ipvodUserSystem) {
		if (ipvodUserSystem.getPassword() != null && !"".equals(ipvodUserSystem.getPassword())) {
			ipvodUserSystem.setPassword(encryptMd5(ipvodUserSystem
					.getPassword()));
		}
		return userSystemDAO.update(ipvodUserSystem);
	}

	@Override
	public List<IpvodUserSystem> findAll() {
		return userSystemDAO.findAll();
	}

	@Override
	public IpvodUserSystem find(long entityID) {
		return userSystemDAO.find(entityID);
	}

	@Override
	public IpvodUserSystem getUserByEmail(String email) {
		return userSystemDAO.getUserByEmail(email);
	}

	@Override
	public IpvodUserSystem getUserByPassswordRecoverCode(String pwRecId) {
		return userSystemDAO.getUserByPasswordRecoverCode(pwRecId);
	}

	public void savePasswordRecoverCode(IpvodSystemPasswordRecover pwdRecover) {
		systemPasswordDAO.save(pwdRecover);
	}

	@Override
	public void deletePasswordRecoverCode(
			IpvodSystemPasswordRecover ipvodSystemPasswordRecover) {
		systemPasswordDAO.deletePassRecover(ipvodSystemPasswordRecover);
	}

	/**
	 * @param value
	 * @return
	 */
	private static String encryptMd5(String value) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(value.getBytes(), 0, value.length());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

	@Override
	public List<IpvodUserSystem> findByRole(String role) {
		return userSystemDAO.findByRole(role);
	}

	@Override
	public List<IpvodUserSystem> findByContentProvider(
			IpvodContentProvider ipvodContentProvider) {
		return userSystemDAO.findByContentProvider(ipvodContentProvider);
	}

}
