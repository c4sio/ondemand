package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppCategoryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String categoryLabel;
	private List<AppInfoVO> items = new ArrayList<AppInfoVO>(0);

	public String getCategoryLabel() {
		return categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public List<AppInfoVO> getItems() {
		return items;
	}

	public void setItems(List<AppInfoVO> items) {
		this.items = items;
	}

}
