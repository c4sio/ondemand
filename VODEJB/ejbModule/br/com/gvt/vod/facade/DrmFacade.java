package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodDrmData;

@Local
public interface DrmFacade {

	public abstract void save(IpvodDrmData ipvodDrmData);

	public abstract void delete(IpvodDrmData ipvodDrmData);

	public abstract IpvodDrmData update(IpvodDrmData ipvodDrmData);

	public abstract List<IpvodDrmData> findAll();

	public abstract IpvodDrmData find(Long entityID);

	/**
	 * Busca todos os registros que nao foram finalizados
	 * 
	 * @return List<IpvodDrmData>
	 */
	public abstract List<IpvodDrmData> findAllLessCompletedStatus();

	/**
	 * Busca todos os registros com mesmo IngestId na tabela @IpvodDrmData
	 * 
	 * @param ingestId
	 * @return List<IpvodDrmData>
	 */
	public abstract List<IpvodDrmData> findDrmDataByIngestId(Long ingestId);

}
