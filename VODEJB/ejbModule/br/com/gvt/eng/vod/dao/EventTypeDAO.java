package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodEventType;

@Stateless
public class EventTypeDAO extends GenericDAO<IpvodEventType> {

	public EventTypeDAO() {
		super(IpvodEventType.class);
	}

	/**
	 * Busca registro na tabela @IpvodEventType pelo eventTypeName
	 * 
	 * @return IpvodEventType
	 */
	public IpvodEventType findDataByEventTypeName(String eventTypeName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventTypeName", eventTypeName);
		return super.findOneResult(IpvodEventType.FIND_DATA_BY_EVENT_TYPE_NAME,
				parameters);
	}

}
