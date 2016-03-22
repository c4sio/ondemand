package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class ActionsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String app;
	private String action;
	private String requestDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

}
