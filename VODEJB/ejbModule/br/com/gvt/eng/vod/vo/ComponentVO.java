package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.List;

public class ComponentVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private List<AssetVO> assets;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AssetVO> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetVO> assets) {
		this.assets = assets;
	}
}
