package br.com.gvt.eng.vod.vo.it;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import br.com.gvt.eng.vod.vo.ITConnectionVO;
import br.com.gvt.eng.vod.vo.MediaAssetVO;

public class OnDemandContentVO implements ITConnectionVO {

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	private Long order;
	private String title;
	private String originalTitle;
	private String genre;
	private String category;
	private String subtitle;
	private String country;
	private String subCategory;
	private Long assetId;
	private Date creationDate;
	private String description;
	private String director;
	private String actors;
	private String episode;
	private String billingID;
	private String episodeName;
	private Date licenseWindowEnd;
	private Date licenseWindowStart;
	private Double price;
	private Integer releaseYear;
	private String season;
	private String languages;
	private String assetInfo;
	private String rating;
	private Boolean isAdult;
	private Long totalTime;
	private String product;
	private String screenFormat;
	private String audioType;
	private Boolean canResume;
	private Boolean isHD;
	private Boolean isRevised;
	private String fileSize;
	private String checksum;
	private String bitrate;
	private String titleAlternative;
	// ipvodAssetType
	private String assetType;
	// ipvodContentProvider
	private String contentProvider;
	// ipvodMediaAssets
	private List<MediaAssetVO> mediaAssets;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getCreationDate() {
		return creationDate != null ? new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(creationDate) : null;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return director;
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
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public String getBillingID() {
		return billingID;
	}

	public void setBillingID(String billingID) {
		this.billingID = billingID;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public String getLicenseWindowEnd() {
		return licenseWindowEnd != null ? new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(licenseWindowEnd) : null;
	}

	public void setLicenseWindowEnd(Date licenseWindowEnd) {
		this.licenseWindowEnd = licenseWindowEnd;
	}

	public String getLicenseWindowStart() {
		return licenseWindowStart != null ? new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(licenseWindowStart) : null;
	}

	public void setLicenseWindowStart(Date licenseWindowStart) {
		this.licenseWindowStart = licenseWindowStart;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	public Boolean getCanResume() {
		return canResume;
	}

	public void setCanResume(Boolean canResume) {
		this.canResume = canResume;
	}

	public Boolean getIsHD() {
		return isHD;
	}

	public void setIsHD(Boolean isHD) {
		this.isHD = isHD;
	}

	public Boolean getIsRevised() {
		return isRevised;
	}

	public void setIsRevised(Boolean isRevised) {
		this.isRevised = isRevised;
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

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

	public Boolean getIsAdult() {
		return isAdult;
	}

	public void setIsAdult(Boolean isAdult) {
		this.isAdult = isAdult;
	}

	public List<MediaAssetVO> getMediaAssets() {
		return mediaAssets;
	}

	public void setMediaAssets(List<MediaAssetVO> mediaAssets) {
		this.mediaAssets = mediaAssets;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

}
