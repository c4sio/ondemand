package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodDrmData;

public class DrmConverter {

	/**
	 * @param listIngest
	 * @return List<IpvodDrmData>
	 */
	public List<IpvodDrmData> getDrmList(List<IpvodDrmData> listDrm) {
		List<IpvodDrmData> finalListDrm = new ArrayList<IpvodDrmData>();
		try {
			// Se for nulo retorna um array vazio
			if (listDrm == null) {
				return finalListDrm;
			}
			// Varre a lista para buscar os dados
			for (IpvodDrmData ipvodDrmData : listDrm) {
				finalListDrm.add(toDrm(ipvodDrmData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListDrm;
	}

	/**
	 * @param ipvodDrmData
	 * @return IpvodDrmData
	 */
	public IpvodDrmData toDrm(IpvodDrmData ipvodDrmData) {
		IpvodDrmData drmData = null;
		IngestConverter ingestConverter = new IngestConverter();
		try {
			drmData = new IpvodDrmData();
			drmData.setDrmId(ipvodDrmData.getDrmId());
			drmData.setDateStart(ipvodDrmData.getDateStart());
			drmData.setDateEndDrm(ipvodDrmData.getDateEndDrm());
			drmData.setJobIdDrm(ipvodDrmData.getJobIdDrm());
			drmData.setNameFile(ipvodDrmData.getNameFile());
			drmData.setCookieDrm(ipvodDrmData.getCookieDrm());
			drmData.setPercentCompDrm(ipvodDrmData.getPercentCompDrm());
			drmData.setSendMail(ipvodDrmData.isSendMail());
			drmData.setStatusDrm(ipvodDrmData.getStatusDrm());

			// Dados de Ingest
			if (ipvodDrmData.getIpvodIngestStage() != null) {
				drmData.setIpvodIngestStage(ingestConverter
						.toIngest(ipvodDrmData.getIpvodIngestStage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drmData;
	}
}
