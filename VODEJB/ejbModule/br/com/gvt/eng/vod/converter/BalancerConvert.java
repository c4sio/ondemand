package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodBalancerData;

public class BalancerConvert {

	/**
	 * @param listBalancer
	 * @return List<IpvodBalancerData>
	 */
	public List<IpvodBalancerData> getBalancerList(
			List<IpvodBalancerData> listBalancer) {
		List<IpvodBalancerData> finalListBalancer = new ArrayList<IpvodBalancerData>();
		try {
			// Se for nulo retorna um array vazio
			if (listBalancer == null) {
				return finalListBalancer;
			}
			// Varre a lista para buscar os dados
			for (IpvodBalancerData ipvodBalancerData : listBalancer) {
				finalListBalancer.add(toBalancer(ipvodBalancerData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListBalancer;
	}

	/**
	 * @param ipvodBalancerData
	 * @return IpvodBalancerData
	 */
	public IpvodBalancerData toBalancer(IpvodBalancerData ipvodBalancerData) {
		IpvodBalancerData balancerData = null;
		IngestConverter ingestConverter = new IngestConverter();
		try {
			balancerData = new IpvodBalancerData();
			balancerData.setId(ipvodBalancerData.getId());
			balancerData.setJobId(ipvodBalancerData.getJobId());
			balancerData.setDateStart(ipvodBalancerData.getDateStart());
			balancerData.setDateEnd(ipvodBalancerData.getDateEnd());
			balancerData.setNameFile(ipvodBalancerData.getNameFile());
			balancerData.setPercentComp(ipvodBalancerData.getPercentComp());
			balancerData.setSendMail(ipvodBalancerData.isSendMail());
			balancerData.setStatus(ipvodBalancerData.getStatus());
			balancerData.setPresetId(ipvodBalancerData.getPresetId());

			// Dados de Ingest
			if (ipvodBalancerData.getIpvodIngestStage() != null) {
				balancerData.setIpvodIngestStage(ingestConverter
						.toIngest(ipvodBalancerData.getIpvodIngestStage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balancerData;
	}
}