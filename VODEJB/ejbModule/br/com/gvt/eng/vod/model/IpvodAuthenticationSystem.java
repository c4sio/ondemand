package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the IPVOD_AUTHENTICATION_SYS database table.
 * 
 */
@Entity
@Table(name = "IPVOD_AUTHENTICATION_SYS")
@NamedQueries({
		@NamedQuery(name = "IpvodAuthenticationSystem.verifyToken", query = "SELECT ia FROM IpvodAuthenticationSystem ia WHERE ia.token =:token"),
		@NamedQuery(name = "IpvodAuthenticationSystem.getAuthByUser", query = "SELECT ia FROM IpvodAuthenticationSystem ia WHERE ia.ipvodUserSys.userSysId =:user"),
		@NamedQuery(name = "IpvodAuthenticationSystem.getAuthByToken", query = "SELECT ia FROM IpvodAuthenticationSystem ia WHERE ia.token =:token"),
		@NamedQuery(name = "IpvodAuthenticationSystem.verifyExpiration", query = "SELECT ia FROM IpvodAuthenticationSystem ia")
})
public class IpvodAuthenticationSystem implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String VERIFY_TOKEN = "IpvodAuthenticationSystem.verifyToken";

	public static final String FIND_AUTH_BY_USER = "IpvodAuthenticationSystem.getAuthByUser";

	public static final String FIND_AUTH_BY_TOKEN = "IpvodAuthenticationSystem.getAuthByToken";
	
	public static final String VERIFY_AUTH_EXPIRATION = "IpvodAuthenticationSystem.verifyExpiration";

	@Id
	@Column(name = "AUTH_SYS_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long authId;

	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_SYS_ID")
	private IpvodUserSystem ipvodUserSys;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUTH_SYS_DATE")
	private Date authDate;

	@Column(name = "AUTH_SYS_TOKEN")
	private String token;

	public IpvodAuthenticationSystem() {
	}

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public Date getAuthDate() {
		return this.authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public IpvodUserSystem getIpvodUserSys() {
		return ipvodUserSys;
	}

	public void setIpvodUserSys(IpvodUserSystem ipvodUserSys) {
		this.ipvodUserSys = ipvodUserSys;
	}

}