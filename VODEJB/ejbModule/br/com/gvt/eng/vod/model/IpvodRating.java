package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * The persistent class for the IPVOD_RATING database table.
 */
@Entity
@Table(name = "IPVOD_RATING")
@NamedQueries({
	@NamedQuery(name = "IpvodRating.getByRatingAdult", query = "SELECT r FROM IpvodRating r WHERE r.adult = :adult AND r.rating = :rating"),
	@NamedQuery(name = "IpvodRating.getRatingOrdered", query = "SELECT r FROM IpvodRating r order by r.ratingLevel asc")
})
@JsonFilter("IpvodRating")
public class IpvodRating implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String GET_BY_RATING_ADULT = "IpvodRating.getByRatingAdult";
	
	public static String GET_RATING_ORDERED = "IpvodRating.getRatingOrdered";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RATING_LEVEL")
	private Long ratingLevel;

	@Column(name = "ADULT")
	private Boolean adult;

	private String rating;

	public Long getRatingLevel() {
		return ratingLevel;
	}

	public void setRatingLevel(Long ratingLevel) {
		this.ratingLevel = ratingLevel;
	}

	public Boolean getAdult() {
		return adult;
	}

	public void setAdult(Boolean adult) {
		this.adult = adult;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDescription() {
		if (adult) {
			return rating + " - Adult";
		} else {
			return rating;
		}
	}
	
	public void setDescription(String desc) {
		
	}
}