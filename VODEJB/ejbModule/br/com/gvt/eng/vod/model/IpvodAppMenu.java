package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "IPVOD_APP_MENU")
public class IpvodAppMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_MENU_ID")
	@SequenceGenerator(name = "SEQ_APP_MENU", sequenceName = "SEQ_APP_MENU", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_APP_MENU")
	private Long appMenuId;

	@OneToOne
	@JoinColumn(name = "APP_IMAGE_ID")
	private IpvodAppImage ipvodAppImage;

	@NotNull
	@NotEmpty
	@Column(name = "MODULE_ID")
	private String moduleId;

	@NotNull
	@NotEmpty
	@Column(name = "FIT_ADDRESS")
	private String fitAddress;

	@NotNull
	@NotEmpty
	@Column(name = "APP_NAME")
	private String appName;

	@Column(name = "APP_ACTIVE")
	private boolean active;

	// @JoinColumn(name = "APP_MENU_ID")
	// @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy =
	// "ipvodAppMenu")
	// private List<IpvodAppMenuCategory> appMenuCategories = new
	// ArrayList<IpvodAppMenuCategory>();

	public Long getAppMenuId() {
		return appMenuId;
	}

	public void setAppMenuId(Long appMenuId) {
		this.appMenuId = appMenuId;
	}

	public IpvodAppImage getIpvodAppImage() {
		return ipvodAppImage;
	}

	public void setIpvodAppImage(IpvodAppImage ipvodAppImage) {
		this.ipvodAppImage = ipvodAppImage;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
