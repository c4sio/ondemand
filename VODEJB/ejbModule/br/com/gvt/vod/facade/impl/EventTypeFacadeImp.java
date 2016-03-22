package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.EventTypeDAO;
import br.com.gvt.eng.vod.model.IpvodEventType;
import br.com.gvt.vod.facade.EventTypeFacade;

@Stateless
public class EventTypeFacadeImp implements EventTypeFacade {

	@EJB
	private EventTypeDAO eventTypeDAO;

	@Override
	public IpvodEventType findDataByEventTypeName(String eventTypeName) {
		return eventTypeDAO.findDataByEventTypeName(eventTypeName);
	}

}
