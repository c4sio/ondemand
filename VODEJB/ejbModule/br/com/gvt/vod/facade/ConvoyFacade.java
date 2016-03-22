package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodConvoyData;

@Local
public interface ConvoyFacade {

	/**
	 * Salva dados na tabela @IpvodConvoyData
	 * 
	 * @param ipvodConvoyData
	 */
	public abstract void save(IpvodConvoyData ipvodConvoyData);

	/**
	 * Remove dados na tabela @IpvodConvoyData
	 * 
	 * @param ipvodConvoyData
	 */
	public abstract void delete(IpvodConvoyData ipvodConvoyData);

	/**
	 * Atualiza dados na tabela @IpvodConvoyData
	 * 
	 * @param ipvodConvoyData
	 * @return IpvodConvoyData
	 */
	public abstract IpvodConvoyData update(IpvodConvoyData ipvodConvoyData);

	/**
	 * Busca todos os registro na tabela @IpvodConvoyData
	 * 
	 * @return List<IpvodConvoyData>
	 */
	public abstract List<IpvodConvoyData> findAll();

	/**
	 * Busca registro por ID na tabela @IpvodConvoyData
	 * 
	 * @param entityID
	 * @return IpvodConvoyData
	 */
	public abstract IpvodConvoyData find(Long entityID);

	/**
	 * Busca todos os registros com mesmo IngestId na tabela @IpvodConvoyData
	 * 
	 * @param ingestId
	 * @return List<IpvodConvoyData>
	 */
	public abstract List<IpvodConvoyData> findConvoyDataByIngestId(Long ingestId);

	/**
	 * Busca registro
	 * 
	 * @return List<IpvodConvoyData>
	 */
	public abstract List<IpvodConvoyData> findAllLessDoneStatus();

}