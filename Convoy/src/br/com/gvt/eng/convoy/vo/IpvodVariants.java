package br.com.gvt.eng.convoy.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class IpvodVariants implements Serializable {

	static Logger logger = Logger.getLogger(IpvodVariants.class.getName());

	private static final long serialVersionUID = 1L;

	private String name;
	private List<Long> bitrates;
	private String location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getBitrates() {
		return bitrates;
	}

	public void setBitrates(List<Long> bitrates) {
		this.bitrates = bitrates;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
