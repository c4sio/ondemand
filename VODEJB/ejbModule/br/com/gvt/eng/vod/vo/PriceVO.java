package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class PriceVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double price;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
