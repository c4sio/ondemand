package br.com.gvt.eng.vod.util;

import java.util.List;

public class Filter {

	private String groupOp;

	private List<FilterRules> rules;

	public String getGroupOp() {
		return groupOp;
	}

	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}

	public List<FilterRules> getRules() {
		return rules;
	}

	public void setRules(List<FilterRules> rules) {
		this.rules = rules;
	}

}
