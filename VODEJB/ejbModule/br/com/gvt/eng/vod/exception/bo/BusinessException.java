package br.com.gvt.eng.vod.exception.bo;

import java.io.Serializable;

public class BusinessException extends Exception implements Serializable {

	private static final long serialVersionUID = 7074203440492115036L;

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
