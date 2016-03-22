package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.EquipmentDAO;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.vo.provisioning.Equipment;
import br.com.gvt.vod.facade.EquipmentFacade;

@Stateless
public class EquipmentFacadeImp implements EquipmentFacade {

	@EJB
	private EquipmentDAO equipmentDAO;

	@Override
	public void save(IpvodEquipment ipvodEquipment) {
		isUserWithAllData(ipvodEquipment);
		equipmentDAO.save(ipvodEquipment);
	}

	@Override
	public void delete(IpvodEquipment ipvodEquipment) {
		equipmentDAO.deleteEquipment(ipvodEquipment);
	}

	@Override
	public IpvodEquipment update(IpvodEquipment ipvodEquipment) {
		isUserWithAllData(ipvodEquipment);
		return equipmentDAO.update(ipvodEquipment);
	}

	@Override
	public List<IpvodEquipment> findAll() {
		return equipmentDAO.findAll();
	}

	@Override
	public IpvodEquipment getEquipmentByMac(String mac) {
		return equipmentDAO.getEquipmentByMac(mac);
	}

	@Override
	public Equipment getEquipmentVOByMac(String mac) {
		return equipmentDAO.getEquipmentVOByMac(mac);
	}

	@Override
	public Equipment getEquipmentVOByCAId(String caId) {
		return equipmentDAO.getEquipmentVOByCAId(caId);
	}
	
	@Override
	public Equipment getEquipmentVOBySerial(String serial){
		return equipmentDAO.getEquipmentVOBySerial(serial);
	}

	@Override
	public IpvodEquipment find(long entityID) {
		IpvodEquipment ipvodEquipment = new IpvodEquipment();
		ipvodEquipment = equipmentDAO.find(entityID);
		// Evita o Lazy Exception
		if (ipvodEquipment != null) {
			ipvodEquipment.getIpvodPurchases().size();
			ipvodEquipment.getIpvodPurchases().size();
		}
		return ipvodEquipment;
	}

	public Boolean isActiveBySerial(String serial) {
		return equipmentDAO.isActiveBySerial(serial);
	}

	/**
	 * Valida��o de campos - Save/Update
	 * 
	 * @param IpvodEquipment
	 */
	private void isUserWithAllData(IpvodEquipment ipvodEquipment) {
		boolean hasError = false;
		if (ipvodEquipment == null)
			hasError = true;

		if (ipvodEquipment.getAuthInfo() == null || "".equals(ipvodEquipment.getAuthInfo().trim()))
			hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The user is missing data. Check, they should have value.");
	}

	@Override
	public void saveAll(List<IpvodEquipment> ipvodEquipments) {
		equipmentDAO.saveAll(ipvodEquipments);
	}

	@Override
	public IpvodEquipment findBySerial(String serial) {
		return equipmentDAO.findBySerial(serial);
	}

}
