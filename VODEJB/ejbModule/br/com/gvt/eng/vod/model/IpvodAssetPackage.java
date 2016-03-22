package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * The persistent class for the IPVOD_ASSET_PACKAGE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_ASSET_PACKAGE")
@NamedQueries({
		@NamedQuery(name = "IpvodAssetPackage.listByPackageSTB", query = "SELECT ap FROM IpvodAssetPackage ap WHERE ap.ipvodPackage.packageId = :packageId AND ap.ipvodAsset.isRevised = true AND ap.ipvodAsset.licenseWindowStart < current_timestamp AND ap.ipvodAsset.licenseWindowEnd > current_timestamp"),
		@NamedQuery(name = "IpvodAssetPackage.listByPackage", query = "SELECT ap FROM IpvodAssetPackage ap WHERE ap.ipvodPackage.packageId = :packageId") })
@JsonFilter("IpvodAssetPackage")
public class IpvodAssetPackage implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String LIST_BY_PACKAGE_STB = "IpvodAssetPackage.listByPackageSTB";
	public static String LIST_BY_PACKAGE = "IpvodAssetPackage.listByPackage";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ASSET_PACKAGE_ID")
	private long assetPackageId;

	private BigDecimal price;

	// bi-directional many-to-one association to IpvodAsset
	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset ipvodAsset;

	// bi-directional many-to-one association to IpvodPackage
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private IpvodPackage ipvodPackage;

	public long getAssetPackageId() {
		return this.assetPackageId;
	}

	public void setAssetPackageId(long assetPackageId) {
		this.assetPackageId = assetPackageId;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public IpvodAsset getIpvodAsset() {
		return this.ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public IpvodPackage getIpvodPackage() {
		return this.ipvodPackage;
	}

	public void setIpvodPackage(IpvodPackage ipvodPackage) {
		this.ipvodPackage = ipvodPackage;
	}

}