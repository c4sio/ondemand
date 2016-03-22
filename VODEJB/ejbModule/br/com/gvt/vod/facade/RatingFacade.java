package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodRating;

@Local
public interface RatingFacade {

	public abstract List<IpvodRating> findAll();

	public abstract IpvodRating find(long entityID);

	public abstract IpvodRating find(String rating, Boolean adult);
}
