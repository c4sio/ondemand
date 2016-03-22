package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.HistoryTypeEnum;
import br.com.gvt.eng.vod.model.IpvodHistory;

@Stateless
public class HistoryDAO extends GenericDAO<IpvodHistory> {

	@EJB
	MenuDAO menuDAO;
	
	public HistoryDAO() {
		super(IpvodHistory.class);
	}
	
	public List<IpvodHistory> listAssetType(HistoryTypeEnum type, long itemId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("itemId", itemId);
		parameters.put("type", type);

		return super.findResultByParameter(IpvodHistory.FIND_BY_TYPE, parameters);
	}

	
}
