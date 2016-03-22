package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodMediaAsset;

public class MediaAssetConverter {

	/**
	 * @param listMediaAsset
	 * @return List<IpvodMediaAsset>
	 */
	public List<IpvodMediaAsset> getMediaAssetList(
			List<IpvodMediaAsset> listMediaAsset) {
		List<IpvodMediaAsset> finalListMediaAsset = new ArrayList<IpvodMediaAsset>();
		try {
			// Se for nulo retorna um array vazio
			if (listMediaAsset == null) {
				return finalListMediaAsset;
			}
			// Varre a lista para buscar os dados
			for (IpvodMediaAsset ipvodMediaAsset : listMediaAsset) {
				finalListMediaAsset.add(toMediaAsset(ipvodMediaAsset));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListMediaAsset;
	}

	/**
	 * @param ipvodMediaAsset
	 * @return IpvodMediaAsset
	 */
	public IpvodMediaAsset toMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		IpvodMediaAsset mediaAssetData = null;
		MediaTypeConverter mediaTypeConverter = new MediaTypeConverter();
		try {
			mediaAssetData = new IpvodMediaAsset();
			mediaAssetData.setMediaAssetId(ipvodMediaAsset.getMediaAssetId());
			mediaAssetData.setUrl(ipvodMediaAsset.getUrl());
			if (ipvodMediaAsset.getIpvodMediaType() != null) {
				mediaAssetData.setIpvodMediaType(mediaTypeConverter
						.toMediaType(ipvodMediaAsset.getIpvodMediaType()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mediaAssetData;
	}
}