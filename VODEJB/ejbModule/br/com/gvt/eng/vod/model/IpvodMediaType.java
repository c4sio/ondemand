package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IPVOD_MEDIA_TYPE database table.
 * 
 */
@Entity
@Table(name="IPVOD_MEDIA_TYPE")
public class IpvodMediaType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MEDIA_TYPE_ID")
	private long mediaTypeId;

	private String description;

	//bi-directional many-to-one association to IpvodMediaAsset
	@OneToMany(mappedBy="ipvodMediaType")
	private List<IpvodMediaAsset> ipvodMediaAssets;

	public IpvodMediaType() {
	}

	public long getMediaTypeId() {
		return this.mediaTypeId;
	}

	public void setMediaTypeId(long mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IpvodMediaAsset> getIpvodMediaAssets() {
		return this.ipvodMediaAssets;
	}

	public void setIpvodMediaAssets(List<IpvodMediaAsset> ipvodMediaAssets) {
		this.ipvodMediaAssets = ipvodMediaAssets;
	}

	public IpvodMediaAsset addIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		getIpvodMediaAssets().add(ipvodMediaAsset);
		ipvodMediaAsset.setIpvodMediaType(this);

		return ipvodMediaAsset;
	}

	public IpvodMediaAsset removeIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		getIpvodMediaAssets().remove(ipvodMediaAsset);
		ipvodMediaAsset.setIpvodMediaType(null);

		return ipvodMediaAsset;
	}

}