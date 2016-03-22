package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodReason;

@Stateless
public class ReasonDAO extends GenericDAO<IpvodReason> {

	public ReasonDAO() {
		super(IpvodReason.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodReason
	 */
	public void deleteReason(IpvodReason ipvodReason) {
		super.delete(ipvodReason.getReasonID(), IpvodReason.class);
	}

	/**
	 * Busca registro na tabela @IpvodReason pelo reasonCode
	 * 
	 * @return IpvodReason
	 */
	public IpvodReason findDataByReasonCode(String reasonCode) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("reasonCode", reasonCode);
		return super.findOneResult(IpvodReason.FIND_DATA_BY_REASON_CODE,
				parameters);
	}
}
