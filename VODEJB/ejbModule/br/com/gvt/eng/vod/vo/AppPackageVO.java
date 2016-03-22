package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppPackageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String packageLabel;

	private List<AppMenuCategoryVO> itemsCategory = new ArrayList<AppMenuCategoryVO>(
			0);

	public String getPackageLabel() {
		return packageLabel;
	}

	public void setPackageLabel(String packageLabel) {
		this.packageLabel = packageLabel;
	}

	public List<AppMenuCategoryVO> getItemsCategory() {
		return itemsCategory;
	}

	public void setItemsCategory(List<AppMenuCategoryVO> itemsCategory) {
		this.itemsCategory = itemsCategory;
	}

}
