package br.com.gvt.vod.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.ReasonDAO;
import br.com.gvt.eng.vod.model.IpvodReason;
import br.com.gvt.vod.facade.ReasonFacade;

@Stateless
public class ReasonFacadeImp implements ReasonFacade {

	@EJB
	private ReasonDAO reasonDAO;

	@Override
	public IpvodReason findDataByReasonCode(String reasonCode) {
		return reasonDAO.findDataByReasonCode(reasonCode);
	}

}
