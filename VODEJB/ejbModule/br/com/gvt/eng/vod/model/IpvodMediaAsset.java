package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.*;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * The persistent class for the IPVOD_MEDIA_ASSET database table.
 * 
 */
@Entity
@Table(name = "IPVOD_MEDIA_ASSET")
@JsonFilter("IpvodMediaAsset")
public class IpvodMediaAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEDIA_ASSET_ID")
	@SequenceGenerator(name = "SEQ_MEDIA_ASSET", sequenceName = "SEQ_MEDIA_ASSET", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEDIA_ASSET")
	private long mediaAssetId;

	private String url;

	// bi-directional many-to-one association to IpvodAsset
	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset ipvodAsset;

	// bi-directional many-to-one association to IpvodMediaType
	@ManyToOne
	@JoinColumn(name = "MEDIA_TYPE_ID")
	private IpvodMediaType ipvodMediaType;

	public long getMediaAssetId() {
		return this.mediaAssetId;
	}

	public void setMediaAssetId(long mediaAssetId) {
		this.mediaAssetId = mediaAssetId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public IpvodAsset getIpvodAsset() {
		return this.ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public IpvodMediaType getIpvodMediaType() {
		return this.ipvodMediaType;
	}

	public void setIpvodMediaType(IpvodMediaType ipvodMediaType) {
		this.ipvodMediaType = ipvodMediaType;
	}
}