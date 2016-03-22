package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodSchedulePublication;

@Stateless
public class SchedulePublicationDAO extends GenericDAO<IpvodSchedulePublication> {

	public SchedulePublicationDAO() {
		super(IpvodSchedulePublication.class);
	}

	public IpvodSchedulePublication getScheduleByAssetId(Long assetId) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tempDataId", assetId.toString());

		List<IpvodSchedulePublication> list = super.findResultByParameter(IpvodSchedulePublication.FIND_BY_TEMP_DATA_ID, parameters);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public void deleteSchedule(Long scheduleId) {
		super.delete(scheduleId, IpvodSchedulePublication.class);
	}
}
