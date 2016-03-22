package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class MediaAssetVO implements Serializable {

	private static final long serialVersionUID = -1646505453669237320L;

	private Long mediaAssetId;

	private String mediaType;

	private String url;

	public Long getMediaAssetId() {
		return mediaAssetId;
	}

	public void setMediaAssetId(Long mediaAssetId) {
		this.mediaAssetId = mediaAssetId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
