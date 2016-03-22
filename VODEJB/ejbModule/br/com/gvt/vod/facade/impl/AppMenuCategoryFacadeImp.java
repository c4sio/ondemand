package br.com.gvt.vod.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AppMenuCategoryDAO;
import br.com.gvt.eng.vod.dao.EquipmentDAO;
import br.com.gvt.eng.vod.enums.PackageHybridEnum;
import br.com.gvt.eng.vod.model.IpvodAppMenuCategory;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.vod.facade.AppMenuCategoryFacade;

@Stateless
public class AppMenuCategoryFacadeImp implements AppMenuCategoryFacade {

	@EJB
	private AppMenuCategoryDAO appMenuCategoryDAO;

	@EJB
	private EquipmentDAO equipmentDAO;

	@Override
	public IpvodAppMenuCategory findByID(Long entityID) {
		return this.appMenuCategoryDAO.find(entityID);
	}

	@Override
	public List<IpvodAppMenuCategory> findAll() {
		return this.appMenuCategoryDAO.findAll();
	}

	@Override
	public List<IpvodAppMenuCategory> findMenuAppsDthByCaId(String caId) {
		List<IpvodAppMenuCategory> listApps = new ArrayList<IpvodAppMenuCategory>();
		try {
			IpvodEquipment equipment = this.equipmentDAO
					.findEquipmentByCAId(caId);

			// Se nao encontrar o codigo devolve o menu default
			if (equipment == null) {
				return listApps = this.appMenuCategoryDAO
						.getAppMenuDefault("ipvod_catchup_super");
			}

			// Tipos de pacotes validos
			String packageValue = ".*ipvod_catchup_super|.*ipvod_catchup_ultimate|.*ipvod_catchup_ultra";
			Pattern p = Pattern.compile(packageValue);

			for (IpvodPackage ipvodPackage : equipment.getIpvodUser()
					.getIpvodPackages()) {
				Matcher m = p.matcher(ipvodPackage.getOtherId().toLowerCase());
				if (m.matches()) {
					listApps = this.appMenuCategoryDAO
							.findAppMenuByPackageId(ipvodPackage.getPackageId());
				} else {
					// Se nao encontrar o codigo devolve o menu default
					listApps = this.appMenuCategoryDAO
							.getAppMenuDefault("ipvod_catchup_super");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listApps;
	}

	@Override
	public List<IpvodAppMenuCategory> findMenuAppsHybridByPackage(
			List<String> listPackadgeIds) {
		List<IpvodAppMenuCategory> listApps = new ArrayList<IpvodAppMenuCategory>();
		try {
			for (String value : listPackadgeIds) {
				try {
					if (PackageHybridEnum.findPackageNameByCode(
							Integer.parseInt(value)).getPackageName() != null) {
						System.out.println(PackageHybridEnum.findPackageNameByCode(
								Integer.parseInt(value)).getPackageName());
						listApps = this.appMenuCategoryDAO
								.getAppMenuDefault(PackageHybridEnum
										.findPackageNameByCode(
												Integer.parseInt(value))
												.getPackageName());
						break;
					}
				} catch (Exception e) {
					continue;
				}
			}

			// Se nao encontrar o codigo devolve o menu default
			if (listApps.isEmpty()) {
				listApps = this.appMenuCategoryDAO
						.getAppMenuDefault("ipvod_catchup_super");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listApps;
	}

	@Override
	public List<IpvodAppMenuCategory> findMenuAppsHybridByPackage(
			String packageValue) {
		List<IpvodAppMenuCategory> listApps = new ArrayList<IpvodAppMenuCategory>();
		try {
			if (PackageHybridEnum.findPackageNameByCode(
					Integer.parseInt(packageValue)).getPackageName() != null) {
				System.out.println(PackageHybridEnum.findPackageNameByCode(
						Integer.parseInt(packageValue)).getPackageName());
				listApps = this.appMenuCategoryDAO
						.getAppMenuDefault(PackageHybridEnum
								.findPackageNameByCode(
										Integer.parseInt(packageValue))
								.getPackageName());
			}

			// Se nao encontrar o codigo devolve o menu default
			if (listApps.isEmpty()) {
				listApps = this.appMenuCategoryDAO
						.getAppMenuDefault("ipvod_catchup_super");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listApps;
	}

	@Override
	public void save(IpvodAppMenuCategory ipvodAppMenuCategory) {
		this.appMenuCategoryDAO.save(ipvodAppMenuCategory);
	}

	@Override
	public void delete(IpvodAppMenuCategory ipvodAppMenuCategory) {
		this.appMenuCategoryDAO.deleteAppMenuCategory(ipvodAppMenuCategory);
	}

	@Override
	public IpvodAppMenuCategory update(IpvodAppMenuCategory ipvodAppMenuCategory) {
		return this.appMenuCategoryDAO.update(ipvodAppMenuCategory);
	}

}
