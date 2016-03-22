package br.com.gvt.eng.ipvod.rest.app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.ipvod.rest.util.Util;
import br.com.gvt.eng.vod.converter.AppMenuCategoryConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAppExclusiveUser;
import br.com.gvt.eng.vod.model.IpvodAppMenu;
import br.com.gvt.eng.vod.model.IpvodAppMenuCategory;
import br.com.gvt.eng.vod.vo.AppProductVO;
import br.com.gvt.vod.facade.AppExclusiveUserFacade;
import br.com.gvt.vod.facade.AppMenuCategoryFacade;
import br.com.gvt.vod.facade.AppMenuFacade;
import br.com.gvt.vod.facade.PaytvServiceFacade;

@Stateless
@Path("/appmenu")
public class AppMenu {

	@EJB
	private AppMenuFacade appMenuFacade;

	@EJB
	private AppMenuCategoryFacade appMenuCategoryFacade;

	@EJB
	private PaytvServiceFacade paytvServiceFacade;

	@EJB
	private AppExclusiveUserFacade appExclusiveUserFacade;

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findAppById(@PathParam("id") Long appId)
			throws RestException {

		// Busca lista de registros para processar
		IpvodAppMenu ipvodAppMenu = this.appMenuFacade.findByID(appId);
		// Bad request
		if (ipvodAppMenu == null) {
			throw RestException.getNoContent();
		}

		// Retorna o objeto
		return Response.status(200).entity(ipvodAppMenu).build();
	}

	@PermitAll
	@GET
	@Path("/")
	@Produces("application/json; charset=UTF-8")
	public Response findListApps() throws RestException {
		// Busca lista de registros para processar
		List<IpvodAppMenu> listApps = this.appMenuFacade.findAll();
		// Bad request
		if (listApps == null) {
			throw RestException.getNoContent();
		}

		// Retorna o objeto
		return Response.status(200).entity(listApps).build();
	}

	@PermitAll
	@GET
	@Path("/menu/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findAppMenuById(@PathParam("id") Long appId)
			throws RestException {
		AppMenuCategoryConverter appMenuCategoryConverter = new AppMenuCategoryConverter();

		// Busca lista de registros para processar
		IpvodAppMenuCategory ipvodAppMenuCategory = this.appMenuCategoryFacade
				.findByID(appId);
		// Bad request
		if (ipvodAppMenuCategory == null) {
			throw RestException.getNoContent();
		}

		// Retorna o objeto
		return Response
				.status(200)
				.entity(appMenuCategoryConverter
						.toAppMenuCategory(ipvodAppMenuCategory)).build();
	}

	@PermitAll
	@GET
	@Path("/menu")
	@Produces("application/json; charset=UTF-8")
	public Response findAllAppMenus() throws RestException {
		AppMenuCategoryConverter appMenuCategoryConverter = new AppMenuCategoryConverter();

		// Busca lista de registros para processar
		List<IpvodAppMenuCategory> listAllApps = this.appMenuCategoryFacade
				.findAll();
		// Bad request
		if (listAllApps == null) {
			throw RestException.getNoContent();
		}

		// Retorna o objeto
		return Response
				.status(200)
				.entity(appMenuCategoryConverter
						.getAppMenuCategoryListAll(listAllApps)).build();
	}

	@PermitAll
	@GET
	@Path("/find/{keyValue}")
	@Produces("application/json; charset=UTF-8")
	public Response findAppsByCaID(@PathParam("keyValue") String keyValue)
			throws RestException {
		AppMenuCategoryConverter appMenuCategoryConverter = new AppMenuCategoryConverter();

		// Bad request
		if (keyValue == null || "".equals(keyValue)) {
			throw RestException.getBadRequest();
		}

		// Busca dados stp hibrido - Ordem Siebel
		String patternString = ".*-.*";
		Pattern pattern = Pattern.compile(patternString,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(keyValue);
		boolean matches = matcher.matches();

		List<IpvodAppMenuCategory> listApps = new ArrayList<IpvodAppMenuCategory>();
		if (matches) {
			// Verifica se o usuario esta cadastrado na base
			IpvodAppExclusiveUser ipvodAppExclusiveUser = appExclusiveUserFacade
					.findByKeyValue(keyValue);

			// Se encontrou carrega menu especial para o usuario
			if (ipvodAppExclusiveUser != null) {
				listApps = this.appMenuCategoryFacade
						.findMenuAppsHybridByPackage(ipvodAppExclusiveUser
								.getPackageValue());
			} else {
				// Busca dados do usuario no IT
				AppProductVO appProductVO = paytvServiceFacade
						.findPackageHybridByService(Util.getServicePaytv(),
								keyValue);
				// Busca o menu de aplicativos do usuario
				listApps = this.appMenuCategoryFacade
						.findMenuAppsHybridByPackage(appProductVO.getResult()
								.getProducts());
			}
		} else {
			// Busca lista de registros para processar
			listApps = this.appMenuCategoryFacade
					.findMenuAppsDthByCaId(keyValue);
		}

		// Bad request
		if (listApps == null) {
			throw RestException.getNoContent();
		}

		// Retorna o objeto
		return Response.status(200)
				.entity(appMenuCategoryConverter.getAppMenuList(listApps))
				.build();
	}

}
