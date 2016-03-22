package br.com.gvt.eng.ipvod.rest.catalog;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.vod.facade.ContentProviderFacade;

@Stateless
@Path("/contentProvider")
public class ContentProvider {

	@EJB
	private ContentProviderFacade contentProviderFacade;

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response findAll() {
		return Response.status(200).entity(contentProviderFacade.findAll()).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA})
	@GET
	@Path("/unique")
	@Produces("application/json; charset=UTF-8")
	public Response findUnique() {
		return Response.status(200).entity(contentProviderFacade.findProviders()).build();
	}
}
