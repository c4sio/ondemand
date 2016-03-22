package br.com.gvt.eng.vod.vo;

import java.util.Date;


public class VDRPurchaseReportVO extends PurchaseReportVO {

	private static final long serialVersionUID = 1003503792249482756L;

	private Date eventDate;
	private String serviceRegion;
	private Date validUntil;
	private String providerName;
	private String providerId;			
	private String mac;
	private String product;
	
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getServiceRegion() {
		return serviceRegion;
	}
	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

	
}
