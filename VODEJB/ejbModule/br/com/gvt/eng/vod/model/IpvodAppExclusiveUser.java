package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "IPVOD_APP_EXCLUSIVE_USER")
@NamedQueries({ @NamedQuery(name = "IpvodAppExclusiveUser.getDataByKeyValue", query = "SELECT user FROM IpvodAppExclusiveUser user WHERE user.keyUser = :keyUser") })
public class IpvodAppExclusiveUser implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_DATA_BY_KEY_VALUE = "IpvodAppExclusiveUser.getDataByKeyValue";

	@Id
	@Column(name = "APP_EXCLUSIVE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long appExclusiveId;

	@NotNull
	@NotEmpty
	@Column(name = "KEY_USER")
	private String keyUser;

	/*
	 * ID: 16116 - ipvod_catchup_super 
	 * ID: 16113 - ipvod_catchup_ultra 
	 * ID: 16114 - ipvod_catchup_ultimate
	 */
	@NotNull
	@NotEmpty
	@Column(name = "PACKAGE_VALUE")
	private String packageValue;

	public Long getAppExclusiveId() {
		return appExclusiveId;
	}

	public void setAppExclusiveId(Long appExclusiveId) {
		this.appExclusiveId = appExclusiveId;
	}

	public String getKeyUser() {
		return keyUser;
	}

	public void setKeyUser(String keyUser) {
		this.keyUser = keyUser;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

}
