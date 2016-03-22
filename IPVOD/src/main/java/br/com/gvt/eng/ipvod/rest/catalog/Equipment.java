package br.com.gvt.eng.ipvod.rest.catalog;

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

import br.com.gvt.vod.facade.EquipmentFacade;
import br.com.gvt.vod.facade.EquipmentTypeFacade;

@Stateless
@Path("/equipment")
public class Equipment {

	@EJB
	private EquipmentFacade equipmentFacade;

	@EJB
	private EquipmentTypeFacade equipmentTypeFacade;

	public Equipment() {
	}

	@PermitAll
	@GET
	@Path("/types")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllEquipmentType() {
		return Response.status(200).entity(equipmentTypeFacade.findAll()).build();
	}

	@PermitAll
	@GET
	@Path("/type/{id}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findEquipmentType(@PathParam("id") Long id) {
		return Response.status(200).entity(equipmentTypeFacade.find(id)).build();
	}

}
