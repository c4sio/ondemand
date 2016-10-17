package br.com.gvt.eng.integ.dao.impl;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.IntegrationDAO;
import br.com.gvt.eng.integ.service.Config4Balancer;
import br.com.gvt.eng.integ.util.Util;
import br.com.gvt.eng.vod.Jobqualityres;
import br.com.gvt.eng.vod.Jobstatusinfo;
import br.com.gvt.eng.vod.Jobstatusinfolist;
import br.com.gvt.eng.vod.Param;
import br.com.gvt.eng.vod.Params;
import br.com.gvt.eng.vod.ServicePortType;

@Repository
@Transactional
public class IntegrationDAOImpl implements IntegrationDAO {

	static Logger logger = Logger.getLogger(IntegrationDAOImpl.class.getName());

	private ServicePortType port = null;

	@Override
	public String createJob(String fileName, String pathInFile,
			String pathOutFile, String presetName, String filelegend) {
		logger.info("Criando novo Job");
		String jobId = null;
		try {
			// Conecta com plataforma 4Balancer
			this.port = Config4Balancer.get4Balancer();

			// Monta os parametros IN
			Param paramIn = new Param();
			paramIn.setName("inputfilename");
			paramIn.setValue(Util.getInFilesPath() + pathInFile
					+ File.separator + fileName);
			System.out.println("Input file " + paramIn.getValue());
			logger.info("Input file " + paramIn.getValue());

			// Monta os parametros JOB
			Params jobparams = new Params();
			// Adiciona um novo parametro
			jobparams.getParam().add(paramIn);

			// Se encontrou legenda cria um novo parametro
			Param paramInleg = null;
			if (!Util.isEmptyOrNull(filelegend)) {
				paramInleg = new Param();
				paramInleg.setName("subtitlefilename");
				paramInleg.setValue(Util.getInFilesPath() + pathInFile
						+ File.separator + filelegend);
				System.out.println("subtitlefilename file "
						+ paramInleg.getValue());
				logger.info("subtitlefilename file " + paramInleg.getValue());
				// Adiciona um novo parametro
				jobparams.getParam().add(paramInleg);
			}

			// Paramentro para mais de um audio
			Param paramAudio = null;
			if (presetName.equalsIgnoreCase(Util.getPresetAudioPlus())) {
				paramAudio = new Param();
				paramAudio.setName("audiostreamorder");
				paramAudio.setValue("#01,#02,#03");
				// Adiciona um novo parametro
				jobparams.getParam().add(paramAudio);
			}

			// Monta os parametros OUT
			Param paramOut = new Param();
			paramOut.setName("outputfilename");
			paramOut.setValue(Util.getOutFilesPath() + pathOutFile);
			System.out.println("Output file " + paramOut.getValue());
			logger.info("Output file " + paramOut.getValue());
			// Adiciona um novo parametro
			jobparams.getParam().add(paramOut);

			String jobname = "ipvodJob_" + Math.random();
			logger.info("Job name " + jobname);
			System.out.println("Job name " + jobname);

			logger.info("Preset name " + presetName);
			System.out.println("Preset name " + presetName);

			// Cria o Job no 4Balancer
			jobId = this.port.launchJob(presetName, jobparams, jobname);
			System.out.println("jobId gerado: " + jobId);
			logger.info("jobId gerado " + jobId);
		} catch (SOAPFaultException e) {
			e.printStackTrace();
			logger.error("Error createJob: ", e);
		}
		return jobId;
	}

	@Override
	public String getStatus4Balancer() throws SOAPFaultException {
		logger.info("Verifica o status do 4Balancer");
		this.port = Config4Balancer.get4Balancer();
		return port.getStatus();
	}

	@Override
	public boolean cancelJob(String jobId) throws SOAPFaultException {
		logger.info("Cancelando o Job " + jobId);
		this.port = Config4Balancer.get4Balancer();
		port.cancelJob(jobId);
		return true;
	}

	@Override
	public String getJobStatus(String jobId) throws SOAPFaultException {
		Holder<String> state = new Holder<String>();
		Holder<Jobstatusinfolist> infos = new Holder<Jobstatusinfolist>();
		Holder<Jobqualityres> quality = new Holder<Jobqualityres>();
		try {
			logger.info("Verificando o status do Job " + jobId);
			this.port = Config4Balancer.get4Balancer();
			port.getJobStatus(jobId, state, infos, quality);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.info("O Job " + jobId + " ja foi removido do 4Balancer - "
					+ e.getMessage());
			state.value = e.getMessage();
		}
		return state.value;
	}

	@Override
	public List<Jobstatusinfo> getInfoJob(String jobId) {
		Holder<String> state = new Holder<String>();
		Holder<Jobstatusinfolist> infos = new Holder<Jobstatusinfolist>();
		Holder<Jobqualityres> quality = new Holder<Jobqualityres>();
		try {
			logger.info("Verificando informacoes de erro do Job " + jobId);
			this.port = Config4Balancer.get4Balancer();
			port.getJobStatus(jobId, state, infos, quality);
		} catch (Exception e) {
			System.out.println("erro: " + e);
			logger.info("Erro ao buscar informacoes InfoJob " + jobId);
		}
		return infos.value.getInfo();
	}
}