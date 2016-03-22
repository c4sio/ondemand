package br.com.gvt.eng.vod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "IPVOD_USER_SYS",
	uniqueConstraints= {
		@UniqueConstraint(name="USER_SYS_USERNAME",columnNames="USER_USERNAME"),
		@UniqueConstraint(name="USER_SYS_EMAIL",columnNames="USER_EMAIL")
	}
)
@NamedQueries({ 
	@NamedQuery(name = "IpvodUserSystem.login", query = "SELECT ius FROM IpvodUserSystem ius WHERE ius.username = :username AND ius.password = :password") ,
	@NamedQuery(name = "IpvodUserSystem.getUserByUsername", query = "SELECT ius FROM IpvodUserSystem ius WHERE ius.username = :username") ,
	@NamedQuery(name = "IpvodUserSystem.getUserByEmail", query = "SELECT ius FROM IpvodUserSystem ius WHERE ius.email = :email") ,
	@NamedQuery(name = "IpvodUserSystem.recoverPassword", query = "SELECT us FROM IpvodUserSystem us WHERE us.userSysId = (SELECT spr.ipvodUserSys.userSysId FROM IpvodSystemPasswordRecover spr WHERE spr.passwordRecoverCode = :passRecover)"),
	@NamedQuery(name = "IpvodUserSystem.getUserByRole", query = "SELECT ius FROM IpvodUserSystem ius WHERE ius.role = :role") ,
	@NamedQuery(name = "IpvodUserSystem.getUserByContentProvider", query = "SELECT ius FROM IpvodUserSystem ius WHERE ius.contentProvider.contentProviderId = :providerId") 
})
public class IpvodUserSystem implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String LOGIN = "IpvodUserSystem.login";
	
	public static final String GET_USER_BY_USERNAME = "IpvodUserSystem.getUserByUsername";
	
	public static final String GET_USER_BY_EMAIL = "IpvodUserSystem.getUserByEmail";

	public static final String RECOVER_PASSWORD = "IpvodUserSystem.recoverPassword";

	public static final String GET_USER_BY_ROLE = "IpvodUserSystem.getUserByRole";
	
	public static final String GET_USER_BY_CONTENT_PROVIDER = "IpvodUserSystem.getUserByContentProvider";
	
	@Id
	@SequenceGenerator(name = "SEQ_USER_SYS", sequenceName = "SEQ_USER_SYS", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_SYS_ID")
	private Long userSysId;

	@Column(name = "USER_USERNAME", nullable = false)
	private String username;

	@Column(name = "USER_PASSWORD")
	private String password;
	
	@Transient
	private String passwordConfirm;

	@Column(name = "USER_EMAIL")
	private String email;

	@Column(name = "USER_ROLE")
	private String role;
	
	@Column(name = "ACTIVE")
	private boolean active;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENT_PROVIDER_ID")
	private IpvodContentProvider contentProvider;
	
	@Transient
	private String registerType; 
	
	public Long getUserSysId() {
		return userSysId;
	}

	public void setUserSysId(Long userSysId) {
		this.userSysId = userSysId;
	}

	public String getUsername() {
		
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public IpvodContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(IpvodContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
