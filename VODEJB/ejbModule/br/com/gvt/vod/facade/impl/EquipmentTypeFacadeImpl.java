package br.com.gvt.vod.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.EquipmentTypeDAO;
import br.com.gvt.eng.vod.model.IpvodEquipmentType;
import br.com.gvt.vod.facade.EquipmentTypeFacade;

@Stateless
public class EquipmentTypeFacadeImpl implements EquipmentTypeFacade {

	@EJB
	private EquipmentTypeDAO equipmentTypeDAO;

	public EquipmentTypeFacadeImpl() {
	}

	@Override
	public IpvodEquipmentType find(long entityID) {
		return equipmentTypeDAO.find(entityID);
	}

	@Override
	public List<IpvodEquipmentType> findAll() {
		return equipmentTypeDAO.findAll();
	}

}
