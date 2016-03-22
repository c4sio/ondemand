package br.com.gvt.vod.facade.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.ConvoyDAO;
import br.com.gvt.eng.vod.model.IpvodConvoyData;
import br.com.gvt.vod.facade.ConvoyFacade;

@Stateless
public class ConvoyFacadeImp implements ConvoyFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private ConvoyDAO convoyDAO;

	@Override
	public void save(IpvodConvoyData ipvodConvoyData) {
		ipvodConvoyData.setDateStart(new Date());
		convoyDAO.save(ipvodConvoyData);
	}

	@Override
	public void delete(IpvodConvoyData ipvodConvoyData) {
		convoyDAO.deleteConvoy(ipvodConvoyData);
	}

	@Override
	public IpvodConvoyData update(IpvodConvoyData ipvodConvoyData) {
		if (ipvodConvoyData.getStatusConvoy().equalsIgnoreCase("done")) {
			ipvodConvoyData.setDateEnd(new Date());
		}
		return convoyDAO.update(ipvodConvoyData);
	}

	@Override
	public List<IpvodConvoyData> findAll() {
		return convoyDAO.findAll();
	}

	@Override
	public IpvodConvoyData find(Long entityID) {
		return convoyDAO.find(entityID);
	}

	@Override
	public List<IpvodConvoyData> findConvoyDataByIngestId(Long ingestId) {
		return convoyDAO.findConvoyDataByIngestId(ingestId);
	}

	@Override
	public List<IpvodConvoyData> findAllLessDoneStatus() {
		return convoyDAO.findAllLessDoneStatus();
	}

}
