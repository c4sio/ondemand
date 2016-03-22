package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the IPVOD_SESSION database table.
 * 
 */
@Entity
@Table(name = "IPVOD_SESSION")
public class IpvodSession implements Serializable {

	private static final long serialVersionUID = 5217442690487189243L;

	@Id
	@Column(name = "SESSION_ID")
	@SequenceGenerator(name = "SEQ_SESSION", sequenceName = "SEQ_SESSION", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SESSION")
	private Long sessionId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
	private Date eventDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE_END")
	private Date eventDateEnd;

	@Column(name = "PLAY_TIME")
	private int playTime;

	// bi-directional many-to-one association to IpvodEquipment
	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_ID")
	private IpvodEquipment ipvodEquipment;

	// bi-directional many-to-one association to IpvodEventType
	@ManyToOne
	@JoinColumn(name = "EVENT_TYPE_ID")
	private IpvodEventType ipvodEventType;

	// bi-directional many-to-one association to IpvodPurchase
	@ManyToOne
	@JoinColumn(name = "PURCHASE_ID")
	private IpvodPurchase ipvodPurchase;

	@ManyToOne
	@JoinColumn(name = "REASON_ID")
	private IpvodReason ipvodReason;

	@Column(name = "RESPONSECODE")
	private String responsecode;

	@Column(name = "DURATION")
	private Long duration;

	public IpvodSession() {
	}

	public Long getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Date getEventDate() {
		return this.eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public IpvodEquipment getIpvodEquipment() {
		return this.ipvodEquipment;
	}

	public void setIpvodEquipment(IpvodEquipment ipvodEquipment) {
		this.ipvodEquipment = ipvodEquipment;
	}

	public IpvodEventType getIpvodEventType() {
		return this.ipvodEventType;
	}

	public void setIpvodEventType(IpvodEventType ipvodEventType) {
		this.ipvodEventType = ipvodEventType;
	}

	public IpvodPurchase getIpvodPurchase() {
		return this.ipvodPurchase;
	}

	public void setIpvodPurchase(IpvodPurchase ipvodPurchase) {
		this.ipvodPurchase = ipvodPurchase;
	}

	public IpvodReason getIpvodReason() {
		return ipvodReason;
	}

	public void setIpvodReason(IpvodReason ipvodReason) {
		this.ipvodReason = ipvodReason;
	}

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public Date getEventDateEnd() {
		return eventDateEnd;
	}

	public void setEventDateEnd(Date eventDateEnd) {
		this.eventDateEnd = eventDateEnd;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
}