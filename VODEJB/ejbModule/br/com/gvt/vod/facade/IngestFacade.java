package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.vo.IngestVO;

@Local
public interface IngestFacade {

	/**
	 * Salva o registro @IpvodIngestStage
	 * 
	 * @param ipvodIngestStage
	 */
	public abstract void save(IpvodIngestStage ipvodIngestStage);

	/**
	 * Atualiza o registro @IpvodIngestStage
	 * 
	 * @param ipvodIngestStage
	 */
	public abstract void update(IpvodIngestStage ipvodIngestStage);

	/**
	 * Lista todos os registros @IpvodIngestStage
	 * 
	 * @return List<IpvodIngestStage>
	 */
	public abstract List<IpvodIngestStage> findAll();

	/**
	 * Lista dados @IpvodIngestStage pelo assetInfo
	 * 
	 * @param assetInfo
	 * @return List<IpvodIngestStage>
	 */
	public abstract List<IpvodIngestStage> findListByAssetInfo(String assetInfo);

	/**
	 * Busca dados @IpvodIngestStage pelo assetInfo e stageType
	 * 
	 * @param assetInfo
	 * @param stageType
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findByAssetInfoStageType(String assetInfo,
			Long stageType);

	/**
	 * Remove registros da tabela @IpvodIngestStage pelo assetInfo
	 * 
	 * @param assetInfo
	 * @return int
	 */
	public abstract int clearIngest(String assetInfo);

	/**
	 * Remove dados na tabela @IpvodIngestStage
	 * 
	 * @param ipvodIngestStage
	 */
	public abstract void delete(IpvodIngestStage ipvodIngestStage);

	/**
	 * Busca dados na tabela @IpvodIngestStage que estao com status igual ao
	 * passado por parametro e asset_id diferente de nulo
	 * 
	 * @param stageType
	 * 
	 * @return List<IpvodIngestStage>
	 */
	public abstract List<IpvodIngestStage> findFilesToExecute(long stageType);

	/**
	 * Busca dados para mostrar na tela Ingest IPVOD-CMS
	 * 
	 * @param uriInfo
	 * 
	 * @return List<IngestVO>
	 */
	public abstract List<IngestVO> findDataIngest(UriInfo uriInfo,
			IpvodContentProvider ipvodContentProvider);

	/**
	 * Contagem dados para mostrar na tela Ingest IPVOD-CMS
	 * 
	 * @param uriInfo
	 * 
	 * @return List<IngestVO>
	 */
	public abstract Long countDataIngest(
			IpvodContentProvider ipvodContentProvider, UriInfo uriInfo);

	/**
	 * Busca dados para mostrar na tela Revision Ingest IPVOD-CMS
	 * 
	 * @return List<IngestVO>
	 */
	public abstract List<IngestVO> findAllDataIngestForRevision(
			UriInfo uriInfo, IpvodContentProvider ipvodContentProvider);

	/**
	 * Contagem dos registros para tela Revision Ingest IPVOD-CMS
	 * 
	 * @return List<IngestVO>
	 */
	public abstract Long countAllDataIngestForRevision(
			IpvodContentProvider ipvodContentProvider, UriInfo uriInfo);

	/**
	 * Busca dado para de Ingest por ID
	 * 
	 * @param ingestId
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findIngestById(long ingestId);

	/**
	 * Busca dados de @IpvodIngestStage pelo AssetInfo e com result igual a zero
	 * 
	 * @param assetInfo
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findRegisterWithErrorByAssetInfo(
			String assetInfo);

	/**
	 * Busca dados de @IpvodIngestStage pelo pathFile
	 * 
	 * @param pathfile
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findByPathFile(String pathfile);

	/**
	 * Busca dados de @IpvodIngestStage pelo AssetInfo
	 * 
	 * @param assetInfo
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findRegisterByAssetInfo(String assetInfo);

	/**
	 * Busca dados de @IpvodIngestStage pelo assetId
	 * 
	 * @param assetId
	 * @return IpvodIngestStage
	 */
	public abstract IpvodIngestStage findByAssetId(Long assetId);

	/**
	 * Lista registros que ja estao em stageType = 7 para remocao das pastas do
	 * servidor
	 * 
	 * @return List<IngestVO>
	 */
	public abstract List<IngestVO> findDataIngestForCleanUp();
	
	/**
	 * Manda o Ingest do assetId para o stage 8
	 * 
	 * @param assetId
	 */
	public abstract void finishRevision(Long assetId);

}
