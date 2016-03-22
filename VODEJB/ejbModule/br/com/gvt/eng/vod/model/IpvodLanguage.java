package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "IPVOD_LANGUAGE")
public class IpvodLanguage implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IpvodLanguagePK ipvodLanguagePK;

	public IpvodLanguagePK getIpvodLanguagePK() {
		return ipvodLanguagePK;
	}

	public void setIpvodLanguagePK(IpvodLanguagePK ipvodLanguagePK) {
		this.ipvodLanguagePK = ipvodLanguagePK;
	}

}
