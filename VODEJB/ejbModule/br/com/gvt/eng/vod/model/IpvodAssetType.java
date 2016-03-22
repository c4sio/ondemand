package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_ASSET_TYPE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_ASSET_TYPE")
@NamedQueries({
		@NamedQuery(name = "IpvodAssetType.list", query = "SELECT iat FROM IpvodAssetType iat"),
		@NamedQuery(name = "IpvodAssetType.findByDescription", query = "SELECT iat FROM IpvodAssetType iat where iat.description = :description") })
public class IpvodAssetType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_DESCRIPTION = "IpvodAssetType.findByDescription";

	public IpvodAssetType() {
	}

	@Id
	@Column(name = "ASSET_TYPE_ID")
	private long assetTypeId;

	@Column(name = "DESCRIPTION")
	private String description;

	// bi-directional many-to-one association to IpvodAsset
//	@OneToMany(mappedBy = "ipvodAssetType")
//	private List<IpvodAsset> ipvodAssets;

	public long getAssetTypeId() {
		return this.assetTypeId;
	}

	public void setAssetTypeId(long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public List<IpvodAsset> getIpvodAssets() {
//		return this.ipvodAssets;
//	}
//
//	public void setIpvodAssets(List<IpvodAsset> ipvodAssets) {
//		this.ipvodAssets = ipvodAssets;
//	}

//	public IpvodAsset addIpvodAsset(IpvodAsset ipvodAsset) {
//		getIpvodAssets().add(ipvodAsset);
//		ipvodAsset.setIpvodAssetType(this);
//
//		return ipvodAsset;
//	}
//
//	public IpvodAsset removeIpvodAsset(IpvodAsset ipvodAsset) {
//		getIpvodAssets().remove(ipvodAsset);
//		ipvodAsset.setIpvodAssetType(null);
//
//		return ipvodAsset;
//	}
}