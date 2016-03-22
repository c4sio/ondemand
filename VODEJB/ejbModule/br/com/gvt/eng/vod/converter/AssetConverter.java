package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAssetType;
import br.com.gvt.eng.vod.model.IpvodCategory;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.eng.vod.vo.AssetVO;
import br.com.gvt.eng.vod.vo.AuthVO;

public class AssetConverter {

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param listAsset
	 * @return List<IpvodAsset>
	 */
	public List<IpvodAsset> toAssetList(List<IpvodAsset> listAsset) {
		List<IpvodAsset> listAssets = new ArrayList<IpvodAsset>();
		for (IpvodAsset ipvodAsset : listAsset) {
			listAssets.add(toAsset(ipvodAsset));
		}
		return listAssets;
	}

	/**
	 * @param listAsset
	 * @return List<IpvodAsset>
	 */
	public List<IpvodAsset> toAssetListNoPurchases(List<IpvodAsset> listAsset) {
		List<IpvodAsset> listAssets = new ArrayList<IpvodAsset>();
		for (IpvodAsset ipvodAsset : listAsset) {
			listAssets.add(toAssetNoPurchases(ipvodAsset));
		}
		return listAssets;
	}

	/**
	 * @param listAsset
	 * @return List<IpvodAsset>
	 */
	public List<IpvodAsset> toAssetIndexList(
			List<IpvodVisualMenuAsset> listAsset) {
		List<IpvodAsset> listAssets = new ArrayList<IpvodAsset>();
		for (IpvodVisualMenuAsset ipvodAsset : listAsset) {
			listAssets.add(toAssetNoPurchasesNoMediaAsset(ipvodAsset
					.getIpvodAsset()));
		}
		return listAssets;
	}

	/**
	 * @param ipvodAsset
	 * @return IpvodAsset
	 */
	public IpvodAsset toAsset(IpvodAsset ipvodAsset) {
		IpvodAsset assetValue = null;
		MediaAssetConverter mediaAssetConverter = new MediaAssetConverter();
		try {
			assetValue = new IpvodAsset();
			assetValue.setAssetId(ipvodAsset.getAssetId());
			assetValue.setCanResume(ipvodAsset.isCanResume());
			assetValue.setBillingID(ipvodAsset.getBillingID());
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
			assetValue.setDubbedLanguage(ipvodAsset.getDubbedLanguage());
			assetValue.setIpvodAssetType(new IpvodAssetType());
			assetValue.getIpvodAssetType().setAssetTypeId(
					ipvodAsset.getIpvodAssetType().getAssetTypeId());
			assetValue.getIpvodAssetType().setDescription(
					ipvodAsset.getIpvodAssetType().getDescription());
			assetValue.setReleaseYear(ipvodAsset.getReleaseYear());
			assetValue.setCountry(ipvodAsset.getCountry());
			assetValue.setAudioType(ipvodAsset.getAudioType());
			assetValue.setHD(ipvodAsset.isHD());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setFileSize(ipvodAsset.getFileSize());
			assetValue.setCreationDate(ipvodAsset.getCreationDate());
			assetValue.setLicenseWindowEnd(ipvodAsset.getLicenseWindowEnd());
			assetValue
					.setLicenseWindowStart(ipvodAsset.getLicenseWindowStart());
			assetValue.setProduct(ipvodAsset.getProduct());
			assetValue.setIpvodContentProvider(ipvodAsset
					.getIpvodContentProvider());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setIpvodMediaAssets(mediaAssetConverter
					.getMediaAssetList(ipvodAsset.getIpvodMediaAssets()));
			assetValue.setIsRevised(ipvodAsset.getIsRevised());
			assetValue.setIpvodPurchases(toPurchaseList(ipvodAsset
					.getIpvodPurchases()));
			if (ipvodAsset.getIpvodCategory1() != null) {
				assetValue.setIpvodCategory1(new IpvodCategory());
				assetValue.getIpvodCategory1().setDescription(
						ipvodAsset.getIpvodCategory1().getDescription());
				assetValue.getIpvodCategory1().setCategoryId(
						ipvodAsset.getIpvodCategory1().getCategoryId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetValue;
	}

	/**
	 * @param ipvodAsset
	 * @return IpvodAsset
	 */
	public IpvodAsset toAssetCMS(IpvodAsset ipvodAsset) {
		IpvodAsset assetValue = null;
		MediaAssetConverter mediaAssetConverter = new MediaAssetConverter();
		try {
			assetValue = new IpvodAsset();
			assetValue.setAssetId(ipvodAsset.getAssetId());
			assetValue.setCanResume(ipvodAsset.isCanResume());
			assetValue.setBillingID(ipvodAsset.getBillingID());
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
			assetValue.setDubbedLanguage(ipvodAsset.getDubbedLanguage());
			assetValue.setIpvodAssetType(new IpvodAssetType());
			assetValue.getIpvodAssetType().setAssetTypeId(
					ipvodAsset.getIpvodAssetType().getAssetTypeId());
			assetValue.getIpvodAssetType().setDescription(
					ipvodAsset.getIpvodAssetType().getDescription());
			assetValue.setReleaseYear(ipvodAsset.getReleaseYear());
			assetValue.setCountry(ipvodAsset.getCountry());
			assetValue.setAudioType(ipvodAsset.getAudioType());
			assetValue.setHD(ipvodAsset.isHD());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setFileSize(ipvodAsset.getFileSize());
			assetValue.setIpvodCategory1(ipvodAsset.getIpvodCategory1());
			if (ipvodAsset.getCreationDate() != null)
				assetValue.setCreationDate(new java.sql.Date(ipvodAsset
						.getCreationDate().getTime()));
			if (ipvodAsset.getLicenseWindowEnd() != null)
				assetValue.setLicenseWindowEnd(new java.sql.Date(ipvodAsset
						.getLicenseWindowEnd().getTime()));
			if (ipvodAsset.getLicenseWindowStart() != null)
				assetValue.setLicenseWindowStart(new java.sql.Date(ipvodAsset
						.getLicenseWindowStart().getTime()));

			assetValue.setProduct(ipvodAsset.getProduct());
			assetValue.setIpvodContentProvider(ipvodAsset
					.getIpvodContentProvider());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setIsRevised(ipvodAsset.getIsRevised());
			if (ipvodAsset.getIpvodCategory1() != null) {
				assetValue.setIpvodCategory1(new IpvodCategory());
				assetValue.getIpvodCategory1().setDescription(
						ipvodAsset.getIpvodCategory1().getDescription());
				assetValue.getIpvodCategory1().setCategoryId(
						ipvodAsset.getIpvodCategory1().getCategoryId());
			}

			assetValue.setIpvodMediaAssets(mediaAssetConverter
					.getMediaAssetList(ipvodAsset.getIpvodMediaAssets()));

			assetValue.setIpvodVisualMenus(new ArrayList<IpvodVisualMenu>());
			for (IpvodVisualMenuAsset m : ipvodAsset.getIpvodVisualMenuAsset()) {
				IpvodVisualMenu menu = new IpvodVisualMenu();
				menu.setMenuId(m.getIpvodVisualMenu().getMenuId());
				menu.setName(m.getIpvodVisualMenu().getName());
				menu.setZindex(m.getZindex());
				assetValue.getIpvodVisualMenus().add(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetValue;
	}

	/**
	 * @param ipvodAsset
	 * @return IpvodAsset
	 */
	public IpvodAsset toAssetNoPurchases(IpvodAsset ipvodAsset) {
		IpvodAsset assetValue = null;
		MediaAssetConverter mediaAssetConverter = new MediaAssetConverter();
		try {
			assetValue = new IpvodAsset();
			assetValue.setAssetId(ipvodAsset.getAssetId());
			assetValue.setCanResume(ipvodAsset.isCanResume());
			assetValue.setBillingID(ipvodAsset.getBillingID());
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
			assetValue.setDubbedLanguage(ipvodAsset.getDubbedLanguage());
			assetValue.setReleaseYear(ipvodAsset.getReleaseYear());
			assetValue.setCountry(ipvodAsset.getCountry());
			assetValue.setAudioType(ipvodAsset.getAudioType());
			assetValue.setHD(ipvodAsset.isHD());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setFileSize(ipvodAsset.getFileSize());
			assetValue.setCreationDate(ipvodAsset.getCreationDate());
			assetValue.setLicenseWindowEnd(ipvodAsset.getLicenseWindowEnd());
			assetValue
					.setLicenseWindowStart(ipvodAsset.getLicenseWindowStart());
			assetValue.setProduct(ipvodAsset.getProduct());
			assetValue.setIpvodContentProvider(ipvodAsset
					.getIpvodContentProvider());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setIpvodMediaAssets(mediaAssetConverter
					.getMediaAssetList(ipvodAsset.getIpvodMediaAssets()));
			assetValue.setIsRevised(ipvodAsset.getIsRevised());
			if (ipvodAsset.getIpvodCategory1() != null) {
				assetValue.setIpvodCategory1(new IpvodCategory());
				assetValue.getIpvodCategory1().setDescription(
						ipvodAsset.getIpvodCategory1().getDescription());
				assetValue.getIpvodCategory1().setCategoryId(
						ipvodAsset.getIpvodCategory1().getCategoryId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetValue;
	}

	/**
	 * @param ipvodAsset
	 * @return IpvodAsset
	 */
	public IpvodAsset toAssetNoPurchasesNoMediaAsset(IpvodAsset ipvodAsset) {
		IpvodAsset assetValue = null;
		try {
			assetValue = new IpvodAsset();
			assetValue.setAssetId(ipvodAsset.getAssetId());
			assetValue.setCanResume(ipvodAsset.isCanResume());
			assetValue.setBillingID(ipvodAsset.getBillingID());
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
			assetValue.setDubbedLanguage(ipvodAsset.getDubbedLanguage());
			assetValue.setReleaseYear(ipvodAsset.getReleaseYear());
			assetValue.setCountry(ipvodAsset.getCountry());
			assetValue.setAudioType(ipvodAsset.getAudioType());
			assetValue.setHD(ipvodAsset.isHD());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setFileSize(ipvodAsset.getFileSize());
			assetValue.setCreationDate(ipvodAsset.getCreationDate());
			assetValue.setLicenseWindowEnd(ipvodAsset.getLicenseWindowEnd());
			assetValue
					.setLicenseWindowStart(ipvodAsset.getLicenseWindowStart());
			assetValue.setProduct(ipvodAsset.getProduct());
			assetValue.setIpvodContentProvider(ipvodAsset
					.getIpvodContentProvider());
			assetValue.setRating(ipvodAsset.getRating());
			assetValue.setIsRevised(ipvodAsset.getIsRevised());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetValue;
	}

	private List<IpvodPurchase> toPurchaseList(
			List<IpvodPurchase> ipvodPurchases) {
		List<IpvodPurchase> purchases = new ArrayList<IpvodPurchase>();
		for (IpvodPurchase p : ipvodPurchases) {
			purchases.add(toPurchase(p));
		}
		return purchases;
	}

	private IpvodPurchase toPurchase(IpvodPurchase purchase) {
		IpvodPurchase p = new IpvodPurchase();
		p.setAmountPaid(purchase.getAmountPaid());
		p.setBilled(purchase.getBilled());
		p.setPurchaseDate(purchase.getPurchaseDate());
		p.setPurchaseId(purchase.getPurchaseId());
		p.setValidUntil(purchase.getValidUntil());
		return p;
	}

	/**
	 * 
	 * @author Cassiano R. Tesseroli
	 * @since 08/01/2014
	 * 
	 */
	public List<AssetVO> toAssetVO(List<IpvodAsset> listAsset) {
		List<AssetVO> listAssetVO = new ArrayList<AssetVO>();
		AssetVO assetVO = new AssetVO();
		for (IpvodAsset asset : listAsset) {
			assetVO = new AssetVO();
			assetVO.setId(asset.getAssetId());
			assetVO.setDescription(asset.getDescription());
			assetVO.setTitle(asset.getTitle());
			assetVO.setOriginalTitle(asset.getOriginalTitle());
			assetVO.setDirector(asset.getDirector());
			assetVO.setDuration(asset.getTotalTime());
			assetVO.setEpisode(asset.getEpisode());
			assetVO.setEpisodeName(asset.getEpisodeName());
			assetVO.setPrice(asset.getPrice());
			assetVO.setSeason(asset.getSeason());
			assetVO.setLanguages(asset.getLanguages());
			assetVO.setSubtitles(asset.getSubtitles());
			assetVO.setDubbedLanguage(asset.getDubbedLanguage());
			assetVO.setType(asset.getIpvodAssetType().getDescription());
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, asset.getReleaseYear());
			assetVO.setRelease(calendar.getTime());
			assetVO.setYear(asset.getReleaseYear());
			assetVO.setCountry(asset.getCountry());
			assetVO.setAudioType(asset.getAudioType());
			assetVO.setHD(asset.isHD());
			assetVO.setRating(asset.getRating().getRating());
			assetVO.setGenre(asset.getIpvodCategory1().getDescription());
			assetVO.setPreview(isPreview(asset.getIpvodMediaAssets()));
			listAssetVO.add(assetVO);
		}
		return listAssetVO;
	}

	/**
	 * 
	 * @author Cassiano R. Tesseroli
	 * @since 08/01/2014
	 * 
	 */
	public String toAssetJson(List<IpvodAsset> listAsset) {
		String result = "";
		try {
			result = mapper.writeValueAsString(toAssetVO(listAsset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * TEST
	 */
	public String vo(AuthVO auth) {
		String result = "";
		try {
			result = mapper.writeValueAsString(auth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean isPreview(List<IpvodMediaAsset> ipvodMediaAssets) {
		for (IpvodMediaAsset ipvodMediaAsset : ipvodMediaAssets) {
			if (ipvodMediaAsset.getIpvodMediaType() != null
					&& ipvodMediaAsset.getIpvodMediaType().getMediaTypeId() == 2)
				return true;
		}
		return false;
	}
}