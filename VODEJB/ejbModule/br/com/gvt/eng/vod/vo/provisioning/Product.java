package br.com.gvt.eng.vod.vo.provisioning;

import java.util.HashMap;

import br.com.gvt.eng.vod.vo.ITConnectionVO;

public class Product implements ITConnectionVO {

	private String id;
	private String name;
	private HashMap<String, String> options;
	
	public Product() {
	}

	public Product(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, String> getOptions() {
		return options;
	}

	public void setOptions(HashMap<String, String> options) {
		this.options = options;
	}

}
