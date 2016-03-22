package br.com.gvt.eng.ipvod.rest.it;

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
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.gvt.eng.vod.dao.LikeOperator;
import br.com.gvt.eng.vod.exception.rest.RestCodeError;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.vo.it.OnDemandContentVO;
import br.com.gvt.vod.facade.AssetFacade;

@Stateless
@Path("/services/ondemandcontent")
public class OnDemandContent {

	@EJB
	protected AssetFacade assetFacade;

	private ObjectMapper mapper = new ObjectMapper();

	@PermitAll
	@GET
	@Path("/fromaz/{letter}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOnDemandFromAZ(@PathParam("letter") String letter) throws RestException {

		try {

			if (!StringUtils.isAlpha(letter) && StringUtils.isBlank(letter)) {
				throw new RestException(RestCodeError.ERRO_CHANNEL_CATALOG, "O valor " + letter
						+ " enviado não é um caractere pertencente a A-Z.", Status.NOT_ACCEPTABLE);
			}

			List<OnDemandContentVO> assets = assetFacade.findByName(String.valueOf(letter), LikeOperator.CONDITION_LEFT);

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/name/{letter}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findByName(@PathParam("letter") String letter) throws RestException {

		try {

			if (StringUtils.isBlank(letter)) {
				throw new RestException(RestCodeError.ERRO_CHANNEL_CATALOG, "O valor não pode ser vazio.", Status.NOT_ACCEPTABLE);
			}

			List<OnDemandContentVO> assets = assetFacade.findByName(letter.trim(), LikeOperator.CONDITION_ALL);

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/top50/")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response top50() throws RestException {

		try {

			List<OnDemandContentVO> assets = assetFacade.findTop50();

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/genre/{genreId}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findByGenre(@PathParam("genreId") Long genreId) throws RestException {

		try {

			List<OnDemandContentVO> assets = assetFacade.findByGenre(genreId);

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/highlights/")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getHighlights() throws RestException {
		
		try {

			List<OnDemandContentVO> assets = assetFacade.findHighlights();

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}
		
	}

	@PermitAll
	@GET
	@Path("/releases/")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getReleases() throws RestException {
		
		try {

			List<OnDemandContentVO> assets = assetFacade.findReleases();

			if (assets.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			}

			String rest = mapper.writeValueAsString(assets);

			return Response.status(Status.OK).entity(rest).build();
		} catch (Exception e) {
			throw new RestException(e);
		}
		
	}

}
