package br.com.gvt.eng.vod.vo.provisioning;

import org.codehaus.jackson.annotate.JsonIgnore;

import br.com.gvt.eng.vod.vo.AuthVO;
import br.com.gvt.eng.vod.vo.ITConnectionVO;
import br.com.gvt.eng.vod.vo.it.UserVO;

public class Equipment implements ITConnectionVO {

	private String serial;
	private String cardId;
	private String mainKey;
	private String cas;
	private Long type;
	private String typeDesc;
	private AuthVO auth;
	@JsonIgnore
	private Long userId;
	private UserVO user;

	public Equipment() {
	}

	public Equipment(String serial) {
		this.serial = serial;
	}

	public Equipment(String serial, String cardId) {
		this.serial = serial;
		this.cardId = cardId;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getMainKey() {
		return mainKey;
	}

	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public AuthVO getAuth() {
		return auth;
	}

	public void setAuth(AuthVO auth) {
		this.auth = auth;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
