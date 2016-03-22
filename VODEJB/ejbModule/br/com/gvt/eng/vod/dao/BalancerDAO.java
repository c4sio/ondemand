package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodBalancerData;

@Stateless
public class BalancerDAO extends GenericDAO<IpvodBalancerData> {

	public BalancerDAO() {
		super(IpvodBalancerData.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodBalancerData
	 */
	public void deleteBalancer(IpvodBalancerData ipvodBalancerData) {
		super.delete(ipvodBalancerData.getId(), IpvodBalancerData.class);
	}

	/**
	 * Busca dados na tabela @IpvodBalancerData que estao com status igual a 4
	 * no @IpvodIngestStage
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public List<IpvodBalancerData> findAllValuesInProcess() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("stageType", 4);
		return super.findResultByParameter(
				IpvodBalancerData.ALL_VALUES_IN_PROCESS, parameters);
	}

	/**
	 * Lista todos os registros associados ao mesmo IngestId
	 * 
	 * @param ingestId
	 * @return List<IpvodBalancerData>
	 */
	public List<IpvodBalancerData> findBalancerDataByIngestId(Long ingestId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ingestId", ingestId);
		return super.findResultByParameter(
				IpvodBalancerData.ALL_DATA_BY_INGESTID, parameters);
	}

	/**
	 * Lista todos os registros que estao em execucao no 4Balancer
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public List<IpvodBalancerData> findAllInExecution() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("success", "success");
		parameters.put("error", "error");
		return super.findResultByParameter(
				IpvodBalancerData.ALL_DATA_IN_EXECUTION, parameters);
	}

	public List<IpvodBalancerData> findRegistersInExecution4Balancer(
			long stageTypeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("stageType", stageTypeId);
		return super.findResultByParameter(
				IpvodBalancerData.DATA_IN_EXECUTION_BALANCER, parameters);
	}

}
