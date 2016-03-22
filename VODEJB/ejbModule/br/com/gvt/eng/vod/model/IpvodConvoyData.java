package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IPVOD_INTEGR_CONVOY_DATA")
@NamedQueries({
		@NamedQuery(name = "IpvodConvoyData.allDataLessDone", query = "SELECT con FROM IpvodConvoyData con WHERE con.statusConvoy <> :state"),
		@NamedQuery(name = "IpvodConvoyData.allDataByIngestId", query = "SELECT con FROM IpvodConvoyData con WHERE con.ipvodIngestStage.id = :ingestId") })
public class IpvodConvoyData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ALL_DATA_LESS_DONE = "IpvodConvoyData.allDataLessDone";

	public static final String ALL_DATA_BY_INGESTID = "IpvodConvoyData.allDataByIngestId";

	@Id
	@Column(name = "CONVOY_ID")
	@SequenceGenerator(name = "SEQ_CONVOY", sequenceName = "SEQ_CONVOY", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONVOY")
	private long convoyId;

	@ManyToOne
	@JoinColumn(name = "INGEST_ID", nullable = false)
	private IpvodIngestStage ipvodIngestStage;

	@Column(name = "JOB_ID", nullable = false)
	private String jobIdConvoy;

	@Column(name = "NAME_FILE", nullable = false)
	private String nameFile;

	@Column(name = "FILE_CONVOY", nullable = false)
	private String fileConvoy;

	@Column(name = "STATUS", nullable = false)
	private String statusConvoy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_START", nullable = false)
	private Date dateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_END")
	private Date dateEnd;

	@Column(name = "SEND_EMAIL", nullable = false)
	private boolean sendMail;

	@Column(name = "PERCENT_UPLOAD", nullable = false)
	private Double percentCompConvoy;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "MEDIA_ASSET_ID", nullable = false)
	private IpvodMediaAsset ipvodMediaAsset;

	public long getConvoyId() {
		return convoyId;
	}

	public void setConvoyId(long convoyId) {
		this.convoyId = convoyId;
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

	public String getFileConvoy() {
		return fileConvoy;
	}

	public void setFileConvoy(String fileConvoy) {
		this.fileConvoy = fileConvoy;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public String getJobIdConvoy() {
		return jobIdConvoy;
	}

	public void setJobIdConvoy(String jobIdConvoy) {
		this.jobIdConvoy = jobIdConvoy;
	}

	public String getStatusConvoy() {
		return statusConvoy;
	}

	public void setStatusConvoy(String statusConvoy) {
		this.statusConvoy = statusConvoy;
	}

	public Double getPercentCompConvoy() {
		return percentCompConvoy;
	}

	public void setPercentCompConvoy(Double percentCompConvoy) {
		this.percentCompConvoy = percentCompConvoy;
	}

	public IpvodMediaAsset getIpvodMediaAsset() {
		return ipvodMediaAsset;
	}

	public void setIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		this.ipvodMediaAsset = ipvodMediaAsset;
	}

}
