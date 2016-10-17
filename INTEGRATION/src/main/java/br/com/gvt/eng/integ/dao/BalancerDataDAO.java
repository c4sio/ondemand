package br.com.gvt.eng.integ.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodBalancerData;

@Local
public interface BalancerDataDAO {

	/**
	 * Salva os dados na tabela @IpvodBalancerData
	 * 
	 * @param ipvodBalancerData
	 */
	public abstract void save(IpvodBalancerData ipvodBalancerData);

	/**
	 * Atualiza os dados na tabela @IpvodBalancerData
	 * 
	 * @param ipvodBalancerData
	 * @return IpvodBalancerData
	 */
	public abstract IpvodBalancerData update(IpvodBalancerData ipvodBalancerData);

	/**
	 * Remove os dados com erro da tabela @IpvodBalancerData
	 * 
	 * @param ipvodBalancerData
	 */
	public abstract void delete(IpvodBalancerData ipvodBalancerData);

	/**
	 * Busca dados na tabela @IpvodBalancerData pelo ID
	 * 
	 * @param entityID
	 * @return IpvodBalancerData
	 */
	public abstract IpvodBalancerData findById(long entityID);

	/**
	 * Busca dados na tabela @IpvodBalancerData que estao com status igual a 4
	 * no @IpvodIngestStage
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAllValuesInProcess();

	/**
	 * Busca dados na tabela @IpvodBalancerData com mesmo numero de ingest_id
	 * 
	 * @return boolean
	 */
	public abstract boolean checkEndOfProcess(long ingestId);

	/**
	 * Busca dados na tabela @IpvodBalancerData que estao em execucao
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAllInExecution();

	/**
	 * Busca todos os registros na tabela @IpvodBalancerData
	 * 
	 * @return List<IpvodBalancerData>
	 */
	public abstract List<IpvodBalancerData> findAllRegisters();

}
