package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAssetType;
import br.com.gvt.eng.vod.model.IpvodCategory;

public class CategoryConverter {

	/**
	 * @param listCateg
	 * @return List
	 */
	public List<IpvodCategory> getCategoryList(List<IpvodCategory> listCateg) {
		List<IpvodCategory> finalList = new ArrayList<IpvodCategory>();
		try {
			IpvodCategory c = null;
			for (IpvodCategory categ : listCateg) {
				c = new IpvodCategory();
				c.setCategoryId(categ.getCategoryId());
				c.setDescription(categ.getDescription());
				finalList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalList;
	}

	/**
	 * @param category
	 * @return IpvodCategory
	 */
	public IpvodCategory getCategoryWithAssetList(IpvodCategory category) {

		IpvodCategory categ = new IpvodCategory();
		IpvodAsset assetValue = null;

		try {
			if (category == null) {
				return null;
			}

			categ.setCategoryId(category.getCategoryId());
			categ.setDescription(category.getDescription());
			categ.setIpvodAssets1(new ArrayList<IpvodAsset>());
			categ.setIpvodAssetsCount(category.getIpvodAssetsCount());
			for (IpvodAsset ipvodAsset : category.getIpvodAssets1()) {
				assetValue = new IpvodAsset();
				assetValue.setAssetId(ipvodAsset.getAssetId());
				assetValue.setAudioType(ipvodAsset.getAudioType());
				assetValue.setDescription(ipvodAsset.getDescription());
				assetValue.setTitle(ipvodAsset.getTitle());
				assetValue.setOriginalTitle(ipvodAsset.getOriginalTitle());
				assetValue.setDirector(ipvodAsset.getDirector());
				assetValue.setActors(ipvodAsset.getActors());
				assetValue.setTotalTime(ipvodAsset.getTotalTime());
				assetValue.setEpisode(ipvodAsset.getEpisode());
				assetValue.setEpisodeName(ipvodAsset.getEpisodeName());
				assetValue.setPrice(ipvodAsset.getPrice());
				assetValue.setSeason(ipvodAsset.getSeason());
				assetValue.setLanguages(ipvodAsset.getLanguages());
				assetValue.setSubtitles(ipvodAsset.getSubtitles());
				assetValue.setIpvodAssetType(new IpvodAssetType());
				assetValue.getIpvodAssetType().setDescription(
						ipvodAsset.getIpvodAssetType().getDescription());
				assetValue.setReleaseYear(ipvodAsset.getReleaseYear());
				assetValue.setCountry(ipvodAsset.getCountry());
				assetValue.setAudioType(ipvodAsset.getAudioType());
				assetValue.setHD(ipvodAsset.isHD());
				assetValue.setRating(ipvodAsset.getRating());
				assetValue.setIpvodCategory1(new IpvodCategory());
				assetValue.getIpvodCategory1().setDescription(
						ipvodAsset.getIpvodCategory1().getDescription());
				// a.setIpvodMediaAssets(asset.getIpvodMediaAssets());
				categ.getIpvodAssets1().add(assetValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categ;
	}
}
