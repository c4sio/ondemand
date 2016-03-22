package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodReason;

@Local
public interface ReasonFacade {

	/**
	 * Busca o registro com o parametro reasonCode na tabela @IpvodReason
	 * 
	 * @param reasonCode
	 * @return IpvodReason
	 */
	public abstract IpvodReason findDataByReasonCode(String reasonCode);

}
