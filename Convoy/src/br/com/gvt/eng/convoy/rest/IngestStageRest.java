package br.com.gvt.eng.convoy.rest;

import java.util.List;

import br.com.gvt.eng.convoy.vo.IpvodIngestStage;

public interface IngestStageRest {

	/**
	 * Busca dados na tabela IPVOD_INGEST_STAGE que estao com status diferente
	 * de sucesso
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodIngestStage> searchDataForProcess(long stageId);

	/**
	 * Atualiza dados na tabela IPVOD_INGEST_STAGE
	 * 
	 * @param ipvodIngestStage
	 */
	public abstract void updateIngest(IpvodIngestStage ipvodIngestStage);

	/**
	 * Busca dados na tabela IPVOD_INGEST_STAGE
	 * 
	 * @param ingestId
	 * 
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findIngestById(long ingestId);

}
