package br.com.gvt.eng.ipvod.rest.auth;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.vo.AuthVO;
import br.com.gvt.vod.facade.AuthenticationFacade;

@Stateless
@Path("/security")
public class Security {

	@EJB
	private AuthenticationFacade authenticationFacade;

	@PermitAll
	@POST
	@Path("/token")
	@Consumes("application/json")
	public Response getToken(AuthVO authVO) throws RestException {

		String token = null;
		
		ArrayList<String> missingParams = new ArrayList<String>();
		if (authVO.getIp() == null) {
			missingParams.add("ip");
		}
		
		if (authVO.getMac() == null && authVO.getCaId() == null) {
			missingParams.add("mac");
			missingParams.add("caId");
		}
		
		if (authVO.getConnection() == null) {
			missingParams.add("connection");
		}
		
		if (!missingParams.isEmpty()) {
			RestException exc = RestException.getBadRequest();
			exc.setMissingParams(missingParams);
			throw exc;
		}
		try {
			token = authenticationFacade.manageToken(authVO);
		} catch (BusinessException e) {
			if ("Equipment not found".equals(e.getMessage())) {
				throw RestException.getEquipmentNotFound();
			}
			if ("User inactive".equals(e.getMessage())) {
				throw RestException.getUserInactive();
			}
			throw new RestException(1, e.getMessage(),
					Status.INTERNAL_SERVER_ERROR);
		}

		if (token == null) {
			throw RestException.ERROR;
		}
		return Response.status(200).entity(token).build();
	}
}
