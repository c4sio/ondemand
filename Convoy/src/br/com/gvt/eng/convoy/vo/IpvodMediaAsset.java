package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class IpvodMediaAsset implements Serializable {

	static Logger logger = Logger.getLogger(IpvodMediaAsset.class.getName());

	private static final long serialVersionUID = 1L;

	private long mediaAssetId;

	private String url;

	private IpvodMediaType ipvodMediaType;

	public long getMediaAssetId() {
		return mediaAssetId;
	}

	public void setMediaAssetId(long mediaAssetId) {
		this.mediaAssetId = mediaAssetId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public IpvodMediaType getIpvodMediaType() {
		return ipvodMediaType;
	}

	public void setIpvodMediaType(IpvodMediaType ipvodMediaType) {
		this.ipvodMediaType = ipvodMediaType;
	}

}
