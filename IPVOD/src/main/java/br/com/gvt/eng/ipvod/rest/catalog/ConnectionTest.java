package br.com.gvt.eng.ipvod.rest.catalog;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.constants.IpvodConstants;

@Stateless
@Path("/check")
public class ConnectionTest {

	@PermitAll
	@GET
	/**
	 * Valida conexão com a aplicação
	 * @return Status.OK
	 */
	public Response returnOK() {
		return Response.status(200).build();
	}
	

	@GET
	@RolesAllowed({ IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING,
			IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB,
			IpvodConstants.ROLE_VOC })
	@Path("/token")
	public Response printMessage2() {
		String result = "Teste Token ok!";
		return Response.status(200).entity(result).build();
	}

	
	

}