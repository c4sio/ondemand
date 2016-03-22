package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the IPVOD_INGEST_STAGE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_INGEST_STAGE")
@NamedQueries({
		@NamedQuery(name = "IpvodIngestStage.getByAssetStage", query = "SELECT ing FROM IpvodIngestStage ing WHERE ing.assetInfo = :assetInfo AND ing.stageType.id = :stageType"),
		@NamedQuery(name = "IpvodIngestStage.getListByAssetInfo", query = "SELECT ing FROM IpvodIngestStage ing WHERE ing.assetInfo = :assetInfo order by ing.stageType.id"),
		@NamedQuery(name = "IpvodIngestStage.clearIngestByAssetInfo", query = "DELETE FROM IpvodIngestStage ing WHERE ing.assetInfo = :assetInfo"),
		@NamedQuery(name = "IpvodIngestStage.getFilesToExecute", query = "SELECT ing FROM IpvodIngestStage ing, IpvodIngestType ingt WHERE ing.stageType.id = ingt.id AND ingt.id = :stageTypeId and ing.ipvodAsset.assetId is not null order by ing.priority, ing.id asc"),
		@NamedQuery(name = "IpvodIngestStage.findIngestByAssetId", query = "SELECT ing FROM IpvodIngestStage ing, IpvodIngestType ingt WHERE ing.ipvodAsset.assetId = :assetId"),
		@NamedQuery(name = "IpvodIngestStage.getDataWithErrorByAssetInfo", query = "SELECT ing FROM IpvodIngestStage ing WHERE ing.assetInfo = :assetInfo and ing.result = 0"),
		@NamedQuery(name = "IpvodIngestStage.getDataByAssetInfo", query = "SELECT ing FROM IpvodIngestStage ing WHERE ing.assetInfo = :assetInfo"),
		@NamedQuery(name = "IpvodIngestStage.getDataByPathFile", query = "SELECT ing FROM IpvodIngestStage ing WHERE ing.adicionalInfo = :pathfile and ing.result = 0") })
public class IpvodIngestStage implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_LIST_BY_ASSET_INFO = "IpvodIngestStage.getListByAssetInfo";

	public static final String FIND_BY_ASSET_STAGE = "IpvodIngestStage.getByAssetStage";

	public static final String CLEAR_INGEST_BY_ASSET_INFO = "IpvodIngestStage.clearIngestByAssetInfo";

	public static final String FIND_FILES_TO_EXECUTE = "IpvodIngestStage.getFilesToExecute";

	public static final String FIND_INGEST_BY_ASSET_ID = "IpvodIngestStage.findIngestByAssetId";

	public static final String FIND_DATA_ERROR_BY_ASSET_INFO = "IpvodIngestStage.getDataWithErrorByAssetInfo";

	public static final String FIND_DATA_BY_PATH_FILE = "IpvodIngestStage.getDataByPathFile";

	public static final String FIND_DATA_BY_ASSET_INFO = "IpvodIngestStage.getDataByAssetInfo";

	@Id
	@Column(name = "INGEST_ID")
	@SequenceGenerator(name = "SEQ_INGEST_ST", sequenceName = "SEQ_INGEST_ST", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INGEST_ST")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STAGE_TYPE")
	private IpvodIngestType stageType;

	@Column(name = "RESULT")
	private Integer result;

	@Column(name = "ADICIONTAL_INFO")
	private String adicionalInfo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INSERT")
	private Date dtInsert;

	@Column(name = "ASSET_INFO")
	private String assetInfo;

	@OneToOne
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset ipvodAsset;

	@Column(name = "ASSET_NAME")
	private String assetName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_CHANGE")
	private Date dtChange;

	@Column(name = "PRIORITY", nullable = false)
	private int priority;

	@Column(name = "CLEANUP", nullable = false)
	private boolean cleanup;

	@Column(name = "FTP_PATH")
	private String ftpPath;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ipvodIngestStage")
	private List<IpvodDrmData> ipvodDrm = new ArrayList<IpvodDrmData>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ipvodIngestStage")
	private List<IpvodConvoyData> ipvodConvoy = new ArrayList<IpvodConvoyData>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ipvodIngestStage")
	private List<IpvodBalancerData> ipvodBalancer = new ArrayList<IpvodBalancerData>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ipvodIngestStage")
	private List<IpvodCheckSumData> ipvodCheckSumData = new ArrayList<IpvodCheckSumData>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the stageType
	 */
	public IpvodIngestType getStageType() {
		return stageType;
	}

	/**
	 * @param stageType
	 *            the stageType to set
	 */
	public void setStageType(IpvodIngestType stageType) {
		this.stageType = stageType;
	}

	/**
	 * @return the result
	 */
	public Integer getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Integer result) {
		this.result = result;
	}

	/**
	 * @return the adicionalInfo
	 */
	public String getAdicionalInfo() {
		return adicionalInfo;
	}

	/**
	 * @param adicionalInfo
	 *            the adicionalInfo to set
	 */
	public void setAdicionalInfo(String adicionalInfo) {
		this.adicionalInfo = adicionalInfo;
	}

	/**
	 * @return the dtInsert
	 */
	public Date getDtInsert() {
		return dtInsert;
	}

	/**
	 * @param dtInsert
	 *            the dtInsert to set
	 */
	public void setDtInsert(Date dtInsert) {
		this.dtInsert = dtInsert;
	}

	/**
	 * @return the assetInfo
	 */
	public String getAssetInfo() {
		return assetInfo;
	}

	/**
	 * @param assetInfo
	 *            the assetInfo to set
	 */
	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	/**
	 * @return the dtChange
	 */
	public Date getDtChange() {
		return dtChange;
	}

	/**
	 * @param dtChange
	 *            the dtChange to set
	 */
	public void setDtChange(Date dtChange) {
		this.dtChange = dtChange;
	}

	/**
	 * @return ipvodConvoy
	 */
	public IpvodAsset getIpvodAsset() {
		return ipvodAsset;
	}

	/**
	 * @param ipvodAsset
	 *            the ipvodAsset to set
	 */
	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	/**
	 * @return assetName
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * @param assetName
	 *            the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/**
	 * @return ipvodDrm the ipvodDrm to set
	 */
	public List<IpvodDrmData> getIpvodDrm() {
		return ipvodDrm;
	}

	/**
	 * @param ipvodDrm
	 */
	public void setIpvodDrm(List<IpvodDrmData> ipvodDrm) {
		this.ipvodDrm = ipvodDrm;
	}

	/**
	 * @return ipvodConvoy the ipvodConvoy to set
	 */
	public List<IpvodConvoyData> getIpvodConvoy() {
		return ipvodConvoy;
	}

	/**
	 * @param ipvodConvoy
	 */
	public void setIpvodConvoy(List<IpvodConvoyData> ipvodConvoy) {
		this.ipvodConvoy = ipvodConvoy;
	}

	/**
	 * @return ipvodBalancer the ipvodBalancer to set
	 */
	public List<IpvodBalancerData> getIpvodBalancer() {
		return ipvodBalancer;
	}

	/**
	 * @param ipvodBalancer
	 */
	public void setIpvodBalancer(List<IpvodBalancerData> ipvodBalancer) {
		this.ipvodBalancer = ipvodBalancer;
	}

	/**
	 * @return ipvodCheckSumData
	 */
	public List<IpvodCheckSumData> getIpvodCheckSumData() {
		return ipvodCheckSumData;
	}

	/**
	 * @param ipvodCheckSumData
	 *            the ipvodCheckSumData to set
	 */
	public void setIpvodCheckSumData(List<IpvodCheckSumData> ipvodCheckSumData) {
		this.ipvodCheckSumData = ipvodCheckSumData;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the cleanup
	 */
	public boolean isCleanup() {
		return cleanup;
	}

	/**
	 * @param cleanup
	 */
	public void setCleanup(boolean cleanup) {
		this.cleanup = cleanup;
	}

	/**
	 * @return the ftpPath
	 */
	public String getFtpPath() {
		return ftpPath;
	}

	/**
	 * @param ftpPath
	 */
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
}