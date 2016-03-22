package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_EQUIPMENT_HYBRID")
public class IpvodEquipmentHybrid implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EQUIPMENT_HYBRID_ID")
	@SequenceGenerator(name = "SEQ_EQUIPMENT_HYBRID", sequenceName = "SEQ_EQUIPMENT_HYBRID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EQUIPMENT_HYBRID")
	private long equipmentId;

	@Column(name = "CRM_CUSTOMER_ID", unique = true, nullable = false)
	private String crmCustomerId;

	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_TYPE_ID", nullable = false)
	private IpvodEquipmentType ipvodEquipmentType;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "IPVOD_APP_EQUIP_HYBRID", joinColumns = { @JoinColumn(name = "EQUIPMENT_HYBRID_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_INFO_ID") })
	private List<IpvodAppInfo> ipvodAppInfos;

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getCrmCustomerId() {
		return crmCustomerId;
	}

	public void setCrmCustomerId(String crmCustomerId) {
		this.crmCustomerId = crmCustomerId;
	}

	public IpvodEquipmentType getIpvodEquipmentType() {
		return ipvodEquipmentType;
	}

	public void setIpvodEquipmentType(IpvodEquipmentType ipvodEquipmentType) {
		this.ipvodEquipmentType = ipvodEquipmentType;
	}

	public List<IpvodAppInfo> getIpvodAppInfos() {
		return ipvodAppInfos;
	}

	public void setIpvodAppInfos(List<IpvodAppInfo> ipvodAppInfos) {
		this.ipvodAppInfos = ipvodAppInfos;
	}

}
