package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.Map;

public class OverviewVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String TYPE_ASSINANTES = "assinantes";
	public static String TYPE_CONTEUDO = "conteudo";
	public static String TYPE_AUDIENCIA = "audiencia";
	public static String TYPE_HITS = "hits";
	public static String TYPE_PACOTES = "pacotes";
	public static String TYPE_MINUTOS = "minutos";
	public static String TYPE_GOOGLEPLUS = "googleplus";
	public static String TYPE_FACEBOOK = "facebook";
	public static String TYPE_TWEETS = "tweets";
	public static String TYPE_LOGON = "logon";
	

	private String type;
	
	private String value;
	
	private Long variation;
	
	private Map<String, long[]> regions;
	
	public OverviewVO(String type, String value, Long variation, Map<String, long[]> map) {
		this.type = type;
		this.value = value;
		this.variation = variation;
		this.regions = map;
	}
	
	public OverviewVO(String type, String value, Long variation) {
		this.type = type;
		this.value = value;
		this.variation = variation;
	}
	
	public Map<String, long[]> getRegions() {
		return regions;
	}

	public void setRegions(Map<String, long[]> regions) {
		this.regions = regions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getVariation() {
		return variation;
	}

	public void setVariation(Long variation) {
		this.variation = variation;
	}
	
}
