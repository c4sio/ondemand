package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class IpvodAsset implements Serializable {

	static Logger logger = Logger.getLogger(IpvodAsset.class.getName());

	private static final long serialVersionUID = 1L;

	private long assetId;

	private String title;

	private long totalTime;

	private List<IpvodMediaAsset> ipvodMediaAssets = new ArrayList<IpvodMediaAsset>();

	public long getAssetId() {
		return assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public List<IpvodMediaAsset> getIpvodMediaAssets() {
		return ipvodMediaAssets;
	}

	public void setIpvodMediaAssets(List<IpvodMediaAsset> ipvodMediaAssets) {
		this.ipvodMediaAssets = ipvodMediaAssets;
	}

}
