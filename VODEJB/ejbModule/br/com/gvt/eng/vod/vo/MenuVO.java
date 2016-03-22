package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.List;

public class MenuVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private boolean active;
	private List<MenuVO> subitens;
	private List<ComponentVO> components;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MenuVO> getSubitens() {
		return subitens;
	}

	public void setSubitens(List<MenuVO> subitens) {
		this.subitens = subitens;
	}

	public List<ComponentVO> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentVO> components) {
		this.components = components;
	}

}