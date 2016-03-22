package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppMenuCategoryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String categoryLabel;
	private List<AppMenuVO> items = new ArrayList<AppMenuVO>(0);

	public String getCategoryLabel() {
		return categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public List<AppMenuVO> getItems() {
		return items;
	}

	public void setItems(List<AppMenuVO> items) {
		this.items = items;
	}

}
