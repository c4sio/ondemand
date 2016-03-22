package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodConvoyData;

public class ConvoyConverter {

	/**
	 * @param listConvoy
	 * @return List<IpvodConvoyData>
	 */
	public List<IpvodConvoyData> getConvoyList(List<IpvodConvoyData> listConvoy) {
		List<IpvodConvoyData> finalListConvoy = new ArrayList<IpvodConvoyData>();
		try {
			// Se for nulo retorna um array vazio
			if (listConvoy == null) {
				return finalListConvoy;
			}
			// Varre a lista para buscar os dados
			for (IpvodConvoyData ipvodConvoyData : listConvoy) {
				finalListConvoy.add(toConvoy(ipvodConvoyData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListConvoy;
	}

	/**
	 * @param ipvodConvoyData
	 * @return IpvodConvoyData
	 */
	public IpvodConvoyData toConvoy(IpvodConvoyData ipvodConvoyData) {
		IpvodConvoyData convoyData = null;
		IngestConverter ingestConverter = new IngestConverter();
		MediaAssetConverter mediaAssetConverter = new MediaAssetConverter();
		try {
			convoyData = new IpvodConvoyData();
			convoyData.setConvoyId(ipvodConvoyData.getConvoyId());
			convoyData.setDateStart(ipvodConvoyData.getDateStart());
			convoyData.setDateEnd(ipvodConvoyData.getDateEnd());
			convoyData.setJobIdConvoy(ipvodConvoyData.getJobIdConvoy());
			convoyData.setNameFile(ipvodConvoyData.getNameFile());
			convoyData.setFileConvoy(ipvodConvoyData.getFileConvoy());
			convoyData.setPercentCompConvoy(ipvodConvoyData
					.getPercentCompConvoy());
			convoyData.setSendMail(ipvodConvoyData.isSendMail());
			convoyData.setStatusConvoy(ipvodConvoyData.getStatusConvoy());

			// Dados @IpvodMediaAsset
			convoyData.setIpvodMediaAsset(mediaAssetConverter
					.toMediaAsset(ipvodConvoyData.getIpvodMediaAsset()));

			// Dados @IpvodIngestStage
			if (ipvodConvoyData.getIpvodIngestStage() != null) {
				convoyData.setIpvodIngestStage(ingestConverter
						.toIngest(ipvodConvoyData.getIpvodIngestStage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convoyData;
	}
}
