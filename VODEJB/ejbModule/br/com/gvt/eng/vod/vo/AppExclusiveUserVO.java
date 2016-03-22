package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class AppExclusiveUserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long appExclusiveId;

	private String keyUser;

	private String packageValue;

	public Long getAppExclusiveId() {
		return appExclusiveId;
	}

	public void setAppExclusiveId(Long appExclusiveId) {
		this.appExclusiveId = appExclusiveId;
	}

	public String getKeyUser() {
		return keyUser;
	}

	public void setKeyUser(String keyUser) {
		this.keyUser = keyUser;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

}
