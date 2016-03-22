package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodEquipmentType;

@Local
public interface EquipmentTypeFacade {
	
	public abstract IpvodEquipmentType find(long entityID);
	
	abstract List<IpvodEquipmentType> findAll();

}
