package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.List;

public class ProductVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;

	private List<String> products;

	public List<String> getProducts() {
		return products;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

}
