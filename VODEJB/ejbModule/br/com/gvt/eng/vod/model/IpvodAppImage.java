package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_APP_IMAGE")
public class IpvodAppImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_IMAGE_ID")
	@SequenceGenerator(name = "SEQ_APP_IMAGE", sequenceName = "SEQ_APP_IMAGE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_APP_IMAGE")
	private Long id;

	@Column(name = "ICON")
	private String icon;

	@Column(name = "ICON_SELECTED")
	private String iconSelected;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
