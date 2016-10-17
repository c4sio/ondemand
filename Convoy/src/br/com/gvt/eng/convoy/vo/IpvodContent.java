package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class IpvodContent implements Serializable {

	static Logger logger = Logger.getLogger(IpvodContent.class.getName());

	private static final long serialVersionUID = 1L;

	private String uri;
	private String filename;
	private String title;
	private String content_type;
	private List<String> entry_urls;
	private Double duration;
	private List<IpvodVariants> variants;
	private String state;
	private String message;
	private Double progress;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public List<String> getEntry_urls() {
		return entry_urls;
	}

	public void setEntry_urls(List<String> entry_urls) {
		this.entry_urls = entry_urls;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public List<IpvodVariants> getVariants() {
		return variants;
	}

	public void setVariants(List<IpvodVariants> variants) {
		this.variants = variants;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getProgress() {
		return progress;
	}

	public void setProgress(Double progress) {
		this.progress = progress;
	}

}
