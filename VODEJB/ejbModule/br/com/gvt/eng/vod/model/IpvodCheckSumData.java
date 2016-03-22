package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_INTEGR_CHECKSUM")
@NamedQueries({ @NamedQuery(name = "IpvodCheckSumData.getDataByFileName", query = "SELECT checkSum FROM IpvodCheckSumData checkSum WHERE checkSum.fileName = :fileName")})
public class IpvodCheckSumData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_FILE_NAME = "IpvodCheckSumData.getDataByFileName";

	@Id
	@Column(name = "CHECKSUM_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long checkSumId;

	@ManyToOne
	@JoinColumn(name = "INGEST_ID", nullable = false)
	private IpvodIngestStage ipvodIngestStage;

	@Column(name = "FILE_NAME", nullable = false)
	private String fileName;

	@Column(name = "CHECKSUM_SEND", nullable = false)
	private String checkSumSend;

	@Column(name = "CHECKSUM_RESULT", nullable = false)
	private String checkSumResult;

	@Column(name = "STATUS", nullable = false)
	private boolean statusFile;

	public long getCheckSumId() {
		return checkSumId;
	}

	public void setCheckSumId(long checkSumId) {
		this.checkSumId = checkSumId;
	}

	public IpvodIngestStage getIpvodIngestStage() {
		return ipvodIngestStage;
	}

	public void setIpvodIngestStage(IpvodIngestStage ipvodIngestStage) {
		this.ipvodIngestStage = ipvodIngestStage;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCheckSumSend() {
		return checkSumSend;
	}

	public void setCheckSumSend(String checkSumSend) {
		this.checkSumSend = checkSumSend;
	}

	public String getCheckSumResult() {
		return checkSumResult;
	}

	public void setCheckSumResult(String checkSumResult) {
		this.checkSumResult = checkSumResult;
	}

	public boolean isStatusFile() {
		return statusFile;
	}

	public void setStatusFile(boolean statusFile) {
		this.statusFile = statusFile;
	}

}
