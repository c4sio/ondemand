package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_VISUAL_COMPONENT database table.
 * 
 */
@Entity
@Table(name = "IPVOD_VISUAL_COMPONENT")
public class IpvodVisualComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VISUAL_COMPONENT_ID")
	private long visualComponentId;

	private String description;

	private String name;

	@Column(name = "\"TYPE\"")
	private BigDecimal type;

	// bi-directional many-to-one association to IpvodPackage
	@OneToMany(mappedBy = "ipvodVisualComponent", fetch = FetchType.LAZY)
	private List<IpvodVisualMenu> IpvodVisualMenus;

	public long getVisualComponentId() {
		return this.visualComponentId;
	}

	public void setVisualComponentId(long visualComponentId) {
		this.visualComponentId = visualComponentId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getType() {
		return this.type;
	}

	public void setType(BigDecimal type) {
		this.type = type;
	}
}