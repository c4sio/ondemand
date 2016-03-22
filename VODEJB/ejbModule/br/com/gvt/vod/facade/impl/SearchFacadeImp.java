package br.com.gvt.vod.facade.impl;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.dao.ElasticSearchDAO;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.SearchFacade;

@Stateless
public class SearchFacadeImp implements SearchFacade {

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private ElasticSearchDAO elasticSearchDAO;

	@Override
	public List<IpvodAsset> findAssets(IpvodUser ipvodUser, UriInfo uriInfo)
			throws RestException {
		List<Long> ids = elasticSearchDAO.searchIdsByText(uriInfo);
		if (ids == null || ids.isEmpty()) {
			throw RestException.NO_CONTENT;
		}
		
		String adult = uriInfo.getQueryParameters().getFirst("adult");
		
		boolean adultFlag = false;
		if (adult != null) {
			adultFlag = true;
		}
		
		List<IpvodAsset> assets = assetFacade.findAssetsByList(ids, ipvodUser.getUserId(), adultFlag);
		
		return assets ;
	}

	@Override
	public List<IpvodAsset> findAssetsByListOnDemand(IpvodUser ipvodUser,
			UriInfo uriInfo) throws RestException {
		List<Long> ids = elasticSearchDAO.searchIdsByText(uriInfo);
		
		if (ids == null || ids.isEmpty()) {
			throw RestException.NO_CONTENT;
		}
		String adult = uriInfo.getQueryParameters().getFirst("adult");
		
		boolean adultFlag = false;
		if (adult != null) {
			adultFlag = true;
		}
		
		List<IpvodAsset> assets = assetFacade.findAssetsByListOnDemand(ids, ipvodUser.getUserId(), adultFlag);
		return assets;
	}

	@Override
	public List<IpvodAsset> findAssetsByListCatchUp(IpvodUser ipvodUser,
			UriInfo uriInfo) throws RestException {
		List<Long> ids = elasticSearchDAO.searchIdsByText(uriInfo);
		
		if (ids == null || ids.isEmpty()) {
			throw RestException.NO_CONTENT;
		}
		String adult = uriInfo.getQueryParameters().getFirst("adult");
		
		boolean adultFlag = false;
		if (adult != null) {
			adultFlag = true;
		}
		
		List<IpvodAsset> assets = assetFacade.findAssetsByListCatchUp(ids, ipvodUser.getUserId(), adultFlag);
		return assets;
	}

	@Override
	public void updateAdultSearchTermsCatchup() throws RestException {
		Map<String, Integer> value = assetFacade.retrieveAdultSearchTermsCatchup();
		updateSearchTerms(value, IpvodConstants.SEARCH_TERMS_CATCHUP_ADULT);
	}
	
	@Override
	public void updateSafeSearchTermsCatchup() throws RestException {
		Map<String, Integer> value = assetFacade.retrieveSafeSearchTermsCatchup();
		updateSearchTerms(value, IpvodConstants.SEARCH_TERMS_CATCHUP_SAFE);
	}
	
	@Override
	public void updateAdultSearchTermsOnDemand() throws RestException {
		Map<String, Integer> value = assetFacade.retrieveAdultSearchTermsOnDemand();
		updateSearchTerms(value, IpvodConstants.SEARCH_TERMS_ONDEMAND_ADULT);
	}
	
	@Override
	public void updateSafeSearchTermsOnDemand() throws RestException {
		Map<String, Integer> value = assetFacade.retrieveSafeSearchTermsOnDemand();
		updateSearchTerms(value, IpvodConstants.SEARCH_TERMS_ONDEMAND_SAFE);
	}
	
	private void updateSearchTerms(Map<String, Integer> value, String id)
			throws RestException {
		elasticSearchDAO.updateElasticSearchTerms(value, id);
	}

	@Override
	public String getAdultSearchTermsCatchup() throws RestException {
		return elasticSearchDAO.getSearchTerms(IpvodConstants.SEARCH_TERMS_CATCHUP_ADULT);
	}
	
	@Override
	public String getSafeSearchTermsCatchup() throws RestException {
		return elasticSearchDAO.getSearchTerms(IpvodConstants.SEARCH_TERMS_CATCHUP_SAFE);
	}
	
	@Override
	public String getAdultSearchTermsOnDemand() throws RestException {
		return elasticSearchDAO.getSearchTerms(IpvodConstants.SEARCH_TERMS_ONDEMAND_ADULT);
	}
	
	@Override
	public String getSafeSearchTermsOnDemand() throws RestException {
		return elasticSearchDAO.getSearchTerms(IpvodConstants.SEARCH_TERMS_ONDEMAND_SAFE);
	}
}