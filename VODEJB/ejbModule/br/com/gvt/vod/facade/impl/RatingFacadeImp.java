package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.RatingDAO;
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.vod.facade.RatingFacade;

@Stateless
public class RatingFacadeImp implements RatingFacade {

	@EJB
	private RatingDAO ratingDAO;
	
	@Override
	public List<IpvodRating> findAll() {
		return ratingDAO.findAllAsc();
	}

	@Override
	public IpvodRating find(long entityID) {
		return ratingDAO.find(entityID);
	}

	@Override
	public IpvodRating find(String rating, Boolean adult) {
		return ratingDAO.findByRatingAdult(rating, adult);
	}

}
