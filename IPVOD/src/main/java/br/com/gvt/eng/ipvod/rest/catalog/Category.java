package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.util.StringUtils;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.CategoryConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodCategory;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.CategoryFacade;

@Stateless
@Path("/category")
public class Category {

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private CategoryFacade categoryFacade;

	private CategoryConverter categoryConverter = new CategoryConverter();

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response listCategory(@Context UriInfo uriInfo) throws RestException {
		MultivaluedMap<String, String> search = uriInfo.getQueryParameters();
        if (search != null && !search.isEmpty()) {
			return getCategoryComplexQuery(uriInfo);
		}
		List<IpvodCategory> listCategory = new ArrayList<IpvodCategory>();
		listCategory = categoryConverter.getCategoryList(categoryFacade.findAll());

		if (listCategory.isEmpty()) {
			throw RestException.ERROR;
		}

		return Response.status(200).entity(listCategory).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/{categoryId}")
	@Produces("application/json; charset=UTF-8")
	public Response getCategory(@PathParam("categoryId") Long categoryId)
			throws RestException {

		if (categoryId == null || categoryId <= 0) {
			 RestException exc = RestException.getBadRequest();
			 exc.getMissingParams().add("categoryId");
			 throw exc;
		}

		IpvodCategory ipvodCategory = categoryConverter
				.getCategoryWithAssetList(categoryFacade.find(categoryId));

		if (ipvodCategory == null) {
			throw RestException.getNoContent();
		}

		return Response.status(200).entity(ipvodCategory).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/{categoryId}/asset")
	@Produces("application/json; charset=UTF-8")
	public Response listCategoryWithAssets(@PathParam("categoryId") Long categoryId) throws RestException {
		IpvodCategory category = categoryFacade.find(categoryId);
		if (category == null) {
			throw RestException.getNoContent();
		}
		category = categoryConverter.getCategoryWithAssetList(category);
		return Response.status(200).entity(category).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/complex")
	@Produces("application/json; charset=UTF-8")
	public Response getCategoryComplexQuery(@Context UriInfo uriInfo) throws RestException {
		try {
			List<IpvodCategory> listCategory = categoryFacade.findResultComplexQuery(uriInfo);
			
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("list", categoryConverter.getCategoryList(listCategory));
			m.put("count", categoryFacade.countResultComplexQuery(uriInfo).intValue());
			return Response.status(200)
					.entity(m).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response createCategory(IpvodCategory ipvodCategory) throws RestException {
		if (StringUtils.isEmpty(ipvodCategory.getDescription())) {
			RestException exc = RestException.getBadRequest();
			 exc.getMissingParams().add("description");
			 throw exc;
		}
		ArrayList<IpvodAsset> assets = new ArrayList<IpvodAsset>();
		for (IpvodAsset asset : ipvodCategory.getIpvodAssets1()) {
			asset = assetFacade.find(asset.getAssetId());
			asset.setIpvodCategory1(ipvodCategory);
			assets.add(asset);
		}
		ipvodCategory.setIpvodAssets1(assets);
		if (ipvodCategory.getCategoryId() != null) {
			categoryFacade.update(ipvodCategory);
		} else {
			categoryFacade.save(ipvodCategory);
		}
		return Response.status(201).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@DELETE
	@Path("/{categoryId}")
	@Produces("application/json; charset=UTF-8")
	public Response deleteCategory(@PathParam("categoryId") Long categoryId)
			throws RestException {

		if (categoryId == null || categoryId <= 0) {
			RestException exc = RestException.getBadRequest();
			 exc.getMissingParams().add("categoryId");
			 throw exc;
		}
		
		IpvodCategory category = categoryFacade.find(categoryId);
		
		if (category == null) {
			throw RestException.getNoContent();
		}
		
		for (IpvodAsset asset : category.getIpvodAssets1()) {
			asset.setIpvodCategory1(null);
		}
		categoryFacade.delete(category);

		return Response.status(200).build();
	}
}
