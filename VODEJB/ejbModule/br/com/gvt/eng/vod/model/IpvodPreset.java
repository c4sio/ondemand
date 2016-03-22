package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_INTEGR_PRESET")
@NamedQueries({ @NamedQuery(name = "IpvodPreset.presetByParameters", query = "SELECT pres FROM IpvodPreset pres WHERE pres.typeVideo =:video and pres.typeAudio =:som and pres.languages =:language and pres.subtitles =:subtitle and pres.dubbedLanguage =:dubbedLanguage") })
public class IpvodPreset implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SEARCH_PRESET_BY_PARAMETERS = "IpvodPreset.presetByParameters";

	@Id
	@Column(name = "PRESET_ID")
	@SequenceGenerator(name = "SEQ_PRESET", sequenceName = "SEQ_PRESET", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRESET")
	private long presetId;

	@Column(name = "PRESET", nullable = false)
	private String presetName;

	@Column(name = "VIDEO_HD", nullable = false, length = 3)
	private String typeVideo;

	@Column(name = "SOM")
	private String typeAudio;

	@Column(name = "LANGUAGE")
	private String languages;

	@Column(name = "SUBTITLE")
	private String subtitles;

	@Column(name = "DUBBED_LANGUAGE")
	private String dubbedLanguage;

	public long getPresetId() {
		return presetId;
	}

	public void setPresetId(long presetId) {
		this.presetId = presetId;
	}

	public String getPresetName() {
		return presetName;
	}

	public void setPresetName(String presetName) {
		this.presetName = presetName;
	}

	public String getTypeVideo() {
		return typeVideo;
	}

	public void setTypeVideo(String typeVideo) {
		this.typeVideo = typeVideo;
	}

	public String getTypeAudio() {
		return typeAudio;
	}

	public void setTypeAudio(String typeAudio) {
		this.typeAudio = typeAudio;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}

	public String getDubbedLanguage() {
		return dubbedLanguage;
	}

	public void setDubbedLanguage(String dubbedLanguage) {
		this.dubbedLanguage = dubbedLanguage;
	}

}