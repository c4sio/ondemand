package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_EQUIPMENT_TYPE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_EQUIPMENT_TYPE")
public class IpvodEquipmentType implements Serializable {

	private static final long serialVersionUID = 9113027363525299427L;

	@Id
	@Column(name = "EQUIPMENT_TYPE_ID")
	private long equipmentTypeId;

	private String description;

	public IpvodEquipmentType() {
	}

	public long getEquipmentTypeId() {
		return this.equipmentTypeId;
	}

	public void setEquipmentTypeId(long equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}