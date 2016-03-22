package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class MobileUserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long CPF;

	private String authInfo;
	
//	private Long pin;
	
	private Long equipmentType;
	
	public Long getCPF() {
		return CPF;
	}

	public void setCPF(Long cPF) {
		CPF = cPF;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

//	public Long getPin() {
//		return pin;
//	}
//	
//	public void setPin(Long pin) {
//		this.pin = pin;
//	}

	public Long getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(Long equipmentType) {
		this.equipmentType = equipmentType;
	}

}