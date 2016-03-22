package br.com.gvt.eng.vod.model;

import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * The persistent class for the IPVOD_VISUAL_MENU database table.
 * 
 */
@Entity
@Table(name = "IPVOD_VISUAL_MENU")
@NamedQueries({ 
	@NamedQuery(name = "IpvodVisualMenu.getMainMenu", query = "SELECT amenu FROM IpvodVisualMenu amenu WHERE amenu.ipvodVisualMenu = null") ,
	@NamedQuery(name = "IpvodVisualMenu.getMainMenuActive", query = "SELECT amenu FROM IpvodVisualMenu amenu WHERE amenu.ipvodVisualMenu = null and amenu.active = 1 order by amenu.zindex asc"),
	@NamedQuery(name = "IpvodVisualMenu.getMenuByAssetId", query = "SELECT m FROM IpvodVisualMenu m, IpvodAsset a, IpvodVisualMenuAsset ma WHERE ma.ipvodVisualMenu.menuId = m.menuId AND a.assetId = ma.ipvodAsset.assetId AND ma.ipvodAsset.assetId = :assetId"),
	@NamedQuery(name = "IpvodVisualMenu.getAllMenuWithPackages", query = "SELECT m FROM IpvodVisualMenu m WHERE m.ipvodPackages is not empty")
})
@JsonRootName("menu")
@JsonFilter("IpvodVisualMenu")
public class IpvodVisualMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_MAIN_MENU = "IpvodVisualMenu.getMainMenu";
	public static final String FIND_MAIN_MENU_ACTIVE = "IpvodVisualMenu.getMainMenuActive";
	public static final String FIND_MENU_BY_ASSET = "IpvodVisualMenu.getMenuByAssetId";
	public static final String FIND_MENU_WITH_PACKAGE = "IpvodVisualMenu.getAllMenuWithPackages";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MENU_ID")
	private long menuId;

	private Integer active;

	private String name;

	// bi-directional many-to-one association to IpvodVisualMenu
	// @ManyToOne(fetch = FetchType.LAZY)
	// PARENT
	@ManyToOne
	@JoinColumn(name = "MENU_SUP_ID")
	private IpvodVisualMenu ipvodVisualMenu;

	//CHILDREN
	// bi-directional many-to-one association to IpvodVisualMenu
	@OneToMany(mappedBy = "ipvodVisualMenu", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<IpvodVisualMenu> ipvodVisualMenus; 

	// bi-directional many-to-one association to IpvodPackageType
	@ManyToOne
	@JoinColumn(name = "VISUAL_COMPONENT_ID")
	private IpvodVisualComponent ipvodVisualComponent;

	// bi-directional many-to-many association to IpvodAsset
//	@ManyToMany
//	@JoinTable(name = "IPVOD_VISUAL_MENU_ASSET", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = { @JoinColumn(name = "ASSET_ID") })
	@Transient
	private List<IpvodAsset> ipvodAssets;
	
	@ManyToMany
	@JoinTable(name = "IPVOD_PACKAGE_MENU", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = { @JoinColumn(name = "PACKAGE_ID") })
	private List<IpvodPackage> ipvodPackages;  
	
//	@Transient
//	@ManyToMany
//	@JoinTable(name = "IPVOD_VISUAL_MENU_ASSET", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = { @JoinColumn(name = "ASSET_ID") })
	@OneToMany(mappedBy="ipvodVisualMenu")
	private List<IpvodVisualMenuAsset> ipvodVisualMenuAsset;

	@ManyToOne	
	@JoinColumn(name = "RATING_LEVEL")
	private IpvodRating ipvodRating;
	
	@Column(name = "ZINDEX")
	private Long zindex;
	
	@Transient
	private String contentURL;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "AVALIABLE_SINCE")
	private Date avaliableSince;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "AVALIABLE_UNTIL")
	private Date avaliableUntil;
	
	@Transient
	private Boolean permanentMenu = false;
	
	@Transient
	private Boolean hasAssets = false;
	
	@Transient
	private Boolean hasMenus = false;
	
	@Transient
	private String breadcrumbs;
	
	public IpvodVisualMenu() {
	}

//	public List<IpvodAsset> getIpvodAssetsIndex() {
//		return ipvodAssetsIndex;
//	}
//
//	public void setIpvodAssetsIndex(List<IpvodAsset> ipvodAssetsIndex) {
//		this.ipvodAssetsIndex = ipvodAssetsIndex;
//	}

	public Long getZindex() {
		return zindex;
	}

	public void setZindex(Long zindex) {
		this.zindex = zindex;
	}

	public long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IpvodVisualMenu getIpvodVisualMenu() {
		return this.ipvodVisualMenu;
	}

	public void setIpvodVisualMenu(IpvodVisualMenu ipvodVisualMenu) {
		this.ipvodVisualMenu = ipvodVisualMenu;
	}

	public List<IpvodVisualMenu> getIpvodVisualMenus() {
		return this.ipvodVisualMenus;
	}

	public void setIpvodVisualMenus(List<IpvodVisualMenu> ipvodVisualMenus) {
		this.ipvodVisualMenus = ipvodVisualMenus;
	}

	public IpvodVisualMenu addIpvodVisualMenus(IpvodVisualMenu ipvodVisualMenus) {
		getIpvodVisualMenus().add(ipvodVisualMenus);
		ipvodVisualMenus.setIpvodVisualMenu(this);

		return ipvodVisualMenus;
	}

	public IpvodVisualMenu removeIpvodVisualMenus(
			IpvodVisualMenu ipvodVisualMenus) {
		getIpvodVisualMenus().remove(ipvodVisualMenus);
		ipvodVisualMenus.setIpvodVisualMenu(null);

		return ipvodVisualMenus;
	}

	public IpvodVisualComponent getIpvodVisualComponent() {
		return ipvodVisualComponent;
	}

	public void setIpvodVisualComponent(
			IpvodVisualComponent ipvodVisualComponent) {
		this.ipvodVisualComponent = ipvodVisualComponent;
	}

	public List<IpvodAsset> getIpvodAssets() {
		return this.ipvodAssets;
	}

	public void setIpvodAssets(List<IpvodAsset> ipvodAssets) {
		this.ipvodAssets = ipvodAssets;
	}

	public IpvodRating getIpvodRating() {
		return ipvodRating;
	}

	public void setIpvodRating(IpvodRating ipvodRating) {
		this.ipvodRating = ipvodRating;
	}

	public String getContentURL() {
		return contentURL;
	}

	public void setContentURL(String contentURL) {
		this.contentURL = contentURL;
	}

	public Date getAvaliableSince() {
		return avaliableSince;
	}

	public void setAvaliableSince(Date avaliableSince) {
		this.avaliableSince = avaliableSince;
	}

	public Date getAvaliableUntil() {
		return avaliableUntil;
	}

	public void setAvaliableUntil(Date avaliableUntil) {
		this.avaliableUntil = avaliableUntil;
	}

	public Boolean getPermanentMenu() {
		return permanentMenu;
	}

	public void setPermanentMenu(Boolean permanentMenu) {
		this.permanentMenu = permanentMenu;
	}

	public List<IpvodPackage> getIpvodPackages() {
		return ipvodPackages;
	}

	public void setIpvodPackages(List<IpvodPackage> ipvodPackages) {
		this.ipvodPackages = ipvodPackages;
	}

	public Boolean getHasAssets() {
		return hasAssets;
	}

	public void setHasAssets(Boolean hasAssets) {
		this.hasAssets = hasAssets;
	}

	public Boolean getHasMenus() {
		return hasMenus;
	}

	public void setHasMenus(Boolean hasMenus) {
		this.hasMenus = hasMenus;
	}

	public List<IpvodVisualMenuAsset> getIpvodVisualMenuAsset() {
		return ipvodVisualMenuAsset;
	}

	public void setIpvodVisualMenuAsset(
			List<IpvodVisualMenuAsset> ipvodVisualMenuAsset) {
		this.ipvodVisualMenuAsset = ipvodVisualMenuAsset;
	}

	public String getBreadcrumbs() {
		return breadcrumbs;
	}

	public void setBreadcrumbs(String breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}

}