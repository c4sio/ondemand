package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.MenuConverter;
import br.com.gvt.eng.vod.converter.PackageConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPackageType;
import br.com.gvt.vod.facade.AssetPackageFacade;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.PackageFacade;

@Stateless
@Path("/package")
public class Package {

	@EJB
	private PackageFacade packageFacade;

	@EJB
	private AuthenticationFacade authenticationFacade;
	
	@EJB
	private AssetPackageFacade assetPackageFacade;

	PackageConverter packageConverter = new PackageConverter();
	
	MenuConverter menuConverter = new MenuConverter();
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response listPackage(@Context UriInfo uriInfo) throws RestException {
		MultivaluedMap<String, String> search = uriInfo.getQueryParameters();
        if (search != null && !search.isEmpty()) {
			return getPackageComplexQuery(uriInfo);
		}
		// Busca os dados
		List<IpvodPackage> listPackage = packageFacade.findAll();

		// Bad Request
		if (listPackage == null || listPackage.isEmpty()) {
			throw new RestException(4,
					"Não encontrou nenhum resultado para a pesquisa.");
		}
		// The server successfully processed the request
		return Response.status(200)
				.entity(packageConverter.toPackageListNoAssets(listPackage)).build();
	}
	
	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/listPackages")
	public Response listAll() throws RestException {
		return Response.status(200)
				.entity(packageConverter.toPackageListNoAssets(packageFacade.findAll())).build();
	}

	@PermitAll
	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/stb")
	@Produces("application/json; charset=UTF-8")
	public Response listPackagesSTB(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		// Busca os dados
		List<IpvodPackage> listPackage = packageFacade.findPackages(auth.getIpvodUser().getUserId());

		List<Long> packageIds = new ArrayList<Long>();
		if (listPackage != null) {
			for (IpvodPackage ipvodPackage : listPackage) {
				packageIds.add(ipvodPackage.getPackageId());
			}
		}
		// The server successfully processed the request
		return Response.status(200).entity(packageIds).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB})
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getPackageById(@PathParam("id") Long packageId, @Context UriInfo uriInfo, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {

		// Bad Request
		if (packageId == null) {
			throw new RestException(1, "Parâmetro obrigatório nulo. [Id]");
		}
		
		// Busca dados na base
		IpvodPackage ipvodPackage = packageFacade.find(packageId);

		// Bad Request
		if (ipvodPackage == null) {
			throw new RestException(4,
					"Não encontrou nenhum resultado para a pesquisa.");
		}
		// The server successfully processed the request
		return Response.status(200)
				.entity(packageConverter.toPackage(ipvodPackage)).build();
	}

	@PermitAll
	@GET
	@Path("/cms/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getPackageByIdCMS(@PathParam("id") Long packageId) throws RestException {

		// Bad Request
		if (packageId == null) {
			throw new RestException(1, "Parâmetro obrigatório nulo. [Id]");
		}
		
		// Busca dados na base
		IpvodPackage ipvodPackage = packageFacade.find(packageId);

		// Bad Request
		if (ipvodPackage == null) {
			throw new RestException(4,
					"Não encontrou nenhum resultado para a pesquisa.");
		}
		// The server successfully processed the request
		return Response.status(200)
				.entity(packageConverter.toPackage(ipvodPackage)).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@DELETE
	@Path("/{packageId}")
	@Produces("application/json; charset=UTF-8")
	public Response deletePackage(@PathParam("packageId") Long packageId)
			throws RestException {

		// Bad request
		if (packageId == null || packageId == 0) {
			throw new RestException(1, "Parâmetro obrigatório nulo. [Package]");
		}

		// Busca dados na base
		IpvodPackage ipvodPackage = packageFacade.find(packageId);

		// Bad Request
		if (ipvodPackage == null) {
			throw new RestException(4,
					"Não encontrou nenhum resultado para a pesquisa.");
		}

		// Delete data packages
		packageFacade.delete(ipvodPackage);

		// The server successfully processed the request
		return Response.status(204).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@POST
	@Produces("application/json; charset=UTF-8")
	public Response saveUpdatePackage(IpvodPackage packageData) throws RestException {

		// Bad request
		if (packageData == null) {
			throw RestException.getBadRequest();
		}
		
		if (packageData.getPackageId() != 0) {
			// Busca dados na base
			IpvodPackage ipvodPackage = packageFacade.find(packageData.getPackageId());
			
			// Bad Request
			if (ipvodPackage == null) {
				RestException exc = RestException.getBadRequest();
				exc.getMissingParams().add("ipvodPackage");
				throw exc;
			}
		}

		//VALIDAÇÕES DOS CAMPOS
		ArrayList<String> missingParams = new ArrayList<String>();
		
		if (packageData.getDescription() == null
				|| "".equals(packageData.getDescription().trim())) {
			missingParams.add("description");
		}

		if (packageData.getPrice() == null) {
			missingParams.add("price");
		}

		if (packageData.getDateStart() == null) {
			missingParams.add("dateStart");
		}

		if (packageData.getIpvodPackageType() == null) {
			missingParams.add("packageType");
		}
		
		if (!missingParams.isEmpty()) {
			RestException exc = RestException.getBadRequest();
			exc.setMissingParams(missingParams);
			throw exc;
		}
		// Delete data packages
		packageFacade.save(packageData);

		// The server successfully processed the request
		return Response.status(201).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/complex")
	@Produces("application/json; charset=UTF-8")
	public Response getPackageComplexQuery(@Context UriInfo uriInfo) throws RestException {
		try {
			
			List<IpvodPackage> listPackage = packageFacade.findResultComplexQuery(uriInfo);
			
			Map<String, Object> m = new HashMap<String, Object>();
			
			m.put("list", packageConverter.toPackageListNoAssets(listPackage));
			m.put("count", packageFacade.countResultComplexQuery(uriInfo).intValue());
			return Response.status(200).entity(m).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/type")
	@Produces("application/json; charset=UTF-8")
	public Response listPackageType() throws RestException {
		try {
			
			List<IpvodPackageType> listPackageType = packageFacade.listPackageType();
			
			return Response.status(200).entity(listPackageType).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@GET
	@Path("/assetPackage")
	@Produces("application/json; charset=UTF-8")
	public Response listAssetPackage(@Context UriInfo uriInfo) throws RestException {
		// Busca os dados
		List<IpvodAssetPackage> listPackage = assetPackageFacade.findComplexQuery(uriInfo);

		if (listPackage == null || listPackage.isEmpty()) {
			throw RestException.getNoContent();
		}
		IpvodPackage ipvodPackage = new IpvodPackage();
		ipvodPackage.setIpvodAssetPackages(listPackage);
		ipvodPackage = packageConverter.toPackage(ipvodPackage);
		// The server successfully processed the request
		return Response.status(200)
				.entity(ipvodPackage.getIpvodAssetPackages()).build();
	}
	
	@DenyAll
	@POST
	@Path("/addAsset")
	@Produces("application/json; charset=UTF-8")
	public Response addAssetPackage(IpvodPackage packageData)
			throws RestException {

		// Bad request
		if (packageData == null || "".equals(packageData.getPackageId())) {
			throw new RestException(1, "Parâmetro obrigatório nulo. [Package]");
		}

		// Busca dados na base
		IpvodPackage ipvodPackage = packageFacade.find(packageData
				.getPackageId());

		// Bad Request
		if (ipvodPackage == null) {
			throw new RestException(3,
					"Este pacote não esta cadastrado na base.");
		}

		// Add Asset into packages
		if (packageFacade.addAsset(ipvodPackage)) {
			// The server successfully processed the request
			return Response.status(202).build();
		} else {
			// Error
			throw new RestException(5,
					"Erro ao efetuar a associação de filmes com pacote");
		}
	}

	@DenyAll
	@POST
	@Path("/removeAsset")
	@Produces("application/json; charset=UTF-8")
	public Response removeAssetPackage(IpvodPackage packageData)
			throws RestException {

		// Bad request
		if (packageData == null || "".equals(packageData.getPackageId())) {
			throw new RestException(1, "Parâmetro obrigatório nulo. [Package]");
		}

		// Busca dados na base
		IpvodPackage ipvodPackage = packageFacade.find(packageData
				 .getPackageId());

		// Bad Request
		if (ipvodPackage == null) {
			throw new RestException(3,
					"Este pacote não esta cadastrado na base.");
		}

		// Add Asset into packages
		if (packageFacade.removeAsset(ipvodPackage)) {
			// The server successfully processed the request
			return Response.status(202).build();
		} else {
			// Error
			throw new RestException(5,
					"Erro ao efetuar a associação de filmes com pacote");
		}
	}
	
}
