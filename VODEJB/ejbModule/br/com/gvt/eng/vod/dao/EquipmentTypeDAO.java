package br.com.gvt.eng.vod.dao;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodEquipmentType;

@Stateless
public class EquipmentTypeDAO extends GenericDAO<IpvodEquipmentType> {

	public EquipmentTypeDAO() {
		super(IpvodEquipmentType.class);
	}

}