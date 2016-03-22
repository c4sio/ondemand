package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_REASON database table.
 */
@Entity
@Table(name = "IPVOD_REASON")
@NamedQueries({ @NamedQuery(name = "IpvodReason.findDataByReasonCode", query = "SELECT rea FROM IpvodReason rea WHERE rea.reasonCode =:reasonCode") })
public class IpvodReason implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_DATA_BY_REASON_CODE = "IpvodReason.findDataByReasonCode";

	@Id
	@Column(name = "REASON_ID")
	private Long reasonID;

	@Column(name = "REASON_CODE")
	private String reasonCode;

	@Column(name = "DESCRIPTION")
	private String description;

	public Long getReasonID() {
		return reasonID;
	}

	public void setReasonID(Long reasonID) {
		this.reasonID = reasonID;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
