package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class TopExecutionVO implements Serializable {

	private static final long serialVersionUID = 1173609003620025346L;

	private Long assetId;
	private String title;
	private Long totalExecutions;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getTotalExecutions() {
		return totalExecutions;
	}

	public void setTotalExecutions(Long totalExecutions) {
		this.totalExecutions = totalExecutions;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

}
