package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_CONTENT_PROVIDER database table.
 * 
 */
@Entity
@Table(name = "IPVOD_CONTENT_PROVIDER")
public class IpvodContentProvider implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONTENT_PROVIDER_ID")
	private long contentProviderId;

	@Column(name = "NAME")
	private String providerName;

	@Column(name = "PROVIDER_ID")
	private String providerId;

	@Column(name = "RATING")
	private long providerRating;

	// // bi-directional many-to-one association to IpvodAsset
	// @OneToMany(mappedBy = "ipvodContentProvider")
	// private List<IpvodAsset> ipvodAssets;

	public IpvodContentProvider() {
	}

	public long getContentProviderId() {
		return this.contentProviderId;
	}

	public void setContentProviderId(long contentProviderId) {
		this.contentProviderId = contentProviderId;
	}

	public String getProviderName() {
		return this.providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public long getProviderRating() {
		return providerRating;
	}

	public void setProviderRating(long providerRating) {
		this.providerRating = providerRating;
	}

	// public List<IpvodAsset> getIpvodAssets() {
	// return this.ipvodAssets;
	// }
	//
	// public void setIpvodAssets(List<IpvodAsset> ipvodAssets) {
	// this.ipvodAssets = ipvodAssets;
	// }
	//
	// public IpvodAsset addIpvodAsset(IpvodAsset ipvodAsset) {
	// getIpvodAssets().add(ipvodAsset);
	// ipvodAsset.setIpvodContentProvider(this);
	//
	// return ipvodAsset;
	// }
	//
	// public IpvodAsset removeIpvodAsset(IpvodAsset ipvodAsset) {
	// getIpvodAssets().remove(ipvodAsset);
	// ipvodAsset.setIpvodContentProvider(null);
	//
	// return ipvodAsset;
	// }

}