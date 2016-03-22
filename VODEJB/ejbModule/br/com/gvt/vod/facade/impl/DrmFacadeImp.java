package br.com.gvt.vod.facade.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.DrmDAO;
import br.com.gvt.eng.vod.model.IpvodDrmData;
import br.com.gvt.vod.facade.DrmFacade;

@Stateless
public class DrmFacadeImp implements DrmFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private DrmDAO drmDAO;

	@Override
	public void save(IpvodDrmData ipvodDrmData) {
		ipvodDrmData.setDateStart(new Date());
		drmDAO.save(ipvodDrmData);
	}

	@Override
	public void delete(IpvodDrmData ipvodDrmData) {
		drmDAO.deleteDrm(ipvodDrmData);
	}

	@Override
	public IpvodDrmData update(IpvodDrmData ipvodDrmData) {
		if (ipvodDrmData.getStatusDrm().equalsIgnoreCase("Completed")) {
			ipvodDrmData.setDateEndDrm(new Date());
		}
		return drmDAO.update(ipvodDrmData);
	}

	@Override
	public List<IpvodDrmData> findAll() {
		return drmDAO.findAll();
	}

	@Override
	public IpvodDrmData find(Long entityID) {
		return drmDAO.find(entityID);
	}

	@Override
	public List<IpvodDrmData> findAllLessCompletedStatus() {
		return drmDAO.findAllLessCompletedStatus();
	}

	@Override
	public List<IpvodDrmData> findDrmDataByIngestId(Long ingestId) {
		return drmDAO.findDrmDataByIngestId(ingestId);
	}
}