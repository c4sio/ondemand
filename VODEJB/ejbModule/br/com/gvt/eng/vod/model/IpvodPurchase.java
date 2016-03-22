package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * The persistent class for the IPVOD_PURCHASE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_PURCHASE")
@NamedQueries({
	@NamedQuery(name = "IpvodPurchase.currentPurchaseByUserAsset", query = "SELECT p FROM IpvodPurchase p WHERE p.ipvodAsset.assetId = :assetId and p.ipvodEquipment.ipvodUser.userId = :userId and p.validUntil >= current_timestamp "),
	@NamedQuery(name = "IpvodPurchase.purchaseByAssetList", query = "SELECT p FROM IpvodPurchase p WHERE p.ipvodEquipment.ipvodUser.userId = :userId and p.validUntil >= current_timestamp and p.ipvodAsset.assetId in(:assets) order by p.purchaseDate desc")
	})
@JsonFilter("IpvodPurchase")
public class IpvodPurchase implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CURRENT_PURCHASE_BY_USER_ASSET = "IpvodPurchase.currentPurchaseByUserAsset";
	public static final String CURRENT_PURCHASE_BY_ASSET_LIST = "IpvodPurchase.purchaseByAssetList";
	@Id
	@Column(name = "PURCHASE_ID")
	@SequenceGenerator(name = "SEQ_PURCHASE", sequenceName = "SEQ_PURCHASE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PURCHASE")
	private long purchaseId;

	private Boolean billed;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PURCHASE_DATE")
	private Date purchaseDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VALID_UNTIL")
	private Date validUntil;	

	@Transient
	private Date validUntilTimezone;
	
	// bi-directional many-to-one association to IpvodAsset
	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset ipvodAsset;

	// bi-directional many-to-one association to IpvodEquipment
	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_ID")
	private IpvodEquipment ipvodEquipment;

	// bi-directional many-to-one association to IpvodSession
	@OneToMany(mappedBy = "ipvodPurchase")
	private List<IpvodSession> ipvodSessions;

	@Column(name = "AMOUNT_PAID", nullable = false, length = 6, precision = 2)
	private Double amountPaid;

	@Column(name = "BILLING_ID")
	private String billingID;

	public long getPurchaseId() {
		return this.purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Boolean getBilled() {
		return this.billed;
	}

	public void setBilled(Boolean billed) {
		this.billed = billed;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getValidUntil() {
		return this.validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getValidUntilTimezone() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXX");
		return sdf.format(this.validUntil);
	}
	
	public void setValidUntilTimezone(Date validUntilTimezone) {
		this.validUntilTimezone = validUntilTimezone;
	}

	public IpvodAsset getIpvodAsset() {
		return this.ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public IpvodEquipment getIpvodEquipment() {
		return this.ipvodEquipment;
	}

	public void setIpvodEquipment(IpvodEquipment ipvodEquipment) {
		this.ipvodEquipment = ipvodEquipment;
	}

	public List<IpvodSession> getIpvodSessions() {
		return this.ipvodSessions;
	}

	public void setIpvodSessions(List<IpvodSession> ipvodSessions) {
		this.ipvodSessions = ipvodSessions;
	}

	public IpvodSession addIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().add(ipvodSession);
		ipvodSession.setIpvodPurchase(this);
		return ipvodSession;
	}

	public IpvodSession removeIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().remove(ipvodSession);
		ipvodSession.setIpvodPurchase(null);
		return ipvodSession;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getBillingID() {
		return billingID;
	}

	public void setBillingID(String billingID) {
		this.billingID = billingID;
	}

}