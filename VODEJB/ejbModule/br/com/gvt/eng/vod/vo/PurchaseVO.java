package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseVO implements Serializable {

	private static final long serialVersionUID = 1003503792249482756L;

	private Date buyTime;

	private String title;

	private String genre;

	private String provider;

	private String mac;

	private Long purchaseId;

	private Long assetId;

	private Double offeringPrice;

	private Boolean status = Boolean.TRUE;

	private Date rentalStart;

	private Date rentalEnd;

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getBuyTime() {
		return buyTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(buyTime) : null;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Double getOfferingPrice() {
		return offeringPrice;
	}

	public void setOfferingPrice(Double offeringPrice) {
		this.offeringPrice = offeringPrice;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getRentalStart() {
		return rentalStart != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rentalStart) : null;
	}

	public void setRentalStart(Date rentalStart) {
		this.rentalStart = rentalStart;
	}

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getRentalEnd() {
		return rentalEnd != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rentalEnd) : null;
	}

	public void setRentalEnd(Date rentalEnd) {
		this.rentalEnd = rentalEnd;
	}

}
