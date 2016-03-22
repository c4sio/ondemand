package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.converter.PackageConverter;
import br.com.gvt.eng.vod.dao.AssetDAO;
import br.com.gvt.eng.vod.dao.AssetPackageDAO;
import br.com.gvt.eng.vod.dao.PackageDAO;
import br.com.gvt.eng.vod.dao.PackageTypeDAO;
import br.com.gvt.eng.vod.dao.PurchaseDAO;
import br.com.gvt.eng.vod.dao.UserDAO;
import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPackageType;
import br.com.gvt.eng.vod.model.IpvodRating;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.vod.facade.PackageFacade;

@Stateless
public class PackageFacadeImp implements PackageFacade {

	@EJB
	private PackageDAO packageDAO;

	@EJB
	private PackageTypeDAO packageTypeDAO;
	
	@EJB
	private UserDAO userDAO;

	@EJB 
	private AssetDAO assetDAO;
	
	@EJB
	private AssetPackageDAO assetPackageDAO;
	
	@EJB
	private PurchaseDAO purchaseDAO;
	
	@Override
	public void save(IpvodPackage ipvodPackage) {
		isUserWithAllData(ipvodPackage);
		for (IpvodAssetPackage assetPackage : ipvodPackage.getIpvodAssetPackages()) {
			assetPackage.setIpvodPackage(ipvodPackage);
		}
		
		packageDAO.update(ipvodPackage);
	}

	@Override
	public void delete(IpvodPackage ipvodPackage) {

		List<IpvodUser> ipvodUser = ipvodPackage.getIpvodUsers();

		// Remove usuário da tabela IPVOD_PACKAGE_SUBSCRIPTION
		for (IpvodUser user : ipvodUser) {
			user.removeIpvodPackage(ipvodPackage);
			userDAO.update(user);
		}

		// Remove os demais dados
		packageDAO.deletePackage(ipvodPackage);
	}

	@Override
	public IpvodPackage update(IpvodPackage ipvodPackage) {
		return packageDAO.update(ipvodPackage);
	}

	@Override
	public List<IpvodPackage> findAll() {
		List<IpvodPackage> listIpvodPackage = new ArrayList<IpvodPackage>();
		listIpvodPackage = packageDAO.findAll();
		// Incluido para evitar o LazyException
//		for (IpvodPackage ipvodPackage : listIpvodPackage) {
//			ipvodPackage.getIpvodAssetPackages().size();
//			ipvodPackage.getIpvodUsers().size();
//		}

		return listIpvodPackage;
	}

	@Override
	public IpvodPackage findByOtherId(String otherId) {
		return packageDAO.findByOtherId(otherId);
	}
	
	@Override
	public IpvodPackage find(Long entityID) {
		IpvodPackage ipvodPackage = packageDAO.find(entityID);
		
		if(ipvodPackage != null){
			// Incluido para evitar o LazyException
			ipvodPackage.getIpvodAssetPackages().size();
			if (ipvodPackage.getIpvodRating() != null) {
				IpvodRating rating = new IpvodRating();
				rating.setAdult(ipvodPackage.getIpvodRating().getAdult());
				rating.setDescription(ipvodPackage.getIpvodRating().getDescription());
				rating.setRating(ipvodPackage.getIpvodRating().getRating());
				rating.setRatingLevel(ipvodPackage.getIpvodRating().getRatingLevel());
				ipvodPackage.setIpvodRating(rating);
			}
//			ipvodPackage.getIpvodUsers().size();
		}
		
		return ipvodPackage;
	}

	@Override
	public IpvodPackage find(long packageId, Long userId, Map<String, Object> pagination) {
		IpvodPackage ipvodPackage = packageDAO.find(packageId);
		
		if(ipvodPackage != null){
			PackageConverter converter = new PackageConverter();
			ipvodPackage = converter.toPackageNoAssets(ipvodPackage);
			
			ipvodPackage.setIpvodAssetPackages(assetPackageDAO.listByPackage(packageId, pagination));
			for (IpvodAssetPackage assetPackage : ipvodPackage.getIpvodAssetPackages()) {
				if (userId != null) {
					assetPackage.getIpvodAsset().setIpvodPurchases(purchaseDAO.findPurchase(assetPackage.getIpvodAsset().getAssetId(), userId));
				} else {
					assetPackage.getIpvodAsset().getIpvodPurchases().size();
				}
			}
		}
		
		return ipvodPackage;
	}
	
	@Override
	public boolean addAsset(IpvodPackage ipvodPackage) {
		try {
			if (ipvodPackage.getIpvodAssetPackages() != null
					|| !ipvodPackage.getIpvodAssetPackages().isEmpty()) {
				for (IpvodAssetPackage ipvodAssetPackage : ipvodPackage
						.getIpvodAssetPackages()) {
					ipvodPackage.addIpvodAssetPackage(ipvodAssetPackage);
				}
				packageDAO.update(ipvodPackage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeAsset(IpvodPackage ipvodPackage) {
		try {

			List<IpvodAssetPackage> listAssetPack = ipvodPackage
					.getIpvodAssetPackages();

			while (!listAssetPack.isEmpty()) {
				Iterator<IpvodAssetPackage> iteAssetPack = listAssetPack
						.iterator();
				if (iteAssetPack.hasNext()) {
					ipvodPackage.removeIpvodAssetPackage(iteAssetPack.next());
					packageDAO.update(ipvodPackage);
				}
				listAssetPack = ipvodPackage.getIpvodAssetPackages();
			}

			// if (!listAssetPack.isEmpty()) {
			// Iterator<IpvodAssetPackage> iteAssetPack = listAssetPack
			// .iterator();
			// while (iteAssetPack.hasNext()) {
			// IpvodAssetPackage assetPackage = iteAssetPack.next();
			//
			// // if (assetPackage.getIpvodAsset().getAssetId() == 0) {
			// ipvodPackage.removeIpvodAssetPackage(assetPackage);
			// packageDAO.update(ipvodPackage);
			// // }
			// listAssetPack = ipvodPackage.getIpvodAssetPackages();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Validacao de campos - Save/Update
	 * 
	 * @param IpvodUser
	 */
	private void isUserWithAllData(IpvodPackage ipvodPackage) {
		boolean hasError = false;
		if (ipvodPackage == null) {
			hasError = true;
		}

		if (ipvodPackage.getDescription() == null
				|| "".equals(ipvodPackage.getDescription().trim())) {
			hasError = true;
		}

		if (ipvodPackage.getPrice() == null) {
			hasError = true;
		}

		if (ipvodPackage.getDateStart() == null) {
			hasError = true;
		}

		if (ipvodPackage.getIpvodPackageType() == null) {
			hasError = true;
		}

		if (hasError) {
			throw new IllegalArgumentException(
					"The User is missing data. Check, they should have value.");
		}
	}
	
	@Override
	public List<IpvodPackage> findPackages(Long userId) {
		return packageDAO.listPackages(userId);
	}
	
	@Override
	public List<IpvodPackage> findResultComplexQuery(UriInfo uriInfo) {
		return packageDAO.findResultComplexQuery(uriInfo);
	}
	
	@Override
	public Long countResultComplexQuery(UriInfo uriInfo) {
		return packageDAO.countResultComplexQuery(uriInfo);
	}

	@Override
	public List<IpvodPackageType> listPackageType() {
		return packageTypeDAO.findAll();
	}
	
}