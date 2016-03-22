package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.SchedulePublicationDAO;
import br.com.gvt.eng.vod.model.IpvodSchedulePublication;
import br.com.gvt.vod.facade.SchedulePublicationFacade;

@Stateless
public class SchedulePublicationFacadeImp implements SchedulePublicationFacade {

	@EJB
	private SchedulePublicationDAO schedulePublicationDAO;
	
	@Override
	public void saveSchedule(IpvodSchedulePublication ipvodSchedulePublication) {
		schedulePublicationDAO.update(ipvodSchedulePublication);
	}

	@Override
	public IpvodSchedulePublication getScheduleByAssetId(Long assetId) {
		return schedulePublicationDAO.getScheduleByAssetId(assetId);
	}

	@Override
	public void deleteSchedule(Long assetId) {
		IpvodSchedulePublication schedule = getScheduleByAssetId(assetId);
		schedulePublicationDAO.deleteSchedule(schedule.getScheduleId());
	}

}
