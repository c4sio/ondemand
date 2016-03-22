package br.com.gvt.vod.facade.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.BalancerDAO;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.vod.facade.BalancerFacade;

@Stateless
public class BalancerFacadeImp implements BalancerFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private BalancerDAO balancerDAO;

	@Override
	public void save(IpvodBalancerData ipvodBalancerData) {
		ipvodBalancerData.setDateStart(new Date());
		balancerDAO.save(ipvodBalancerData);
	}

	@Override
	public void delete(IpvodBalancerData ipvodBalancerData) {
		balancerDAO.deleteBalancer(ipvodBalancerData);
	}

	@Override
	public IpvodBalancerData update(IpvodBalancerData ipvodBalancerData) {
		if (ipvodBalancerData.getStatus().equalsIgnoreCase("success")) {
			ipvodBalancerData.setDateEnd(new Date());
		}
		return balancerDAO.update(ipvodBalancerData);
	}

	@Override
	public List<IpvodBalancerData> findAll() {
		return balancerDAO.findAll();
	}

	@Override
	public IpvodBalancerData find(Long entityID) {
		return balancerDAO.find(entityID);
	}

	@Override
	public List<IpvodBalancerData> findAllValuesInProcess() {
		return balancerDAO.findAllValuesInProcess();
	}

	@Override
	public List<IpvodBalancerData> findBalancerDataByIngestId(Long ingestId) {
		return balancerDAO.findBalancerDataByIngestId(ingestId);
	}

	@Override
	public List<IpvodBalancerData> findAllInExecution() {
		return balancerDAO.findAllInExecution();
	}

}
