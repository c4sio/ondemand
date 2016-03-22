package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodBalancerData;

@Local
public interface BalancerFacade {

	/**
	 * @param ipvodBalancerData
	 */
	public abstract void save(IpvodBalancerData ipvodBalancerData);

	/**
	 * @param ipvodBalancerData
	 */
	public abstract void delete(IpvodBalancerData ipvodBalancerData);

	/**
	 * @param ipvodBalancerData
	 * @return IpvodBalancerData
	 */
	public abstract IpvodBalancerData update(IpvodBalancerData ipvodBalancerData);

	/**
	 * Lista tosos os registros da tabela @IpvodBalancerData
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAll();

	/**
	 * Busca registro por ID na tabela @IpvodBalancerData
	 * 
	 * @param entityID
	 * @return IpvodBalancerData
	 */
	public abstract IpvodBalancerData find(Long entityID);

	/**
	 * * Busca dados na tabela @IpvodBalancerData que estao com status igual a 4
	 * no @IpvodIngestStage
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAllValuesInProcess();

	/**
	 * Busca registros na tabela @IpvodBalancerData com o mesmo ingestId
	 * 
	 * @param ingestId
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findBalancerDataByIngestId(
			Long ingestId);

	/**
	 * Lista todos os registros que estao em execucao no 4Balancer
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAllInExecution();

}
