package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class IpvodMediaType implements Serializable {

	static Logger logger = Logger.getLogger(IpvodMediaType.class.getName());

	private static final long serialVersionUID = 1L;

	private long mediaTypeId;

	private String description;

	public long getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(long mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
