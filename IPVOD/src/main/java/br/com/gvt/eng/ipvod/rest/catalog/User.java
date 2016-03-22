package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.converter.UserConverter;
import br.com.gvt.eng.vod.converter.UserSystemConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.vod.facade.UserFacade;
import br.com.gvt.vod.facade.UserSystemFacade;

@Stateless
@Path("/user")
public class User {

	@EJB
	private UserSystemFacade userSystemFacade;

	@EJB
	private UserFacade userFacade;
	
	private UserConverter userConverter = new UserConverter();

	@PermitAll
	@GET
	@Path("/provider/{providerId:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findUserByProviderId(@PathParam("providerId") Long providerId) throws RestException {
		try {
			// Bad request
			if (providerId == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			UserSystemConverter userSystemConverter = new UserSystemConverter();

			IpvodContentProvider ipvodContentProvider = new IpvodContentProvider();
			ipvodContentProvider.setContentProviderId(providerId);

			// Busca lista de registros para processar
			List<IpvodUserSystem> listUserSystem = userSystemFacade.findByContentProvider(ipvodContentProvider);

			// Retorna o objeto
			return Response.status(200).entity(userSystemConverter.getUserSystemList(listUserSystem)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar usuarios pelo providerId");
		}
	}

	@PermitAll
	@GET
	@Path("/userTest")
	@Produces("application/json")
	public Response printMessage() {
		String result = "Restful example: Hello, I'm a user!";
		return Response.status(200).entity(result).build();
	}

	@PermitAll
	@GET
	@Path("/list")
	@Produces("application/json; charset=UTF-8")
	public Response listUsers(@Context UriInfo uriInfo) throws RestException {

		try {

			List<IpvodUser> users = userFacade.find(uriInfo);
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("list", userConverter.toListIpvoduser(users));
			m.put("count", userFacade.countResultComplexQuery(uriInfo).intValue());
			return Response.status(200).entity(m).build();

		} catch (Exception e) {
			throw RestException.ERROR;
		}

	}

}
