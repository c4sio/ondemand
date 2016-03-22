package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodUser;

@Stateless
public class PackageDAO extends GenericDAO<IpvodPackage> {

	public PackageDAO() {
		super(IpvodPackage.class);
	}
	
	public void deletePackage(IpvodPackage ipvodPackage) {
		super.delete(ipvodPackage.getPackageId(), IpvodPackage.class);
	}

	public IpvodPackage findByOtherId(String otherId) {
		IpvodPackage ipvodPackage = (IpvodPackage) getSession().createQuery("from IpvodPackage p where p.otherId = :otherId").setParameter("otherId", otherId).uniqueResult();
		
		if(ipvodPackage != null){	
			Hibernate.initialize(ipvodPackage.getIpvodAssetPackages());
			Hibernate.initialize(ipvodPackage.getIpvodUsers());
		}
		
		return ipvodPackage;
	}

	public List<IpvodPackage> listPackages(Long userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);

		return super.findResultByParameter(IpvodPackage.LIST_PACKAGES,
				parameters);
	}
	
	public IpvodPackage getLowerPricePackage(IpvodUser ipvodUser, IpvodAsset ipvodAsset) {
		final int PRICE = 0;
		final int BILLING_ID = 1;
		String sql = "select ap.price as price_package, p.billing_id "
					+ "from IPVOD_ASSET_PACKAGE ap "
			 		+ "left join IPVOD_PACKAGE p on p.PACKAGE_ID = ap.PACKAGE_ID "
			 		+ "where ap.asset_id =  " + ipvodAsset.getAssetId() 
			 		+ " and ap.package_id in(select package_id from IPVOD_PACKAGE_SUBSCRIPTION where user_id = " + ipvodUser.getUserId() + " ) "
			 		+ "and ap.price = (select min(ap.price) from IPVOD_ASSET_PACKAGE ap left join IPVOD_PACKAGE p on p.PACKAGE_ID = ap.PACKAGE_ID "
			 		+ "where ap.asset_id = " + ipvodAsset.getAssetId() + " and ap.package_id in(select package_id from IPVOD_PACKAGE_SUBSCRIPTION where user_id = " + ipvodUser.getUserId() + " ))"
			 		;
	   	
		IpvodPackage ipvodPackage = new IpvodPackage();
		javax.persistence.Query query = super.getEm().createNativeQuery(sql);
		List<Object[]> result = query.getResultList();
	    if (result != null && !result.isEmpty()) {
	    	Object[] res = result.get(0);
	    	ipvodPackage.setPrice(((BigDecimal) res[PRICE]).doubleValue());
	    	ipvodPackage.setOtherId((String) res[BILLING_ID]);
	    }
	    return ipvodPackage;
	}
	
}
