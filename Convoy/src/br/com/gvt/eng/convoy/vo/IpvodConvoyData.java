package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class IpvodConvoyData implements Serializable {

	static Logger logger = Logger.getLogger(IpvodConvoyData.class.getName());

	private static final long serialVersionUID = 1L;

	private long convoyId;
	private IpvodIngestStage ipvodIngestStage;
	private String jobIdConvoy;
	private String nameFile;
	private String fileConvoy;
	private String statusConvoy;
	private boolean sendMail;
	private Double percentCompConvoy;
	private IpvodMediaAsset ipvodMediaAsset;

	public long getConvoyId() {
		return convoyId;
	}

	public void setConvoyId(long convoyId) {
		this.convoyId = convoyId;
	}

	public IpvodIngestStage getIpvodIngestStage() {
		return ipvodIngestStage;
	}

	public void setIpvodIngestStage(IpvodIngestStage ipvodIngestStage) {
		this.ipvodIngestStage = ipvodIngestStage;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getFileConvoy() {
		return fileConvoy;
	}

	public void setFileConvoy(String fileConvoy) {
		this.fileConvoy = fileConvoy;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public String getJobIdConvoy() {
		return jobIdConvoy;
	}

	public void setJobIdConvoy(String jobIdConvoy) {
		this.jobIdConvoy = jobIdConvoy;
	}

	public String getStatusConvoy() {
		return statusConvoy;
	}

	public void setStatusConvoy(String statusConvoy) {
		this.statusConvoy = statusConvoy;
	}

	public Double getPercentCompConvoy() {
		return percentCompConvoy;
	}

	public void setPercentCompConvoy(Double percentCompConvoy) {
		this.percentCompConvoy = percentCompConvoy;
	}

	public IpvodMediaAsset getIpvodMediaAsset() {
		return ipvodMediaAsset;
	}

	public void setIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		this.ipvodMediaAsset = ipvodMediaAsset;
	}

}