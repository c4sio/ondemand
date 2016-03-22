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
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.vod.facade.RatingFacade;

@Stateless
@Path("/rating")
public class Rating {

	@EJB
	private RatingFacade ratingFacade;

	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getRatings() throws RestException {
		List<IpvodRating> ratings = ratingFacade.findAll();
		return Response.ok(ratings).build();
	}

	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/{rating}/{adult}")
	public Response getRating(@PathParam("rating") String rating,
			@PathParam("adult") Boolean adult) throws RestException {
		IpvodRating ratings = ratingFacade.find(rating, adult);
		return Response.ok(ratings).build();
	}

	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/{id}")
	public Response getRatingById(@PathParam("id") Long id)
			throws RestException {
		IpvodRating ratings = ratingFacade.find(id);
		return Response.ok(ratings).build();
	}

}