package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.vo.provisioning.Equipment;

@Stateless
public class EquipmentDAO extends GenericDAO<IpvodEquipment> {

	public EquipmentDAO() {
		super(IpvodEquipment.class);
	}

	/**
	 * @param IpvodEquipment
	 */
	public void deleteEquipment(IpvodEquipment IpvodEquipment) {
		super.delete(IpvodEquipment.getEquipmentId(), IpvodEquipment.class);
	}

	/**
	 * @param serial
	 * @return Boolean
	 */
	public Boolean isActiveBySerial(String serial) {
		return findBySerial(serial) != null ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * @param serial
	 * @return IpvodEquipment
	 */
	public IpvodEquipment findBySerial(String serial) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("serial", serial);
		IpvodEquipment equipment = null;
		try {
			equipment = super.findOneResult(IpvodEquipment.BY_SERIAL,
					parameters);
		} catch (NoResultException nre) {
			nre.printStackTrace();
		}
		return equipment;
	}

	/**
	 * @param ipvodEquipments
	 */
	public void saveAll(List<IpvodEquipment> ipvodEquipments) {
		for (IpvodEquipment equipment : ipvodEquipments) {
			super.save(equipment);
		}
	}

	/**
	 * @param mac
	 * @return IpvodEquipment
	 */
	public IpvodEquipment getEquipmentByMac(String mac) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("mac", mac);
		return super.findOneResult(IpvodEquipment.BY_MAC, parameters);
	}

	/**
	 * @param caId
	 * @return IpvodEquipment
	 */
	public IpvodEquipment getEquipmentByCAId(String caId) {
		// WILDCARD FOR CAID
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cas", caId.substring(0, caId.length() - 1) + "%");
		return super.findOneResult(IpvodEquipment.BY_CAS, parameters);
	}

	/**
	 * @param caId
	 * @return Equipment
	 */
	public Equipment getEquipmentVOByCAId(String caId) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("e.serial as serial, e.authInfo as cardId, ");
		hql.append("e.mainKey as mainKey, e.cas as cas, ");
		hql.append("et.equipmentTypeId as type, ");
		hql.append("et.description as typeDesc, ");
		hql.append("u.userId as userId ");
		hql.append("FROM ");
		hql.append("IpvodEquipment e ");
		hql.append("join e.ipvodEquipmentType et ");
		hql.append("join e.ipvodUser u ");
		hql.append("WHERE e.cas = :cas");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("cas", caId);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				Equipment.class));
		return (Equipment) query.uniqueResult();
	}

	/**
	 * @param mac
	 * @return Equipment
	 */
	public Equipment getEquipmentVOByMac(String mac) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("e.serial as serial, e.authInfo as cardId, ");
		hql.append("e.mainKey as mainKey, e.cas as cas, ");
		hql.append("et.equipmentTypeId as type, ");
		hql.append("et.description as typeDesc, ");
		hql.append("u.userId as userId ");
		hql.append("FROM ");
		hql.append("IpvodEquipment e ");
		hql.append("join e.ipvodEquipmentType et ");
		hql.append("join e.ipvodUser u ");
		hql.append("WHERE e.mac = :mac");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("mac", mac);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				Equipment.class));
		return (Equipment) query.uniqueResult();
	}

	/**
	 * @param serial
	 * @return Equipment
	 */
	public Equipment getEquipmentVOBySerial(String serial) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("e.serial as serial, e.authInfo as cardId, ");
		hql.append("e.mainKey as mainKey, e.cas as cas, ");
		hql.append("et.equipmentTypeId as type, ");
		hql.append("et.description as typeDesc, ");
		hql.append("u.userId as userId ");
		hql.append("FROM ");
		hql.append("IpvodEquipment e ");
		hql.append("join e.ipvodEquipmentType et ");
		hql.append("join e.ipvodUser u ");
		hql.append("WHERE e.serial = :serial");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("serial", serial);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				Equipment.class));
		return (Equipment) query.uniqueResult();
	}

	/**
	 * @param keyValue
	 * @return IpvodEquipment
	 */
	public IpvodEquipment findEquipmentByCAId(String keyValue) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cas", keyValue);
		return super.findOneResult(IpvodEquipment.BY_CAS, parameters);
	}
}