package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonFilter;

import br.com.gvt.eng.vod.constants.IpvodConstants;

/**
 * The persistent class for the IPVOD_ASSET database table.
 */
@Entity
@Table(name = "IPVOD_ASSET")
@NamedQueries({
		@NamedQuery(name = "IpvodAsset.listByAssetType", query = "SELECT ia FROM IpvodAsset ia WHERE ia.ipvodAssetType.assetTypeId = :idAssetType"),
		@NamedQuery(name = "IpvodAsset.listByCategory", query = "SELECT ia FROM IpvodAsset ia WHERE ia.ipvodCategory1.categoryId = :category"),
		@NamedQuery(name = "IpvodAsset.listByWord", query = "SELECT ia FROM IpvodAsset ia WHERE UPPER(ia.title) LIKE :word"),
		@NamedQuery(name = "IpvodAsset.listByMenu", query = "SELECT a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId = :menuId AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp order by ma.zindex"),
		@NamedQuery(name = "IpvodAsset.listByAssetList", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (:assets) AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByAssetListCatchUp", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (:assets) AND (a.assetId IN (SELECT distinct a.assetId FROM IpvodAsset a, IpvodPackage p, IpvodAssetPackage ap WHERE (ap.ipvodAsset.assetId = a.assetId AND ap.ipvodPackage.packageId = p.packageId AND p.ipvodPackageType.packageTypeId = 6)) OR a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel < 8 AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByAssetListCatchUpAdult", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (:assets) AND (a.assetId IN (SELECT distinct a.assetId FROM IpvodAsset a, IpvodPackage p, IpvodAssetPackage ap WHERE (ap.ipvodAsset.assetId = a.assetId AND ap.ipvodPackage.packageId = p.packageId AND p.ipvodPackageType.packageTypeId = 6)) OR a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel = 8 AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByAssetListOnDemand", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (:assets) AND (a.assetId IN (SELECT distinct a.assetId FROM IpvodAsset a, IpvodPackage p, IpvodAssetPackage ap WHERE (ap.ipvodAsset.assetId = a.assetId AND ap.ipvodPackage.packageId = p.packageId AND p.ipvodPackageType.packageTypeId <> 6)) OR a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel < 8 AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByAssetListOnDemandAdult", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (:assets) AND (a.assetId IN (SELECT distinct a.assetId FROM IpvodAsset a, IpvodPackage p, IpvodAssetPackage ap WHERE (ap.ipvodAsset.assetId = a.assetId AND ap.ipvodPackage.packageId = p.packageId AND p.ipvodPackageType.packageTypeId <> 6)) OR a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel = 8 AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByMyContentOnDemand", query = "SELECT a FROM IpvodAsset a WHERE (a.assetId IN (SELECT p.ipvodAsset.assetId FROM IpvodPurchase p WHERE p.ipvodEquipment.ipvodUser.userId = :userId AND p.validUntil >= current_timestamp) AND a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel < 8  AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByMyContentOnDemandAdult", query = "SELECT a FROM IpvodAsset a WHERE (a.assetId IN (SELECT p.ipvodAsset.assetId FROM IpvodPurchase p WHERE p.ipvodEquipment.ipvodUser.userId = :userId AND p.validUntil >= current_timestamp) AND a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.rating.ratingLevel = 8  AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp"),
		@NamedQuery(name = "IpvodAsset.listByMyContentCatchUp", query = "SELECT a FROM IpvodAsset a WHERE a.assetId IN (SELECT a.assetId FROM IpvodAsset a, IpvodPackage p, IpvodAssetPackage ap, IpvodUser u WHERE (ap.ipvodAsset.assetId = a.assetId AND ap.ipvodPackage.packageId = p.packageId AND p.ipvodPackageType.packageTypeId = 6 AND p MEMBER OF u.ipvodPackages AND u.userId = :userId) OR a.assetId IN (SELECT distinct a FROM IpvodAsset a, IpvodVisualMenuAsset ma WHERE a.assetId = ma.ipvodAsset.assetId AND ma.ipvodVisualMenu.menuId  IN (:menus))) AND a.assetId IN (SELECT p.ipvodAsset.assetId FROM IpvodPurchase p WHERE p.ipvodEquipment.ipvodUser.userId = :userId AND p.validUntil >= current_timestamp)  AND a.isRevised = true AND a.licenseWindowStart <= current_timestamp AND a.licenseWindowEnd >= current_timestamp") })
// TODO original, default e short title
@JsonFilter("IpvodAsset")
public class IpvodAsset implements Serializable, Importer {

	private static final long serialVersionUID = 1L;
	public static final String FIND_ASSET_BY_TYPE = "IpvodAsset.listByAssetType";
	public static final String FIND_BY_CATEGORY = "IpvodAsset.listByCategory";
	public static final String FIND_BY_WORD = "IpvodAsset.listByWord";
	public static final String FIND_BY_NEW_RELEASES = "IpvodAsset.listByNewReleases";
	public static final String FIND_BY_MENU = "IpvodAsset.listByMenu";
	public static final String FIND_BY_ASSET_LIST = "IpvodAsset.listByAssetList";
	public static final String FIND_MY_CONTENT_ONDEMAND = "IpvodAsset.listByMyContentOnDemand";
	public static final String FIND_MY_CONTENT_ONDEMAND_ADULT = "IpvodAsset.listByMyContentOnDemandAdult";
	public static final String FIND_MY_CONTENT_CATCHUP = "IpvodAsset.listByMyContentCatchUp";
	public static final String FIND_BY_ASSET_LIST_CATCHUP_ADULT = "IpvodAsset.listByAssetListCatchUp";
	public static final String FIND_BY_ASSET_LIST_CATCHUP = "IpvodAsset.listByAssetListCatchUpAdult";
	public static final String FIND_BY_ASSET_LIST_ONDEMAND = "IpvodAsset.listByAssetListOnDemand";
	public static final String FIND_BY_ASSET_LIST_ONDEMAND_ADULT = "IpvodAsset.listByAssetListOnDemandAdult";

	/**
	 * TODO verify this field:ASSET_ID_SUP
	 */
	@Id
	@Column(name = "ASSET_ID")
	@SequenceGenerator(name = "SEQ_ASSET", sequenceName = "SEQ_ASSET", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ASSET")
	private long assetId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate;

	private String description;

	private String director;

	private String actors;

	private String episode;

	@Column(name = "BILLING_ID")
	private String billingID;

	@Column(name = "EPISODE_NAME")
	private String episodeName;

	@Temporal(TemporalType.DATE)
	@Column(name = "LICENSE_WINDOW_END", nullable = false)
	private Date licenseWindowEnd;

	@Temporal(TemporalType.DATE)
	@Column(name = "LICENSE_WINDOW_START", nullable = false)
	private Date licenseWindowStart;

	@Column(length = 6, precision = 2, nullable = false)
	private Double price;

	@Column(name = "RELEASE_YEAR")
	private Integer releaseYear;

	private String season;

	private String subtitles;

	private String languages;

	@Column(name = "DUBBED_LANGUAGE")
	private String dubbedLanguage;

	@Column(name = "TITLE", nullable = false, length = 255)
	private String title;

	@Column(name = "ASSET_INFO", length = 300)
	private String assetInfo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RATING_LEVEL")
	private IpvodRating rating;

	@Column(name = "TOTAL_TIME")
	private long totalTime;

	private String product;

	private String country;

	@Column(name = "SCREEN_FORMAT")
	private String screenFormat;

	@Column(name = "AUDIO_TYPE")
	private String audioType;

	@Column(name = "CAN_RESUME")
	private boolean canResume;

	@Column(name = "HD_CONTENT")
	private boolean isHD;

	@Column(name = "IS_REVISED")
	private Boolean isRevised;

	@Column(name = "FILE_SIZE")
	private String fileSize;

	@Column(name = "CHECK_SUM")
	private String checksum;

	@Column(name = "BITRATE")
	private String bitrate;

	@Column(name = "ALTERNATIVE_TITLE")
	private String titleAlternative;

	@Column(name = "ORIGINAL_TITLE")
	private String originalTitle;

	// bi-directional many-to-one association to IpvodAssetType
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ASSET_TYPE_ID")
	private IpvodAssetType ipvodAssetType;

	// bi-directional many-to-one association to IpvodCategory
	@ManyToOne
	@JoinColumn(name = "MAIN_CATEGORY_ID")
	private IpvodCategory ipvodCategory1;

	// bi-directional many-to-one association to IpvodCategory
	@ManyToOne
	@JoinColumn(name = "SUB_CATEGORY_ID")
	private IpvodCategory ipvodCategory2;

	// bi-directional many-to-one association to IpvodContentProvider
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONTENT_PROVIDER_ID")
	private IpvodContentProvider ipvodContentProvider;

	// // bi-directional many-to-many association to IpvodEquipmentType
	// @OneToMany(mappedBy = "ipvodAssets")
	// private List<IpvodEquipmentType> ipvodEquipmentTypes = new
	// ArrayList<IpvodEquipmentType>();

	// bi-directional many-to-one association to IpvodAssetPackage
	@OneToMany(mappedBy = "ipvodAsset")
	private List<IpvodAssetPackage> ipvodAssetPackages = new ArrayList<IpvodAssetPackage>();

	// bi-directional many-to-one association to IpvodMediaAsset
	@OneToMany(mappedBy = "ipvodAsset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<IpvodMediaAsset> ipvodMediaAssets = new ArrayList<IpvodMediaAsset>();

	// bi-directional many-to-one association to IpvodPurchase
	@OneToMany(mappedBy = "ipvodAsset")
	private List<IpvodPurchase> ipvodPurchases = new ArrayList<IpvodPurchase>();

	// bi-directional many-to-many association to IpvodVisualComponent
	// @ManyToMany(mappedBy = "ipvodAssets")
	@Transient
	private List<IpvodVisualMenu> ipvodVisualMenus = new ArrayList<IpvodVisualMenu>();

	@OneToMany(mappedBy = "ipvodAsset")
	private List<IpvodVisualMenuAsset> ipvodVisualMenuAsset = new ArrayList<IpvodVisualMenuAsset>();

	public Boolean getIsRevised() {
		return isRevised;
	}

	public void setIsRevised(Boolean isRevised) {
		this.isRevised = isRevised;
	}

	public long getAssetId() {
		return this.assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getEpisode() {
		return this.episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public String getEpisodeName() {
		return this.episodeName;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public Date getLicenseWindowEnd() {
		return this.licenseWindowEnd;
	}

	public void setLicenseWindowEnd(Date licenseWindowEnd) {
		this.licenseWindowEnd = licenseWindowEnd;
	}

	public Date getLicenseWindowStart() {
		return this.licenseWindowStart;
	}

	public void setLicenseWindowStart(Date licenseWindowStart) {
		this.licenseWindowStart = licenseWindowStart;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getReleaseYear() {
		return this.releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSubtitles() {
		return this.subtitles;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getDubbedLanguage() {
		return dubbedLanguage;
	}

	public void setDubbedLanguage(String dubbedLanguage) {
		this.dubbedLanguage = dubbedLanguage;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public IpvodRating getRating() {
		return rating;
	}

	public void setRating(IpvodRating rating) {
		this.rating = rating;
	}

	public long getTotalTime() {
		return this.totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBillingID() {
		return billingID;
	}

	public void setBillingID(String billingID) {
		this.billingID = billingID;
	}

	public String getScreenFormat() {
		return screenFormat;
	}

	public void setScreenFormat(String screenFormat) {
		this.screenFormat = screenFormat;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public boolean isCanResume() {
		return canResume;
	}

	public void setCanResume(boolean canResume) {
		this.canResume = canResume;
	}

	public boolean isHD() {
		return isHD;
	}

	public void setHD(boolean isHD) {
		this.isHD = isHD;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getTitleAlternative() {
		return titleAlternative;
	}

	public void setTitleAlternative(String titleAlternative) {
		this.titleAlternative = titleAlternative;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public IpvodAssetType getIpvodAssetType() {
		return this.ipvodAssetType;
	}

	public void setIpvodAssetType(IpvodAssetType ipvodAssetType) {
		this.ipvodAssetType = ipvodAssetType;
	}

	public IpvodCategory getIpvodCategory1() {
		return this.ipvodCategory1;
	}

	public void setIpvodCategory1(IpvodCategory ipvodCategory1) {
		this.ipvodCategory1 = ipvodCategory1;
	}

	public IpvodCategory getIpvodCategory2() {
		return this.ipvodCategory2;
	}

	public void setIpvodCategory2(IpvodCategory ipvodCategory2) {
		this.ipvodCategory2 = ipvodCategory2;
	}

	public IpvodContentProvider getIpvodContentProvider() {
		return this.ipvodContentProvider;
	}

	public void setIpvodContentProvider(
			IpvodContentProvider ipvodContentProvider) {
		this.ipvodContentProvider = ipvodContentProvider;
	}

	// public List<IpvodEquipmentType> getIpvodEquipmentTypes() {
	// return this.ipvodEquipmentTypes;
	// }
	//
	// public void setIpvodEquipmentTypes(
	// List<IpvodEquipmentType> ipvodEquipmentTypes) {
	// this.ipvodEquipmentTypes = ipvodEquipmentTypes;
	// }

	public List<IpvodAssetPackage> getIpvodAssetPackages() {
		return this.ipvodAssetPackages;
	}

	public void setIpvodAssetPackages(List<IpvodAssetPackage> ipvodAssetPackages) {
		this.ipvodAssetPackages = ipvodAssetPackages;
	}

	public IpvodAssetPackage addIpvodAssetPackage(
			IpvodAssetPackage ipvodAssetPackage) {
		getIpvodAssetPackages().add(ipvodAssetPackage);
		ipvodAssetPackage.setIpvodAsset(this);

		return ipvodAssetPackage;
	}

	public IpvodAssetPackage removeIpvodAssetPackage(
			IpvodAssetPackage ipvodAssetPackage) {
		getIpvodAssetPackages().remove(ipvodAssetPackage);
		ipvodAssetPackage.setIpvodAsset(null);

		return ipvodAssetPackage;
	}

	public List<IpvodMediaAsset> getIpvodMediaAssets() {

		List<IpvodMediaAsset> ipvodMediaAssets = new ArrayList<IpvodMediaAsset>();

		for (IpvodMediaAsset mediaAsset : this.ipvodMediaAssets) {

			if (!mediaAsset.getUrl().substring(0, 4).equals("http")
					&& mediaAsset.getIpvodMediaType().getMediaTypeId() == 1) {
				mediaAsset.setUrl(IpvodConstants.IMAGE_SERVER_URL
//						"http://186.215.183.213/pics/"
						+ mediaAsset.getUrl());
			} else {
				mediaAsset.setUrl(mediaAsset.getUrl());
			}

			ipvodMediaAssets.add(mediaAsset);
		}

		return ipvodMediaAssets;
	}

	public void setIpvodMediaAssets(List<IpvodMediaAsset> ipvodMediaAssets) {
		this.ipvodMediaAssets = ipvodMediaAssets;
	}

	public IpvodMediaAsset addIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		getIpvodMediaAssets().add(ipvodMediaAsset);
		ipvodMediaAsset.setIpvodAsset(this);

		return ipvodMediaAsset;
	}

	public IpvodMediaAsset removeIpvodMediaAsset(IpvodMediaAsset ipvodMediaAsset) {
		getIpvodMediaAssets().remove(ipvodMediaAsset);
		ipvodMediaAsset.setIpvodAsset(null);

		return ipvodMediaAsset;
	}

	public List<IpvodPurchase> getIpvodPurchases() {
		return this.ipvodPurchases;
	}

	public void setIpvodPurchases(List<IpvodPurchase> ipvodPurchases) {
		this.ipvodPurchases = ipvodPurchases;
	}

	public IpvodPurchase addIpvodPurchases(IpvodPurchase ipvodPurchas) {
		getIpvodPurchases().add(ipvodPurchas);
		ipvodPurchas.setIpvodAsset(this);
		return ipvodPurchas;
	}

	public IpvodPurchase removeIpvodPurchases(IpvodPurchase ipvodPurchas) {
		getIpvodPurchases().remove(ipvodPurchas);
		ipvodPurchas.setIpvodAsset(null);
		return ipvodPurchas;
	}

	public List<IpvodVisualMenuAsset> getIpvodVisualMenuAsset() {
		return ipvodVisualMenuAsset;
	}

	public void setIpvodVisualMenuAsset(
			List<IpvodVisualMenuAsset> ipvodVisualMenuAsset) {
		this.ipvodVisualMenuAsset = ipvodVisualMenuAsset;
	}

	public List<IpvodVisualMenu> getIpvodVisualMenus() {
		return ipvodVisualMenus;
	}

	public void setIpvodVisualMenus(List<IpvodVisualMenu> ipvodVisualMenus) {
		this.ipvodVisualMenus = ipvodVisualMenus;
	}

}