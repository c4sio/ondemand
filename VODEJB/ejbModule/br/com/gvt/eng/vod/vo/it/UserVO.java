package br.com.gvt.eng.vod.vo.it;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.gvt.eng.vod.vo.ITConnectionVO;

/**
 * 
 * @author joaozarate
 *
 */
public class UserVO implements ITConnectionVO, Serializable {

	private static final long serialVersionUID = -258329274751511977L;

	private Boolean active;

	private String crmCustomerId;

	private String serviceRegion;

	private String authInfo;

	private Calendar createdAt;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCrmCustomerId() {
		return crmCustomerId;
	}

	public void setCrmCustomerId(String crmCustomerId) {
		this.crmCustomerId = crmCustomerId;
	}

	public String getServiceRegion() {
		return serviceRegion;
	}

	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public String getCreatedAt() {
		return createdAt != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt.getTime()) : null;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

}
