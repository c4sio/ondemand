package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionVO implements Serializable {

	private static final long serialVersionUID = 1173609003620025346L;

	private int playTime;
	private String title;
	private String provider;
	private String genre;
	private String mac;
	private Date sessionInstantiationTime;
	private Date sessionDestructionTime;
	// private Integer reasonCode;
	private ReasonVO reasonVO;
	private String responseCode;
	private Long minutesPlayed;
	private Long assetId;
	private Long purchaseId;
	private Boolean status = Boolean.TRUE;

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
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

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getSessionInstantiationTime() {
		return sessionInstantiationTime != null ? new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(sessionInstantiationTime) : null;
	}

	public void setSessionInstantiationTime(Date sessionInstantiationTime) {
		this.sessionInstantiationTime = sessionInstantiationTime;
	}

	/*
	 * Alternativa utilizada para evitar que seja enviado para o cliente o
	 * atributo fastTime que é os milisegundos.
	 */
	public String getSessionDestructionTime() {
		return sessionDestructionTime != null ? new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(sessionDestructionTime) : null;
	}

	public void setSessionDestructionTime(Date sessionDestructionTime) {
		this.sessionDestructionTime = sessionDestructionTime;
	}

	public ReasonVO getReasonVO() {
		return reasonVO;
	}

	public void setReasonVO(ReasonVO reasonVO) {
		this.reasonVO = reasonVO;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Long getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(Long minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
