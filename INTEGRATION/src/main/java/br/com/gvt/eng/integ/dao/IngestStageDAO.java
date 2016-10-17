package br.com.gvt.eng.integ.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodIngestStage;

@Local
public interface IngestStageDAO {

	/**
	 * Busca dados na tabela @IpvodIngestStage que estao com status igual a
	 * ENCODING (4)
	 * 
	 * @return List<IpvodIngestStage>
	 */
	public abstract List<IpvodIngestStage> findFilesToExecute();

	/**
	 * Atualiza os dados na tabela @IpvodIngestStage
	 * 
	 * @param ipvodIngestStage
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage update(IpvodIngestStage ipvodIngestStage);

	/**
	 * Busca os dados na tabela @IpvodIngestStage para atualizacao do status
	 * 
	 * @param long ingestId
	 * @return IpvodIngestStage
	 */
	public abstract boolean findByIdAndUpdateStage(long ingestId);

	/**
	 * Busca os dados na tabela @IpvodIngestStage
	 * 
	 * @param long ingestId
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findById(long ingestId);

	/**
	 * Busca os dados na tabela @IpvodIngestStage e altera o status para erro
	 * 
	 * @param ingestId
	 */
	public abstract void updateStagePresetError(long ingestId);

}
