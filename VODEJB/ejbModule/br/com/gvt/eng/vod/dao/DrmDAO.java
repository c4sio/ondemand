package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodDrmData;

@Stateless
public class DrmDAO extends GenericDAO<IpvodDrmData> {

	public DrmDAO() {
		super(IpvodDrmData.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodDrmData
	 */
	public void deleteDrm(IpvodDrmData ipvodDrmData) {
		super.delete(ipvodDrmData.getDrmId(), IpvodDrmData.class);
	}

	/**
	 * Busca todos os registros que nao foram finalizados no DRM
	 * 
	 * @return List<IpvodDrmData>
	 */
	public List<IpvodDrmData> findAllLessCompletedStatus() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("state", "Completed");
		return super.findResultByParameter(
				IpvodDrmData.ALL_DATA_LESS_COMPLETED, parameters);
	}

	/**
	 * Busca todos os registros que tem o mesmo IngestId no DRM
	 * 
	 * @param ingestId
	 * @return List<IpvodDrmData>
	 */
	public List<IpvodDrmData> findDrmDataByIngestId(Long ingestId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ingestId", ingestId);
		return super.findResultByParameter(IpvodDrmData.ALL_DATA_BY_INGESTID,
				parameters);
	}

}
