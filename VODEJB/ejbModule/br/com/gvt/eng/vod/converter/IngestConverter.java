package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodIngestStage;

public class IngestConverter {

	/**
	 * @param listIngest
	 * @return List<IpvodIngestStage>
	 */
	public List<IpvodIngestStage> getIngestList(
			List<IpvodIngestStage> listIngest) {
		List<IpvodIngestStage> finalListIngest = new ArrayList<IpvodIngestStage>();
		try {
			// Se for nulo retorna um array vazio
			if (listIngest == null) {
				return finalListIngest;
			}
			// Varre a lista para buscar os dados
			for (IpvodIngestStage ipvodIngestStage : listIngest) {
				finalListIngest.add(toIngest(ipvodIngestStage));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListIngest;
	}

	/**
	 * @param ingestData
	 * @return ipvodIngestStage
	 */
	public IpvodIngestStage toIngest(IpvodIngestStage ingestData) {
		IpvodIngestStage ipvodIngestStage = null;
		AssetConverter assetConverter = new AssetConverter();
		try {
			ipvodIngestStage = new IpvodIngestStage();
			ipvodIngestStage.setId(ingestData.getId());
			ipvodIngestStage.setAssetInfo(ingestData.getAssetInfo());
			ipvodIngestStage.setResult(ingestData.getResult());
			ipvodIngestStage.setStageType(ingestData.getStageType());
			ipvodIngestStage.setDtInsert(ingestData.getDtInsert());
			ipvodIngestStage.setDtChange(ingestData.getDtChange());
			ipvodIngestStage.setAdicionalInfo(ingestData.getAdicionalInfo());
			ipvodIngestStage.setPriority(ingestData.getPriority());
			ipvodIngestStage.setCleanup(ingestData.isCleanup());
			ipvodIngestStage.setFtpPath(ingestData.getFtpPath());
			ipvodIngestStage.setAssetName(ingestData.getAssetName());

			// Dados de assets
			if (ingestData.getIpvodAsset() != null) {
				ipvodIngestStage.setIpvodAsset(assetConverter
						.toAsset(ingestData.getIpvodAsset()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodIngestStage;
	}

}
