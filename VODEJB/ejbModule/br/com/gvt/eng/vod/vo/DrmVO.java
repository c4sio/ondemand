package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DrmVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long drmId;
	private String nameFile;
	private String statusDrm;
	private Double percentCompDrm;
	private Date dateEndDrm;

	public Long getDrmId() {
		return drmId;
	}

	public void setDrmId(Long drmId) {
		this.drmId = drmId;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getStatusDrm() {
		return statusDrm;
	}

	public void setStatusDrm(String statusDrm) {
		this.statusDrm = statusDrm;
	}

	public Double getPercentCompDrm() {
		return percentCompDrm;
	}

	public void setPercentCompDrm(Double percentCompDrm) {
		this.percentCompDrm = percentCompDrm;
	}

	public String getDateEndDrm() {
		return dateEndDrm == null ? "" : new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss").format(dateEndDrm);
	}

	public void setDateEndDrm(Date dateEndDrm) {
		this.dateEndDrm = dateEndDrm;
	}

}
