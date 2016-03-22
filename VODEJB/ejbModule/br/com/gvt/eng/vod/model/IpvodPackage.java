package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * The persistent class for the IPVOD_PACKAGE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_PACKAGE")
@NamedQueries({
		@NamedQuery(name = "IpvodPackage.listOnDemand", query = "SELECT p FROM IpvodPackage p, IpvodUser u WHERE p member of u.ipvodPackages  AND u.userId = :userId AND p.ipvodPackageType.packageTypeId <> 6"),
		@NamedQuery(name = "IpvodPackage.listCatchup", query = "SELECT p FROM IpvodPackage p, IpvodUser u WHERE p member of u.ipvodPackages  AND u.userId = :userId AND p.ipvodPackageType.packageTypeId = 6"),
		@NamedQuery(name = "IpvodPackage.listPackages", query = "SELECT p FROM IpvodPackage p, IpvodUser u WHERE p member of u.ipvodPackages  AND u.userId = :userId") })
@JsonFilter("IpvodPackage")
public class IpvodPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	public static String LIST_ON_DEMAND = "IpvodPackage.listOnDemand";
	public static String LIST_CATCHUP = "IpvodPackage.listCatchup";
	public static String LIST_PACKAGES = "IpvodPackage.listPackages";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PACKAGE_ID")
	private long packageId;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_END")
	private Date dateEnd;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_START")
	private Date dateStart;

	@Column(name = "DESCRIPTION", nullable = false, length = 50)
	private String description;

	@Column(length = 3, precision = 2)
	private Double price;

	// bi-directional many-to-one association to IpvodAssetPackage
	@OneToMany(mappedBy = "ipvodPackage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IpvodAssetPackage> ipvodAssetPackages = new ArrayList<IpvodAssetPackage>(
			0);

	// bi-directional many-to-one association to IpvodPackageType
	@ManyToOne
	@JoinColumn(name = "PACKAGE_TYPE_ID")
	private IpvodPackageType ipvodPackageType;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "IPVOD_PACKAGE_SUBSCRIPTION", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "PACKAGE_ID") })
	private List<IpvodUser> ipvodUsers = new ArrayList<IpvodUser>(0);

	@Column(name = "BILLING_ID", length = 50, unique = true, nullable = false)
	private String otherId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RATING_LEVEL")
	private IpvodRating ipvodRating;

	// bi-directional many-to-many association to IpvodVisualComponent
	@ManyToMany(mappedBy = "ipvodPackages")
	private List<IpvodVisualMenu> ipvodVisualMenus = new ArrayList<IpvodVisualMenu>();

	public IpvodPackage() {
	}

	public long getPackageId() {
		return this.packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public Date getDateEnd() {
		return this.dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getDateStart() {
		return this.dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<IpvodAssetPackage> getIpvodAssetPackages() {
		return this.ipvodAssetPackages;
	}

	public void setIpvodAssetPackages(List<IpvodAssetPackage> ipvodAssetPackages) {
		this.ipvodAssetPackages = ipvodAssetPackages;
	}

	public IpvodAssetPackage addIpvodAssetPackage(
			IpvodAssetPackage ipvodAssetPackage) {
		getIpvodAssetPackages().add(ipvodAssetPackage);
		ipvodAssetPackage.setIpvodPackage(this);

		return ipvodAssetPackage;
	}

	public IpvodAssetPackage removeIpvodAssetPackage(
			IpvodAssetPackage ipvodAssetPackage) {
		getIpvodAssetPackages().remove(ipvodAssetPackage);
		ipvodAssetPackage.setIpvodPackage(null);

		return ipvodAssetPackage;
	}

	public IpvodPackageType getIpvodPackageType() {
		return this.ipvodPackageType;
	}

	public void setIpvodPackageType(IpvodPackageType ipvodPackageType) {
		this.ipvodPackageType = ipvodPackageType;
	}

	public List<IpvodUser> getIpvodUsers() {
		return this.ipvodUsers;
	}

	public void setIpvodUsers(List<IpvodUser> ipvodUsers) {
		this.ipvodUsers = ipvodUsers;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	public IpvodRating getIpvodRating() {
		return ipvodRating;
	}

	public void setIpvodRating(IpvodRating ipvodRating) {
		this.ipvodRating = ipvodRating;
	}

	public List<IpvodVisualMenu> getIpvodVisualMenus() {
		return ipvodVisualMenus;
	}

	public void setIpvodVisualMenus(List<IpvodVisualMenu> ipvodVisualMenus) {
		this.ipvodVisualMenus = ipvodVisualMenus;
	}
}