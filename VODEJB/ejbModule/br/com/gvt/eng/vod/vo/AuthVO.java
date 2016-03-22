package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String mac;
	private String ip;
	private Date requestDate;
	private String equipament;
	private String authInfo;
	private String token;
	private String connection;
	private String caId;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequestDate() {
		return requestDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestDate) : null;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getEquipament() {
		return equipament;
	}

	public void setEquipament(String equipament) {
		this.equipament = equipament;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getCaId() {
		return caId;
	}

	public void setCaId(String caId) {
		this.caId = caId;
	}

	
}
