package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodRating;

@Stateless
public class RatingDAO extends GenericDAO<IpvodRating> {

	public RatingDAO() {
		super(IpvodRating.class);
	}
	
	public IpvodRating findByRatingAdult(String rating, Boolean adult) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("rating", rating);
		parameters.put("adult", adult);

		return super.findOneResult(IpvodRating.GET_BY_RATING_ADULT, parameters);
	}
	
	public List<IpvodRating> findAllAsc() {
		return super.findResultByParameter(IpvodRating.GET_RATING_ORDERED, null);
	}
	
}
