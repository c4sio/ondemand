package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class IpvodIngestStage implements Serializable {

	static Logger logger = Logger.getLogger(IpvodIngestStage.class.getName());

	private static final long serialVersionUID = 1L;

	private Long id;
	private IpvodIngestType stageType;
	private Integer result;
	private String adicionalInfo;
	private String assetInfo;
	private IpvodAsset ipvodAsset;
	private String assetName;
	private int priority;
	private String ftpPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IpvodIngestType getStageType() {
		return stageType;
	}

	public void setStageType(IpvodIngestType stageType) {
		this.stageType = stageType;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getAdicionalInfo() {
		return adicionalInfo;
	}

	public void setAdicionalInfo(String adicionalInfo) {
		this.adicionalInfo = adicionalInfo;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public IpvodAsset getIpvodAsset() {
		return ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

}