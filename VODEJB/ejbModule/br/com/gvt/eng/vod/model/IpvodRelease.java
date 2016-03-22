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
@Table(name = "IPVOD_RELEASE")
public class IpvodRelease implements Serializable {

	private static final long serialVersionUID = -3106582693769801265L;

	@Id
	@Column(name = "RELEASE_ID")
	@SequenceGenerator(name = "SEQ_RELEASE", sequenceName = "SEQ_RELEASE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RELEASE")
	private Long releaseId;

	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset asset;

	@Column(name = "RELEASE_ORDER")
	private Long order;

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public IpvodAsset getAsset() {
		return asset;
	}

	public void setAsset(IpvodAsset asset) {
		this.asset = asset;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

}
