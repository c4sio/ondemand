package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.ipvod.rest.util.Util;
import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.AssetConverter;
import br.com.gvt.eng.vod.converter.PurchaseConverter;
import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodEventType;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodReason;
import br.com.gvt.eng.vod.model.IpvodSession;
import br.com.gvt.eng.vod.vo.SessionVO;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.EventTypeFacade;
import br.com.gvt.vod.facade.MenuFacade;
import br.com.gvt.vod.facade.PurchaseFacade;
import br.com.gvt.vod.facade.ReasonFacade;
import br.com.gvt.vod.facade.SessionFacade;
import br.com.gvt.vod.facade.VerifyConnectionFacade;

@Stateless
@Path("/catalog")
public class Catalog {

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private MenuFacade menuFacade;

	@EJB
	private PurchaseFacade purchaseFacade;

	@EJB
	private AuthenticationFacade authenticationFacade;

	@EJB
	private SessionFacade sessionFacade;

	@EJB
	private ReasonFacade reasonFacade;

	@EJB
	private EventTypeFacade eventTypeFacade;

	@EJB
	private VerifyConnectionFacade verifyConnectionFacade;

	private AssetConverter converter = new AssetConverter();

	private PurchaseConverter purchaseConverter = new PurchaseConverter();

	@PermitAll
	@GET
	@Path("/listTest")
	public Response printMessage() {
		String result = "Restful example : It works - "
				+ verifyConnectionFacade.verifyConnection();
		return Response.status(200).entity(result).build();
	}

	@RolesAllowed({ IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING,
			IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB,
			IpvodConstants.ROLE_VOC })
	@GET
	@Path("/testToken")
	public Response printMessage2() {
		String result = "Teste Token ok!";
		return Response.status(200).entity(result).build();
	}

	// @RolesAllowed("USERS")
	@PermitAll
	@GET
	@Path("/list")
	@Produces("application/json; charset=UTF-8")
	public Response list() {
		return Response.status(200)
				.entity(converter.toAssetJson(assetFacade.findAll())).build();
	}

	@PermitAll
	@GET
	@Path("/genre/{genreId}")
	@Produces("application/json; charset=UTF-8")
	public Response listByGenre(@PathParam("genreId") Long genreId)
			throws RestException {

		if (genreId == null) {
			throw new RestException(1, "Parametro obrigatorio nulo. [genreId]");
		}

		return Response
				.status(200)
				.entity(converter.toAssetJson(assetFacade
						.listAssetByCategory(genreId))).build();
	}

	@PermitAll
	@GET
	@Path("/type/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response listByAssetType(@PathParam("id") Long idAssetType)
			throws RestException {

		if (idAssetType == null) {
			throw new RestException(1, "Parametro obrigatorio nulo. [ID]");
		}

		return Response
				.status(200)
				.entity(converter.toAssetJson(assetFacade
						.listAssetType(idAssetType))).build();
	}

	@GET
	@Path("/listNewReleases/{param}")
	@Produces("application/json; charset=UTF-8")
	public Response listByNewReleases(@PathParam("param") String genre)
			throws RestException {

		if (genre == null || genre.isEmpty()) {
			throw new RestException(1, "Par�metro obrigat�rio nulo. [genre]");
		}

		return Response
				.status(200)
				.entity(converter.toAssetJson(assetFacade
						.listAssetByNewReleases(genre))).build();
	}

	@PermitAll
	@GET
	@Path("/purchase")
	@Produces("application/json; charset=UTF-8")
	public Response listAllPurchase() {
		List<IpvodPurchase> listIpvodPurchase = new ArrayList<IpvodPurchase>();
		listIpvodPurchase = purchaseFacade.findAll();
		return Response.status(200)
				.entity(purchaseConverter.getPurchaseList(listIpvodPurchase))
				.build();
	}

	@PermitAll
	@GET
	@Path("/purchase/{purchaseID:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findPurchaseByID(@PathParam("purchaseID") long purchaseID) {
		IpvodPurchase ipvodPurchase = new IpvodPurchase();
		ipvodPurchase = purchaseFacade.find(purchaseID);
		return Response.status(200)
				.entity(purchaseConverter.toPurchase(ipvodPurchase)).build();
	}

	@POST
	@PermitAll
	@RolesAllowed(IpvodConstants.ROLE_STB)
	@Path("/purchase/save")
	@Produces("application/json; charset=UTF-8")
	public Response savePurchase(IpvodPurchase purchase,
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		purchase.setIpvodEquipment(auth.getEquipment());

		if (purchase.getIpvodAsset() == null
				|| purchase.getIpvodAsset().getAssetId() == 0) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("assetId");
			throw exc;
		}

		IpvodPurchase p = purchaseFacade.findPurchase(purchase,
				auth.getIpvodUser());
		if (p != null) {
			purchase = p;
		} else {
			try {
				purchase = purchaseFacade.savePurchase(purchase.getIpvodAsset()
						.getAssetId(), purchase.getIpvodEquipment());
			} catch (BusinessException e) {
				RestException exc = RestException.getBadRequest();
				exc.getMissingParams().add(e.getMessage());
				throw exc;
			}
		}

		return Response.ok(purchaseConverter.toPurchase(purchase)).build();
	}

	/**
	 * Metodo de ativação do householding
	 * 
	 * @param chipId
	 * @return token
	 * @throws RestException
	 */
	@GET
	@PermitAll
	@Path("/activate")
	@Produces("application/json; charset=UTF-8")
	public Response active(@QueryParam("chipId") String chipId) throws RestException {
		String token;
		try {
			token = authenticationFacade.activate(chipId);
		} catch (BusinessException e) {
			throw new RestException(e);
		}
		return Response.ok(token).build();
	}
	
	/**
	 * Metodo Play do Set top box
	 * 
	 * @param purchaseId
	 * @param chipId
	 * @return token
	 * @throws RestException
	 */
	@GET
	@PermitAll
	@Path("/play")
	@Produces("application/json; charset=UTF-8")
	public Response play(@QueryParam("purchaseId") Long purchaseId,
			@QueryParam("chipId") String chipId) throws RestException {

		if (purchaseId == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("purchaseId");
			throw exc;
		}

		if (Util.isEmptyOrNull(chipId)) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("chipId");
			throw exc;
		}

		String token = null;
		Date expirationDate = null;
		Long assetId = null;

		try {
			if (purchaseId != null) {
				IpvodPurchase ipvodPurchase = new IpvodPurchase();
				// Busca dados @IpvodPurchase
				ipvodPurchase = purchaseFacade.find(purchaseId);

				if (ipvodPurchase == null) {
					throw RestException.getNoContent();
				}

				assetId = ipvodPurchase.getIpvodAsset().getAssetId();

				// Soma novos valores para data de expiracao do movie
				TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
				Calendar sumValues = Calendar.getInstance(TimeZone
						.getTimeZone("UTC"));
				// Trinta minutos como valor plus para todos os filmes
				long thirtyMinutesPlus = 1800;
				// Se o campo total time estiver zerado e atribuido 1 hora (3600
				// segundos) como default
				if (ipvodPurchase.getIpvodAsset().getTotalTime() == 0) {
					ipvodPurchase.getIpvodAsset().setTotalTime(3600);
				}

				// Atribui o valor ipvodPurchase.getValidUntil() para somar com
				// os novos valores
				sumValues.setTime(ipvodPurchase.getValidUntil());
				// Soma os novos valores
				sumValues
						.add(Calendar.MINUTE,
								(int) ((ipvodPurchase.getIpvodAsset()
										.getTotalTime() + thirtyMinutesPlus) / 60));

				// Novo valor atribuido a variavel expirationDate
				expirationDate = sumValues.getTime();
			}

			// Gera token
			token = authenticationFacade.getTokenStreaming(assetId,
					expirationDate, chipId);
		} catch (Exception e) {
			throw new RestException(e);
		}
		return Response.ok(token).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@POST
	@Path("/session/save")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response saveSession(SessionVO sessionVO,
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {

		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}

		// Verifica se o objeto esta vazio
		if (sessionVO == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("Session");
			throw exc;
		}

		// Busca dados @IpvodPurchase
		IpvodPurchase ipvodPurchase = null;
		if (sessionVO.getPurchaseId() > 0) {
			ipvodPurchase = new IpvodPurchase();
			ipvodPurchase = purchaseFacade.find(sessionVO.getPurchaseId());
			if (ipvodPurchase == null) {
				throw RestException.getNoContent();
			}
		} else {
			throw RestException.getNoContent();
		}

		// Se o valor for vazio atribui o valor default 999
		if (sessionVO.getReasonCode() == null
				|| "".equals(sessionVO.getReasonCode())) {
			sessionVO.setReasonCode("999");
		}

		// Busca dados @IpvodReason
		IpvodReason ipvodReason = null;
		if (sessionVO.getReasonCode() != null
				&& !"".equalsIgnoreCase(sessionVO.getReasonCode())) {
			ipvodReason = new IpvodReason();
			ipvodReason = reasonFacade.findDataByReasonCode(sessionVO
					.getReasonCode());
			if (ipvodReason == null) {
				throw RestException.getNoContent();
			}
		} else {
			throw RestException.getNoContent();
		}

		// Busca dados @IpvodEventType
		IpvodEventType ipvodEventType = null;
		if (sessionVO.getEventType() != null
				&& !"".equalsIgnoreCase(sessionVO.getEventType())) {
			ipvodEventType = new IpvodEventType();
			ipvodEventType = eventTypeFacade.findDataByEventTypeName(sessionVO
					.getEventType());
			if (ipvodEventType == null) {
				throw RestException.getNoContent();
			}
		} else {
			throw RestException.getNoContent();
		}

		// Atribui os valores para o objeto @IpvodSession
		IpvodSession ipvodSession = new IpvodSession();

		// A data e enviada como milliseconds, necessario converter
		Date eventDate = new Date(sessionVO.getEventDate());
		ipvodSession.setEventDate(eventDate);

		ipvodSession.setIpvodEquipment(auth.getEquipment());
		ipvodSession.setDuration(ipvodPurchase.getIpvodAsset().getTotalTime());
		ipvodSession.setPlayTime(sessionVO.getPlayTime());
		ipvodSession.setIpvodReason(ipvodReason);
		ipvodSession.setIpvodPurchase(ipvodPurchase);
		ipvodSession.setIpvodEventType(ipvodEventType);

		System.out.println("Salva Session[" + auth.getEquipment() + ", "
				+ ipvodPurchase.getIpvodAsset().getTotalTime() + ", "
				+ sessionVO.getPlayTime() + ", " + ipvodReason.getDescription()
				+ ", " + ipvodPurchase.getPurchaseId() + ", "
				+ ipvodEventType.getDescription() + "]");

		// Salva os dados na tabela @IpvodSession
		sessionFacade.save(ipvodSession);

		return Response.status(201).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/myPurchases")
	@Produces("application/json; charset=UTF-8")
	public Response listMyPurchase(
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		return Response.status(200)
				.entity(purchaseFacade.findMyPurchases(auth.getIpvodUser()))
				.build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/myPurchases/menu/{menuId}")
	@Produces("application/json; charset=UTF-8")
	public Response listMyPurchase(@PathParam("menuId") long menuId,
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		return Response
				.status(200)
				.entity(purchaseFacade.findMyPurchases(menuId,
						auth.getIpvodUser())).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/myPurchases/menu/mycontent/ondemand")
	@Produces("application/json; charset=UTF-8")
	public Response listMyPurchaseOnDemand(
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		return Response
				.status(200)
				.entity(purchaseFacade.findMyPurchasesOnDemand(auth
						.getIpvodUser())).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/myPurchases/menu/mycontent/ondemand/adult")
	@Produces("application/json; charset=UTF-8")
	public Response listMyPurchaseOnDemandAdult(
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		return Response
				.status(200)
				.entity(purchaseFacade.findMyPurchasesOnDemandAdult(auth
						.getIpvodUser())).build();
	}

	@RolesAllowed(IpvodConstants.ROLE_STB)
	@GET
	@Path("/myPurchases/menu/mycontent/catchup")
	@Produces("application/json; charset=UTF-8")
	public Response listMyPurchaseCatchup(
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token)
			throws RestException {
		// Valida a autenticacao do Set Top box
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authentication");
			throw exc;
		}
		return Response
				.status(200)
				.entity(purchaseFacade.findMyPurchasesCatchup(auth
						.getIpvodUser())).build();
	}
}