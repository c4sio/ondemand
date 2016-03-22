package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.HistoryDAO;
import br.com.gvt.eng.vod.model.HistoryTypeEnum;
import br.com.gvt.eng.vod.model.IpvodHistory;
import br.com.gvt.vod.facade.HistoryFacade;

@Stateless
public class HistoryFacadeImp implements HistoryFacade {

	@EJB
	private HistoryDAO historyDAO;
	
	@Override
	public void save(IpvodHistory ipvodHistory) {
		historyDAO.save(ipvodHistory);
	}
	
	@Override
	public List<IpvodHistory> listAssetType(HistoryTypeEnum type, long itemId) {
		return historyDAO.listAssetType(type, itemId);
	}


}