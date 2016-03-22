package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.JsonSTB;
import br.com.gvt.eng.vod.converter.MenuConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.SearchFacade;

@Stateless
@Path("/search")
public class Search {

	@EJB
	private AuthenticationFacade authenticationFacade;
	
	@EJB
	private SearchFacade searchFacade;
	
	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response searchAssets(@Context UriInfo uriInfo, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		List<IpvodAsset> assets = searchFacade.findAssets(auth.getIpvodUser(), uriInfo);
		
		IpvodVisualMenu menu = new IpvodVisualMenu();
		menu.setIpvodAssets(assets);

		MenuConverter converter = new MenuConverter();
		menu = converter.toMenu(menu);
		
		return Response.status(200).
				entity(JsonSTB.toJsonSTB(menu)).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/ondemand")
	@Produces("application/json; charset=UTF-8")
	public Response searchAssetsOndemand(@Context UriInfo uriInfo, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		List<IpvodAsset> assets = searchFacade.findAssetsByListOnDemand(auth.getIpvodUser(), uriInfo);
		
		IpvodVisualMenu menu = new IpvodVisualMenu(); 
		menu.setIpvodAssets(assets);

		MenuConverter converter = new MenuConverter();
		menu = converter.toMenu(menu);
		
		return Response.status(200).
				entity(JsonSTB.toJsonSTB(menu)).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/catchup")
	@Produces("application/json; charset=UTF-8")
	public Response searchAssetsCatchUp(@Context UriInfo uriInfo, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
	
		List<IpvodAsset> assets = searchFacade.findAssetsByListCatchUp(auth.getIpvodUser(), uriInfo);
		
		IpvodVisualMenu menu = new IpvodVisualMenu(); 
		menu.setIpvodAssets(assets);

		MenuConverter converter = new MenuConverter();
		menu = converter.toMenu(menu);
		
		return Response.status(200).
				entity(JsonSTB.toJsonSTB(menu)).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/terms/ondemand")
	@Produces("application/json; charset=UTF-8")
	public Response getSafeSearchTermsOnDemand() throws RestException {
		return Response.status(200).
				entity(searchFacade.getSafeSearchTermsOnDemand()).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/terms/ondemand/adult")
	@Produces("application/json; charset=UTF-8")
	public Response getAdultSearchTermsOnDemand() throws RestException {
		return Response.status(200).
				entity(searchFacade.getAdultSearchTermsOnDemand()).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/terms/catchup")
	@Produces("application/json; charset=UTF-8")
	public Response getSafeSearchTermsCatchup() throws RestException {
		return Response.status(200).
				entity(searchFacade.getSafeSearchTermsCatchup()).
				build();
	}
	
	@PermitAll
	@GET
	@Path("/terms/catchup/adult")
	@Produces("application/json; charset=UTF-8")
	public Response getAdultSearchTermsCatchup() throws RestException {
		return Response.status(200).
				entity(searchFacade.getAdultSearchTermsCatchup()).
				build();
	}
	
	@PermitAll
	@POST
	@Path("/terms")
	@Produces("application/json; charset=UTF-8")
	public Response updateSearchTerms() throws RestException {
		searchFacade.updateSafeSearchTermsOnDemand();
		searchFacade.updateSafeSearchTermsCatchup();
		searchFacade.updateAdultSearchTermsCatchup();
		searchFacade.updateAdultSearchTermsOnDemand();
		return Response.status(200).
				build();
	}
}