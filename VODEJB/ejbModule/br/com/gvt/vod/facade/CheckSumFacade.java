package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodCheckSumData;

@Local
public interface CheckSumFacade {

	/**
	 * @param ipvodCheckSumData
	 */
	public abstract void save(IpvodCheckSumData ipvodCheckSumData);

	/**
	 * @param ipvodCheckSumData
	 */
	public abstract void delete(IpvodCheckSumData ipvodCheckSumData);

	/**
	 * @param ipvodCheckSumData
	 * @return IpvodCheckSumData
	 */
	public abstract IpvodCheckSumData update(IpvodCheckSumData ipvodCheckSumData);

	/**
	 * Lista tosos os registros da tabela @IpvodBalancerData
	 * 
	 * @return List<IpvodCheckSumData>
	 */
	public abstract List<IpvodCheckSumData> findAll();

	/**
	 * Busca registro por ID na tabela @IpvodCheckSumData
	 * 
	 * @param entityID
	 * @return IpvodCheckSumData
	 */
	public abstract IpvodCheckSumData find(Long entityID);

	/**
	 * @param fileName
	 * @return IpvodCheckSumData
	 */
	public abstract IpvodCheckSumData findCheckSumByFileName(String fileName);

}
