package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class AppMenuVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String appName;
	private String icon;
	private String iconSelected;
	private String moduleId;
	private String fitAddress;
	private boolean active;
	private long order;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconSelected() {
		return iconSelected;
	}

	public void setIconSelected(String iconSelected) {
		this.iconSelected = iconSelected;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getFitAddress() {
		return fitAddress;
	}

	public void setFitAddress(String fitAddress) {
		this.fitAddress = fitAddress;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

}
