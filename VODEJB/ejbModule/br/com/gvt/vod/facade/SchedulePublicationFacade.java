package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodSchedulePublication;

@Local
public interface SchedulePublicationFacade {

	void saveSchedule(IpvodSchedulePublication ipvodSchedulePublication);

	IpvodSchedulePublication getScheduleByAssetId(Long assetId);
	
	void deleteSchedule(Long assetId);
}
