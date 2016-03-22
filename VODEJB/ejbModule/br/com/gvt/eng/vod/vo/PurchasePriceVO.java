package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.Date;

public class PurchasePriceVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long purchaseId;
	private Date validUntil;
	private Date purchaseDate;
	private Double price;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
