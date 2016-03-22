package br.com.gvt.vod.facade;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodUser;

@Local
public interface SearchFacade {
	
	List<IpvodAsset> findAssets(IpvodUser ipvodUser, UriInfo uriInfo) throws RestException;
	
	List<IpvodAsset> findAssetsByListOnDemand(IpvodUser ipvodUser, UriInfo uriInfo) throws RestException;
	
	List<IpvodAsset> findAssetsByListCatchUp(IpvodUser ipvodUser, UriInfo uriInfo) throws RestException;

	void updateAdultSearchTermsCatchup() throws RestException;

	void updateSafeSearchTermsCatchup() throws RestException;

	void updateAdultSearchTermsOnDemand() throws RestException;

	void updateSafeSearchTermsOnDemand() throws RestException;

	String getAdultSearchTermsCatchup() throws RestException;

	String getSafeSearchTermsCatchup() throws RestException;

	String getAdultSearchTermsOnDemand() throws RestException;

	String getSafeSearchTermsOnDemand() throws RestException;
	
}