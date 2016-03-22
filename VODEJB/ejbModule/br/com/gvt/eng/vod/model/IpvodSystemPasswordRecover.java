package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_SYS_PW_REC database table.
 * 
 */
@Entity
@Table(name = "IPVOD_SYS_PW_REC")
public class IpvodSystemPasswordRecover implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public IpvodSystemPasswordRecover(){
		
	}
	public IpvodSystemPasswordRecover(IpvodUserSystem ipvodUserSys, String passwordRecoverCode) {
		this.ipvodUserSys = ipvodUserSys;
		this.passwordRecoverCode = passwordRecoverCode;
	}

	@Id
	@Column(name = "SYS_PW_REC_ID")
	private String passwordRecoverCode;

	@OneToOne
	@JoinColumn(name = "USER_SYS_ID")
	private IpvodUserSystem ipvodUserSys;

	public String getPasswordRecoverCode() {
		return passwordRecoverCode;
	}
	public void setPasswordRecoverCode(String passwordRecoverCode) {
		this.passwordRecoverCode = passwordRecoverCode;
	}
	public IpvodUserSystem getIpvodUserSys() {
		return ipvodUserSys;
	}

	public void setIpvodUserSys(IpvodUserSystem ipvodUserSys) {
		this.ipvodUserSys = ipvodUserSys;
	}

}