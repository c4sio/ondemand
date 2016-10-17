package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class IpvodIngestType implements Serializable {

	static Logger logger = Logger.getLogger(IpvodIngestType.class.getName());
	
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}