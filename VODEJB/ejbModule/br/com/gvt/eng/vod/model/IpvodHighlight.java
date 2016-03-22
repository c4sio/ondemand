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
@Table(name = "IPVOD_HIGHLIGHT")
public class IpvodHighlight implements Serializable {

	private static final long serialVersionUID = -1468926795752236393L;

	@Id
	@Column(name = "HIGHLIGHT_ID")
	@SequenceGenerator(name = "SEQ_HIGHLIGHT", sequenceName = "SEQ_HIGHLIGHT", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HIGHLIGHT")
	private Long highlightId;

	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset asset;

	@Column(name = "RELEASE_ORDER")
	private Long order;

	public Long getHighlightId() {
		return highlightId;
	}

	public void setHighlightId(Long highlightId) {
		this.highlightId = highlightId;
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
