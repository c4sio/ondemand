package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class AppInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String icon;
	private String iconSelected;
	private String moduleId;
	private String fitAddress;
	private long order;
	private long version;
	private String equipment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

}
