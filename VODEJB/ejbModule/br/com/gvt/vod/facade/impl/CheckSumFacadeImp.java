package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.CheckSumDAO;
import br.com.gvt.eng.vod.model.IpvodCheckSumData;
import br.com.gvt.vod.facade.CheckSumFacade;

@Stateless
public class CheckSumFacadeImp implements CheckSumFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private CheckSumDAO checkSumDAO;

	@Override
	public void save(IpvodCheckSumData ipvodCheckSumData) {
		checkSumDAO.save(ipvodCheckSumData);
	}

	@Override
	public void delete(IpvodCheckSumData ipvodCheckSumData) {
		checkSumDAO.deleteCheckSum(ipvodCheckSumData);
	}

	@Override
	public IpvodCheckSumData update(IpvodCheckSumData ipvodCheckSumData) {
		return checkSumDAO.update(ipvodCheckSumData);
	}

	@Override
	public List<IpvodCheckSumData> findAll() {
		return checkSumDAO.findAll();
	}

	@Override
	public IpvodCheckSumData find(Long entityID) {
		return checkSumDAO.find(entityID);
	}

	@Override
	public IpvodCheckSumData findCheckSumByFileName(String fileName) {
		return checkSumDAO.findCheckSumByFileName(fileName);
	}

}
