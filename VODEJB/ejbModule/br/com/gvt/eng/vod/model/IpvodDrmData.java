package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IPVOD_INTEGR_DRM_DATA")
@NamedQueries({
		@NamedQuery(name = "IpvodDrmData.allDataLessCompleted", query = "SELECT con FROM IpvodDrmData con WHERE con.statusDrm <> :state"),
		@NamedQuery(name = "IpvodDrmData.allDataByIngestId", query = "SELECT con FROM IpvodDrmData con WHERE con.ipvodIngestStage.id = :ingestId") })
public class IpvodDrmData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ALL_DATA_LESS_COMPLETED = "IpvodDrmData.allDataLessCompleted";

	public static final String ALL_DATA_BY_INGESTID = "IpvodDrmData.allDataByIngestId";

	@Id
	@Column(name = "DRM_ID")
	@SequenceGenerator(name = "SEQ_DRM", sequenceName = "SEQ_DRM", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DRM")
	private long drmId;

	@ManyToOne
	@JoinColumn(name = "INGEST_ID", nullable = false)
	private IpvodIngestStage ipvodIngestStage;

	@Column(name = "JOB_ID", nullable = false)
	private String jobIdDrm;

	@Column(name = "NAME_FILE", nullable = false)
	private String nameFile;

	@Column(name = "COOKIE_DRM", nullable = false)
	private String cookieDrm;

	@Column(name = "STATUS", nullable = false)
	private String statusDrm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_START", nullable = false)
	private Date dateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_END")
	private Date dateEndDrm;

	@Column(name = "SEND_EMAIL", nullable = false)
	private boolean sendMail;

	@Column(name = "PERCENT_COMP", nullable = false)
	private Double percentCompDrm;

	public long getDrmId() {
		return drmId;
	}

	public void setDrmId(long drmId) {
		this.drmId = drmId;
	}

	public IpvodIngestStage getIpvodIngestStage() {
		return ipvodIngestStage;
	}

	public void setIpvodIngestStage(IpvodIngestStage ipvodIngestStage) {
		this.ipvodIngestStage = ipvodIngestStage;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getCookieDrm() {
		return cookieDrm;
	}

	public void setCookieDrm(String cookieDrm) {
		this.cookieDrm = cookieDrm;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEndDrm() {
		return dateEndDrm;
	}

	public void setDateEndDrm(Date dateEndDrm) {
		this.dateEndDrm = dateEndDrm;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public String getJobIdDrm() {
		return jobIdDrm;
	}

	public void setJobIdDrm(String jobIdDrm) {
		this.jobIdDrm = jobIdDrm;
	}

	public String getStatusDrm() {
		return statusDrm;
	}

	public void setStatusDrm(String statusDrm) {
		this.statusDrm = statusDrm;
	}

	public Double getPercentCompDrm() {
		return percentCompDrm;
	}

	public void setPercentCompDrm(Double percentCompDrm) {
		this.percentCompDrm = percentCompDrm;
	}

}
