package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class AppProductVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProductVO result;

	public ProductVO getResult() {
		return result;
	}

	public void setResult(ProductVO result) {
		this.result = result;
	}

}
