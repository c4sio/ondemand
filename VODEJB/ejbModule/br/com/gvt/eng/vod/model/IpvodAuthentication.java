package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the IPVOD_AUTHENTICATION database table.
 * 
 */
@Entity
@Table(name = "IPVOD_AUTHENTICATION")
@NamedQueries({
		@NamedQuery(name = "IpvodAuthentication.verifyToken", query = "SELECT ia FROM IpvodAuthentication ia WHERE ia.token =:token AND ia.ipvodUser.userId =:user"),
		@NamedQuery(name = "IpvodAuthentication.getAuthByToken", query = "SELECT ia FROM IpvodAuthentication ia WHERE ia.token =:token"),
		@NamedQuery(name = "IpvodAuthentication.getAuthByUser", query = "SELECT ia FROM IpvodAuthentication ia WHERE ia.ipvodUser.userId =:user"),
		@NamedQuery(name = "IpvodAuthentication.getValidAuthByEquipment", query = "SELECT ia FROM IpvodAuthentication ia WHERE ia.equipment.equipmentId = :equipId AND ia.expirationDate > current_timestamp order by ia.authDate"),
		@NamedQuery(name = "IpvodAuthentication.verifyExpiration", query = "SELECT ia FROM IpvodAuthentication ia") })
public class IpvodAuthentication implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String VERIFY_TOKEN = "IpvodAuthentication.verifyToken";

	public static final String FIND_AUTH_BY_TOKEN = "IpvodAuthentication.getAuthByToken";

	public static final String FIND_AUTH_BY_USER = "IpvodAuthentication.getAuthByUser";

	public static final String FIND_VALID_AUTH_BY_EQUIPMENT = "IpvodAuthentication.getValidAuthByEquipment";

	public static final String VERIFY_AUTH_EXPIRATION = "IpvodAuthentication.verifyExpiration";

	@Id
	@Column(name = "AUTHENTICATION_ID")
	@SequenceGenerator(name = "SEQ_AUTHENTICATION", sequenceName = "SEQ_AUTHENTICATION", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AUTHENTICATION")
	private long authenticationId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUTH_DATE")
	private Date authDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "IP_ADDRESS")
	private String ipAddress;

	@Column(name = "TOKEN", length = 255)
	private String token;

	// bi-directional many-to-one association to IpvodUser
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private IpvodUser ipvodUser;

	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_ID")
	private IpvodEquipment equipment;

	@Column(name = "CONNECTION", length = 10, nullable = true)
	private String connection;

	public IpvodAuthentication() {
	}

	public long getAuthenticationId() {
		return this.authenticationId;
	}

	public void setAuthenticationId(long authenticationId) {
		this.authenticationId = authenticationId;
	}

	public Date getAuthDate() {
		return this.authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public IpvodUser getIpvodUser() {
		return this.ipvodUser;
	}

	public void setIpvodUser(IpvodUser ipvodUser) {
		this.ipvodUser = ipvodUser;
	}

	public IpvodEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(IpvodEquipment equipment) {
		this.equipment = equipment;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

}