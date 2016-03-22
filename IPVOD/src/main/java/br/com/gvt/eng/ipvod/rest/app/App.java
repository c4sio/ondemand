package br.com.gvt.eng.ipvod.rest.app;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.converter.AppCategoryConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAppCategory;
import br.com.gvt.vod.facade.AppFacade;

@Stateless
@Path("/app")
public class App {

	@EJB
	private AppFacade appFacade;

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findAppById(@PathParam("id") Long appId)
			throws RestException {
		try {
			// Busca lista de registros para processar
			IpvodAppCategory ipvodAppCategory = appFacade.find(appId);
			// Bad request
			if (ipvodAppCategory == null) {
				throw RestException.getNoContent();
			}

			// Retorna o objeto
			return Response.status(200).entity(ipvodAppCategory).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de apps");
		}
	}

	@PermitAll
	@GET
	@Path("/")
	@Produces("application/json; charset=UTF-8")
	public Response findListApps() throws RestException {
		try {
			// Busca lista de registros para processar
			List<IpvodAppCategory> listApps = appFacade.findAll();
			// Bad request
			if (listApps == null) {
				throw RestException.getNoContent();
			}

			// Retorna o objeto
			return Response.status(200).entity(listApps).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de apps");
		}
	}

	@PermitAll
	@GET
	@Path("/find/{keyValue}")
	@Produces("application/json; charset=UTF-8")
	public Response findAppsByCaID(@PathParam("keyValue") String keyValue)
			throws RestException {
		try {
			AppCategoryConverter appCategoryConverter = new AppCategoryConverter();

			// Busca lista de registros para processar
			List<IpvodAppCategory> listApps = appFacade
					.findAppsByValue(keyValue);
			// Bad request
			if (listApps == null) {
				throw RestException.getNoContent();
			}
			// Retorna o objeto
			return Response.status(200)
					.entity(appCategoryConverter.getAppCategoryList(listApps))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de apps");
		}
	}

}
