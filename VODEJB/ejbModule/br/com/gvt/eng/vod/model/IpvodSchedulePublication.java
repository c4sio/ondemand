package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 * The persistent class for the IPVOD_USER database table.
 * 
 */
@Entity
@Table(name = "IPVOD_SCHEDULE_PUBLICATION")
@NamedQueries({
	@NamedQuery(name = "IpvodSchedulePublication.getByTempDataId", query = "SELECT s FROM IpvodSchedulePublication s WHERE s.tempDataId = :tempDataId")
})
public class IpvodSchedulePublication implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_TEMP_DATA_ID = "IpvodSchedulePublication.getByTempDataId";

	@Id
	@Column(name = "SCH_ID")
	@SequenceGenerator(name = "SEQ_SCHEDULE", sequenceName = "SEQ_SCHEDULE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SCHEDULE")
	private long scheduleId;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private IpvodUserSystem user;

	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	@Column(name = "TEMP_DATA_ID")
	private String tempDataId;

	@Transient
	private String data;

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public IpvodUserSystem getUser() {
		return user;
	}

	public void setUser(IpvodUserSystem user) {
		this.user = user;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getTempDataId() {
		return tempDataId;
	}

	public void setTempDataId(String tempDataId) {
		this.tempDataId = tempDataId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
