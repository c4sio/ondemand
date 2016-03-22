package br.com.gvt.vod.facade;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.dao.LikeOperator;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.vo.it.OnDemandContentVO;

@Local
public interface AssetFacade {

	public abstract void save(IpvodAsset ipvodAsset);

	public abstract void delete(IpvodAsset ipvodAsset);

	public abstract IpvodAsset update(IpvodAsset ipvodAsset);

	public abstract List<IpvodAsset> findAll();

	public abstract IpvodAsset find(long entityID);

	public abstract List<IpvodAsset> find_teste(long entityID);

	public List<IpvodAsset> listAssetByCategory(long categoryId);

	public List<IpvodAsset> listAssetByInicialWord(String word);

	public List<IpvodAsset> listAssetType(long idAssetType);

	public abstract List<IpvodAsset> listAssetByNewReleases(String category);

	public List<IpvodAsset> findResultComplexQuery(UriInfo uriInfo);

	public Long countResultComplexQuery(UriInfo uriInfo);

	public List<IpvodAsset> findAssetsByList(List<Long> ids, long userId, boolean adult);
	
	public List<IpvodAsset> findAssetsByListOnDemand(List<Long> ids, long userId, boolean adult);
	
	public List<IpvodAsset> findAssetsByListCatchUp(List<Long> ids, long userId, boolean adult);

	public List<Object> getTotalNewAssets(GregorianCalendar date);

	List<OnDemandContentVO> findByName(String c, LikeOperator like);

	List<OnDemandContentVO> findTop50();

	List<OnDemandContentVO> findByGenre(Long genreId);

	List<OnDemandContentVO> findHighlights();

	List<OnDemandContentVO> findReleases();

	IpvodAsset updateNotRevised(IpvodAsset ipvodAsset);

	List<IpvodAsset> getMyContentOnDemand(long userId);

	List<IpvodAsset> getMyContentOnDemandAdult(long userId);

	List<IpvodAsset> getMyContentCatchUp(long userId);

	void saveElasticSearch(Long id, String ipvodAsset) throws RestException;

	IpvodAsset getElasticSearch(Long id) throws RestException;

	void deleteElasticSearch(Long id) throws RestException;

	Map<String, Integer> retrieveSafeSearchTermsCatchup();

	Map<String, Integer> retrieveAdultSearchTermsCatchup();

	Map<String, Integer> retrieveSafeSearchTermsOnDemand();

	Map<String, Integer> retrieveAdultSearchTermsOnDemand();

	void updateMediaAssetImage(String assetId);

}
