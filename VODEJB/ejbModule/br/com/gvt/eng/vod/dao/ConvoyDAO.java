package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodConvoyData;

@Stateless
public class ConvoyDAO extends GenericDAO<IpvodConvoyData> {

	public ConvoyDAO() {
		super(IpvodConvoyData.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodConvoyData
	 */
	public void deleteConvoy(IpvodConvoyData ipvodConvoyData) {
		super.delete(ipvodConvoyData.getConvoyId(), IpvodConvoyData.class);
	}

	/**
	 * Busca todos os registros que nao foram finalizados no Convoy
	 * 
	 * @return List<IpvodConvoyData>
	 */
	public List<IpvodConvoyData> findAllLessDoneStatus() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("state", "done");
		return super.findResultByParameter(IpvodConvoyData.ALL_DATA_LESS_DONE,
				parameters);
	}

	/**
	 * Busca todos os registros que tem o mesmo IngestId no Convoy
	 * 
	 * @return List<IpvodConvoyData>
	 */
	public List<IpvodConvoyData> findConvoyDataByIngestId(long ingestId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ingestId", ingestId);
		return super.findResultByParameter(
				IpvodConvoyData.ALL_DATA_BY_INGESTID, parameters);
	}

}
