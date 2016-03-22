package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodAppExclusiveUser;

@Local
public interface AppExclusiveUserFacade {

	/**
	 * @return IpvodAppExclusiveUser
	 */
	public abstract IpvodAppExclusiveUser findByID(Long entityID);

	/**
	 * List all Apps
	 * 
	 * @return List<IpvodAppExclusiveUser>
	 */
	public abstract List<IpvodAppExclusiveUser> findAll();

	/**
	 * @param keyValue
	 * @return IpvodAppExclusiveUser
	 */
	public abstract IpvodAppExclusiveUser findByKeyValue(String keyValue);

}
