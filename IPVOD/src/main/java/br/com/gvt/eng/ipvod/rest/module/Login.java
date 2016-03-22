package br.com.gvt.eng.ipvod.rest.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.util.StringUtils;

import br.com.gvt.eng.ipvod.rest.util.Util;
import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodSystemPasswordRecover;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.eng.vod.util.PropertiesConfig;
import br.com.gvt.eng.vod.util.SendEmail;
import br.com.gvt.vod.facade.AuthenticationSystemFacade;
import br.com.gvt.vod.facade.UserSystemFacade;

@Stateless
@Path("/user")
public class Login {

	@EJB
	private UserSystemFacade userSystemFacade;
	
	@EJB
	private AuthenticationSystemFacade authenticationSystemFacade;

	@PermitAll
	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response login(IpvodUserSystem ipvodUserSystem) throws RestException {
		if (StringUtils.isEmpty(ipvodUserSystem.getUsername()) || StringUtils.isEmpty(ipvodUserSystem.getPassword())){
			throw new RestException(1, "Login/Senha não preenchido.");
		}
		try {
			IpvodUserSystem userLogado = userSystemFacade.login(ipvodUserSystem.getUsername(), ipvodUserSystem.getPassword());
			
			IpvodAuthenticationSystem auth = authenticationSystemFacade.login(userLogado);
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("token", auth.getToken());
			returnMap.put("user", userLogado);
			return Response.status(200).entity(returnMap).build();
		} catch (IllegalArgumentException e) {
			throw new RestException(1, e.getMessage());
		} 
	}
	
	@PermitAll
	@POST
	@Path("/logout")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response logout(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) {
		try {
			IpvodAuthenticationSystem auth = new IpvodAuthenticationSystem();
			auth.setToken(token);
			authenticationSystemFacade.logout(auth);
			return Response.status(200).build();
		} catch (Exception e) {
			return Response.status(450).build();
		}
	}
	
	@RolesAllowed(IpvodConstants.ROLE_ADMIN)
	@POST
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response createUser(IpvodUserSystem ipvodUserSystem) throws RestException {
		if ("GVT".equals(ipvodUserSystem.getRegisterType())) {
			if (StringUtils.isEmpty(ipvodUserSystem.getUsername())) {
				throw new RestException(1, "Campos obrigatórios não preenchidos.");
			}
		} else  if ("ADM".equals(ipvodUserSystem.getRegisterType())){
			if (StringUtils.isEmpty(ipvodUserSystem.getPassword()) ||
					StringUtils.isEmpty(ipvodUserSystem.getRole()) ||
					StringUtils.isEmpty(ipvodUserSystem.getUsername())){
				throw new RestException(1, "Campos obrigatórios não preenchidos.");
			}
		} else {
			if (StringUtils.isEmpty(ipvodUserSystem.getEmail()) ||
					StringUtils.isEmpty(ipvodUserSystem.getPassword()) ||
					StringUtils.isEmpty(ipvodUserSystem.getRole()) ||
					StringUtils.isEmpty(ipvodUserSystem.getUsername())){
				throw new RestException(1, "Campos obrigatórios não preenchidos.");
			}
		}

		try {
			if (ipvodUserSystem.getUserSysId() != null && ipvodUserSystem.getUserSysId().doubleValue() > 0) {
				userSystemFacade.update(ipvodUserSystem);
			} else {
				userSystemFacade.save(ipvodUserSystem);
			}
		} catch (Exception e) {
			if (e.getCause().getCause() instanceof ConstraintViolationException) {
				String constraint = ((ConstraintViolationException)e.getCause().getCause()).getConstraintName();
				
				if (constraint.contains("UNIQUE_USERNAME")){
					throw new RestException(1, "Username já cadastrado.");
				} 
				if (constraint.contains("UNIQUE_USER_EMAIL")){
					throw new RestException(1, "E-mail já cadastrado.");
				}
			} else {
				throw new RestException(1, "Erro ao salvar Usuário.");
			}
		}
		return Response.status(200).build();
	}

	@PermitAll
	@RolesAllowed(IpvodConstants.ROLE_ADMIN)
	@POST
	@Path("/{id}")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Response updateuser(@PathParam("id") Long id, IpvodUserSystem ipvodUserSystem) {
		userSystemFacade.update(ipvodUserSystem);
		return Response.status(200).build();
	}

	@PermitAll
	@RolesAllowed(IpvodConstants.ROLE_ADMIN)
	@POST
	@Path("/{id}/activate")
	public Response activateUser(@PathParam("id") Long id) throws RestException {
		IpvodUserSystem ipvodUserSystem = userSystemFacade.find(id);
		if (ipvodUserSystem == null) {
			throw new RestException(1, "Usuário não encontrado");
		}
		ipvodUserSystem.setActive(true);
		userSystemFacade.update(ipvodUserSystem);
		return Response.status(200).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN})
	@POST
	@Path("/{id}/deactivate")
	public Response deactivateUser(@PathParam("id") Long id) throws RestException {
		IpvodUserSystem ipvodUserSystem = userSystemFacade.find(id);
		if (ipvodUserSystem == null) {
			throw new RestException(1, "Usuário não encontrado");
		}
		ipvodUserSystem.setActive(false);
		userSystemFacade.update(ipvodUserSystem);
		return Response.status(200).build();
	}
	
	@PermitAll
	@RolesAllowed(IpvodConstants.ROLE_ADMIN)
	@DELETE
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response deleteUser(@PathParam("id") Long id) throws RestException {
		
		if (id == null || id <= 0) {
			throw new RestException(1,
					"Parâmetro obrigatório nulo ou inválido. [categoryId = "
							+ id + "]");
		}
		IpvodUserSystem ipvodUserSystem = userSystemFacade.find(id);
		
		if (ipvodUserSystem == null) {
			throw new RestException(2,
					"Registro não encontrado na base.");
		}
		
		userSystemFacade.delete(ipvodUserSystem );
		return Response.status(200).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN})
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getUser(@PathParam("id") Long id) {
		IpvodUserSystem user = userSystemFacade.find(id);
		return Response.status(200).entity(convertUserSys(user)).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getUserList() {
		 List<IpvodUserSystem> users = userSystemFacade.findAll();
		return Response.status(200).entity(convertUserSysList(users)).build();
	}
	
	@PermitAll
	@GET
	@Path("/recoverPassword/{email}")
	@Produces("application/json; charset=UTF-8")
	public Response recoverPassword(@PathParam("email") String email) throws RestException {
		if (StringUtils.isEmpty(email)) {
			throw new RestException(1, "E-mail não preenchido.");
		}
		IpvodUserSystem returnedUser = userSystemFacade.getUserByEmail(email);
		if (returnedUser != null) {
			try {
				IpvodSystemPasswordRecover ipvodSystemPasswordRecover = new IpvodSystemPasswordRecover(returnedUser, Util.generatePasswordRecoverCode());
				userSystemFacade.savePasswordRecoverCode(ipvodSystemPasswordRecover);
				
				SendEmail sendEmail = new SendEmail();
				String url = PropertiesConfig.getString("cms.pwdRec");
				String body = "<p style=\"color: rgb(57, 57, 57);font-family: sans-serif;font-size: 13px;\">" +
						"Para reiniciar sua senha clique " +
						"<a href=\""+url +"?pwRec="+ipvodSystemPasswordRecover.getPasswordRecoverCode() +"\">aqui</a>" +
					 "</p>";
				sendEmail.sendEmail(returnedUser, body, "IPVOD Admin - Recupere sua senha");
			} catch (Exception e) {
				throw new RestException(1, "Erro no envio de e-mail.");
			}
			return Response.status(200).build();
		} else {
			throw new RestException(1, "E-mail não cadastrado.");
		}
	}
	
	@PermitAll
	@POST
	@Path("/restartPassword")
	@Produces("application/json; charset=UTF-8")
	public Response recoverPassword(IpvodSystemPasswordRecover ipvodSysPwdRec) throws RestException {
		if (StringUtils.isEmpty(ipvodSysPwdRec.getPasswordRecoverCode()) 
				|| StringUtils.isEmpty(ipvodSysPwdRec.getIpvodUserSys().getPassword())){
			throw new RestException(1, "Campos obrigatórios não preenchidos."); 
		}
		IpvodUserSystem user = userSystemFacade.getUserByPassswordRecoverCode(ipvodSysPwdRec.getPasswordRecoverCode());
		
		if (user != null) {
			user.setPassword(ipvodSysPwdRec.getIpvodUserSys().getPassword());
			userSystemFacade.save(user);
			
			userSystemFacade.deletePasswordRecoverCode(ipvodSysPwdRec);
			return Response.status(200).build();
		} else {
			throw new RestException(1, "Código inválido.");
		}
	}

	
	private List<IpvodUserSystem> convertUserSysList(List<IpvodUserSystem> list) {
		List<IpvodUserSystem> newList = new ArrayList<IpvodUserSystem>();
		for (IpvodUserSystem user : list) {
			newList.add(convertUserSys(user));
		}
		return newList;
	}
	
	private IpvodUserSystem convertUserSys(IpvodUserSystem ipvodUserSystem) {
		IpvodUserSystem userSys = new IpvodUserSystem();
		userSys.setEmail(ipvodUserSystem.getEmail());
		userSys.setRole(ipvodUserSystem.getRole());
		userSys.setUsername(ipvodUserSystem.getUsername());
		userSys.setUserSysId(ipvodUserSystem.getUserSysId());
		userSys.setActive(ipvodUserSystem.isActive());
		if (ipvodUserSystem.getContentProvider() != null) {
			userSys.setContentProvider(new IpvodContentProvider());
			userSys.getContentProvider().setContentProviderId(ipvodUserSystem.getContentProvider().getContentProviderId());
			userSys.getContentProvider().setProviderId(ipvodUserSystem.getContentProvider().getProviderId());
			userSys.getContentProvider().setProviderName(ipvodUserSystem.getContentProvider().getProviderName());
			userSys.getContentProvider().setProviderRating(ipvodUserSystem.getContentProvider().getProviderRating());
			
		}
		return userSys; 
	}
}
