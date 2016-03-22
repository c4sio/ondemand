package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodSession;

public class PurchaseConverter {

	/**
	 * @param listPurchase
	 * @return List<IpvodPurchase>
	 */
	public List<IpvodPurchase> getPurchaseList(List<IpvodPurchase> listPurchase) {
		List<IpvodPurchase> finalListPurchase = new ArrayList<IpvodPurchase>();
		try {
			// Se for nulo retorna um array vazio
			if (listPurchase == null) {
				return finalListPurchase;
			}
			// Varre a lista para buscar os dados
			for (IpvodPurchase ipvodPurchase : listPurchase) {
				finalListPurchase.add(toPurchase(ipvodPurchase));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListPurchase;
	}

	/**
	 * @param purchaseData
	 * @return IpvodPurchase
	 */
	public IpvodPurchase toPurchase(IpvodPurchase purchaseData) {
		IpvodPurchase ipvodPurchase = null;
		try {
			ipvodPurchase = new IpvodPurchase();
			ipvodPurchase.setPurchaseId(purchaseData.getPurchaseId());
			ipvodPurchase.setIpvodEquipment(new IpvodEquipment());
			if (ipvodPurchase.getIpvodEquipment() != null) {
				ipvodPurchase.getIpvodEquipment().setEquipmentId(
						purchaseData.getIpvodEquipment().getEquipmentId());
			}

			ipvodPurchase.setIpvodAsset(new IpvodAsset());
			if (ipvodPurchase.getIpvodAsset() != null) {
				ipvodPurchase.getIpvodAsset().setAssetId(
						purchaseData.getIpvodAsset().getAssetId());
			}

			ipvodPurchase.setPurchaseDate(purchaseData.getPurchaseDate());
			ipvodPurchase.setValidUntil(purchaseData.getValidUntil());
			ipvodPurchase.setBilled(purchaseData.getBilled());
			ipvodPurchase.setAmountPaid(purchaseData.getAmountPaid());

			if (purchaseData.getIpvodSessions() != null
					&& !purchaseData.getIpvodSessions().isEmpty()) {
				List<IpvodSession> listSession = new ArrayList<IpvodSession>();
				IpvodSession ipvodSession = new IpvodSession();
				for (IpvodSession ipvodSessionValue : purchaseData
						.getIpvodSessions()) {
					ipvodSession = new IpvodSession();
					ipvodSession.setSessionId(ipvodSessionValue.getSessionId());
					ipvodSession.setEventDate(ipvodSessionValue.getEventDate());
					ipvodSession.setIpvodReason(ipvodSessionValue
							.getIpvodReason());
					ipvodSession.setResponsecode(ipvodSessionValue
							.getResponsecode());
					ipvodSession.setPlayTime(ipvodSessionValue.getPlayTime());
					listSession.add(ipvodSession);
				}
				ipvodPurchase.setIpvodSessions(listSession);
			}
		} catch (Exception e) {
			// ignore
			e.printStackTrace();
		}
		return ipvodPurchase;
	}
}
