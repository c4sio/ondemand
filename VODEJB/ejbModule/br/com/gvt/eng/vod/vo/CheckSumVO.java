package br.com.gvt.eng.vod.vo;

import java.io.Serializable;

public class CheckSumVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long checkSumId;
	private String fileName;
	private String checkSumSend;
	private String checkSumResult;
	private boolean statusFile;

	public long getCheckSumId() {
		return checkSumId;
	}

	public void setCheckSumId(long checkSumId) {
		this.checkSumId = checkSumId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCheckSumSend() {
		return checkSumSend;
	}

	public void setCheckSumSend(String checkSumSend) {
		this.checkSumSend = checkSumSend;
	}

	public String getCheckSumResult() {
		return checkSumResult;
	}

	public void setCheckSumResult(String checkSumResult) {
		this.checkSumResult = checkSumResult;
	}

	public boolean isStatusFile() {
		return statusFile;
	}

	public void setStatusFile(boolean statusFile) {
		this.statusFile = statusFile;
	}

}
