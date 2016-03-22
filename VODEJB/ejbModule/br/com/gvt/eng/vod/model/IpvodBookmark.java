package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IPVOD_BOOKMARK")
@NamedQueries({
	@NamedQuery(name = "IpvodBookmark.listByAssetUser", query = "SELECT b FROM IpvodBookmark b WHERE b.ipvodAsset.assetId = :assetId AND b.ipvodUser.userId = :userId")
})
public class IpvodBookmark implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_ASSET_USER = "IpvodBookmark.listByAssetUser";
	
	@Id
	@Column(name = "BOOKMARK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bookmarkId;

	@ManyToOne
	private IpvodUser ipvodUser;

	@ManyToOne
	private IpvodAsset ipvodAsset;

	@Column(name = "INSERT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertDate;

	@Column(name = "ASSET_TIME")
	private Long assetTime;

	@ManyToOne
	private IpvodEquipment ipvodEquipment;
	
	public Long getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(Long bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Long getAssetTime() {
		return assetTime;
	}

	public void setAssetTime(Long assetTime) {
		this.assetTime = assetTime;
	}

	public IpvodUser getIpvodUser() {
		return ipvodUser;
	}

	public void setIpvodUser(IpvodUser ipvodUser) {
		this.ipvodUser = ipvodUser;
	}

	public IpvodAsset getIpvodAsset() {
		return ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public IpvodEquipment getIpvodEquipment() {
		return ipvodEquipment;
	}

	public void setIpvodEquipment(IpvodEquipment ipvodEquipment) {
		this.ipvodEquipment = ipvodEquipment;
	}

}
