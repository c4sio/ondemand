package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.dao.BalancerDAO;
import br.com.gvt.eng.vod.dao.ConvoyDAO;
import br.com.gvt.eng.vod.dao.DrmDAO;
import br.com.gvt.eng.vod.dao.IngestDAO;
import br.com.gvt.eng.vod.dao.MenuAssetDAO;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodConvoyData;
import br.com.gvt.eng.vod.model.IpvodDrmData;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.vo.IngestVO;
import br.com.gvt.vod.facade.IngestFacade;

@Stateless
public class IngestFacadeImp implements IngestFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private IngestDAO ingestDAO;

	@EJB
	private DrmDAO drmDAO;

	@EJB
	private ConvoyDAO convoyDAO;

	@EJB
	private BalancerDAO balancerDAO;

	@EJB
	private MenuAssetDAO menuAssetDAO;

	@Override
	public void save(IpvodIngestStage ipvodIngestStage) {
		ipvodIngestStage.setDtInsert(new Date());
		ipvodIngestStage.setDtChange(new Date());
		ingestDAO.save(ipvodIngestStage);
	}

	@Override
	public void update(IpvodIngestStage ipvodIngestStage) {
		ipvodIngestStage.setDtChange(new Date());
		ingestDAO.update(ipvodIngestStage);
	}

	@Override
	public void delete(IpvodIngestStage ipvodIngestStage) {
		ingestDAO.deleteIngest(ipvodIngestStage);
	}

	@Override
	public List<IpvodIngestStage> findAll() {
		return ingestDAO.findAll();
	}

	@Override
	public List<IpvodIngestStage> findListByAssetInfo(String assetInfo) {
		return ingestDAO.getByAssetInfo(assetInfo);
	}

	@Override
	public IpvodIngestStage findByAssetInfoStageType(String assetInfo,
			Long stageType) {
		return ingestDAO.getByAssetInfoStageType(assetInfo, stageType);
	}

	@Override
	public int clearIngest(String assetInfo) {
		return ingestDAO.clearIngestByAssetInfo(assetInfo);
	}

	@Override
	public IpvodIngestStage findByAssetId(Long assetId) {
		return ingestDAO.findByAssetId(assetId);
	}

	@Override
	public List<IpvodIngestStage> findFilesToExecute(long stageType) {
		List<IpvodIngestStage> listData = new ArrayList<IpvodIngestStage>();
		try {
			listData = ingestDAO.findFilesToExecute(stageType);
			if (!listData.isEmpty()) {
				switch ((int) stageType) {
				case 4:
					List<IpvodBalancerData> listBalancerData = new ArrayList<IpvodBalancerData>();
					// Busca registros que estao cadastrados na
					// @IpvodBalancerData
					listBalancerData = balancerDAO
							.findRegistersInExecution4Balancer(new Long(4));
					// Seta somente valores de mediaAsset que ainda nao foram
					// incluidos no @IpvodBalancerData
					for (IpvodIngestStage ipvodIngestStage : listData) {
						ipvodIngestStage.getIpvodAsset().setIpvodMediaAssets(
								verifyFilesInExecutionBalancer(ipvodIngestStage
										.getIpvodAsset().getIpvodMediaAssets(),
										listBalancerData));
					}
					break;
				case 5:
					listData = verifyFilesInExecutionDRM(listData);
					break;
				case 6:
					listData = verifyFilesInExecutionConvoy(listData);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}

	/**
	 * Verifica se os arquivos de leitura ja estao casdastrados na base @IpvodBalancerData
	 * 
	 * @param ipvodMediaAssets
	 * @param listBalancerData
	 * @return List<IpvodMediaAsset>
	 */
	private List<IpvodMediaAsset> verifyFilesInExecutionBalancer(
			List<IpvodMediaAsset> ipvodMediaAssets,
			List<IpvodBalancerData> listBalancerData) {
		List<IpvodMediaAsset> mediaList = new ArrayList<IpvodMediaAsset>(0);
		try {
			// Copia os valores para a nova lista
			mediaList = ipvodMediaAssets;
			String value = null;

			// Verifica os registros
			if (!listBalancerData.isEmpty()) {
				Iterator<IpvodMediaAsset> ite = ipvodMediaAssets.iterator();
				for (IpvodBalancerData ipvodBalancerData : listBalancerData) {
					while (ite.hasNext()) {
						value = ite.next().getUrl();
						// Se os valores forem iguais remove da lista
						if (value.equals(ipvodBalancerData.getNameFile())) {
							ite.remove();
							mediaList.remove(ite);
							System.out
									.println("Arquivo "
											+ value
											+ " removido da lista de leitura, pois ja foi incluido na base e criado o job.");
						}
					}
					ite = ipvodMediaAssets.iterator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mediaList;
	}

	/**
	 * Verifica se os arquivos de leitura ja estao casdastrados na base @IpvodConvoyData
	 * 
	 * @param listData
	 * @return List<IpvodIngestStage>
	 */
	private List<IpvodIngestStage> verifyFilesInExecutionConvoy(
			List<IpvodIngestStage> listData) {
		List<IpvodConvoyData> listConvoy = new ArrayList<IpvodConvoyData>();
		try {
			// Busca registros na @IpvodConvoyData
			listConvoy = convoyDAO.findAllLessDoneStatus();

			// Executa se enconttrou registros
			if (!listConvoy.isEmpty()) {
				// Carrega os arquivos do @IpvodIngestStage
				Iterator<IpvodIngestStage> ite = listData.iterator();

				// Varre a lista de registros
				for (IpvodConvoyData ipvodConvoyData : listConvoy) {
					while (ite.hasNext()) {
						IpvodIngestStage ingestStage = ite.next();
						// Verifica se ID do Ingest eh o mesmo
						if (ingestStage.getId() == ipvodConvoyData
								.getIpvodIngestStage().getId()) {
							// Varre a lista de dados para encontrar o mesmo
							// nome de arquivo
							for (IpvodConvoyData convoyValue : ingestStage
									.getIpvodConvoy()) {
								if (convoyValue.getNameFile().equals(
										ipvodConvoyData.getNameFile())) {
									// Remove registro da lista
									ite.remove();
									listData.remove(ite);
									break;
								}
							}
						}
					}
					// Recarrega a interacao
					ite = listData.iterator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}

	/**
	 * Verifica se os arquivos de leitura ja estao casdastrados na base @IpvodDrmData
	 * 
	 * @param listData
	 * @return List<IpvodIngestStage>
	 */
	private List<IpvodIngestStage> verifyFilesInExecutionDRM(
			List<IpvodIngestStage> listData) {
		List<IpvodDrmData> listDrm = new ArrayList<IpvodDrmData>();
		try {
			// Busca registros na @IpvodDrmData
			listDrm = drmDAO.findAllLessCompletedStatus();

			if (!listDrm.isEmpty()) {
				// Carrega os arquivos do @IpvodIngestStage
				Iterator<IpvodIngestStage> ite = listData.iterator();
				// Varre a lista de registros
				for (IpvodDrmData ipvodDrmData : listDrm) {
					while (ite.hasNext()) {
						IpvodIngestStage ingestStage = ite.next();
						// Verifica se ID do Ingest eh o mesmo
						if (ingestStage.getId() == ipvodDrmData
								.getIpvodIngestStage().getId()) {
							// Varre a lista de dados para encontrar o mesmo
							// nome de arquivo
							for (IpvodDrmData drmValue : ingestStage
									.getIpvodDrm()) {
								if (drmValue.getNameFile().equals(
										ipvodDrmData.getNameFile())) {
									// Remove registro da lista
									ite.remove();
									listData.remove(ite);
									break;
								}
							}
						}
					}
					// Recarrega a interacao
					ite = listData.iterator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData;
	}

	@Override
	public IpvodIngestStage findIngestById(long ingestId) {
		return ingestDAO.find(ingestId);
	}

	@Override
	public List<IngestVO> findAllDataIngestForRevision(UriInfo uriInfo,
			IpvodContentProvider ipvodContentProvider) {
		List<IngestVO> ingestList = ingestDAO.findAllDataIngestForRevision(
				uriInfo, ipvodContentProvider);
		for (IngestVO ingest : ingestList) {
			ingest.setIpvodVisualMenu(menuAssetDAO.findByAssetId(ingest
					.getAssetId()));
		}
		return ingestList;
	}

	@Override
	public Long countAllDataIngestForRevision(
			IpvodContentProvider ipvodContentProvider, UriInfo uriInfo) {
		return ingestDAO.countAllDataIngestForRevision(ipvodContentProvider,
				uriInfo);
	}

	@Override
	public IpvodIngestStage findRegisterWithErrorByAssetInfo(String assetInfo) {
		return ingestDAO.findRegisterWithErrorByAssetInfo(assetInfo);
	}

	@Override
	public IpvodIngestStage findByPathFile(String pathfile) {
		return ingestDAO.getDataByPathFile(pathfile);
	}

	@Override
	public IpvodIngestStage findRegisterByAssetInfo(String assetInfo) {
		return ingestDAO.getDataByAssetInfo(assetInfo);
	}

	@Override
	public List<IngestVO> findDataIngest(UriInfo uriInfo,
			IpvodContentProvider ipvodContentProvider) {
		List<IngestVO> listData = new ArrayList<IngestVO>();
		List<IngestVO> listCompleted = new ArrayList<IngestVO>();
		try {
			listData = ingestDAO.findDataIngestBeforePlataform(uriInfo,
					ipvodContentProvider);
			if (!listData.isEmpty()) {
				// So busca os dados quando necessario
				for (IngestVO ingestVO : listData) {
					switch (ingestVO.getStageType().intValue()) {
					case 2:
						ingestVO.setCkeckSumVO(ingestDAO
								.findDataCheckSum(ingestVO.getIdIngest()));
						break;
					case 4:
						ingestVO.setBalancerVO(ingestDAO
								.findData4Balancer(ingestVO.getIdIngest()));
						break;
					case 5:
						ingestVO.setBalancerVO(ingestDAO
								.findData4Balancer(ingestVO.getIdIngest()));
						ingestVO.setDrmVO(ingestDAO.findDataDrm(ingestVO
								.getIdIngest()));
						break;
					case 6:
						ingestVO.setBalancerVO(ingestDAO
								.findData4Balancer(ingestVO.getIdIngest()));
						ingestVO.setDrmVO(ingestDAO.findDataDrm(ingestVO
								.getIdIngest()));
						ingestVO.setConvoyVO(ingestDAO.findDataConvoy(ingestVO
								.getIdIngest()));
						break;
					default:
						break;
					}
					listCompleted.add(ingestVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listCompleted;
	}

	@Override
	public Long countDataIngest(IpvodContentProvider ipvodContentProvider,
			UriInfo uriInfo) {
		return ingestDAO.countDataIngestBeforePlataform(ipvodContentProvider,
				uriInfo);
	}

	@Override
	public List<IngestVO> findDataIngestForCleanUp() {
		return ingestDAO.findDataIngestForCleanUp();
	}

	@Override
	public void finishRevision(Long assetId) {
		ingestDAO.finishRevision(assetId);
	}
}