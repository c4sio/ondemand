package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.gvt.eng.vod.model.IpvodAppMenuCategory;
import br.com.gvt.eng.vod.vo.AppMenuCategoryVO;
import br.com.gvt.eng.vod.vo.AppMenuVO;
import br.com.gvt.eng.vod.vo.AppPackageVO;

public class AppMenuCategoryConverter {

	/**
	 * @param listApps
	 * @return Object
	 */
	public Object getAppMenuCategoryListAll(List<IpvodAppMenuCategory> listApps) {
		List<AppPackageVO> listAppMenus = new ArrayList<AppPackageVO>();
		try {
			if (listApps.isEmpty()) {
				return listAppMenus;
			}

			// Seleciona os pacotes
			Set<Object> setPackage = new HashSet<>();
			for (IpvodAppMenuCategory ipvodAppMenuCategory : listApps) {
				setPackage.add(ipvodAppMenuCategory.getIpvodPackage()
						.getDescription());
			}

			AppPackageVO appPackageVO = null;
			for (Object packageName : setPackage) {
				appPackageVO = new AppPackageVO();
				appPackageVO.setPackageLabel(packageName.toString());
				// Busca as categorias
				appPackageVO.setItemsCategory(setCategories(
						packageName.toString(), listApps));
				listAppMenus.add(appPackageVO);
			}

			// Orderna o menu
			Collections.sort(listAppMenus, new Comparator<AppPackageVO>() {
				public int compare(AppPackageVO appPac, AppPackageVO appPack) {
					return appPac.getPackageLabel().compareTo(
							appPack.getPackageLabel());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppMenus;
	}

	/**
	 * @param packageName
	 * @param listApps
	 * @return List<AppMenuCategoryVO>
	 */
	private List<AppMenuCategoryVO> setCategories(String packageName,
			List<IpvodAppMenuCategory> listApps) {
		List<AppMenuCategoryVO> listAppMenuCategoryVO = new ArrayList<AppMenuCategoryVO>();
		try {
			if (listApps.isEmpty()) {
				return listAppMenuCategoryVO;
			}

			// Seleciona as categorias
			Set<Object> setCategory = new HashSet<>();
			for (IpvodAppMenuCategory ipvodAppMenuCategory : listApps) {
				if (packageName.equalsIgnoreCase(ipvodAppMenuCategory
						.getIpvodPackage().getDescription())) {
					setCategory.add(ipvodAppMenuCategory.getIpvodAppCategory()
							.getName());
				}
			}

			AppMenuCategoryVO appMenuCategoryVO = null;
			for (Object category : setCategory) {
				appMenuCategoryVO = new AppMenuCategoryVO();
				appMenuCategoryVO.setCategoryLabel(category.toString());
				// Busca os menus
				appMenuCategoryVO.setItems(setMenus(packageName,
						category.toString(), listApps));
				listAppMenuCategoryVO.add(appMenuCategoryVO);
			}

			// Orderna o menu
			Collections.sort(listAppMenuCategoryVO,
					new Comparator<AppMenuCategoryVO>() {
						public int compare(AppMenuCategoryVO appCat,
								AppMenuCategoryVO appCate) {
							return appCat.getCategoryLabel().compareTo(
									appCate.getCategoryLabel());
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppMenuCategoryVO;
	}

	/**
	 * @param packageName
	 * @param categoryValue
	 * @param listApps
	 * @return List<AppMenuVO>
	 */
	private List<AppMenuVO> setMenus(String packageName, String categoryValue,
			List<IpvodAppMenuCategory> listApps) {
		List<AppMenuVO> listAppMenuVO = new ArrayList<AppMenuVO>();
		try {
			AppMenuConverter appMenuConverter = new AppMenuConverter();
			for (IpvodAppMenuCategory ipvodAppMenuCategory : listApps) {
				if (packageName.equalsIgnoreCase(ipvodAppMenuCategory
						.getIpvodPackage().getDescription())
						&& categoryValue.equalsIgnoreCase(ipvodAppMenuCategory
								.getIpvodAppCategory().getName())) {
					listAppMenuVO.add(appMenuConverter.toAppMenuVO(
							ipvodAppMenuCategory.getIpvodAppMenu(),
							ipvodAppMenuCategory.getOrder()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppMenuVO;
	}

	/**
	 * @param ipvodAppMenuCategory
	 * @return Object
	 */
	public Object toAppMenuCategory(IpvodAppMenuCategory ipvodAppMenuCategory) {
		AppMenuCategoryVO appMenuCategoryVO = new AppMenuCategoryVO();
		try {
			AppMenuConverter appMenuConverter = new AppMenuConverter();
			appMenuCategoryVO.setCategoryLabel(ipvodAppMenuCategory
					.getIpvodAppCategory().getName());

			List<AppMenuVO> listAppMenuVO = new ArrayList<AppMenuVO>();
			listAppMenuVO.add(appMenuConverter.toAppMenuVO(
					ipvodAppMenuCategory.getIpvodAppMenu(),
					ipvodAppMenuCategory.getOrder()));

			appMenuCategoryVO.setItems(listAppMenuVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appMenuCategoryVO;
	}

	/**
	 * @param listApps
	 * @return Object
	 */
	public Object getAppMenuList(List<IpvodAppMenuCategory> listApps) {
		List<AppMenuCategoryVO> listAppMenuCategoryVO = new ArrayList<AppMenuCategoryVO>();
		try {
			if (listApps.isEmpty()) {
				return listAppMenuCategoryVO;
			}

			// Seleciona as categorias
			Set<Object> setCategory = new HashSet<>();
			for (IpvodAppMenuCategory ipvodAppMenuCategory : listApps) {
				setCategory.add(ipvodAppMenuCategory.getIpvodAppCategory()
						.getName());
			}

			AppMenuCategoryVO appMenuCategoryVO = null;
			for (Object category : setCategory) {
				appMenuCategoryVO = new AppMenuCategoryVO();
				appMenuCategoryVO.setCategoryLabel(category.toString());
				// Busca os menus
				appMenuCategoryVO.setItems(setMenus(category.toString(),
						listApps));
				listAppMenuCategoryVO.add(appMenuCategoryVO);
			}

			// Orderna o menu
			Collections.sort(listAppMenuCategoryVO,
					new Comparator<AppMenuCategoryVO>() {
						public int compare(AppMenuCategoryVO appCat,
								AppMenuCategoryVO appCate) {
							return appCat.getCategoryLabel().compareTo(
									appCate.getCategoryLabel());
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppMenuCategoryVO;
	}

	/**
	 * @param categoryValue
	 * @param listApps
	 * @return List<AppMenuVO>
	 */
	private List<AppMenuVO> setMenus(String categoryValue,
			List<IpvodAppMenuCategory> listApps) {
		List<AppMenuVO> listAppMenuVO = new ArrayList<AppMenuVO>();
		try {
			AppMenuConverter appMenuConverter = new AppMenuConverter();
			for (IpvodAppMenuCategory ipvodAppMenuCategory : listApps) {
				if (categoryValue.equalsIgnoreCase(ipvodAppMenuCategory
						.getIpvodAppCategory().getName())) {
					listAppMenuVO.add(appMenuConverter.toAppMenuVO(
							ipvodAppMenuCategory.getIpvodAppMenu(),
							ipvodAppMenuCategory.getOrder()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppMenuVO;
	}
}