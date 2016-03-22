package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * The persistent class for the IPVOD_CATEGORY database table.
 * 
 */
@Entity
@Table(name = "IPVOD_CATEGORY")
@NamedQueries({ @NamedQuery(name = "IpvodCategory.findByDescription", query = "SELECT ic FROM IpvodCategory ic WHERE ic.description = :description") })
@JsonFilter("IpvodCategory")
public class IpvodCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_DESCRIPTION = "IpvodCategory.findByDescription";

	@Id
	@Column(name = "CATEGORY_ID")
	@SequenceGenerator(name = "SEQ_CATEGORY", sequenceName = "SEQ_CATEGORY", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CATEGORY")
	private Long categoryId;

	@Column(name = "DESCRIPTION", length = 30)
	private String description;

	// bi-directional many-to-one association to IpvodAsset
	@OneToMany(mappedBy = "ipvodCategory1")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<IpvodAsset> ipvodAssets1;

	// bi-directional many-to-one association to IpvodAsset
	@OneToMany(mappedBy = "ipvodCategory2")
	private List<IpvodAsset> ipvodAssets2;

	@Transient
	private int ipvodAssetsCount;

	public IpvodCategory() {
	}

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IpvodAsset> getIpvodAssets1() {
		return this.ipvodAssets1;
	}

	public void setIpvodAssets1(List<IpvodAsset> ipvodAssets1) {
		this.ipvodAssets1 = ipvodAssets1;
	}

	public IpvodAsset addIpvodAssets1(IpvodAsset ipvodAssets1) {
		getIpvodAssets1().add(ipvodAssets1);
		ipvodAssets1.setIpvodCategory1(this);

		return ipvodAssets1;
	}

	public IpvodAsset removeIpvodAssets1(IpvodAsset ipvodAssets1) {
		getIpvodAssets1().remove(ipvodAssets1);
		ipvodAssets1.setIpvodCategory1(null);

		return ipvodAssets1;
	}

	public List<IpvodAsset> getIpvodAssets2() {
		return this.ipvodAssets2;
	}

	public void setIpvodAssets2(List<IpvodAsset> ipvodAssets2) {
		this.ipvodAssets2 = ipvodAssets2;
	}

	public IpvodAsset addIpvodAssets2(IpvodAsset ipvodAssets2) {
		getIpvodAssets2().add(ipvodAssets2);
		ipvodAssets2.setIpvodCategory2(this);

		return ipvodAssets2;
	}

	public IpvodAsset removeIpvodAssets2(IpvodAsset ipvodAssets2) {
		getIpvodAssets2().remove(ipvodAssets2);
		ipvodAssets2.setIpvodCategory2(null);

		return ipvodAssets2;
	}

	public int getIpvodAssetsCount() {
		return ipvodAssetsCount;
	}

	public void setIpvodAssetsCount(int ipvodAssetsCount) {
		this.ipvodAssetsCount = ipvodAssetsCount;
	}

}