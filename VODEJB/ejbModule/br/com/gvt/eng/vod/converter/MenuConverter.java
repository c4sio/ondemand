package br.com.gvt.eng.vod.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;

public class MenuConverter {

	/**
	 * Converte os dados da tabela IpvodVisualMenu
	 * 
	 * @param ipvodMenu
	 * @return List IpvodVisualMenu
	 */
	public List<IpvodVisualMenu> toMenuList(List<IpvodVisualMenu> ipvodMenu) {
		List<IpvodVisualMenu> listMenu = new ArrayList<IpvodVisualMenu>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodMenu == null) {
				return listMenu;
			}
			// Varre a lista para buscar os dados
			for (IpvodVisualMenu menu : ipvodMenu) {
				listMenu.add(toMenu(menu));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(listMenu, new Comparator<IpvodVisualMenu>() {
	        @Override public int compare(IpvodVisualMenu p1, IpvodVisualMenu p2) {
	            return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
	        }

	    });
		
		return listMenu;
	}

	/**
	 * @param menu
	 * @return IpvodVisualMenu
	 */
	public IpvodVisualMenu toMenu(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		AssetConverter assetConverter = new AssetConverter();
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());
			ipvodMenu.setZindex(menu.getZindex());

			// Chamada recursiva para buscar submenus
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setIpvodVisualMenus(toMenuList(menu
						.getIpvodVisualMenus()));
			}

			// Dados de componentes
			if (menu.getIpvodVisualComponent() != null) {
				ipvodMenu.setIpvodVisualComponent(menu.getIpvodVisualComponent());
			}

			// Dados de assets
			if (menu.getIpvodAssets() != null && !menu.getIpvodAssets().isEmpty()) {
				ipvodMenu.setIpvodAssets(assetConverter.toAssetList(menu.getIpvodAssets()));
			}
			if (menu.getIpvodPackages() != null && !menu.getIpvodPackages().isEmpty()) {
				ipvodMenu.setIpvodPackages(menu.getIpvodPackages());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}

	/**
	 * @param menu
	 * @return IpvodVisualMenu
	 */
	public IpvodVisualMenu toMenuNoPurchases(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		AssetConverter assetConverter = new AssetConverter();
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());
			ipvodMenu.setZindex(menu.getZindex());

			// Chamada recursiva para buscar submenus
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setIpvodVisualMenus(toMenuList(menu
						.getIpvodVisualMenus()));
			}

			// Dados de componentes
			if (menu.getIpvodVisualComponent() != null) {
				ipvodMenu.setIpvodVisualComponent(menu.getIpvodVisualComponent());
			}

			// Dados de assets
			if (menu.getIpvodAssets() != null && !menu.getIpvodAssets().isEmpty()) {
				ipvodMenu.setIpvodAssets(assetConverter.toAssetListNoPurchases(menu.getIpvodAssets()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}
	/**
	 * @param menu
	 * @return IpvodVisualMenu
	 */
	public IpvodVisualMenu toMenuWithoutAssets(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());
			ipvodMenu.setContentURL(menu.getContentURL());
			
			PackageConverter packageConverter = new PackageConverter(); 
			ipvodMenu.setIpvodPackages(packageConverter.toPackageListNoAssets(menu.getIpvodPackages()));
			
			if ((ipvodMenu.getContentURL() == null || "".equals(ipvodMenu.getContentURL())) && menu.getMenuId() != 0) {
				ipvodMenu.setContentURL("/menu/" + ipvodMenu.getMenuId());
			}
			
			// Chamada recursiva para buscar submenus
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setIpvodVisualMenus(toMenuActiveList(menu
						.getIpvodVisualMenus()));
			}

			// Dados de componentes
			if (menu.getIpvodVisualComponent() != null) {
				ipvodMenu.setIpvodVisualComponent(menu
						.getIpvodVisualComponent());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}
																																																																																																																																																																																																																																																																							
	public List<IpvodVisualMenu> toMenuActiveList(
			List<IpvodVisualMenu> ipvodMenu) {
		List<IpvodVisualMenu> listMenu = new ArrayList<IpvodVisualMenu>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodMenu == null) {
				return listMenu;
			}
			// Varre a lista para buscar os dados
			for (IpvodVisualMenu menu : ipvodMenu) {
				//VERIFICA SE MENU ESTÁ ATIVO
				if (menu.getActive() == 1) {
					//VERIFICA SE A DATA DE DISPONIBILIDADE ESTÁ VALIDA
					if ((menu.getAvaliableSince() == null || menu.getAvaliableSince().before(new Date())) &&
							(menu.getAvaliableUntil() == null || menu.getAvaliableUntil().after(new Date()))) {
						listMenu.add(toMenuWithoutAssets(menu));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMenu;
	}

	public List<IpvodVisualMenu> toMenuActiveList(
			List<IpvodVisualMenu> ipvodMenu, Map<Long, IpvodPackage> userPackages) {
		List<IpvodVisualMenu> listMenu = new ArrayList<IpvodVisualMenu>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodMenu == null) {
				return listMenu;
			}
			// Varre a lista para buscar os dados
			for (IpvodVisualMenu menu : ipvodMenu) {
				//VERIFICA SE MENU ESTÁ ATIVO
				if (menu.getActive() == 1) {
					//VERIFICA SE A DATA DE DISPONIBILIDADE ESTÁ VALIDA
					if ((menu.getAvaliableSince() == null || menu.getAvaliableSince().before(new Date())) &&
							(menu.getAvaliableUntil() == null || menu.getAvaliableUntil().after(new Date()))) {
						//VERIFICA SE O USUARIO POSSUI OS PACOTES AGREGADOS AO MENU
						if (menu.getIpvodPackages() != null && !menu.getIpvodPackages().isEmpty()) {
							for (IpvodPackage pckage : menu.getIpvodPackages() ){
								if (userPackages.containsKey(pckage.getPackageId())) {
									listMenu.add(toMenuWithoutAssets(menu, userPackages));
									break;
								}
							}
						} else {
							listMenu.add(toMenuWithoutAssets(menu, userPackages));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMenu;
	}

	public IpvodVisualMenu toMenuWithoutAssets(IpvodVisualMenu menu, Map<Long, IpvodPackage> userPackages) {
		IpvodVisualMenu ipvodMenu = null;
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());
			ipvodMenu.setContentURL(menu.getContentURL());
			
			PackageConverter packageConverter = new PackageConverter(); 
			ipvodMenu.setIpvodPackages(packageConverter.toPackageListNoAssets(menu.getIpvodPackages()));
			
			if ((ipvodMenu.getContentURL() == null || "".equals(ipvodMenu.getContentURL())) && menu.getMenuId() != 0) {
				ipvodMenu.setContentURL("/menu/" + ipvodMenu.getMenuId());
			}
			
			// Chamada recursiva para buscar submenus
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setIpvodVisualMenus(toMenuActiveList(menu.getIpvodVisualMenus(), userPackages));
			}

			// Dados de componentes
			if (menu.getIpvodVisualComponent() != null) {
				ipvodMenu.setIpvodVisualComponent(menu
						.getIpvodVisualComponent());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}

	/**
	 * @param menu
	 * @return IpvodVisualMenu
	 */
	public IpvodVisualMenu toActiveMenu(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		AssetConverter assetConverter = new AssetConverter();
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());

			// Chamada recursiva para buscar submenus
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setIpvodVisualMenus(toMenuActiveList(menu
						.getIpvodVisualMenus()));
			}

			// Dados de componentes
			if (menu.getIpvodVisualComponent() != null) {
				ipvodMenu.setIpvodVisualComponent(menu
						.getIpvodVisualComponent());
			}

			// Dados de assets
			if (menu.getIpvodAssets() != null
					&& !menu.getIpvodAssets().isEmpty()) {
				ipvodMenu.setIpvodAssets(assetConverter.toAssetList(menu
						.getIpvodAssets()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}

	public String toSTBJson(IpvodVisualMenu ipvodVisualMenu) {
		ObjectMapper mapper = new ObjectMapper();
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodVisualMenu");
		prop.add("ipvodVisualMenus");
		prop.add("ipvodVisualComponent");
		prop.add("packageMenu");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		FilterProvider filters = new SimpleFilterProvider().addFilter(
				"myFilter", theFilter);

		String dtoAsString = null;
		try {
			dtoAsString = mapper.writer(filters).writeValueAsString(
					toMenu(ipvodVisualMenu));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dtoAsString;
	}
	
	public List<IpvodVisualMenu> toMenuLevelOne(List<IpvodVisualMenu> ipvodMenu) {
		List<IpvodVisualMenu> listMenu = new ArrayList<IpvodVisualMenu>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodMenu == null) {
				return listMenu;
			}
			// Varre a lista para buscar os dados
			for (IpvodVisualMenu menu : ipvodMenu) {
				listMenu.add(toMenuLevelOne(menu));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listMenu;
	}
	
	public IpvodVisualMenu toMenuLevelOne(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		try {
			ipvodMenu = new IpvodVisualMenu();
			ipvodMenu.setMenuId(menu.getMenuId());
			ipvodMenu.setName(menu.getName());
			ipvodMenu.setActive(menu.getActive());
			ipvodMenu.setIpvodRating(menu.getIpvodRating());
			ipvodMenu.setZindex(menu.getZindex());
			ipvodMenu.setAvaliableSince(menu.getAvaliableSince());
			ipvodMenu.setAvaliableUntil(menu.getAvaliableUntil());
			ipvodMenu.setPermanentMenu(menu.getPermanentMenu());
			
			if (menu.getIpvodVisualMenus() != null
					&& !menu.getIpvodVisualMenus().isEmpty()) {
				ipvodMenu.setHasMenus(true);
			}
			
			if (menu.getIpvodAssets() != null
					&& !menu.getIpvodAssets().isEmpty()) {
				ipvodMenu.setHasAssets(true);
			}
			
			PackageConverter packageConverter = new PackageConverter();
			ipvodMenu.setIpvodPackages(packageConverter.toPackageListNoAssets(menu.getIpvodPackages()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodMenu;
	}
	
	public IpvodVisualMenu toMenuLevelTwo(IpvodVisualMenu menu) {
		IpvodVisualMenu ipvodMenu = null;
		ipvodMenu = toMenuLevelOne(menu);
		AssetConverter assetConverter = new AssetConverter();
//		ipvodMenu.setIpvodAssets(assetConverter.toAssetList(menu.getIpvodAssets()));
		Collections.sort(menu.getIpvodVisualMenuAsset(), new Comparator<IpvodVisualMenuAsset>() {
	        @Override public int compare(IpvodVisualMenuAsset p1, IpvodVisualMenuAsset p2) {
	        	if(p1.getZindex() != null && p2.getZindex() != null) {
	        		return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
	        	}
	        	return 0;
	        }

	    });
		ipvodMenu.setIpvodAssets(assetConverter.toAssetIndexList(menu.getIpvodVisualMenuAsset()));
		ipvodMenu.setIpvodVisualMenus(toMenuLevelOne(menu.getIpvodVisualMenus()));
		Collections.sort(ipvodMenu.getIpvodVisualMenus(), new Comparator<IpvodVisualMenu>() {
	        @Override public int compare(IpvodVisualMenu p1, IpvodVisualMenu p2) {
	        	if(p1.getZindex() != null && p2.getZindex() != null) {
	        		return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
	        	}
	        	return 0;
	        }
	    });
		
		if (menu.getIpvodVisualMenu() != null) {
			ipvodMenu.setIpvodVisualMenu(new IpvodVisualMenu());
			ipvodMenu.getIpvodVisualMenu().setMenuId(menu.getIpvodVisualMenu().getMenuId());
		}
		
		return ipvodMenu;
	}
}