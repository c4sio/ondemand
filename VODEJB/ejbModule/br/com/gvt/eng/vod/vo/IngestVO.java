package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodVisualMenu;

public class IngestVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idIngest;
	private Long assetId;
	private String assetInfo;
	private String adicionalInfo;
	private String assetTitle;
	private Date dtInsertIngest;
	private Date dtChangeIngest;
	private Long stageType;
	private boolean resultOK;
	private int priority;
	private boolean cleanup;
	private String ftpPath;
	private String categoryDescription;
	private Long assetCategoryId;
	private List<BalancerVO> balancerVO;
	private List<DrmVO> drmVO;
	private List<ConvoyVO> convoyVO;
	private List<CheckSumVO> ckeckSumVO;
	private List<IpvodVisualMenu> ipvodVisualMenu;
	private String language;
	private String billingId;
	private Double price;
	private String assetName;

	public Long getCategoryId() {
		return assetCategoryId;
	}

	public void setCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public Long getIdIngest() {
		return idIngest;
	}

	public void setIdIngest(Long idIngest) {
		this.idIngest = idIngest;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public String getAdicionalInfo() {
		return adicionalInfo;
	}

	public void setAdicionalInfo(String adicionalInfo) {
		this.adicionalInfo = adicionalInfo;
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public String getDtInsertIngest() {
		return dtInsertIngest == null ? "" : new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss").format(dtInsertIngest);
	}

	public void setDtInsertIngest(Date dtInsertIngest) {
		this.dtInsertIngest = dtInsertIngest;
	}

	public String getDtChangeIngest() {
		return dtChangeIngest == null ? "" : new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss").format(dtChangeIngest);
	}

	public void setDtChangeIngest(Date dtChangeIngest) {
		this.dtChangeIngest = dtChangeIngest;
	}

	public Long getStageType() {
		return stageType;
	}

	public void setStageType(Long stageType) {
		this.stageType = stageType;
	}

	public boolean getResultOK() {
		return resultOK;
	}

	public void setResultOK(boolean resultOK) {
		this.resultOK = resultOK;
	}

	public List<BalancerVO> getBalancerVO() {
		return balancerVO;
	}

	public void setBalancerVO(List<BalancerVO> balancerVO) {
		this.balancerVO = balancerVO;
	}

	public List<DrmVO> getDrmVO() {
		return drmVO;
	}

	public void setDrmVO(List<DrmVO> drmVO) {
		this.drmVO = drmVO;
	}

	public List<ConvoyVO> getConvoyVO() {
		return convoyVO;
	}

	public void setConvoyVO(List<ConvoyVO> convoyVO) {
		this.convoyVO = convoyVO;
	}

	public List<CheckSumVO> getCkeckSumVO() {
		return ckeckSumVO;
	}

	public void setCkeckSumVO(List<CheckSumVO> ckeckSumVO) {
		this.ckeckSumVO = ckeckSumVO;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCleanup() {
		return cleanup;
	}

	public void setCleanup(boolean cleanup) {
		this.cleanup = cleanup;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public List<IpvodVisualMenu> getIpvodVisualMenu() {
		return ipvodVisualMenu;
	}

	public void setIpvodVisualMenu(List<IpvodVisualMenu> ipvodVisualMenu) {
		this.ipvodVisualMenu = ipvodVisualMenu;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

}