package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BalancerVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idBalancer;
	private String nameFile;
	private String statusBalancer;
	private Double percentCompBalancer;
	private Date dateEndBalancer;

	public Long getIdBalancer() {
		return idBalancer;
	}

	public void setIdBalancer(Long idBalancer) {
		this.idBalancer = idBalancer;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getStatusBalancer() {
		return statusBalancer;
	}

	public void setStatusBalancer(String statusBalancer) {
		this.statusBalancer = statusBalancer;
	}

	public Double getPercentCompBalancer() {
		return percentCompBalancer;
	}

	public void setPercentCompBalancer(Double percentCompBalancer) {
		this.percentCompBalancer = percentCompBalancer;
	}

	public String getDateEndBalancer() {
		return dateEndBalancer == null ? "" : new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss").format(dateEndBalancer);
	}

	public void setDateEndBalancer(Date dateEndBalancer) {
		this.dateEndBalancer = dateEndBalancer;
	}

}
