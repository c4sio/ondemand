package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodBookmark;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.BookmarkFacade;

@Stateless
@Path("/bookmark")
public class Bookmark {

	@EJB
	private BookmarkFacade bookmarkFacade;
	
	@EJB
	private AuthenticationFacade authenticationFacade;

	@EJB
	private AssetFacade assetFacade; 
	
	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/asset/{assetId}")
	public Response getBookmark(@PathParam("assetId") Long assetId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		System.out.println("/bookmark/asset/" + assetId + " inicio");
		if (assetId == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("assetId");
			throw exc;
		}
		System.out.println("/bookmark/asset/" + assetId + " find asset");
		IpvodAsset asset = assetFacade.find(assetId);
		System.out.println("/bookmark/asset/" + assetId + " fim find asset");
		if (asset == null) {
			System.out.println("/bookmark/asset/" + assetId + " asset não encontrado");
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("asset");
			throw exc;
		}
		System.out.println("/bookmark/asset/" + assetId + " get auth");
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		System.out.println("/bookmark/asset/" + assetId + " get auth fim");
		if (auth == null) {
			System.out.println("/bookmark/asset/" + assetId + " auth não encontrado");
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		System.out.println("/bookmark/asset/" + assetId + " find bookmark");
		IpvodBookmark bookmark = bookmarkFacade.findByAssetUser(assetId, auth.getIpvodUser().getUserId());
		if (bookmark == null) {
			System.out.println("/bookmark/asset/" + assetId + " bookmark não encontrado");
			throw RestException.getNoContent();
		}
		System.out.println("/bookmark/asset/" + assetId + " fim");
		IpvodBookmark returnBookmark = new IpvodBookmark();
		returnBookmark.setAssetTime(bookmark.getAssetTime());
		return Response.ok(returnBookmark).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@POST
	@Produces("application/json; charset=UTF-8")
	@Path("/asset/{assetId}")
	public Response saveBookmark(IpvodBookmark bookmark, @PathParam("assetId") Long assetId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		System.out.println("POST /bookmark/asset/" + assetId + " inicio");
		
		List<String> missingParams = new ArrayList<String>();
		if (bookmark.getAssetTime() == null) {
			missingParams.add("assetTime");
		}
		if (assetId == null) {
			missingParams.add("assetId");
		} else {
			bookmark.setIpvodAsset(new IpvodAsset());
			bookmark.getIpvodAsset().setAssetId(assetId);
		}
		
		if (!missingParams.isEmpty()) {
			RestException exc = RestException.getBadRequest();
			exc.setMissingParams(missingParams);
			throw exc;
		}
		System.out.println("POST /bookmark/asset/" + assetId + " auth");
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		System.out.println("POST /bookmark/asset/" + assetId + " auth fim");
		bookmark.setIpvodUser(auth.getIpvodUser());
		
		System.out.println("POST /bookmark/asset/" + assetId + " asset");
		IpvodAsset asset = assetFacade.find(bookmark.getIpvodAsset().getAssetId());
		System.out.println("POST /bookmark/asset/" + assetId + " asset fim");
		if (asset == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("asset");
			throw exc;
		}
		
		bookmark.setIpvodAsset(asset);
		
		System.out.println("POST /bookmark/asset/" + assetId + " existing bookmark");
		IpvodBookmark existingBookmark = bookmarkFacade.findByAssetUser(asset.getAssetId(), auth.getIpvodUser().getUserId());
		System.out.println("POST /bookmark/asset/" + assetId + " existing bookmark fim");
		if (existingBookmark != null) {
			existingBookmark.setAssetTime(bookmark.getAssetTime());
			bookmark = existingBookmark;
		}
		
		bookmark.setIpvodEquipment(auth.getEquipment());
		
		System.out.println("POST /bookmark/asset/" + assetId + " save bookmark");
		bookmarkFacade.save(bookmark);
		System.out.println("POST /bookmark/asset/" + assetId + " save bookmark fim");
		return Response.ok().build();
	} 
	
	@RolesAllowed(IpvodConstants.ROLE_STB)
	@DELETE
	@Produces("application/json; charset=UTF-8")
	@Path("/asset/{assetId}")
	public Response deleteBookmark(@PathParam("assetId") Long assetId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		if (assetId == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("assetId");
			throw exc;
		}
		System.out.println("DELETE /bookmark/asset/" + assetId + " AUTH");
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		System.out.println("DELETE /bookmark/asset/" + assetId + " AUTH FIM");
		
		System.out.println("DELETE /bookmark/asset/" + assetId + " ASSET");
		IpvodAsset asset = assetFacade.find(assetId);
		System.out.println("DELETE /bookmark/asset/" + assetId + " ASSET FIM");
		if (asset == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("asset");
			throw exc;
		}

		System.out.println("DELETE /bookmark/asset/" + assetId + " EXISTING BOOKMARK");
		IpvodBookmark existingBookmark = bookmarkFacade.findByAssetUser(asset.getAssetId(), auth.getIpvodUser().getUserId());
		System.out.println("DELETE /bookmark/asset/" + assetId + " EXISTING BOOKMARK FIM");
		if (existingBookmark != null) {
			System.out.println("DELETE /bookmark/asset/" + assetId + " DELETE BOOKMARK");
			bookmarkFacade.delete(existingBookmark);
			System.out.println("DELETE /bookmark/asset/" + assetId + " DELETE BOOKMARK FIM");
		} else {
			throw RestException.getNoContent();
		}
		
		return Response.status(Status.NO_CONTENT).build();
	} 	
}