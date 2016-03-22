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
@Table(name = "IPVOD_INTEGR_BALANCER_DATA")
@NamedQueries({
		@NamedQuery(name = "IpvodBalancerData.allValuesInProcess", query = "SELECT ban FROM IpvodBalancerData ban, IpvodIngestStage ing WHERE ban.ipvodIngestStage.id = ing.id and ing.stageType.id =:stageType order by ban.id, ing.priority desc"),
		@NamedQuery(name = "IpvodBalancerData.allDataByIngestId", query = "SELECT ban FROM IpvodBalancerData ban WHERE ban.ipvodIngestStage.id =:ingestId"),
		@NamedQuery(name = "IpvodBalancerData.allDataInExecution", query = "SELECT ban FROM IpvodBalancerData ban WHERE ban.status not in(:success, :error)"),
		@NamedQuery(name = "IpvodBalancerData.dataInExecutionBalancer", query = "SELECT ban FROM IpvodBalancerData ban, IpvodIngestStage ing WHERE ban.ipvodIngestStage.id = ing.id and ing.stageType.id =:stageType ") })
public class IpvodBalancerData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ALL_VALUES_IN_PROCESS = "IpvodBalancerData.allValuesInProcess";

	public static final String ALL_DATA_BY_INGESTID = "IpvodBalancerData.allDataByIngestId";

	public static final String ALL_DATA_IN_EXECUTION = "IpvodBalancerData.allDataInExecution";

	public static final String DATA_IN_EXECUTION_BALANCER = "IpvodBalancerData.dataInExecutionBalancer";

	@Id
	@Column(name = "BALANCER_ID")
	@SequenceGenerator(name = "SEQ_BALANCER", sequenceName = "SEQ_BALANCER", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BALANCER")
	private long id;

	@Column(name = "JOB_ID", nullable = false)
	private String jobId;

	@Column(name = "NAME_FILE", nullable = false)
	private String nameFile;

	@Column(name = "STATUS", nullable = false)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_START", nullable = false)
	private Date dateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_END")
	private Date dateEnd;

	@Column(name = "SEND_EMAIL", nullable = false)
	private boolean sendMail;

	@Column(name = "PERCENT_COMP", nullable = false)
	private Double percentComp;

	@Column(name = "PRESET", nullable = false)
	private String presetId;

	@ManyToOne
	@JoinColumn(name = "INGEST_ID", nullable = false)
	private IpvodIngestStage ipvodIngestStage;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Double getPercentComp() {
		return percentComp;
	}

	public void setPercentComp(Double percentComp) {
		this.percentComp = percentComp;
	}

	public String getPresetId() {
		return presetId;
	}

	public void setPresetId(String presetId) {
		this.presetId = presetId;
	}

	public IpvodIngestStage getIpvodIngestStage() {
		return ipvodIngestStage;
	}

	public void setIpvodIngestStage(IpvodIngestStage ipvodIngestStage) {
		this.ipvodIngestStage = ipvodIngestStage;
	}
}