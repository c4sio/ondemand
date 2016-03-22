package br.com.gvt.eng.vod.model;

import java.io.Serializable;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_APP_MENU_CATEGORY")
@NamedQueries({
		@NamedQuery(name = "IpvodAppMenuCategory.getListByPackageId", query = "SELECT menu FROM IpvodAppMenuCategory menu WHERE menu.ipvodPackage.packageId = :packageId and menu.ipvodAppMenu.active = true"),
		@NamedQuery(name = "IpvodAppMenuCategory.getListByBillingId", query = "SELECT menu FROM IpvodAppMenuCategory menu WHERE menu.ipvodPackage.otherId = :packageBillingId and menu.ipvodAppMenu.active = true") })
public class IpvodAppMenuCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_LIST_BY_PACKAGE_ID = "IpvodAppMenuCategory.getListByPackageId";

	public static final String FIND_LIST_BY_PACKAGE_BILLING_ID = "IpvodAppMenuCategory.getListByBillingId";

	@Id
	@Column(name = "MENU_CATEGORY_ID")
	@SequenceGenerator(name = "SEQ_APP_MENU_CATEGORY", sequenceName = "SEQ_APP_MENU_CATEGORY", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_APP_MENU_CATEGORY")
	private long menuCategoryId;

	@Column(name = "APP_ORDER")
	private long order;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PACKAGE_ID")
	private IpvodPackage ipvodPackage;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "APP_MENU_ID")
	private IpvodAppMenu ipvodAppMenu;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "APP_CAT_ID")
	private IpvodAppCategory ipvodAppCategory;

	public long getMenuCategoryId() {
		return menuCategoryId;
	}

	public void setMenuCategoryId(long menuCategoryId) {
		this.menuCategoryId = menuCategoryId;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public IpvodPackage getIpvodPackage() {
		return ipvodPackage;
	}

	public void setIpvodPackage(IpvodPackage ipvodPackage) {
		this.ipvodPackage = ipvodPackage;
	}

	public IpvodAppMenu getIpvodAppMenu() {
		return ipvodAppMenu;
	}

	public void setIpvodAppMenu(IpvodAppMenu ipvodAppMenu) {
		this.ipvodAppMenu = ipvodAppMenu;
	}

	public IpvodAppCategory getIpvodAppCategory() {
		return ipvodAppCategory;
	}

	public void setIpvodAppCategory(IpvodAppCategory ipvodAppCategory) {
		this.ipvodAppCategory = ipvodAppCategory;
	}

}
