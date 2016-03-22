package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_EQUIPMENT database table.
 * 
 */
@Entity
@Table(name = "IPVOD_EQUIPMENT")
@NamedQueries({
		@NamedQuery(name = "IpvodEquipment.getBySerial", query = "SELECT equipment FROM IpvodEquipment equipment WHERE equipment.serial = :serial"),
		@NamedQuery(name = "IpvodEquipment.getMac", query = "SELECT equipment FROM IpvodEquipment equipment WHERE equipment.mac = :mac"),
		@NamedQuery(name = "IpvodEquipment.getCAS", query = "SELECT equipment FROM IpvodEquipment equipment WHERE equipment.cas like :cas") })
public class IpvodEquipment implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String BY_SERIAL = "IpvodEquipment.getBySerial";
	public static String BY_MAC = "IpvodEquipment.getMac";
	public static String BY_CAS = "IpvodEquipment.getCAS";

	@Id
	@Column(name = "EQUIPMENT_ID")
	@SequenceGenerator(name = "SEQ_EQUIPMENT", sequenceName = "SEQ_EQUIPMENT", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EQUIPMENT")
	private long equipmentId;

	@Column(name = "AUTH_INFO")
	private String authInfo;

	// bi-directional many-to-one association to IpvodEquipmentType
	@ManyToOne
	@JoinColumn(name = "EQUIPMENT_TYPE_ID")
	private IpvodEquipmentType ipvodEquipmentType;

	// bi-directional many-to-one association to IpvodUser
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private IpvodUser ipvodUser;

	// bi-directional many-to-one association to IpvodPurchase
	@OneToMany(mappedBy = "ipvodEquipment")
	private List<IpvodPurchase> ipvodPurchases;

	// bi-directional many-to-one association to IpvodSession
	@OneToMany(mappedBy = "ipvodEquipment")
	private List<IpvodSession> ipvodSessions;

	@OneToMany(mappedBy = "equipment")
	private List<IpvodAuthentication> ipvodAuthentications;

	@Column(name = "SERIAL_SET_TOP", length = 20, unique = true, nullable = false)
	private String serial;

	@Column(name = "MAIN_KEY", length = 20, unique = true, nullable = false)
	private String mainKey;

	@Column(name = "CA_ID", length = 20, unique = true, nullable = false)
	private String cas;

	@Column(name = "MAC_ADDRESS", unique = true)
	private String mac;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "IPVOD_APP_EQUIP", joinColumns = { @JoinColumn(name = "EQUIPMENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_INFO_ID") })
	private List<IpvodAppInfo> ipvodAppInfos;

	public IpvodEquipment() {
	}

	public long getEquipmentId() {
		return this.equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getAuthInfo() {
		return this.authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public IpvodEquipmentType getIpvodEquipmentType() {
		return this.ipvodEquipmentType;
	}

	public void setIpvodEquipmentType(IpvodEquipmentType ipvodEquipmentType) {
		this.ipvodEquipmentType = ipvodEquipmentType;
	}

	public IpvodUser getIpvodUser() {
		return this.ipvodUser;
	}

	public void setIpvodUser(IpvodUser ipvodUser) {
		this.ipvodUser = ipvodUser;
	}

	public List<IpvodPurchase> getIpvodPurchases() {
		return this.ipvodPurchases;
	}

	public void setIpvodPurchases(List<IpvodPurchase> ipvodPurchases) {
		this.ipvodPurchases = ipvodPurchases;
	}

	public IpvodPurchase addIpvodPurchas(IpvodPurchase ipvodPurchas) {
		getIpvodPurchases().add(ipvodPurchas);
		ipvodPurchas.setIpvodEquipment(this);

		return ipvodPurchas;
	}

	public IpvodPurchase removeIpvodPurchas(IpvodPurchase ipvodPurchas) {
		getIpvodPurchases().remove(ipvodPurchas);
		ipvodPurchas.setIpvodEquipment(null);

		return ipvodPurchas;
	}

	public List<IpvodSession> getIpvodSessions() {
		return this.ipvodSessions;
	}

	public void setIpvodSessions(List<IpvodSession> ipvodSessions) {
		this.ipvodSessions = ipvodSessions;
	}

	public IpvodSession addIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().add(ipvodSession);
		ipvodSession.setIpvodEquipment(this);

		return ipvodSession;
	}

	public IpvodSession removeIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().remove(ipvodSession);
		ipvodSession.setIpvodEquipment(null);

		return ipvodSession;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getMainKey() {
		return mainKey;
	}

	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public List<IpvodAuthentication> getIpvodAuthentications() {
		return ipvodAuthentications;
	}

	public void setIpvodAuthentications(
			List<IpvodAuthentication> ipvodAuthentications) {
		this.ipvodAuthentications = ipvodAuthentications;
	}

	public List<IpvodAppInfo> getIpvodAppInfos() {
		return ipvodAppInfos;
	}

	public void setIpvodAppInfos(List<IpvodAppInfo> ipvodAppInfos) {
		this.ipvodAppInfos = ipvodAppInfos;
	}

}