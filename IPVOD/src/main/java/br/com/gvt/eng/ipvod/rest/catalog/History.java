package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.HistoryTypeEnum;
import br.com.gvt.eng.vod.model.IpvodHistory;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.vod.facade.HistoryFacade;

@Stateless
@Path("/history")
public class History {

	@EJB
	private HistoryFacade historyFacade;

	
	@PermitAll
	@GET
	@Path("/asset/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response listByAssetType(@PathParam("id") Long itemId)
			throws RestException {
		List<IpvodHistory> histories = historyFacade.listAssetType(HistoryTypeEnum.ASSET, itemId);
		
		if (histories == null || histories.isEmpty()) {
			throw RestException.getNoContent();
		}
		
		for(IpvodHistory history : histories) {
			IpvodUserSystem user = new IpvodUserSystem();
			user.setUsername(history.getUser().getUsername());
			user.setUserSysId(history.getUser().getUserSysId());
			history.setUser(user);
		}
		
		return Response.status(200).entity(histories)
				.build();
	}
	
}

