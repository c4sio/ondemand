package br.com.gvt.eng.ipvod.rest.it;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Hibernate;

import br.com.gvt.eng.vod.converter.UserConverter;
import br.com.gvt.eng.vod.exception.rest.RestCodeError;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.AuthVO;
import br.com.gvt.eng.vod.vo.ExecutionVO;
import br.com.gvt.eng.vod.vo.ITConnectionVO;
import br.com.gvt.eng.vod.vo.PurchaseVO;
import br.com.gvt.eng.vod.vo.TopExecutionVO;
import br.com.gvt.eng.vod.vo.TransactionVO;
import br.com.gvt.eng.vod.vo.provisioning.Equipment;
import br.com.gvt.eng.vod.vo.provisioning.Product;
import br.com.gvt.eng.vod.vo.provisioning.User;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.EquipmentFacade;
import br.com.gvt.vod.facade.EquipmentTypeFacade;
import br.com.gvt.vod.facade.PackageFacade;
import br.com.gvt.vod.facade.PurchaseFacade;
import br.com.gvt.vod.facade.SessionFacade;
import br.com.gvt.vod.facade.UserFacade;

@Stateless
@Path("/services")
public class ITConnection {

	@EJB
	protected UserFacade userFacade;

	@EJB
	protected SessionFacade sessionFacade;

	@EJB
	protected PurchaseFacade purchaseFacade;

	@EJB
	protected AuthenticationFacade authenticationFacade;

	@EJB
	protected EquipmentFacade equipmentFacade;

	@EJB
	protected EquipmentTypeFacade equipmentTypeFacade;

	@EJB
	protected PackageFacade packageFacade;

	private ObjectMapper mapper = new ObjectMapper();

	@PermitAll
	@POST
	@Path("/subscriber")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User user) throws RestException {

		voIsNull(user);

		if(StringUtils.isBlank(user.getSubscriberId()) || StringUtils.isBlank(user.getServiceRegion())){
			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Os campos subscriberId e serviceRegion são obrigatórios.", Status.BAD_REQUEST);
		}

		IpvodUser ipvodUser = StringUtils.isNotBlank(user.getSubscriberId()) ? userFacade.findUserBySubscriberID(user.getSubscriberId()) : null;

		if (ipvodUser != null) {

			updateFields(ipvodUser, user);
			return Response.status(Status.OK).build();

		}

		ipvodUser = new IpvodUser();
		ipvodUser.setActive(Boolean.TRUE);
		ipvodUser.setCrmCustomerId(user.getSubscriberId());
		ipvodUser.setAuthInfo(user.getAuthInfo());
		ipvodUser.setCreatedAt(Calendar.getInstance());

		// Caso tenha equipamento para inserir
		if (user.getEquipments() != null && !user.getEquipments().isEmpty()) {

			List<IpvodEquipment> ipvodEquipments = validateEquipments(user.getSubscriberId(), user.getEquipments());
			containsEquipments(ipvodUser, ipvodEquipments);
			ipvodUser.setIpvodEquipments(ipvodEquipments);

			for (IpvodEquipment equipment : ipvodUser.getIpvodEquipments()) {
				equipment.setIpvodUser(ipvodUser);
			}

		}

		// Caso tenha produto para inserir
		if (user.getProducts() != null && !user.getProducts().isEmpty()) {

			List<IpvodPackage> ipvodPackages = validateProducts(user.getProducts());
			containsProducts(ipvodUser, ipvodPackages);
			ipvodUser.setIpvodPackages(ipvodPackages);

		}

		ipvodUser.setServiceRegion(user.getServiceRegion());

		userFacade.saveUser(ipvodUser);

		return Response.status(Status.CREATED).build();

	}

	@PermitAll
	@DELETE
	@Path("/subscriber/{subscriberId}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("subscriberId") String subscriberId) throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);
			userFacade.deleteUser(ipvodUser);
			return Response.status(Status.OK).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		} catch (EJBTransactionRolledbackException e) {
			throw new RestException(RestCodeError.ENTIDADE_EXISTENTE, "Usuário não pode ser excluído, pois possui relacionamento com equipamento ativo.", Status.OK);
		}

	}

	@PermitAll
	@POST
	@Path("/subscriber/{subscriberId}/block")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response block(@PathParam("subscriberId") String subscriberId) throws RestException {

		try {

			IpvodUser user = validateSubscriber(subscriberId);

			if (!user.getActive()) {
				throw new RestException(RestCodeError.SEM_MODIFICACAO, "Subscriber com ID " + subscriberId
						+ " já encontra-se bloqueado.", Status.NOT_MODIFIED);
			} else {
				user.setActive(Boolean.FALSE);
				userFacade.update(user);
			}

			return Response.status(Status.OK).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		}

	}

	@PermitAll
	@DELETE
	@Path("/subscriber/{subscriberId}/block")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response unblock(@PathParam("subscriberId") String subscriberId) throws RestException {

		try {

			IpvodUser user = validateSubscriber(subscriberId);

			if (user.getActive()) {
				throw new RestException(RestCodeError.SEM_MODIFICACAO, "Subscriber com ID " + subscriberId
						+ " já encontra-se ativo.", Status.NOT_MODIFIED);
			} else {

				user.setActive(Boolean.TRUE);
				userFacade.update(user);

			}

			return Response.status(Status.OK).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		}

	}

	@PermitAll
	@GET
	@Path("/subscriber/{subscriberId}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSubscriber(@PathParam("subscriberId") String subscriberId) throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);

			User userVo = new UserConverter().toUser(ipvodUser);

			lastAuthenticationEquipmentSubscriber(userVo);

			return Response.status(Status.OK).entity(mapper.writeValueAsString(userVo)).build();

		} catch (RestException re) {
			throw re;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@PermitAll
	@PUT
	@Path("/subscriber/{subscriberId}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSubscriber(@PathParam("subscriberId") String subscriberId, User user) throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);

			if (StringUtils.isBlank(user.getServiceRegion())) {
				throw new RestException(RestCodeError.ERRO_UPDATE_SUBSCRIBER,
						"Erro ao validar [service region]. Favor inserir um valor válido.", Status.OK);
			}

			if (user.getServiceRegion().equals(ipvodUser.getServiceRegion())) {
				throw new RestException(RestCodeError.ERRO_UPDATE_SUBSCRIBER, 
						"Service region enviado para atualização já encontra-se registrado no subscriber.", Status.OK);
			}

			ipvodUser.setServiceRegion(user.getServiceRegion());
			userFacade.save(ipvodUser);
			return Response.status(Status.OK).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}
			throw re;

		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@PermitAll
	@POST
	@Path("/subscriber/{subscriberId}/product")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addServices(@PathParam("subscriberId") String subscriberId, List<Product> products) throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);
			List<IpvodPackage> packages = validateProducts(products);
			List<IpvodPackage> listAdd = containsProducts(ipvodUser, packages);

			if (listAdd.size() <= 0) {
				return Response.status(Status.OK).build();
			}

			// Add services to user
			userFacade.addServicesUser(ipvodUser, listAdd);
			// The server successfully processed the request
			return Response.status(Status.CREATED).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		}

	}

	@PermitAll
	@DELETE
	@Path("/subscriber/{subscriberId}/product/{productId}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeService(@PathParam("subscriberId") String subscriberId, @PathParam("productId") String productId)
			throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);

			// Valida se o serviço é válido, ou seja, esta registrado na base,
			// mesmo não estando vinculado ao usuário
			IpvodPackage ipvodPackage = validateProduct(new Product(productId));

			IpvodPackage flag = null;

			if (ipvodUser.getIpvodPackages() != null && !ipvodUser.getIpvodPackages().isEmpty()) {
				
				Boolean no = Boolean.TRUE;
				
				for(IpvodPackage p : ipvodUser.getIpvodPackages()){
					if (ipvodPackage.getOtherId().equals(p.getOtherId())) {
						no = Boolean.FALSE;
					}
				}
				
				if(no){
					return Response.status(Status.NOT_MODIFIED).build();
				}

				for (IpvodPackage product : ipvodUser.getIpvodPackages()) {

					if (ipvodPackage.getOtherId().equals(product.getOtherId())) {
						flag = product;
						break;
					}

				}

			} else {
				return Response.status(Status.NOT_MODIFIED).build();
			}

			if (flag != null) {
				ipvodUser.getIpvodPackages().remove(flag);
			} else {
				// Significa que o produto que esta sendo removido não esta
				// adicionado ao usuário em questão.
				return Response.status(Status.OK).build();
			}

			userFacade.update(ipvodUser);

			return Response.status(Status.ACCEPTED).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		}

	}

	@PermitAll
	@POST
	@Path("/subscriber/{subscriberId}/equipment")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addEquipments(@PathParam("subscriberId") String subscriberId, List<Equipment> equipments)
			throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);
			
			//Refatorar posteriormente
			List<Equipment> temp = new ArrayList<Equipment>();
			for (Equipment equipment : equipments) {
				IpvodEquipment e = equipmentFacade.findBySerial(equipment.getSerial());
				if(e != null && subscriberId.equals(e.getIpvodUser().getCrmCustomerId())){
					temp.add(equipment);
				}
			}
			equipments.removeAll(temp);
			if(equipments.isEmpty()){
				throw new RestException(RestCodeError.OBJETO_DUPLICADO, "", Status.NOT_MODIFIED);
			}
			//Refatorar posteriormente
			
			List<IpvodEquipment> ipvodEquipments = validateEquipments(subscriberId, equipments);
			containsEquipments(ipvodUser, ipvodEquipments);

			userFacade.addEquipmentsUser(ipvodUser, ipvodEquipments);

			return Response.status(Status.CREATED).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;
		}

	}

	@PermitAll
	@GET
	@Path("/equipment/{param}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findEquipment(@PathParam("param") String param) throws RestException {

		try {

			String rest = "";

			if (StringUtils.isBlank(param)) {
				throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Erro recuperar Equipamento. Necessário informar CA_ID ou MAC.", Status.OK);
			}

			param.trim();
			Equipment equipment = equipmentFacade.getEquipmentVOByMac(param);

			if (equipment == null) {
				equipment = equipmentFacade.getEquipmentVOByCAId(param);
			}

			if(equipment == null){
				equipment = equipmentFacade.getEquipmentVOBySerial(param);
			}

			if (equipment == null) {
				return Response.status(Status.NO_CONTENT).build();
			}

			equipment.setAuth(authenticationFacade.lastAuthenticationEquipment(equipment.getSerial()));
			equipment.setUser(userFacade.findVO(equipment.getUserId()));

			rest = mapper.writeValueAsString(equipment);
			return Response.status(Status.OK).entity(rest).build();

		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@DELETE
	@Path("/subscriber/{subscriberId}/equipment/{serial}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeEquipment(@PathParam("subscriberId") String subscriberId, @PathParam("serial") String serial)
			throws RestException {

		try {

			IpvodUser ipvodUser = validateSubscriber(subscriberId);

			if (StringUtils.isBlank(serial)) {
				throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS,
						"Erro ao validar Equipamento, valor Serial necessário.", Status.BAD_REQUEST);
			}

			IpvodEquipment ipvodEquipment = equipmentFacade.findBySerial(serial);

			if (ipvodEquipment == null) {
				throw new RestException(3, "Equipamento não encontrado.",
						Status.BAD_REQUEST);
			}

			Hibernate.initialize(ipvodEquipment.getIpvodPurchases());
			Hibernate.initialize(ipvodEquipment.getIpvodSessions());

			IpvodEquipment flag = null;

			if (ipvodUser.getIpvodEquipments() != null && !ipvodUser.getIpvodEquipments().isEmpty()) {

				for (IpvodEquipment equipment : ipvodUser.getIpvodEquipments()) {

					if (ipvodEquipment.getSerial().equals(equipment.getSerial())) {
						flag = equipment;
						break;
					}

				}

			} else {
				throw new RestException(3, "Não há equipamentos cadastrados.", Status.BAD_REQUEST);
			}

			if (flag != null) {
				ipvodUser.getIpvodPackages().remove(flag);
			} else {
				throw new RestException(3, "Equipamento com serial " + serial
						+ " não esta cadastrado para o usuário " + subscriberId + ".", Status.NOT_FOUND);
			}

			// Verifica se há compras que ainda não foram faturadas
			for (IpvodPurchase purchase : ipvodEquipment.getIpvodPurchases()) {

				if (!purchase.getBilled()) {
					throw new RestException(RestCodeError.PENDING_BILLING, "Equipamento com serial " + serial
							+ " não pode ser removido.", Status.CONFLICT);
				}

			}

			sessionFacade.delete(ipvodEquipment.getIpvodSessions());
			purchaseFacade.delete(ipvodEquipment.getIpvodPurchases());
			authenticationFacade.delete(ipvodEquipment.getIpvodAuthentications());
			equipmentFacade.delete(ipvodEquipment);

			return Response.status(Status.ACCEPTED).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;
		}

	}

	@PermitAll
	@GET
	@Path("/subscriber/{subscriberId}/purchases")
	@Produces("application/json; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listPurchaseByRangeDaysCustomer(@PathParam("subscriberId") String subscriberId,
			@QueryParam("start") String playTimeStart, @QueryParam("end") String playTimeEnd) throws RestException {

		try {

			validateSubscriber(subscriberId);
			dateValidationExecution(playTimeStart, playTimeEnd);

			String rest = "";

			List<PurchaseVO> list = purchaseFacade.findPurchaseExecutions(
					subscriberId,
					StringUtils.isNotBlank(playTimeStart) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(playTimeStart
							+ " 00:00:00") : null, StringUtils.isNotBlank(playTimeEnd) ? new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").parse(playTimeEnd + " 23:59:59") : null);

			doRental(list);

			rest = mapper.writeValueAsString(list);

			return Response.status(Status.OK).entity(rest).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/subscriber/{subscriberId}/executions")
	@Produces("application/json; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listExecutions(@PathParam("subscriberId") String subscriberId,
			@QueryParam("start") String playTimeStart, @QueryParam("end") String playTimeEnd) throws RestException {

		try {

			validateSubscriber(subscriberId);
			dateValidationExecution(playTimeStart, playTimeEnd);

			String rest = "";

			try {
				List<ExecutionVO> list = sessionFacade.findSubscriberExecutions(
						subscriberId,
						StringUtils.isNotBlank(playTimeStart) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(playTimeStart + " 00:00:00") : null,
						StringUtils.isNotBlank(playTimeEnd) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(playTimeEnd
								+ " 23:59:59") : null);
				rest = mapper.writeValueAsString(list);
			} catch (Exception e) {
				throw new RestException(e);
			}

			return Response.status(Status.OK).entity(rest).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;
		}

	}

	@PermitAll
	@GET
	@Path("/executions")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listTopExecutions(@QueryParam("top") Integer top) throws RestException {

		try {

			List<TopExecutionVO> list = sessionFacade.listTopExecutions(top);
			String rest = mapper.writeValueAsString(list);
			return Response.status(Status.OK).entity(rest).build();

		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@PermitAll
	@GET
	@Path("/subscriber/{subscriberId}/transactions")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listTransactions(@PathParam("subscriberId") String subscriberId,
			@QueryParam("start") String playTimeStart, @QueryParam("end") String playTimeEnd) throws RestException {

		try {

			validateSubscriber(subscriberId);
			dateValidationExecution(playTimeStart, playTimeEnd);

			String rest = "";

			List<TransactionVO> list = sessionFacade.findTransactions(
					subscriberId,
					StringUtils.isNotBlank(playTimeStart) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(playTimeStart
							+ " 00:00:00") : null, StringUtils.isNotBlank(playTimeEnd) ? new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").parse(playTimeEnd + " 23:59:59") : null);

			rest = mapper.writeValueAsString(list);

			return Response.status(Status.OK).entity(rest).build();

		} catch (RestException re) {

			if (RestCodeError.ERRO_SUBSCRIBER.getCode().equals(re.getCodigoErro())) {
				re.setStatus(Status.NOT_FOUND);
			}

			throw re;

		} catch (ParseException e) {
			e.printStackTrace();
			throw new RestException(e);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new RestException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RestException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RestException(e);
		}

	}

	@PermitAll
	@GET
	@Path("/testParser")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response testParser() throws RestException {
		User user = new User();

		List<Equipment> equipments = new ArrayList<Equipment>();
		Equipment equipment = new Equipment();

		equipment.setSerial("SN1234567890");
		equipment.setCardId("CARDID");

		equipments.add(equipment);

		user.setSubscriberId("USER-012345");
		user.setEquipments(equipments);

		List<Product> products = new ArrayList<Product>();
		Product product = new Product();

		product.setId("1");
		product.setName("Pacote Telecine Play");

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("opt01", "opt01");

		product.setOptions(options);
		products.add(product);

		user.setProducts(products);
		user.setServiceRegion("SUL_PR");
		user.setSubscriberId("teste");

		String rest = "";
		try {
			rest = mapper.writeValueAsString(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(200).entity(rest).build();
	}

	private void voIsNull(ITConnectionVO vo) throws RestException {

		if (vo == null) {

			String msg = null;

			if (User.class.isInstance(vo)) {
				msg = "Erro ao validar Subscriber.";
			} else if (Product.class.isInstance(vo)) {
				msg = "Erro ao validar Produto.";
			} else if (Equipment.class.isInstance(vo)) {
				msg = "Erro ao validar Equipamento.";
			}

			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, msg, Status.BAD_REQUEST);

		}

	}

	private IpvodUser validateSubscriber(String subscriberId) throws RestException {

		if (StringUtils.isBlank(subscriberId)) {
			throw new RestException(8, "Erro ao validar Subscriber, valor ID necessário.", Status.BAD_REQUEST);
		}

		IpvodUser user = userFacade.findUserBySubscriberID(subscriberId);

		if (user == null) {
			throw new RestException(8, "Subscriber com ID " + subscriberId + " não foi encontrado.", Status.NO_CONTENT);
		}

		return user;

	}

	private void dateValidationExecution(String playTimeStart, String playTimeEnd) throws RestException {
		Pattern pattern = Pattern.compile("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$");

		if (StringUtils.isNotBlank(playTimeStart)) {
			if (!pattern.matcher(playTimeStart).matches()) {
				throw new RestException(RestCodeError.FORMATO_INCOMPATIVEL, "Formato de data incompatível. [yyyy-MM-dd]",
						Status.BAD_REQUEST);
			}
		}

		if (StringUtils.isNotBlank(playTimeEnd)) {
			if (!pattern.matcher(playTimeEnd).matches()) {
				throw new RestException(RestCodeError.FORMATO_INCOMPATIVEL, "Formato de data incompatível. [yyyy-MM-dd]",
						Status.BAD_REQUEST);
			}
		}
	}

	/**
	 * Recupera o registro da ultima autenticação para cada equipamento do
	 * usuário.
	 * 
	 * @param dataUser
	 */
	private void lastAuthenticationEquipmentSubscriber(User dataUser) {

		for (Equipment equipment : dataUser.getEquipments()) {
			AuthVO vo = authenticationFacade.lastAuthenticationEquipment(equipment.getSerial());
			equipment.setAuth(vo);
		}

	}

	private List<IpvodPackage> validateProducts(List<Product> products) throws RestException {

		List<IpvodPackage> packages = new ArrayList<IpvodPackage>();

		if (products == null || products.isEmpty()) {
			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Erro ao validar Produto, valor ID necessário.", Status.BAD_REQUEST);
		} else {
			for (Product product : products) {
				packages.add(validateProduct(product));
			}
		}

		return packages;
	}

	private List<IpvodEquipment> validateEquipments(String subscriberId, List<Equipment> equipments) throws RestException {

		List<IpvodEquipment> ipvodEquipments = new ArrayList<IpvodEquipment>();

		if (equipments == null || equipments.isEmpty()) {
			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Erro ao validar Equipamento. Valores [serial, cardId, mainKey, cas, type] são obrigatórios.", Status.BAD_REQUEST);
		} else {
			for (Equipment equipment : equipments) {
				ipvodEquipments.add(validateEquipment(subscriberId, equipment));
			}
		}

		return ipvodEquipments;
	}

	private IpvodPackage validateProduct(Product product) throws RestException {

		voIsNull(product);

		if (StringUtils.isBlank(product.getId())) {
			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Erro ao validar Produto, valor ID necessário.",
					Status.BAD_REQUEST);
		}

		IpvodPackage ipvodPackage = packageFacade.findByOtherId(product.getId());

		if (ipvodPackage == null) {
			throw new RestException(RestCodeError.RELACIONAMENTO_INEXISTENTE, "Produto com ID " + product.getId()
					+ " não foi encontrado.", Status.BAD_REQUEST);
		}

		return ipvodPackage;
	}

	private IpvodEquipment validateEquipment(String subscriberId, Equipment equipment) throws RestException {

		voIsNull(equipment);
		IpvodEquipment e = equipmentFacade.findBySerial(equipment.getSerial());
		if(e != null && !e.getIpvodUser().getCrmCustomerId().equals(subscriberId)){
			StringBuilder sb = new StringBuilder("O equipamento de serial ")
			.append(equipment.getSerial())
			.append(" e/ou cas ")
			.append(equipment.getCas())
			.append(" encontra-se ativo no sistema para o subscriber com ID ")
			.append(e.getIpvodUser().getCrmCustomerId())
			.append(" .");
			throw new RestException(RestCodeError.EQUIPAMENTO_ATIVO, String.valueOf(sb), Status.CONFLICT);
		}

		if (StringUtils.isBlank(equipment.getSerial()) || StringUtils.isBlank(equipment.getCardId()) || StringUtils.isBlank(equipment.getMainKey()) || StringUtils.isBlank(equipment.getCas()) || (equipment.getType() == null)) {
			throw new RestException(RestCodeError.CAMPOS_OBRIGATORIOS, "Erro ao validar Equipamento. Valores [serial, cardId, mainKey, cas, type] são obrigatórios.",
					Status.BAD_REQUEST);
		}

		if (equipmentFacade.isActiveBySerial(equipment.getSerial()) || (equipmentFacade.getEquipmentVOByCAId(equipment.getCas())) != null) {
			StringBuilder sb = new StringBuilder("O equipamento de serial ")
			.append(equipment.getSerial())
			.append(" e/ou cas ")
			.append(equipment.getCas())
			.append(" encontra-se ativo no sistema.");
			throw new RestException(RestCodeError.EQUIPAMENTO_ATIVO, String.valueOf(sb), Status.NOT_MODIFIED);
		}

		IpvodEquipment ipvodEquipment = new IpvodEquipment();
		ipvodEquipment.setSerial(equipment.getSerial());
		ipvodEquipment.setAuthInfo(equipment.getCardId());
		ipvodEquipment.setMainKey(equipment.getMainKey());
		ipvodEquipment.setCas(equipment.getCas());
		ipvodEquipment.setIpvodEquipmentType(equipmentTypeFacade.find(equipment.getType()));

		return ipvodEquipment;
	}

	private List<IpvodPackage> containsProducts(IpvodUser ipvodUser, List<IpvodPackage> packagesToVerify)
			throws RestException {

		List<IpvodPackage> list = new ArrayList<IpvodPackage>();

		if (ipvodUser.getIpvodPackages() != null) {
			for (IpvodPackage packageUser : ipvodUser.getIpvodPackages()) {
				for (IpvodPackage packageToVerify : packagesToVerify) {
					if (packageToVerify.getOtherId().equals(packageUser.getOtherId())) {
						list.add(packageToVerify);
//						 throw new RestException(RestCodeError.OBJETO_DUPLICADO, "Produto com ID " + packageToVerify.getOtherId() + " foi enviado em duplicidade para cadastro.", Status.NOT_MODIFIED);
					}
				}
			}
		}

		packagesToVerify.removeAll(list);
		
		if(packagesToVerify.isEmpty()){
			throw new RestException(RestCodeError.OBJETO_DUPLICADO, "", Status.NOT_MODIFIED);
		}

		return packagesToVerify;

	}

	private void containsEquipments(IpvodUser ipvodUser, List<IpvodEquipment> equipmentsToVerify) throws RestException {

		if (ipvodUser.getIpvodEquipments() != null) {
			for (IpvodEquipment equipmentUser : ipvodUser.getIpvodEquipments()) {
				for (IpvodEquipment equipmentToVerify : equipmentsToVerify) {
					if (equipmentToVerify.getSerial().equals(equipmentUser.getSerial())) {
						throw new RestException(RestCodeError.OBJETO_DUPLICADO, "Equipamento com Serial " + equipmentToVerify.getSerial() + " foi enviado em duplicidade para cadastro.", Status.OK);
					}
				}
			}
		}

	}

	private void doRental(List<PurchaseVO> list) throws ParseException {
		for (PurchaseVO vo : list) {

			if (StringUtils.isNotBlank(vo.getBuyTime())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getBuyTime()));
				vo.setRentalStart(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				vo.setRentalEnd(calendar.getTime());
			}

		}
	}

	private void updateFields(IpvodUser ipvodUser, User user) throws RestException {

		updateFieldServiceRegion(ipvodUser, user.getServiceRegion());

	}

	private void updateFieldServiceRegion(IpvodUser ipvodUser, String serviceRegion) throws RestException {

		if (StringUtils.isNotEmpty(serviceRegion)) {

			if (serviceRegion.equals(ipvodUser.getServiceRegion())) {
				throw new RestException(RestCodeError.ERRO_UPDATE_SUBSCRIBER, "Subscriber com ID "
						+ ipvodUser.getCrmCustomerId() + " já encontra-se registrado com o Service Region " + serviceRegion,
						Status.OK);
			} else {
				ipvodUser.setServiceRegion(serviceRegion);
				userFacade.update(ipvodUser);
			}

		} else {
			throw new RestException(RestCodeError.ERRO_UPDATE_SUBSCRIBER,
					"O valor submetido para atualização do Service Region não pode ser nulo", Status.OK);
		}

	}

}
