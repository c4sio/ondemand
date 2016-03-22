package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class ReasonVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long reasonID;

	private String reasonCode;

	private String description;

	public Long getReasonID() {
		return reasonID;
	}

	public void setReasonID(Long reasonID) {
		this.reasonID = reasonID;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}