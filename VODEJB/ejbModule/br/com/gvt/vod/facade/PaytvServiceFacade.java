package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.vo.AppProductVO;

@Local
public interface PaytvServiceFacade {

	/**
	 * @param urlService
	 * @param SiebelOrder
	 * @return AppProductVO
	 */
	public abstract AppProductVO findPackageHybridByService(String urlService,
			String SiebelOrder);

}
