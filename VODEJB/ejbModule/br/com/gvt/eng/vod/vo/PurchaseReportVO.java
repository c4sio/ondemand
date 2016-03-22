package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.Date;

public class PurchaseReportVO implements Serializable {

	private static final long serialVersionUID = 1003503792249482756L;

	private Long purchaseId;

	private Long assetId;

	private String title;

	private Date purchaseDate;
	
	private Double amountPaid;

	private Boolean billed = Boolean.FALSE;
	
	private String crmCustomerId;

	public Long getPurchaseId() {
		return purchaseId;
	}


	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
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


	public Double getAmountPaid() {
		return amountPaid;
	}


	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}


	public Date getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public Boolean getBilled() {
		return billed;
	}


	public void setBilled(Boolean billed) {
		this.billed = billed;
	}


	public String getCrmCustomerId() {
		return crmCustomerId;
	}


	public void setCrmCustomerId(String crmCustomerId) {
		this.crmCustomerId = crmCustomerId;
	}

}
