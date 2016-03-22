package br.com.gvt.eng.ipvod.rest.adm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.converter.AssetConverter;
import br.com.gvt.eng.vod.converter.UserConverter;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.UserFacade;

@Stateless
@Path("/admin")
public class Admin {

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private UserFacade userFacade;

	private AssetConverter converter = new AssetConverter();

	private UserConverter userConverter = new UserConverter();

	@GET
	@Path("/user/createasset")
	public Response createAsset() {
		// assetBO.createTestAsset();
		return Response.status(201).build();
	}

	@GET
	@Path("/asset/{id}")
	public Response getAsset(@PathParam("id") Long id) {
		List<IpvodAsset> lstAsset = new ArrayList<IpvodAsset>();
		lstAsset.add(assetFacade.find(id));
		return Response.status(200).entity(converter.toAssetJson(lstAsset)).build();
	}

	@GET
	@Path("/asset/list")
	public Response getAllAsset() {
		return Response.status(200).entity(converter.toAssetJson(assetFacade.findAll())).build();
	}

	@PermitAll
	@GET
	@Path("/user/{id}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getValue(@PathParam("id") Long id) {
		return Response.status(200).entity(userConverter.toIpvodUser(userFacade.find(id))).build();
	}

}
