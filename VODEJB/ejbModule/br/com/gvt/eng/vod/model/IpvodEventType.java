package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the IPVOD_EVENT_TYPE database table.
 * 
 */
@Entity
@Table(name = "IPVOD_EVENT_TYPE")
@NamedQueries({ @NamedQuery(name = "IpvodEventType.findDataByEventTypeName", query = "SELECT even FROM IpvodEventType even WHERE even.description =:eventTypeName") })
public class IpvodEventType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_DATA_BY_EVENT_TYPE_NAME = "IpvodEventType.findDataByEventTypeName";

	@Id
	@Column(name = "EVENT_TYPE_ID")
	private long eventTypeId;

	private String description;

	// bi-directional many-to-one association to IpvodSession
	@OneToMany(mappedBy = "ipvodEventType")
	private List<IpvodSession> ipvodSessions;

	public IpvodEventType() {
	}

	public long getEventTypeId() {
		return this.eventTypeId;
	}

	public void setEventTypeId(long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IpvodSession> getIpvodSessions() {
		return this.ipvodSessions;
	}

	public void setIpvodSessions(List<IpvodSession> ipvodSessions) {
		this.ipvodSessions = ipvodSessions;
	}

	public IpvodSession addIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().add(ipvodSession);
		ipvodSession.setIpvodEventType(this);

		return ipvodSession;
	}

	public IpvodSession removeIpvodSession(IpvodSession ipvodSession) {
		getIpvodSessions().remove(ipvodSession);
		ipvodSession.setIpvodEventType(null);

		return ipvodSession;
	}

}