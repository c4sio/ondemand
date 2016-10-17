package br.com.gvt.eng.integ.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.Jobstatusinfo;

@Local
public interface IntegrationDAO {

	/**
	 * Cria o job no 4Balancer
	 * 
	 * @param fileaName
	 * @param pathInFile
	 * @param pathOutFile
	 * @param presetName
	 * @param filelegend 
	 * @return IdJob
	 */
	public abstract String createJob(String fileaName, String pathInFile,
			String pathOutFile, String presetName, String filelegend);

	/**
	 * Cancela o Job no 4Balancer
	 * 
	 * @param jobId
	 * @return boolean
	 */
	public abstract boolean cancelJob(String jobId);

	/**
	 * Verifica o status do 4Balancer
	 * 
	 * @return Status
	 */
	public abstract String getStatus4Balancer();

	/**
	 * Verifica o status do job no 4Balancer
	 * 
	 * @param jobId
	 * @return Status job
	 */
	public abstract String getJobStatus(String jobId);

	/**
	 * Verifica o status do job no 4Balancer e informacao de execucao
	 * 
	 * @param jobId
	 * @return List<Jobstatusinfo>
	 */
	public abstract List<Jobstatusinfo> getInfoJob(String jobId);

}
