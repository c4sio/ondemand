package br.com.gvt.eng.vod.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodAssetPackage;

@Stateless
public class AssetPackageDAO extends GenericDAO<IpvodAssetPackage> {

	public AssetPackageDAO() {
		super(IpvodAssetPackage.class);
	}
	
	public List<IpvodAssetPackage> listByPackage(Long packageId, Map<String, Object> pagination) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("packageId", packageId);

		return super.findResultByParameterPagination(IpvodAssetPackage.LIST_BY_PACKAGE_STB,
				parameters, pagination);
	}
	
	public List<IpvodAssetPackage> listByPackage(Long packageId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("packageId", packageId);

		return super.findResultByParameter(IpvodAssetPackage.LIST_BY_PACKAGE,
				parameters);
	}
	
	public void updateAssetPackages() {
	}

	
	public void insertAssetPackage(List<IpvodAssetPackage> ipvodAssetPackages) {
		for (IpvodAssetPackage ipvodAssetPackage : ipvodAssetPackages) {
			//TODO REGRA DO PREÇO DO FILME NO PACOTE
			ipvodAssetPackage.setPrice(new BigDecimal(0));
			super.save(ipvodAssetPackage);
		}
	}

}
