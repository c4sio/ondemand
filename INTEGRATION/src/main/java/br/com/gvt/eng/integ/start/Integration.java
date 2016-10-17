package br.com.gvt.eng.integ.start;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import br.com.gvt.eng.integ.dao.BalancerDataDAO;
import br.com.gvt.eng.integ.dao.IngestStageDAO;
import br.com.gvt.eng.integ.dao.IntegrationDAO;
import br.com.gvt.eng.integ.dao.PresetDAO;
import br.com.gvt.eng.integ.dao.UserSystemDAO;
import br.com.gvt.eng.integ.file.FileFunctions;
import br.com.gvt.eng.integ.util.Util;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;

/**
 * @author Joao Luis
 * 
 */
@Component
public class Integration {

	static Logger logger = Logger.getLogger(Integration.class.getName());

	@Autowired
	private IntegrationDAO integrationDAO;

	@Autowired
	private BalancerDataDAO balancerDataDAO;

	@Autowired
	private IngestStageDAO ingestStageDAO;

	@Autowired
	private UserSystemDAO userSystemDAO;

	@Autowired
	private PresetDAO presetDAO;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("log4j.properties");
			logger.info("Arquivo de log inicializado.");
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					"classpath*:/spring-config.xml");
			Integration integration = ctx.getBean(Integration.class);
			logger.info("Arquivo de contexto inicializado.");
			integration.execute();
		} catch (Exception e) {
			logger.error("Erro ao inicializar processamento Integration: ", e);
		}
	}

	/**
	 * Executa as acoes no 4Balancer
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void execute() {
		System.out.println(">> STARTING THE INTEGRATION <<");
		logger.info("STARTING THE INTEGRATION");

		try {
			// Variavel utilizada para calcular o tempo de execucao
			long tempoInicio = System.currentTimeMillis();

			// Status 4Balancer
			logger.info("Status 4Balancer: "
					+ integrationDAO.getStatus4Balancer());
			System.out.println("Status 4Balancer: "
					+ integrationDAO.getStatus4Balancer());

			logger.info("Verifica base e atualiza status dos jobs.");
			// Busca registros na base para atualizar o status
			List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
			results = balancerDataDAO.findAllValuesInProcess();
			logger.info("Encontrou " + results.size()
					+ " na tabela IpvodBalancerData para processar.");

			String status = null;
			if (!results.isEmpty()) {
				for (IpvodBalancerData balancerData : results) {
					// Verifica o status do job no 4Balancer
					status = integrationDAO.getJobStatus(balancerData
							.getJobId());
					if (!Util.isEmptyOrNull(status)) {
						switch (status) {
						case "Processing Failure":
							// Remove da base os registros de arquivos com erro
							// para reenvio no 4Balancer
							balancerDataDAO.delete(balancerData);
							break;
						case "success":
							// Atualiza os arquivos com sucesso
							balancerData.setDateEnd(Util.getCurrentDate());
							balancerData.setPercentComp(new Double(100));
							balancerData.setStatus(status);
							balancerDataDAO.update(balancerData);

							// Verifica se todos os registros foram processados
							// com sucesso antes de atualizar a Ingest
							if (balancerDataDAO.checkEndOfProcess(balancerData
									.getIpvodIngestStage().getId())) {
								// Atualiza o status da tabela Ingest
								if (ingestStageDAO
										.findByIdAndUpdateStage(balancerData
												.getIpvodIngestStage().getId())) {
									System.out
											.println("Registro "
													+ balancerData
															.getIpvodIngestStage()
															.getId()
													+ " atualizado.");
									logger.info("Registro "
											+ balancerData
													.getIpvodIngestStage()
													.getId()
											+ " atualizado.");
								}
							}
						default:
							// Atualiza o status do job
							balancerData.setStatus(status);
							balancerDataDAO.update(balancerData);
							break;
						}
					}
				}
				logger.info("Fim da verificacao da base e atualizacao dos status dos jobs.");
			}

			// Busca os arquivos na pasta de entrada
			List<IpvodIngestStage> listData = new ArrayList<IpvodIngestStage>();
			listData = ingestStageDAO.findFilesToExecute();

			// Verifica se a lista de arquivos esta vazia
			if (!listData.isEmpty()) {
				// Criando as variaveis
				IpvodBalancerData ipvodBalancerData = null;
				String jobId = null;
				String presetName = null;
				String pathOutFile = null;
				List<IpvodMediaAsset> listMediaAsset = new ArrayList<IpvodMediaAsset>();
				FileFunctions fileFunctions = null;
				String filelegend = null;

				// Lendo os arquivos
				for (IpvodIngestStage ipvodIngestStage : listData) {

					// Busca todos os arquivos do asset
					listMediaAsset = ipvodIngestStage.getIpvodAsset()
							.getIpvodMediaAssets();

					// Verifica se a lista esta vazia
					if (!listMediaAsset.isEmpty()) {
						// Em ordem decrescente por tipo de media
						Collections.sort(listMediaAsset, new Comparator() {
							public int compare(Object o1, Object o2) {
								IpvodMediaAsset ipvodMediaAsset1 = (IpvodMediaAsset) o1;
								IpvodMediaAsset ipvodMediaAsset2 = (IpvodMediaAsset) o2;
								return ipvodMediaAsset1.getIpvodMediaType()
										.getMediaTypeId() < ipvodMediaAsset2
										.getIpvodMediaType().getMediaTypeId() ? +1
										: (ipvodMediaAsset1.getIpvodMediaType()
												.getMediaTypeId() > ipvodMediaAsset2
												.getIpvodMediaType()
												.getMediaTypeId() ? -1 : 0);
							}
						});

						// Faz a leitura dos arquivos - movie/trailer
						for (IpvodMediaAsset ipvodMediaAsset : listMediaAsset) {
							if (ipvodMediaAsset.getIpvodMediaType()
									.getMediaTypeId() == 2
									|| ipvodMediaAsset.getIpvodMediaType()
											.getMediaTypeId() == 3) {
								System.out.println("Processando o arquivo: "
										+ ipvodMediaAsset.getUrl());
								logger.info("Processando o arquivo: "
										+ ipvodMediaAsset.getUrl());

								// Busca dados do Preset para enconder, quando
								// for trailer sempre vai utilizar o Preset
								// Trailer
								if (ipvodMediaAsset.getIpvodMediaType()
										.getMediaTypeId() == 2) {
									presetName = Util.getPresetTrailer();
								} else {
									presetName = presetDAO
											.findPresetByParameters(
													ipvodIngestStage
															.getIpvodAsset()
															.getAudioType(),
													ipvodIngestStage
															.getIpvodAsset()
															.getLanguages(),
													ipvodIngestStage
															.getIpvodAsset()
															.isHD() == true ? "Y"
															: "N",
													ipvodIngestStage
															.getIpvodAsset()
															.getSubtitles() == null ? "null"
															: ipvodIngestStage
																	.getIpvodAsset()
																	.getSubtitles(),
													ipvodIngestStage
															.getIpvodAsset()
															.getDubbedLanguage() == null ? "null"
															: ipvodIngestStage
																	.getIpvodAsset()
																	.getDubbedLanguage());
								}

								// Se nao encontrou o
								if (Util.isEmptyOrNull(presetName)) {
									System.out
											.println("Nao encontrou Preset para o assetID: "
													+ ipvodIngestStage
															.getIpvodAsset()
															.getAssetId());
									logger.error("Nao encontrou Preset para o assetID: "
											+ ipvodIngestStage.getIpvodAsset()
													.getAssetId());
									// Atualiza o status da @IpvodIngestStage
									ingestStageDAO
											.updateStagePresetError(ipvodIngestStage
													.getId());
									break;
								}

								// Monta o caminho de saida dos arquivos
								pathOutFile = ipvodIngestStage.getAssetInfo()
										+ "_"
										+ ipvodMediaAsset.getIpvodMediaType()
												.getDescription();

								// Busca a legenda
								filelegend = null;
								if (ipvodMediaAsset.getIpvodMediaType()
										.getMediaTypeId() == 3) {
									fileFunctions = new FileFunctions();
									filelegend = fileFunctions.findLegend(Util
											.getLegendFilePath()
											+ ipvodIngestStage.getAssetInfo()
											+ File.separator);
									System.out.println("Verificando SRT: "
											+ Util.getLegendFilePath()
											+ ipvodIngestStage.getAssetInfo()
											+ File.separator);
									System.out.println("Encontrou a legenda "
											+ filelegend);
									logger.info("Verificando SRT: "
											+ Util.getLegendFilePath()
											+ ipvodIngestStage.getAssetInfo()
											+ File.separator);
									logger.info("Encontrou a legenda "
											+ filelegend);
								}

								// Criando os jobs no 4Balancer
								jobId = integrationDAO.createJob(
										ipvodMediaAsset.getUrl(),
										ipvodIngestStage.getAssetInfo(),
										pathOutFile, presetName, filelegend);

								// Populando o objeto balancerData
								if (!Util.isEmptyOrNull(jobId)) {
									ipvodBalancerData = new IpvodBalancerData();
									ipvodBalancerData.setJobId(jobId);
									ipvodBalancerData
											.setNameFile(ipvodMediaAsset
													.getUrl());
									ipvodBalancerData
											.setIpvodIngestStage(ipvodIngestStage);
									ipvodBalancerData.setDateStart(Util
											.getCurrentDate());
									ipvodBalancerData.setStatus(integrationDAO
											.getJobStatus(jobId));
									ipvodBalancerData
											.setPercentComp(new Double(0));
									ipvodBalancerData.setPresetId(presetName);

									// Salva dados na base
									balancerDataDAO.save(ipvodBalancerData);
								}
							}
						}
					}
				}
			} else {
				System.out.println("Have found no file for reading!");
				logger.info("Have found no file for reading!");
			}

			// Mostra a quantidade de arquivos em execucao
			results = balancerDataDAO.findAllInExecution();
			System.out.println("Arquivos em execucao: " + results.size());
			logger.info("Arquivos em execucao: " + results.size());

			// Mostra o tempo de execucao no display
			System.out.println("Tempo Total processamento Integration: "
					+ new SimpleDateFormat("mm:ss").format(new Date(System
							.currentTimeMillis() - tempoInicio)));
			// Mostra o tempo de execucao
			logger.info("Tempo Total processamento Integration: "
					+ new SimpleDateFormat("mm:ss").format(new Date(System
							.currentTimeMillis() - tempoInicio)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro no processamento Integration: ", e);
		}
		System.out.println(">> ENDING THE INTEGRATION <<");
		logger.info("ENDING THE INTEGRATION");
	}
}