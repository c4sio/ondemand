package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IpvodLanguagePK implements Serializable {

	private static final long serialVersionUID = 1L;

	public IpvodLanguagePK() {
	}

	@Column(name = "LANGUAGE", nullable = false)
	private String language;

	@Column(name = "VARIANTS", nullable = false)
	private String variants;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVariants() {
		return variants;
	}

	public void setVariants(String variants) {
		this.variants = variants;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IpvodLanguagePK)) {
			IpvodLanguagePK languagePK = (IpvodLanguagePK) obj;

			if (!languagePK.getLanguage().equals(language)) {
				return false;
			}

			if (!languagePK.getVariants().equals(variants)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return language.hashCode() + variants.hashCode();
	}

}
