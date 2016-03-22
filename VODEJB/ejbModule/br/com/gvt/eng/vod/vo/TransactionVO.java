package br.com.gvt.eng.vod.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionVO implements ITConnectionVO {

	private Long playKey;

	private Date playTime;

	private Long minutesPlayed;

	private String service;

	private String macAdress;

	private String title;

	private String provider;

	private String customerIdentity;

	public Long getPlayKey() {
		return playKey;
	}

	public void setPlayKey(Long playKey) {
		this.playKey = playKey;
	}

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getPlayTime() {
		return playTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(playTime) : null;
	}

	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}

	public Long getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(Long minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMacAdress() {
		return macAdress;
	}

	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCustomerIdentity() {
		return customerIdentity;
	}

	public void setCustomerIdentity(String customerIdentity) {
		this.customerIdentity = customerIdentity;
	}

}
