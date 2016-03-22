package br.com.gvt.eng.vod.util;

public class FilterRules {
	public static final String OPERATION_EQUAL = "eq";
	public static final String OPERATION_NOT_EQUAL = "ne";
	public static final String OPERATION_LESS = "lt";
	public static final String OPERATION_LESS_OR_EQUAL = "le";
	public static final String OPERATION_GREATER = "gt";
	public static final String OPERATION_GREATER_OR_EQUAL = "ge";
	public static final String OPERATION_BEGINS_WITH = "bw";
	public static final String OPERATION_DOES_NOT_BEGIN_WITH = "bn";
	public static final String OPERATION_IS_IN = "in";
	public static final String OPERATION_IS_NOT_IN = "ni";
	public static final String OPERATION_ENDS_WITH = "ew";
	public static final String OPERATION_DOES_NOT_END_WITH = "en";
	public static final String OPERATION_CONTAINS = "cn";
	public static final String OPERATION_DOES_NOT_CONTAIN = "nc";
	public static final String OPERATION_IS_NULL = "nl";
	public static final String OPERATION_IS_NOT_NULL = "nn";
	
	private String field;

	private String op;

	private String data;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
