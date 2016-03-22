package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.dao.AssetDAO;
import br.com.gvt.eng.vod.dao.ElasticSearchDAO;
import br.com.gvt.eng.vod.dao.HistoryDAO;
import br.com.gvt.eng.vod.dao.IngestDAO;
import br.com.gvt.eng.vod.dao.LikeOperator;
import br.com.gvt.eng.vod.dao.MediaAssetDAO;
import br.com.gvt.eng.vod.dao.MenuDAO;
import br.com.gvt.eng.vod.dao.PurchaseDAO;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.util.Utils;
import br.com.gvt.eng.vod.vo.it.OnDemandContentVO;
import br.com.gvt.vod.facade.AssetFacade;

@Stateless
public class AssetFacadeImp implements AssetFacade {

	@EJB
	private AssetDAO assetDAO;

	@EJB
	private MediaAssetDAO mediaAssetDAO;

	@EJB
	private IngestDAO ingestDAO;

	@EJB
	private PurchaseDAO purchaseDAO;

	@EJB
	private MenuDAO menuDAO;

	@EJB
	private HistoryDAO historyDAO;

	@EJB
	private ElasticSearchDAO elasticSearchDAO;

	@Override
	public void save(IpvodAsset ipvodAsset) {
		isAssetWithAllData(ipvodAsset);
		assetDAO.save(ipvodAsset);
	}

	@Override
	public void delete(IpvodAsset ipvodAsset) {
		assetDAO.deleteAsset(ipvodAsset);
	}

	@Override
	public IpvodAsset update(IpvodAsset ipvodAsset) {
		isAssetWithAllData(ipvodAsset);
		for (IpvodMediaAsset mediaAsset : ipvodAsset.getIpvodMediaAssets()) {
			mediaAsset.setIpvodAsset(ipvodAsset);
		}

		IpvodIngestStage ingest = ingestDAO.findByAssetId(ipvodAsset
				.getAssetId());
		if (ingest != null && ingest.getStageType().getId() == 7) {
			ipvodAsset.setIsRevised(true);
		}

		IpvodAsset newIpvodAsset = assetDAO.update(ipvodAsset);

		return newIpvodAsset;
	}

	@Override
	public IpvodAsset updateNotRevised(IpvodAsset ipvodAsset) {
		isAssetWithAllData(ipvodAsset);
		for (IpvodMediaAsset mediaAsset : ipvodAsset.getIpvodMediaAssets()) {
			mediaAsset.setIpvodAsset(ipvodAsset);
		}

		IpvodIngestStage ingest = ingestDAO.findByAssetId(ipvodAsset
				.getAssetId());

		return assetDAO.update(ipvodAsset);
	}

	@Override
	public List<IpvodAsset> findAll() {
		List<IpvodAsset> listIpvodAsset = new ArrayList<IpvodAsset>();
		listIpvodAsset = assetDAO.findAll();
		// Evita o Lazy Exception
		for (IpvodAsset ipvodAsset : listIpvodAsset) {
			ipvodAsset.getIpvodAssetPackages().size();
			// ipvodAsset.getIpvodEquipmentTypes().size();
			ipvodAsset.getIpvodMediaAssets().size();
			ipvodAsset.getIpvodVisualMenus().size();
			ipvodAsset.getIpvodPurchases().size();
		}
		return listIpvodAsset;
	}

	@Override
	public IpvodAsset find(long entityID) {
		IpvodAsset ipvodAsset = new IpvodAsset();
		ipvodAsset = assetDAO.find(entityID);
		// Evita o Lazy Exception
		if (ipvodAsset != null) {
			ipvodAsset.getIpvodAssetPackages().size();
			// ipvodAsset.getIpvodEquipmentTypes().size();
			ipvodAsset.getIpvodMediaAssets().size();
			ipvodAsset.getIpvodVisualMenus().size();
			ipvodAsset.getIpvodPurchases().size();
		}
		return ipvodAsset;
	}

	@Override
	public List<IpvodAsset> find_teste(long entityID) {
		List<IpvodAsset> tete = new ArrayList<IpvodAsset>();
		tete.add(assetDAO.find(entityID));
		return tete;
	}

	@Override
	public List<IpvodAsset> listAssetByCategory(long categoryId) {
		return assetDAO.listAssetByCategory(categoryId);
	}

	@Override
	public List<IpvodAsset> listAssetByInicialWord(String word) {
		return assetDAO.listAssetByInicialWord(word);
	}

	@Override
	public List<IpvodAsset> listAssetType(long idAssetType) {
		return assetDAO.listAssetType(idAssetType);
	}

	@Override
	public List<IpvodAsset> listAssetByNewReleases(String category) {
		return assetDAO.listAssetByNewReleases(category);
	}

	@Override
	public List<IpvodAsset> findResultComplexQuery(UriInfo uriInfo) {
		return assetDAO.findResultComplexQuery(uriInfo);
	}

	@Override
	public Long countResultComplexQuery(UriInfo uriInfo) {
		return assetDAO.countResultComplexQuery(uriInfo);
	}

	@Override
	public List<Object> getTotalNewAssets(GregorianCalendar date) {
		return assetDAO.getTotalNewAssets(date);
	}

	/**
	 * Validação de campos - Save/Update
	 * 
	 * @param ipvodAsset
	 */
	private void isAssetWithAllData(IpvodAsset ipvodAsset) {
		boolean hasError = false;
		if (ipvodAsset == null)
			hasError = true;

		// TODO: Verificar quais campos devem ser validados no Servidor

		// if (ipvodAsset.getDescription() == null
		// || "".equals(ipvodAsset.getDescription().trim()))
		// hasError = true;
		//
		// if (ipvodAsset.getPrice() <= 0)
		// hasError = true;

		if (hasError)
			throw new IllegalArgumentException(
					"The asset is missing data. Check, they should have value.");
	}

	@Override
	public List<IpvodAsset> findAssetsByList(List<Long> assetIds, long userId,
			boolean adult) {
		List<IpvodAsset> assets = assetDAO.findAssetsByList(assetIds, userId,
				adult);
		return assets;
	}

	@Override
	public List<OnDemandContentVO> findByName(String c, LikeOperator like) {
		List<OnDemandContentVO> l = assetDAO.findByName(c, like);
		return mediaAssetDAO.findVOByAsset(l);
	}

	@Override
	public Map<String, Integer> retrieveSafeSearchTermsCatchup() {
		return assetDAO.retrieveSafeSearchTermsCatchup();
	}

	@Override
	public Map<String, Integer> retrieveAdultSearchTermsCatchup() {
		return assetDAO.retrieveAdultSearchTermsCatchup();
	}

	@Override
	public Map<String, Integer> retrieveSafeSearchTermsOnDemand() {
		return assetDAO.retrieveSafeSearchTermsOnDemand();
	}

	@Override
	public Map<String, Integer> retrieveAdultSearchTermsOnDemand() {
		return assetDAO.retrieveAdultSearchTermsOnDemand();
	}

	@Override
	public List<OnDemandContentVO> findTop50() {
		return assetDAO.findTop50();
	}

	@Override
	public List<OnDemandContentVO> findByGenre(Long genreId) {
		return mediaAssetDAO.findVOByAsset(assetDAO.findByGenre(genreId));
	}

	@Override
	public List<IpvodAsset> findAssetsByListOnDemand(List<Long> assetIds,
			long userId, boolean adult) {
		List<IpvodAsset> assets = assetDAO.findAssetsOnDemand(assetIds, userId,
				adult);
		return assets;
	}

	@Override
	public List<IpvodAsset> findAssetsByListCatchUp(List<Long> assetIds,
			long userId, boolean adult) {
		List<IpvodAsset> assets = assetDAO.findAssetsCatchUp(assetIds, userId,
				adult);
		return assets;
	}

	@Override
	public List<OnDemandContentVO> findHighlights() {
		return assetDAO.findHighlights();
	}

	@Override
	public List<OnDemandContentVO> findReleases() {
		return assetDAO.findReleases();
	}

	@Override
	public List<IpvodAsset> getMyContentOnDemand(long userId) {
		return assetDAO.getMyContentOnDemand(userId);
	}

	@Override
	public List<IpvodAsset> getMyContentOnDemandAdult(long userId) {
		return assetDAO.getMyContentOnDemandAdult(userId);
	}

	@Override
	public List<IpvodAsset> getMyContentCatchUp(long userId) {
		return assetDAO.getMyContentCatchUp(userId);
	}

	@Override
	public void saveElasticSearch(Long id, String ipvodAsset)
			throws RestException {
		elasticSearchDAO.saveElasticSearchAsset(id, ipvodAsset);
	}

	@Override
	public IpvodAsset getElasticSearch(Long id) throws RestException {
		String asset = elasticSearchDAO.getElasticSearchAsset(id.toString());
		if (asset == null) {
			return null;
		}
		return Utils.getGson().fromJson(asset, IpvodAsset.class);
	}

	@Override
	public void deleteElasticSearch(Long id) throws RestException {
		elasticSearchDAO.deleteElasticSearchAsset(id.toString());
	}
	
	@Override
	public void updateMediaAssetImage(String assetId) {
		assetDAO.updateMediaAssetImage(assetId);
	}
}