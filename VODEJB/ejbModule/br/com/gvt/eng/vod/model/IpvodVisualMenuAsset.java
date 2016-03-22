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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * The persistent class for the IPVOD_VISUAL_MENU database table.
 * 
 */
@Entity
@Table(name = "IPVOD_VISUAL_MENU_ASSET_INDEX")
@NamedQueries({ 
	@NamedQuery(name = "IpvodVisualMenuAsset.findByMenuId", query = "SELECT amenu FROM IpvodVisualMenuAsset amenu WHERE amenu.ipvodVisualMenu.menuId = :menuId"),
	@NamedQuery(name = "IpvodVisualMenuAsset.lab", query = "SELECT DISTINCT c.ipvodVisualMenu FROM IpvodVisualMenuAsset c WHERE c.ipvodVisualMenu.ipvodVisualMenu = null"),
	@NamedQuery(name = "IpvodVisualMenuAsset.deleteAll", query = "DELETE FROM IpvodVisualMenu o")
})
@JsonRootName("menu")
@JsonFilter("IpvodVisualMenuAsset")
public class IpvodVisualMenuAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_MENU_ID = "IpvodVisualMenuAsset.findByMenuId";
	public static final String CLEAN = "IpvodVisualMenuAsset.deleteAll";
	public static final String LAB = "IpvodVisualMenuAsset.lab";

	
	@Id
	@Column(name = "MENU_ASSET_ID")
	@SequenceGenerator(name = "SEQ_MENU_ASSET_INDEX", sequenceName = "SEQ_MENU_ASSET_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENU_ASSET_INDEX")
	private long id;

	// bi-directional many-to-many association to IpvodAsset
	/*@ManyToMany
	@JoinTable(name = "IPVOD_VISUAL_MENU_ASSET", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = { @JoinColumn(name = "ASSET_ID") })
	private List<IpvodAsset> ipvodAssets;*/

	@ManyToOne
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "MENU_ID")
	private IpvodVisualMenu ipvodVisualMenu;
	
	@ManyToOne
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "ASSET_ID")
	private IpvodAsset ipvodAsset;
	
	@Column(name = "ZINDEX")
	private Long zindex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public IpvodVisualMenu getIpvodVisualMenu() {
		return ipvodVisualMenu;
	}

	public void setIpvodVisualMenu(IpvodVisualMenu ipvodVisualMenu) {
		this.ipvodVisualMenu = ipvodVisualMenu;
	}

	public IpvodAsset getIpvodAsset() {
		return ipvodAsset;
	}

	public void setIpvodAsset(IpvodAsset ipvodAsset) {
		this.ipvodAsset = ipvodAsset;
	}

	public Long getZindex() {
		return zindex;
	}

	public void setZindex(Long zindex) {
		this.zindex = zindex;
	}
	

}