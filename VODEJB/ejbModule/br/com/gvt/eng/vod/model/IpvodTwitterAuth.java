package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_ASSET database table.
 */
@Entity
@Table(name = "IPVOD_TWITTER_AUTH")
@NamedQueries({@NamedQuery(name = "IpvodTwitterAuth.findByUser", query = "SELECT t FROM IpvodTwitterAuth t WHERE t.user.userId = :userId") })
public class IpvodTwitterAuth implements Serializable, Importer {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_USER = "IpvodTwitterAuth.findByUser";

	/**
	 * TODO verify this field:ASSET_ID_SUP
	 */
	@Id
	@Column(name = "TWITTERAUTH_ID")
	@SequenceGenerator(name = "SEQ_TWITTER", sequenceName = "SEQ_TWITTER", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TWITTER")
	private long twitterAuthId;

	@OneToOne
	@JoinColumn(name = "STB_USER")
	private IpvodUser user;
	
	private String url;
	
	private String oAuthAccessToken;
	
	private String oAuthAccessTokenSecret;

	private String requestToken;
	
	private String requestTokenSecret;

	public long getTwitterAuthId() {
		return twitterAuthId;
	}

	public void setTwitterAuthId(long twitterAuthId) {
		this.twitterAuthId = twitterAuthId;
	}

	public IpvodUser getUser() {
		return user;
	}

	public void setUser(IpvodUser user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getoAuthAccessToken() {
		return oAuthAccessToken;
	}

	public void setoAuthAccessToken(String oAuthAccessToken) {
		this.oAuthAccessToken = oAuthAccessToken;
	}

	public String getoAuthAccessTokenSecret() {
		return oAuthAccessTokenSecret;
	}

	public void setoAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
		this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}

	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}

}