package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the IPVOD_PACKAGE_TYPE database table.
 * 
 */
@Entity
@Table(name="IPVOD_PACKAGE_TYPE")
public class IpvodPackageType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PACKAGE_TYPE_ID")
	private long packageTypeId;

	private String description;

//	//bi-directional many-to-one association to IpvodPackage
//	@OneToMany(mappedBy="ipvodPackageType", fetch=FetchType.LAZY)
//	private List<IpvodPackage> ipvodPackages;

	public IpvodPackageType() {
	}

	public long getPackageTypeId() {
		return this.packageTypeId;
	}

	public void setPackageTypeId(long packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public List<IpvodPackage> getIpvodPackages() {
//		return this.ipvodPackages;
//	}
//
//	public void setIpvodPackages(List<IpvodPackage> ipvodPackages) {
//		this.ipvodPackages = ipvodPackages;
//	}

//	public IpvodPackage addIpvodPackage(IpvodPackage ipvodPackage) {
//		getIpvodPackages().add(ipvodPackage);
//		ipvodPackage.setIpvodPackageType(this);
//
//		return ipvodPackage;
//	}

//	public IpvodPackage removeIpvodPackage(IpvodPackage ipvodPackage) {
//		getIpvodPackages().remove(ipvodPackage);
//		ipvodPackage.setIpvodPackageType(null);
//
//		return ipvodPackage;
//	}

}