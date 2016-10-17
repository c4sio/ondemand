package br.com.gvt.eng.convoy.rest;

import java.util.List;

import br.com.gvt.eng.convoy.vo.IpvodConvoyData;

public interface ConvoyDataRest {

	/**
	 * Salva os dados na base
	 * 
	 * @param ipvodConvoyData
	 */
	public abstract void saveConvoyData(IpvodConvoyData ipvodConvoyData);

	/**
	 * @param ipvodConvoyData
	 * @return IpvodConvoyData
	 */
	public abstract IpvodConvoyData update(IpvodConvoyData ipvodConvoyData);

	/**
	 * Remove registro da base
	 * 
	 * @param ipvodConvoyData
	 */
	public abstract void delete(Long convoyId);

	/**
	 * Busca registros dos dados enviados para o Convoy
	 * 
	 * @return List<IpvodConvoyData>
	 */
	public abstract List<IpvodConvoyData> findAllLessDone();

	/**
	 * Busca dados de convoy na base
	 * 
	 * @param entityID
	 * @return IpvodConvoyData
	 */
	public abstract IpvodConvoyData find(Long entityID);

	/**
	 * Busca dados de convoy na base com o mesmo IngestID
	 * 
	 * @param ingestID
	 * @return boolean
	 */
	public abstract boolean checkEndOfProcess(Long ingestID);

}
