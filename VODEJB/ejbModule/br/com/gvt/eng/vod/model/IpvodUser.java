package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the IPVOD_USER database table.
 * 
 */
@Entity
@Table(name = "IPVOD_USER")
@NamedQueries({
		@NamedQuery(name = "IpvodUser.getBySubscriberID", query = "SELECT iu FROM IpvodUser iu WHERE iu.crmCustomerId = :subscriberID"),
		@NamedQuery(name = "IpvodUser.getUserAtctive", query = "SELECT iu FROM IpvodUser iu WHERE iu.active = :active"),
		@NamedQuery(name = "IpvodUser.getUserByAuthInfo", query = "SELECT user FROM IpvodUser user WHERE user.authInfo = :authInfo") })
public class IpvodUser implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_USER_ACTIVE = "IpvodUser.getUserAtctive";

	public static final String FIND_BY_USER_BY_SUBSCRIBER_ID = "IpvodUser.getBySubscriberID";

	public static final String FIND_BY_USER_AUTH_INFO = "IpvodUser.getUserByAuthInfo";

	@Id
	@Column(name = "USER_ID")
	@SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
	private long userId;

	@Column(columnDefinition = "TINYINT", name = "ACTIVE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean active;

	@Column(name = "CRM_CUSTOMER_ID", unique = true, nullable = false)
	private String crmCustomerId;

	@Column(name = "SERVICE_REGION", nullable = false)
	private String serviceRegion;

	@Column(name = "AUTH_INFO")
	private String authInfo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Calendar createdAt;

	// bi-directional many-to-one association to IpvodAuthentication
	@OneToMany(mappedBy = "ipvodUser", cascade = CascadeType.REMOVE)
	private List<IpvodAuthentication> ipvodAuthentications;

	// bi-directional many-to-one association to IpvodEquipment
	@OneToMany(mappedBy = "ipvodUser", cascade = CascadeType.REMOVE)
	private List<IpvodEquipment> ipvodEquipments;

	// bi-directional many-to-many association to IpvodPackage
	@OneToMany
	@JoinTable(name = "IPVOD_PACKAGE_SUBSCRIPTION", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "PACKAGE_ID") })
	private List<IpvodPackage> ipvodPackages = new ArrayList<IpvodPackage>(0);

	@OneToMany
	@JoinTable(name = "IPVOD_RECOMM_USER", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ASSET_ID", referencedColumnName = "ASSET_ID") })
	private List<IpvodAsset> ipvodAssets = new ArrayList<IpvodAsset>(0);

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getServiceRegion() {
		return serviceRegion;
	}

	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCrmCustomerId() {
		return this.crmCustomerId;
	}

	public void setCrmCustomerId(String crmCustomerId) {
		this.crmCustomerId = crmCustomerId;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public List<IpvodAuthentication> getIpvodAuthentications() {
		return this.ipvodAuthentications;
	}

	public void setIpvodAuthentications(
			List<IpvodAuthentication> ipvodAuthentications) {
		this.ipvodAuthentications = ipvodAuthentications;
	}

	public IpvodAuthentication addIpvodAuthentication(
			IpvodAuthentication ipvodAuthentication) {
		getIpvodAuthentications().add(ipvodAuthentication);
		ipvodAuthentication.setIpvodUser(this);

		return ipvodAuthentication;
	}

	public IpvodAuthentication removeIpvodAuthentication(
			IpvodAuthentication ipvodAuthentication) {
		getIpvodAuthentications().remove(ipvodAuthentication);
		ipvodAuthentication.setIpvodUser(null);

		return ipvodAuthentication;
	}

	public List<IpvodEquipment> getIpvodEquipments() {
		return this.ipvodEquipments;
	}

	public void setIpvodEquipments(List<IpvodEquipment> ipvodEquipments) {
		this.ipvodEquipments = ipvodEquipments;
	}

	public IpvodEquipment addIpvodEquipment(IpvodEquipment ipvodEquipment) {
		getIpvodEquipments().add(ipvodEquipment);
		ipvodEquipment.setIpvodUser(this);

		return ipvodEquipment;
	}

	public IpvodEquipment removeIpvodEquipment(IpvodEquipment ipvodEquipment) {
		getIpvodEquipments().remove(ipvodEquipment);
		ipvodEquipment.setIpvodUser(null);

		return ipvodEquipment;
	}

	public List<IpvodPackage> getIpvodPackages() {
		return this.ipvodPackages;
	}

	public void setIpvodPackages(List<IpvodPackage> ipvodPackages) {
		this.ipvodPackages = ipvodPackages;
	}

	public List<IpvodAsset> getIpvodAssets() {
		return ipvodAssets;
	}

	public void setIpvodAssets(List<IpvodAsset> ipvodAssets) {
		this.ipvodAssets = ipvodAssets;
	}

	public IpvodPackage removeIpvodPackage(IpvodPackage ipvodPackage) {
		getIpvodPackages().remove(ipvodPackage);
		ipvodPackage.setIpvodUsers(null);
		return ipvodPackage;
	}

}