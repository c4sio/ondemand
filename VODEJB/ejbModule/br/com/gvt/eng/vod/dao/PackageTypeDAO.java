package br.com.gvt.eng.vod.dao;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodPackageType;

@Stateless
public class PackageTypeDAO extends GenericDAO<IpvodPackageType> {

	public PackageTypeDAO() {
		super(IpvodPackageType.class);
	}

}
