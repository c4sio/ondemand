package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.transaction.annotation.Transactional;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.JsonSTB;
import br.com.gvt.eng.vod.converter.MenuConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.eng.vod.util.PropertiesConfig;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.AssetPackageFacade;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.MenuAssetFacade;
import br.com.gvt.vod.facade.MenuFacade;
import br.com.gvt.vod.facade.PackageFacade;
import br.com.gvt.vod.facade.PurchaseFacade;
import br.com.gvt.vod.facade.RatingFacade;

@Stateless
@Path("/menu")
public class ConfigVisualMenu {

	@EJB
	private MenuFacade menuFacade;

	@EJB
	private AssetFacade assetFacade;
	
	@EJB
	private MenuAssetFacade menuAssetFacade;

	@EJB
	private AuthenticationFacade authenticationFacade;

	@EJB 
	private PackageFacade packageFacade;

	@EJB 
	private RatingFacade ratingFacade;
	
	@EJB
	private AssetPackageFacade assetPackageFacade; 

	@EJB 
	private PurchaseFacade purchaseFacade;
	
	MenuConverter menuConverter = new MenuConverter();
	private int menuIndex = 0;
	private int assetIndex = 0;
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/all")
	@Produces("application/json; charset=UTF-8")
	public Response listMenu2() throws RestException {
		
		@SuppressWarnings("unchecked")
		List<Object[]> mainmenu = menuAssetFacade.lab();
		// Bad Request
		if (mainmenu == null) {
			throw RestException.getNoContent();
		}
	
		List<IpvodVisualMenu> allMenus = ConfigVisualMenuUtils.filterMenus(mainmenu);

		for (IpvodVisualMenu menu : allMenus) {
			if (menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP)
					|| menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) ) {
				menu.setPermanentMenu(true);
			}
		}
		// The server successfully processed the request
		return Response.status(200).entity(/*menuConverter.toMenuList*/(allMenus))
				.build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/main")
	@Produces("application/json; charset=UTF-8")
	public Response mainMenus() throws RestException {
		
		List<IpvodVisualMenu> mainmenu = menuFacade.findMenuActive();
		// Bad Request
		if (mainmenu == null) {
			throw RestException.getNoContent();
		}
	

		for (IpvodVisualMenu menu : mainmenu) {
			if (menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP)
					|| menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) ) {
				menu.setPermanentMenu(true);
			}
		}
		// The server successfully processed the request
		return Response.status(200).entity(menuConverter.toMenuLevelOne(mainmenu))
				.build();
	}
	private void sortMenusByMenu(List<IpvodVisualMenu> ipvodVisualMenus) {
		
		try {
			if(ipvodVisualMenus != null) {
				
				for(IpvodVisualMenu ipvodVisualMenu : ipvodVisualMenus) {
					sortMenusByMenu(ipvodVisualMenu.getIpvodVisualMenus());
				}
			
			
				Collections.sort(ipvodVisualMenus, new Comparator<IpvodVisualMenu>() {
			        @Override public int compare(IpvodVisualMenu p1, IpvodVisualMenu p2) {
			        	if(p1.getZindex() != null && p2.getZindex() != null) {
			        		return p1.getZindex().intValue() - p2.getZindex().intValue(); // Ascending
			        	}
			        	return 0;
			        }
		
			    });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getMenuById(@PathParam("id") Long menuId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		// Busca dados na base
		IpvodVisualMenu ipvodVisualMenu = menuFacade.findMenuSTB(menuId);

		// Bad Request
		if (ipvodVisualMenu == null) {
			throw RestException.getNoContent();
		}
		
		//CONVERTER PARA O MENU
		IpvodVisualMenu menuFinal = menuConverter.toMenuNoPurchases(ipvodVisualMenu);
		
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTB(menuFinal)).build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/{id}/purchase")
	@Produces("application/json; charset=UTF-8")
	public Response getMenuByIdWithPurchases(@PathParam("id") Long menuId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		// Busca dados na base
		IpvodVisualMenu ipvodVisualMenu = menuFacade.findMenuSTB(menuId, auth.getIpvodUser().getUserId());

		// Bad Request
		if (ipvodVisualMenu == null) {
			throw RestException.getNoContent();
		}
		
		//CONVERTER PARA O MENU
		IpvodVisualMenu menuFinal = menuConverter.toMenu(ipvodVisualMenu);
		
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTB(menuFinal)).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/cms/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getMenuByIdCMS(@PathParam("id") Long id) throws RestException {
		if (id == null) {
			RestException exc = RestException.getBadRequest();
			 exc.getMissingParams().add("menuId");
			 throw exc;
		}

		// Busca dados na base
		IpvodVisualMenu ipvodVisualMenu = menuFacade.find(id);

		// Bad Request
		if (ipvodVisualMenu == null) {
			throw RestException.getNoContent();
		}
		if (ipvodVisualMenu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP)
				|| ipvodVisualMenu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND) ) {
			ipvodVisualMenu.setPermanentMenu(true);
		}
		
		IpvodVisualMenu menu = menuConverter.toMenuLevelTwo(ipvodVisualMenu);
		
		//remove assets out of license window 
		List<IpvodAsset> assetList = new ArrayList<IpvodAsset>();
		Calendar yesterday  = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DATE, -1);
		for (IpvodAsset asset: menu.getIpvodAssets()) {
			Calendar licenseWindowEnd = Calendar.getInstance();
			licenseWindowEnd.setTime(asset.getLicenseWindowEnd());
			if (yesterday.before(licenseWindowEnd)) {
				assetList.add(asset);
			}
		}
		menu.setIpvodAssets(assetList);
		// The server successfully processed the request
		return Response.status(200).entity(menu).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/save")
	@Consumes("application/json")
	@Transactional
	public Response saveMenus(Map<String, List<IpvodVisualMenu>> ipvodVisualMap)
			throws Exception {

		List<IpvodVisualMenu> ipvodVisualMenuList = ipvodVisualMap.get("save");
		// Bad Request
		if (ipvodVisualMenuList == null) {
			throw RestException.getBadRequest();
		}
		menuIndex = 0;
		
		boolean erro = false;
		Exception err = null;
		List<IpvodVisualMenuAsset> menus = menuAssetFacade.findAll();
		
		menuAssetFacade.clean();
		
		try {
			
			saveMenu(ipvodVisualMenuList);
			
		} catch (Exception e) {
			
			menuAssetFacade.clean();
			
			err = e;
			
			for(IpvodVisualMenuAsset menu : menus) {
				IpvodVisualMenuAsset imenu = new IpvodVisualMenuAsset();
				imenu.setIpvodAsset(menu.getIpvodAsset());
				imenu.setIpvodVisualMenu(menu.getIpvodVisualMenu());
				imenu.setZindex(menu.getZindex());
				try {
					menuAssetFacade.save(imenu);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			erro = true;			
			e.printStackTrace();
			
		}
			
		if(erro) {
			throw err;
		}
		
		// The server successfully processed the request
		return Response.status(201).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@PUT
	@Path("/update")
	@Consumes("application/json")
	public Response updateMenu(IpvodVisualMenu ipvodVisualMenu)
			throws RestException {

		// Bad Request
		if (ipvodVisualMenu == null || ipvodVisualMenu.getMenuId() == 0) {
			RestException exc = RestException.getBadRequest();
			 exc.getMissingParams().add("menuId");
			 throw exc;
		}

		// Busca dados na base
		IpvodVisualMenu ipvodMenu = menuFacade
				.find(ipvodVisualMenu.getMenuId());

		// Bad Request
		if (ipvodMenu == null) {
			throw RestException.getNoContent();
		}

		// To update Menu
		menuFacade.update(ipvodMenu);
		// The server successfully processed the request
		return Response.status(201).build();
	}

	@PermitAll
	@POST
	@Path("/{id}")
	@Consumes("application/json")
	public Response createMenu(Map<String, List<IpvodVisualMenu>> ipvodVisualMap, @PathParam("id") Long menuId)
			throws RestException {
		List<IpvodVisualMenu> saveList = ipvodVisualMap.get("save");
		
		//PREENCHE O MENU PAI
//		IpvodVisualMenu originalMenu = menuFacade.find(menuId);
//		saveList.get(0 ).setIpvodVisualMenu(originalMenu.getIpvodVisualMenu());
		
		for (IpvodVisualMenu menu : saveList) {
 			long indexAsset = 0;
 			//LIMPA TODOS OS MENU-ASSET DO MENU ATUAL 
 			menuAssetFacade.clean(menu.getMenuId());
			if (menu.getIpvodVisualMenuAsset() == null) {
				menu.setIpvodVisualMenuAsset(new ArrayList<IpvodVisualMenuAsset>());
			}
			for (IpvodAsset asset : menu.getIpvodAssets()) {
				IpvodVisualMenuAsset e = new IpvodVisualMenuAsset();
				e.setIpvodAsset(asset);
				e.setIpvodVisualMenu(menu);
				e.setZindex(indexAsset);
				menu.getIpvodVisualMenuAsset().add(e);
				indexAsset++;
			}
			saveMenu(menu);
			assetPackageFacade.insertAssetPackages(menu);
			
		}
		for (IpvodVisualMenu menu : ipvodVisualMap.get("delete")) {
			menuFacade.delete(menu);
		}
		// The server successfully processed the request
		return Response.status(201).build();
	}
	
	private void saveMenu(IpvodVisualMenu ipvodVisualMenu)
			throws RestException {
		if (ipvodVisualMenu.getIpvodVisualMenus() != null) {
			for (IpvodVisualMenu childVisualMenu: ipvodVisualMenu.getIpvodVisualMenus()) {
				childVisualMenu.setIpvodVisualMenu(ipvodVisualMenu);
			}
			saveMenu(ipvodVisualMenu.getIpvodVisualMenus());
		}
		
		List<IpvodAsset> assets = ipvodVisualMenu.getIpvodAssets();
		if (assets !=  null) {
			for(IpvodAsset asset: assets) {
				IpvodVisualMenuAsset ipvodVisualMenuAsset = new IpvodVisualMenuAsset();
				ipvodVisualMenuAsset.setZindex((long)(assetIndex++));
				ipvodVisualMenuAsset.setIpvodAsset(asset);
				ipvodVisualMenuAsset.setIpvodVisualMenu(ipvodVisualMenu);
				menuAssetFacade.save(ipvodVisualMenuAsset);
			}
		}
		menuFacade.update(ipvodVisualMenu);
	}
	
	private void saveMenu(List<IpvodVisualMenu> ipvodVisualMenuList)
			throws RestException {
		menuIndex = 0;
		assetIndex = 0;
		for (IpvodVisualMenu ipvodVisualMenu:ipvodVisualMenuList) {
			
			//menuIndex
			ipvodVisualMenu.setZindex((long)(menuIndex++));
			
			if (ipvodVisualMenu.getMenuId() == 0) {
				menuFacade.save(ipvodVisualMenu);
			} else {
				menuFacade.update(ipvodVisualMenu);
			}
			
			if (ipvodVisualMenu.getIpvodVisualMenus() != null) {
				for (IpvodVisualMenu childVisualMenu: ipvodVisualMenu.getIpvodVisualMenus()) {
					childVisualMenu.setIpvodVisualMenu(ipvodVisualMenu);
				}
				
				saveMenu(ipvodVisualMenu.getIpvodVisualMenus());
			}
			
			List<IpvodAsset> assets = ipvodVisualMenu.getIpvodAssets();
			if (assets !=  null) {
				for(IpvodAsset asset: assets) {
					IpvodVisualMenuAsset ipvodVisualMenuAsset = new IpvodVisualMenuAsset();
					ipvodVisualMenuAsset.setZindex((long)(assetIndex++));
					ipvodVisualMenuAsset.setIpvodAsset(asset);
					ipvodVisualMenuAsset.setIpvodVisualMenu(ipvodVisualMenu);
					menuAssetFacade.save(ipvodVisualMenuAsset);
				}
			}
			
		}
	}
	
	@PermitAll
	@Path("/asset/{assetId}")
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getMenusByAsset(@PathParam("assetId") Long assetId) {
		List<IpvodVisualMenu> menus = menuFacade.findByAssetId(assetId);

		List<IpvodVisualMenu> hierarchy = new ArrayList<IpvodVisualMenu>();
		IpvodVisualMenu mParent = null;
		IpvodVisualMenu mChild = null;
		for (IpvodVisualMenu menu : menus) {
			//verifica parent
			mParent = menu;

			while (mParent.getIpvodVisualMenu() != null) {
				mChild = menuConverter.toMenu(mParent);
				mParent = mParent.getIpvodVisualMenu();
				mParent.setIpvodVisualMenus(new ArrayList<IpvodVisualMenu>());
				mParent.getIpvodVisualMenus().add(mChild);
			}
			hierarchy.add(menuConverter.toMenu(mParent));
		}

		Set<String> prop = new HashSet<String>();
		prop.add("ipvodVisualMenu");
		prop.add("ipvodVisualComponent");
		prop.add("ipvodAssets");
		prop.add("ipvodRating");
		prop.add("ipvodAssetsIndex");
		prop.add("zindex");
		prop.add("ipvodAssetPackages");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
	
		FilterProvider filters = new SimpleFilterProvider()
			.addFilter("IpvodVisualMenu", theFilter)
			.addFilter("IpvodRating", theFilter)
			.addFilter("IpvodPackage", theFilter)
			.addFilter("IpvodAsset", theFilter)
			.addFilter("IpvodAssetPackage", theFilter);

		return Response.ok().entity(JsonSTB.writeAsString(filters, hierarchy)).build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response listMenuSTB(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		Map<Long, IpvodPackage> packages = new HashMap<Long, IpvodPackage>();
		for (IpvodPackage pckage : auth.getIpvodUser().getIpvodPackages()) {
			packages.put(pckage.getPackageId(), pckage);
		}
		
		List<IpvodVisualMenu> mainmenu = menuFacade.findMenuActive();
		
		// Bad Request
		if (mainmenu == null) {
			throw RestException.getNoContent();
		}
		IpvodVisualMenu onDemandMenu = null;
		IpvodVisualMenu catchUpMenu = null;
		for (IpvodVisualMenu menu : mainmenu) {
			if (menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_ONDEMAND)) {
				onDemandMenu = menu;
			}
			if (menu.getMenuId() == PropertiesConfig.getLong(IpvodConstants.MENU_ID_CATCHUP)) {
				catchUpMenu = menu;
			}
			
		}
		
		IpvodVisualMenu myContent = createFolder("Meus conteúdos", null, 2l);
		IpvodVisualMenu avaliable = createFolder("Disponíveis", "/menu/mycontent/ondemand", 2l);
		IpvodVisualMenu adult = createFolder("Adultos", "/menu/mycontent/ondemand/adult", 8l);
		myContent.addIpvodVisualMenus(avaliable);
		myContent.addIpvodVisualMenus(adult);
		onDemandMenu.getIpvodVisualMenus().add(myContent);
		
		IpvodVisualMenu watched = createFolder("Assistidos", "/menu/mycontent/catchup", 2l);
		catchUpMenu.getIpvodVisualMenus().add(watched);
		
		sortMenusByMenu(mainmenu);
		
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTBVisualMenu(menuConverter.toMenuActiveList(mainmenu, packages))).build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/ondemand")
	@Produces("application/json; charset=UTF-8")
	public Response listOnDemandSTB(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		IpvodVisualMenu onDemandMenu = menuFacade.getOnDemand(auth.getIpvodUser().getUserId());
		
		// Bad Request
		if (onDemandMenu == null) {
			throw RestException.getNoContent();
		}
		
		IpvodVisualMenu myContent = createFolder("Meus conteúdos", null, 2l);
		IpvodVisualMenu avaliable = createFolder("Disponíveis", "/menu/mycontent/ondemand", 2l);
		IpvodVisualMenu adult = createFolder("Adultos", "/menu/mycontent/ondemand/adult", 8l);
		myContent.addIpvodVisualMenus(avaliable);
		myContent.addIpvodVisualMenus(adult);
		onDemandMenu.getIpvodVisualMenus().add(myContent);
		
		// The server successfully processed the request
		return Response.status(200)
				.entity(JsonSTB.toJsonSTBParentMenu(onDemandMenu))
				.build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/ondemandcatchup")
	@Produces("application/json; charset=UTF-8")
	public Response listOnDemandAndCatchUpSTB(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		IpvodVisualMenu onDemandMenu = menuFacade.getOnDemand(auth.getIpvodUser().getUserId());
		
		// Bad Request
		if (onDemandMenu == null) {
			throw RestException.getNoContent();
		}
		
		IpvodVisualMenu myContent = createFolder("Meus conteúdos", null, 2l);
		IpvodVisualMenu avaliable = createFolder("Disponíveis", "/menu/mycontent/ondemand", 2l);
		IpvodVisualMenu adult = createFolder("Adultos", "/menu/mycontent/ondemand/adult", 8l);
		myContent.addIpvodVisualMenus(avaliable);
		myContent.addIpvodVisualMenus(adult);
		onDemandMenu.getIpvodVisualMenus().add(myContent);
		
		IpvodVisualMenu parentMenu = new IpvodVisualMenu();
		
		/*IpvodVisualMenu searchMenu = menuFacade.find(99);		
		parentMenu.getIpvodVisualMenus().add(searchMenu);*/
		
		parentMenu.setIpvodVisualMenus(new ArrayList<IpvodVisualMenu>());
		parentMenu.getIpvodVisualMenus().add(onDemandMenu);
		
		
		IpvodVisualMenu catchUpMenu = menuFacade.getCatchup(auth.getIpvodUser().getUserId());
		
		// Bad Request
		if (catchUpMenu == null) {
			throw RestException.getNoContent();
		}
		
		
		IpvodVisualMenu watched = createFolder("Assistidos", "/menu/mycontent/catchup", 2l);
		catchUpMenu.getIpvodVisualMenus().add(watched);
		catchUpMenu.setName("Outra Chance");
		parentMenu.getIpvodVisualMenus().add(catchUpMenu);
		
		
		IpvodVisualMenu searchMenu = new IpvodVisualMenu();
		//searchMenu.getIpvodVisualMenus().add(searchMenu);
		searchMenu.setName("Busca");
		searchMenu.setMenuId(9999999);
		parentMenu.addIpvodVisualMenus(searchMenu);
		
		
		
		
	
		
		// The server successfully processed the request
		return Response.status(200)
				.entity(JsonSTB.toJsonSTBParentMenu(parentMenu))
				.build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/catchup")
	@Produces("application/json; charset=UTF-8")
	public Response listOnCatchup(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		IpvodVisualMenu catchUpMenu = menuFacade.getCatchup(auth.getIpvodUser().getUserId());
		
		// Bad Request
		if (catchUpMenu == null) {
			throw RestException.getNoContent();
		}
		
		IpvodVisualMenu watched = createFolder("Assistidos", "/menu/mycontent/catchup", 2l);
		catchUpMenu.getIpvodVisualMenus().add(watched);
		
		// The server successfully processed the request
		return Response.status(200)
				.entity(JsonSTB.toJsonSTBParentMenu(catchUpMenu))
				.build();
	}
	
	private IpvodVisualMenu createFolder(String name, String request, Long ratingLevel) {
		IpvodVisualMenu ipvodMenu = new IpvodVisualMenu();
		ipvodMenu.setActive(1);
		ipvodMenu.setName(name);
		ipvodMenu.setIpvodRating(ratingFacade.find(ratingLevel));
		ipvodMenu.setZindex(999999l);
		ipvodMenu.setContentURL(request);
		ipvodMenu.setIpvodVisualMenus(new ArrayList<IpvodVisualMenu>());
		return ipvodMenu;
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/mycontent/ondemand")
	@Produces("application/json; charset=UTF-8")
	public Response getMyContentOnDemand(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		IpvodVisualMenu avaliable = createFolder("Disponíveis", "/mycontent/ondemand", 2l);
		List<IpvodAsset> assets = assetFacade.getMyContentOnDemand(auth.getIpvodUser().getUserId());
		avaliable.setIpvodAssets(assets);
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTB(menuConverter.toMenu(avaliable))).build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/mycontent/ondemand/adult")
	@Produces("application/json; charset=UTF-8")
	public Response getMyContentOnDemandAdult(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		IpvodVisualMenu avaliable = createFolder("Adult", "/mycontent/ondemand/adult", 8l);
		List<IpvodAsset> assets = assetFacade.getMyContentOnDemandAdult(auth.getIpvodUser().getUserId());
		avaliable.setIpvodAssets(assets);
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTB(menuConverter.toMenu(avaliable))).build();
	}
	
	@RolesAllowed({IpvodConstants.ROLE_STB})
	@GET
	@Path("/mycontent/catchup")
	@Produces("application/json; charset=UTF-8")
	public Response getMyContentCatchUp(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		IpvodVisualMenu avaliable = createFolder("Disponíveis", "/mycontent/ondemand", 2l);
		avaliable.setIpvodAssets(assetFacade.getMyContentCatchUp(auth.getIpvodUser().getUserId()));
		// The server successfully processed the request
		return Response.status(200).entity(JsonSTB.toJsonSTB(menuConverter.toMenu(avaliable))).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/categorize")  
	@Produces("application/json; charset=UTF-8")
	public Response listMenuCategorize() throws RestException {
		List<IpvodVisualMenu> listMenu = menuFacade.findMenuCategorize();
		// The server successfully processed the request
		return Response.status(200).entity(listMenu)
				.build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/remove")
	@Consumes("application/json")
	public Response removeMenus(List<IpvodVisualMenuAsset> listMenuAsset)
			throws Exception {
		menuAssetFacade.delete(listMenuAsset);
		assetPackageFacade.cleanAssetPackage();
		// The server successfully processed the request
		return Response.status(201).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/insert")
	@Consumes("application/json")
	public Response insertMenus(IpvodVisualMenuAsset menuAsset)
			throws Exception {
		menuAsset.setZindex(new Long(0));
		menuAssetFacade.save(menuAsset);
		menuAsset.setIpvodVisualMenu(menuFacade.find(menuAsset.getIpvodVisualMenu().getMenuId()));
		assetPackageFacade.insertAssetPackages(menuAsset.getIpvodVisualMenu());
		// The server successfully processed the request
		return Response.status(201).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Consumes("application/json")
	public Response newMenu(IpvodVisualMenu ipvodVisualMenu)
			throws Exception {
		ipvodVisualMenu.setActive(1);
		ipvodVisualMenu.setZindex(0l);
		menuFacade.save(ipvodVisualMenu);
		// The server successfully processed the request
		return Response.status(201).build();
	}	
}