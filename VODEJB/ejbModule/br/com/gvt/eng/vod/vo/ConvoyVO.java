package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvoyVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long convoyId;
	private String nameFile;
	private String statusConvoy;
	private Double percentCompConvoy;
	private Date dateEndConvoy;

	public Long getConvoyId() {
		return convoyId;
	}

	public void setConvoyId(Long convoyId) {
		this.convoyId = convoyId;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
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

	public String getDateEndConvoy() {
		return dateEndConvoy == null ? "" : new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss").format(dateEndConvoy);
	}

	public void setDateEndConvoy(Date dateEndConvoy) {
		this.dateEndConvoy = dateEndConvoy;
	}

}
