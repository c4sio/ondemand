package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodEventType;

@Local
public interface EventTypeFacade {

	/**
	 * Busca o registro com o parametro eventTypeName na tabela @IpvodEventType
	 * 
	 * @param eventTypeName
	 * @return IpvodEventType
	 */
	public abstract IpvodEventType findDataByEventTypeName(String eventTypeName);

}
