package br.com.gvt.eng.vod.model;

import java.io.Serializable;
import java.util.Calendar;

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

/**
 * The persistent class for the IPVOD_USER database table.
 * 
 */
@Entity
@Table(name = "IPVOD_HISTORY")
@NamedQueries({ @NamedQuery(name = "IpvodHistory.getByTypeAndId", query = "SELECT iu FROM IpvodHistory iu WHERE iu.type = :type AND iu.itemId = :itemId ORDER BY iu.changedAt desc") })
public class IpvodHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_TYPE = "IpvodHistory.getByTypeAndId";

	@Id
	@Column(name = "HST_ID")
	@SequenceGenerator(name = "SEQ_HISTORY", sequenceName = "SEQ_HISTORY", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORY")
	private long historyId;

	@Column(name = "HST_TYPE")
	private HistoryTypeEnum type;

	@Column(name = "HST_ITEM_ID")
	private Long itemId;

	@ManyToOne
	@JoinColumn(name = "HST_USER")
	private IpvodUserSystem user;

	@Column(name = "HST_NEW_VALUE", length = 4000)
	private String newValue;

	@Column(name = "HST_OLD_VALUE", length = 4000)
	private String oldValue;

	@Column(name = "HST_CHANGED_AT")
	private Calendar changedAt;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(long historyId) {
		this.historyId = historyId;
	}

	public HistoryTypeEnum getType() {
		return type;
	}

	public void setType(HistoryTypeEnum type) {
		this.type = type;
	}

	public IpvodUserSystem getUser() {
		return user;
	}

	public void setUser(IpvodUserSystem user) {
		this.user = user;
	}

	public Calendar getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(Calendar changedAt) {
		this.changedAt = changedAt;
	}

}