package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.vo.provisioning.Equipment;

@Local
public interface EquipmentFacade {

	public abstract void save(IpvodEquipment ipvodEquipment);

	public abstract void delete(IpvodEquipment ipvodEquipment);

	public abstract IpvodEquipment update(IpvodEquipment ipvodEquipment);

	public abstract List<IpvodEquipment> findAll();

	public abstract IpvodEquipment find(long entityID);

	public abstract Boolean isActiveBySerial(String serial);

	IpvodEquipment getEquipmentByMac(String mac);

	public abstract void saveAll(List<IpvodEquipment> ipvodEquipments);

	public IpvodEquipment findBySerial(String serial);

	public Equipment getEquipmentVOByMac(String mac);

	public Equipment getEquipmentVOByCAId(String caId);
	
	public Equipment getEquipmentVOBySerial(String serial);

}
