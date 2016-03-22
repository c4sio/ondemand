package br.com.gvt.vod.facade;

import java.util.List;

import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodSystemPasswordRecover;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

public interface UserSystemFacade {

	public IpvodUserSystem login(String username, String password);

	public IpvodUserSystem getUserByEmail(String email);

	public List<IpvodUserSystem> findByRole(String role);

	/**
	 * Lista de usuarios do sistema pelo contentProviderId
	 * 
	 * @param ipvodContentProvider
	 * @return List<IpvodUserSystem>
	 */
	public List<IpvodUserSystem> findByContentProvider(
			IpvodContentProvider ipvodContentProvider);

	public abstract void save(IpvodUserSystem ipvodUserSystem);

	public abstract void delete(IpvodUserSystem ipvodUserSystem);

	public abstract IpvodUserSystem update(IpvodUserSystem ipvodUserSystem);

	public abstract List<IpvodUserSystem> findAll();

	public abstract IpvodUserSystem find(long entityID);

	public IpvodUserSystem getUserByPassswordRecoverCode(String pwRecId);

	public void savePasswordRecoverCode(
			IpvodSystemPasswordRecover ipvodSystemPasswordRecover);

	public abstract void deletePasswordRecoverCode(
			IpvodSystemPasswordRecover ipvodSystemPasswordRecover);
}
