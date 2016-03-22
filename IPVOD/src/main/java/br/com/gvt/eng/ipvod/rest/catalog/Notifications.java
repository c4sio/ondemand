package br.com.gvt.eng.ipvod.rest.catalog;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.dao.ContentProviderDAO;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodNotification;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.vod.facade.AuthenticationSystemFacade;
import br.com.gvt.vod.facade.NotificationFacade;

@Stateless
@Path("/notification")
public class Notifications {
	
	@EJB
	AuthenticationSystemFacade authenticationSystemFacade;
	
	@EJB
	NotificationFacade notificationFacade;

	@EJB
	ContentProviderDAO contentProvider;
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_VOC})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getNotifications(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token) throws RestException {
		IpvodAuthenticationSystem auth = authenticationSystemFacade.findByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		
		IpvodUserSystem user = auth.getIpvodUserSys();
		List<IpvodNotification> notificationList = null;
		if (user.getRole().equals(IpvodConstants.ROLE_OPERADORA)) {
			notificationList = notificationFacade.findByContentProvider(user);
		} else {
			notificationList = notificationFacade.findByRole(user.getRole());
		}

		return Response.ok(notificationList).build();
	}
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_VOC})
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getNotification(@PathParam("id") Long id) throws RestException {
		IpvodNotification notification = notificationFacade.findById(id);

		return Response.ok(notification).build();
	}
	@PermitAll
	@POST
	@Produces("application/json; charset=UTF-8")
	public Response saveNotification(IpvodNotification notification, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token) throws RestException {
		notificationFacade.saveNotification(notification);
		return Response.ok().build();
	} 
	
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_VOC})
	@DELETE
	@Produces("application/json; charset=UTF-8")
	@Path("/{id}")
	public Response deleteNotification(@PathParam("id") Long notificationId, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token) throws RestException {
		IpvodNotification ipvodNotification = notificationFacade.findById(notificationId);
		if (ipvodNotification == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("ipvodNotification");
			throw exc;
		}
		notificationFacade.delete(ipvodNotification );
		return Response.status(Status.OK).build();
	}
	
	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/email/{role}")
	public Response testSendEmail(@PathParam("role") String role) throws RestException {
		notificationFacade.saveNotification(role, "TESTE DE ENVIO DE EMAIL E NOTIFICATION", "ENVIADO COM SUCESSO <BR/>TOBIGADO");
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@PermitAll
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/email/{role}/provider/{providerId}")
	public Response testSendEmail2(@PathParam("role") String role, @PathParam("providerId") Long providerId) throws RestException {
		IpvodContentProvider provider = new IpvodContentProvider();
		provider.setContentProviderId(providerId);
		notificationFacade.saveNotification(provider , role, "TESTE DE ENVIO DE EMAIL E NOTIFICATION", "ENVIADO COM SUCESSO <BR/>TOBIGADO");
		return Response.status(Status.NOT_FOUND).build();
	}
}