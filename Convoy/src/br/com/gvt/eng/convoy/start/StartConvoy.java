package br.com.gvt.eng.convoy.start;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import br.com.gvt.eng.convoy.file.FileFunctions;
import br.com.gvt.eng.convoy.properties.PropertiesConfig;
import br.com.gvt.eng.convoy.rest.ContentRest;
import br.com.gvt.eng.convoy.rest.ConvoyDataRest;
import br.com.gvt.eng.convoy.rest.IngestStageRest;
import br.com.gvt.eng.convoy.rest.UploadRest;
import br.com.gvt.eng.convoy.rest.impl.ContentRestImpl;
import br.com.gvt.eng.convoy.rest.impl.ConvoyDataRestImpl;
import br.com.gvt.eng.convoy.rest.impl.IngestStageRestImpl;
import br.com.gvt.eng.convoy.rest.impl.UploadRestImpl;
import br.com.gvt.eng.convoy.util.Util;
import br.com.gvt.eng.convoy.vo.IpvodContent;
import br.com.gvt.eng.convoy.vo.IpvodConvoyData;
import br.com.gvt.eng.convoy.vo.IpvodIngestStage;
import br.com.gvt.eng.convoy.vo.IpvodMediaAsset;

/**
 * @author GVT
 * 
 */
public class StartConvoy {

	static Logger logger = Logger.getLogger(StartConvoy.class.getName());

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("log4j.properties");
			logger.info("Arquivo de log inicializado.");
			StartConvoy startConvoy = new StartConvoy();
			startConvoy.execute();
		} catch (Exception e) {
			logger.error(
					"Erro ao inicializar processamento Integration Convoy: ", e);
		}
	}

	/**
	 * Executa as acoes no convoy
	 */
	private static void execute() {
		System.out.println(">> STARTING THE INTEGRATION CONVOY <<");
		logger.info("STARTING THE INTEGRATION CONVOY");
		try {
			// Variavel utilizada para calcular o tempo de execucao
			long tempoInicio = System.currentTimeMillis();

			List<IpvodConvoyData> listDataConvoy = new ArrayList<IpvodConvoyData>();
			ConvoyDataRest convoyDataRest = new ConvoyDataRestImpl();
			UploadRest uploadRest = new UploadRestImpl();
			ContentRest contentRest = new ContentRestImpl();

			// 1) Busca dados para verificar - @IpvodConvoyData
			listDataConvoy = convoyDataRest.findAllLessDone();
			logger.info("Encontrou " + listDataConvoy.size()
					+ " registros para verificar o status.");

			// Se a lista nao estiver vazia executa a consulta de status
			if (!listDataConvoy.isEmpty()) {
				IngestStageRest ingestStageRest = new IngestStageRestImpl();
				IpvodContent ipvodContent = null;
				IpvodIngestStage ipvodIngestStage = null;
				// Varre todos os registros
				for (IpvodConvoyData ipvodConvoyData : listDataConvoy) {
					ipvodContent = new IpvodContent();

					// 2) Verificar o status do arquivo - Convoy
					ipvodContent = uploadRest.checkStatusUpload(ipvodConvoyData
							.getJobIdConvoy());

					// Se encontrou informacao atualiza os dados
					if (ipvodContent.getState() != null) {
						// 3) Atualizar o status na base @IpvodConvoyData
						switch (ipvodContent.getState()) {
						case "error":
							// Remove da base os registros de arquivos com erro
							// para reenvio no Convoy
							convoyDataRest
									.delete(ipvodConvoyData.getConvoyId());
							break;
						case "done":
							// Atualiza os arquivos com sucesso
							ipvodConvoyData.setStatusConvoy("done");
							ipvodConvoyData
									.setPercentCompConvoy(new Double(100));
							// Verifica o conteudo no Convoy
							ipvodContent = contentRest
									.checkContent(ipvodConvoyData
											.getFileConvoy());

							// Atualiza @IpvodMediaAsset
							ipvodConvoyData.getIpvodMediaAsset().setUrl(
									ipvodContent.getEntry_urls().get(0));

							// Atualiza dados @IpvodConvoyData
							convoyDataRest.update(ipvodConvoyData);

							// Verifica se todos os registros foram processados
							// com sucesso antes de atualizar a Ingest
							if (convoyDataRest
									.checkEndOfProcess(ipvodConvoyData
											.getIpvodIngestStage().getId())) {
								// Atualiza o status da tabela Ingest
								ipvodIngestStage = new IpvodIngestStage();
								ipvodIngestStage = ingestStageRest
										.findIngestById(ipvodConvoyData
												.getIpvodIngestStage().getId());
								ingestStageRest.updateIngest(ipvodIngestStage);
							}
							break;
						default:
							// Atualiza o status do job
							ipvodConvoyData.setStatusConvoy(ipvodContent
									.getState());
							ipvodConvoyData.setPercentCompConvoy(ipvodContent
									.getProgress() == null ? 0 : ipvodContent
									.getProgress());
							convoyDataRest.update(ipvodConvoyData);
							break;
						}
					}
				}
			}

			// 4) Busca novos dados para processar - Ingest @IpvodIngestStage
			IngestStageRest ingestStageRest = new IngestStageRestImpl();
			List<IpvodIngestStage> listaData = new ArrayList<IpvodIngestStage>();

			Properties prop = PropertiesConfig.getProp();
			long statusSearch = Long.parseLong(prop
					.getProperty("status.convoy"));
			// Busca dados com status = 6 - PUBLISH
			listaData = ingestStageRest.searchDataForProcess(statusSearch);

			logger.info("Encontrou " + listaData.size()
					+ " novos registros para processar.");

			IpvodConvoyData ipvodConvoyData = null;
			IpvodContent ipvodContent = null;
			FileFunctions fileFunctions = new FileFunctions();
			List<File> listFiles = new ArrayList<File>();
			List<IpvodIngestStage> listaDataFinal = null;
			String fileToConvoy = null;

			if (!listaData.isEmpty()) {
				listaDataFinal = new ArrayList<IpvodIngestStage>(0);

				// Necessario esta logica para mandar apenas um registro
				// para a plataforma
				listaDataFinal.add(listaData.get(0));

				for (IpvodIngestStage ipvodIngest : listaDataFinal) {
					// Verifica o tipo de arquivo - Movie/Trailer
					for (IpvodMediaAsset ipvodMediaAsset : ipvodIngest
							.getIpvodAsset().getIpvodMediaAssets()) {
						// Se encontra a registro - altera o path para buscar
						// dados para Convoy
						if (ipvodMediaAsset.getIpvodMediaType()
								.getMediaTypeId() == 2
								|| ipvodMediaAsset.getIpvodMediaType()
										.getMediaTypeId() == 3) {

							// Iniciando as variaveis
							listFiles = new ArrayList<File>();
							fileToConvoy = null;

							// 5) Busca Arquivos no diretorio de entrada do
							// servidor, O path eh o valordo AssetId concatenado
							// com o tipo Movie/Trailer
							listFiles = fileFunctions.searchFiles(ipvodIngest
									.getIpvodAsset().getAssetId()
									+ "_"
									+ ipvodMediaAsset.getIpvodMediaType()
											.getDescription());

							// Se encontrou algum registro executa a criacao do
							// JobId e grava os dados na @IpvodConvoyData
							if (!listFiles.isEmpty()) {
								for (File file : listFiles) {
									// Gera o nome do arquivo
									fileToConvoy = file.getName() + "_"
											+ System.currentTimeMillis();
									// Inicializando o objeto
									ipvodContent = new IpvodContent();
									// 6) Envia o arquivo para Upload - Convoy
									ipvodContent = uploadRest
											.uploadFile(
													file.getName(),
													fileToConvoy,
													ipvodIngest.getIpvodAsset()
															.getTitle(),
													ipvodIngest.getIpvodAsset()
															.getAssetId()
															+ "_"
															+ ipvodMediaAsset
																	.getIpvodMediaType()
																	.getDescription());

									// Populando o objeto balancerData
									if (!Util.isEmptyOrNull(ipvodContent
											.getUri())) {
										ipvodConvoyData = new IpvodConvoyData();
										ipvodConvoyData
												.setJobIdConvoy(ipvodContent
														.getUri());
										ipvodConvoyData
												.setStatusConvoy(ipvodContent
														.getState());
										ipvodConvoyData
												.setIpvodIngestStage(ipvodIngest);
										ipvodConvoyData
												.setPercentCompConvoy(new Double(
														0));
										ipvodConvoyData.setNameFile(file
												.getName());
										ipvodConvoyData.setFileConvoy(file
												.getName());
										ipvodConvoyData
												.setIpvodMediaAsset(ipvodMediaAsset);
										// 7) Grava dados na base -
										// @IpvodConvoyData
										convoyDataRest
												.saveConvoyData(ipvodConvoyData);
									}
								}
							}
						}
					}
				}
			} else {
				System.out.println("Have found no file for reading!");
				logger.info("Have found no file for reading!");
			}
			// Mostra o tempo de execucao no display
			System.out.println("Tempo Total processamento Integration Convoy: "
					+ new SimpleDateFormat("mm:ss").format(new Date(System
							.currentTimeMillis() - tempoInicio)));
			// Mostra o tempo de execucao
			logger.info("Tempo Total processamento Integration Convoy: "
					+ new SimpleDateFormat("mm:ss").format(new Date(System
							.currentTimeMillis() - tempoInicio)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro: " + e.getMessage());
			logger.error("Erro no processamento Integration Convoy: ", e);
		}
		System.out.println(">> ENDING THE INTEGRATION CONVOY <<");
		logger.info("ENDING THE INTEGRATION CONVOY");
	}
}