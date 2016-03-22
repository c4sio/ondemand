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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_APP_CATEGORY")
public class IpvodAppCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APP_CAT_ID")
	@SequenceGenerator(name = "SEQ_APP_CATEGORY", sequenceName = "SEQ_APP_CATEGORY", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_APP_CATEGORY")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "IPVOD_APP_CAT_INFO", joinColumns = { @JoinColumn(name = "APP_CAT_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_INFO_ID") })
	private List<IpvodAppInfo> ipvodAppInfos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IpvodAppInfo> getIpvodAppInfos() {
		return ipvodAppInfos;
	}

	public void setIpvodAppInfos(List<IpvodAppInfo> ipvodAppInfos) {
		this.ipvodAppInfos = ipvodAppInfos;
	}

}
