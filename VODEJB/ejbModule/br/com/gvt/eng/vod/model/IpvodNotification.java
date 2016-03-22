package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonFilter;

@Entity
@Table(name = "IPVOD_NOTIFICATION")
@NamedQueries({
		@NamedQuery(name = "IpvodNotification.findByRoleGroup", query = "SELECT n FROM IpvodNotification n WHERE n.roleGroup = :role AND n.expirationDate > current_timestamp"),
		@NamedQuery(name = "IpvodNotification.findByContentProvider", query = "SELECT n FROM IpvodNotification n WHERE n.ipvodContentProvider.contentProviderId = :providerId AND n.expirationDate > current_timestamp") })
@JsonFilter("IpvodNotification")
public class IpvodNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String FIND_BY_CONTENT_PROVIDER = "IpvodNotification.findByContentProvider";

	public static String FIND_BY_ROLE_GROUP = "IpvodNotification.findByRoleGroup";

	@Id
	@Column(name = "NOTIFICATION_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long notificationId;

	@Column(length=500)
	private String text;

	private String title;

	private String roleGroup;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROVIDER_ID")
	private IpvodContentProvider ipvodContentProvider;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;
	
	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(String roleGroup) {
		this.roleGroup = roleGroup;
	}

	public IpvodContentProvider getIpvodContentProvider() {
		return ipvodContentProvider;
	}

	public void setIpvodContentProvider(IpvodContentProvider ipvodContentProvider) {
		this.ipvodContentProvider = ipvodContentProvider;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	
}
