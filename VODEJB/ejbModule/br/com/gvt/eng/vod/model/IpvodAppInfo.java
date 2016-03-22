package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_APP_INFO")
public class IpvodAppInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_INFO_ID")
	@SequenceGenerator(name = "SEQ_APP_INFO", sequenceName = "SEQ_APP_INFO", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_APP_INFO")
	private Long id;

	@Column(name = "MODULE_ID")
	private String moduleId;

	@Column(name = "FIT_ADDRESS")
	private String fitAddress;

	@Column(name = "APP_ORDER")
	private long order;

	@Column(name = "APP_NAME")
	private String appName;

	@Column(name = "APP_VERSION")
	private long version;

	@Column(name = "APP_ACTIVE")
	private boolean active;

	@Column(name = "APP_DEFAULT")
	private boolean consultDefault;

	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_TYPE_ID")
	private IpvodEquipmentType ipvodEquipmentType;

	@ManyToOne
	@JoinColumn(name = "APP_IMAGE_ID")
	private IpvodAppImage ipvodAppImage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public IpvodEquipmentType getIpvodEquipmentType() {
		return ipvodEquipmentType;
	}

	public void setIpvodEquipmentType(IpvodEquipmentType ipvodEquipmentType) {
		this.ipvodEquipmentType = ipvodEquipmentType;
	}

	public IpvodAppImage getIpvodAppImage() {
		return ipvodAppImage;
	}

	public void setIpvodAppImage(IpvodAppImage ipvodAppImage) {
		this.ipvodAppImage = ipvodAppImage;
	}

	public boolean isConsultDefault() {
		return consultDefault;
	}

	public void setConsultDefault(boolean consultDefault) {
		this.consultDefault = consultDefault;
	}
}