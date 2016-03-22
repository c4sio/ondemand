package br.com.gvt.eng.ipvod.rest.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;

/**
 * This interceptor verify the access permissions for a user based on username
 * and password provided in request
 * */

@Provider
@ServerInterceptor
@Stateless
public class SecurityInterceptor implements PreProcessInterceptor {

	private ApplicationContext ctx;

	private SessionFactory sessionFactory;

	private static final ServerResponse ACCESS_DENIED = new ServerResponse(
			"{\"errorMessage\":\"accessDenied\"}", 401, new Headers<Object>());

	private static final ServerResponse TIMEOUT = new ServerResponse(
			"{\"errorMessage\":\"timeout\"}", 408, new Headers<Object>());

	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse(
			"{\"errorMessage\":\"accessForbidden\"}", 403,
			new Headers<Object>());

	@SuppressWarnings("unused")
	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			"INTERNAL SERVER ERROR", 500, new Headers<Object>());

	@Override
	public ServerResponse preProcess(HttpRequest request,
			ResourceMethod methodInvoked) throws Failure,
			WebApplicationException {

		Method method = methodInvoked.getMethod();

		if (method.isAnnotationPresent(PermitAll.class)) {
			return null;
		}
		if (method.isAnnotationPresent(DenyAll.class)) {
			return ACCESS_FORBIDDEN;
		}
		
		try {
			System.out.println("SecurityInterceptor: " + method.getName());
			if (ctx == null) {
				String url = "classpath*:/spring-config.xml";
				ctx = new ClassPathXmlApplicationContext(url);
			}

			if (sessionFactory == null) {
				sessionFactory = (SessionFactory) ctx
						.getBean("mySessionFactory");
			}

			// VALIDAÇÃO TOKEN LOGADO
			HttpHeaders headers = request.getHttpHeaders();
			List<String> tokensSTB = headers
					.getRequestHeader(IpvodConstants.AUTHORIZATION_PROPERTY_STB);

			List<String> tokensCMS = headers
					.getRequestHeader(IpvodConstants.AUTHORIZATION_PROPERTY_CMS);
			
			String token = "";
			// TOKEN VINDA DO STB
			if (tokensSTB != null && !tokensSTB.isEmpty()
					&& !tokensSTB.get(0).isEmpty()) {
				System.out.println("SecurityInterceptor: VALIDATE STB");
				token = tokensSTB.get(0);
				return validateSTBSession(token, method);
			} else
			// TOKEN VINDA DO CMS
			if (tokensCMS != null && !tokensCMS.isEmpty()
					&& !tokensCMS.get(0).isEmpty()) {
				System.out.println("SecurityInterceptor: VALIDATE CMS");
				token = tokensCMS.get(0);
				return validateCMSSession(token, method);
			} else {
				return ACCESS_DENIED;
			}

		} catch (Exception e) {
			System.out.println("Error SecurityInterceptor: " + e.getMessage());
			e.printStackTrace();
			return new ServerResponse(e.getMessage(), 500,
					new Headers<Object>());
		} 
	}

	/**
	 * @param token
	 * @param expirationDate 
	 * @param role 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private ServerResponse validateCMSSession(String token, Method method) {
		Session session = null;
		try {
			System.out.println("SecurityInterceptor: find Token CMS - INICIO");
			session = sessionFactory.openSession();
			List<IpvodAuthenticationSystem> response = session
					.getNamedQuery(IpvodAuthenticationSystem.VERIFY_TOKEN)
					.setParameter("token", token).list();
			System.out.println("SecurityInterceptor: find Token CMS - FIM");
			if (!response.isEmpty()) {
				IpvodAuthenticationSystem auth = response.get(0);
				String role = auth.getIpvodUserSys().getRole();
				System.out
						.println("SecurityInterceptor: find Token CMS SUCCESS");
				return validateAuth(method, role, null);
			} else {
				System.out.println("SecurityInterceptor: find Token CMS ERROR");
				return ACCESS_DENIED;
			}
		} catch (Exception e) {
			System.out.println("Error validateCMSSession: " + e.getMessage());
			e.printStackTrace();
			return ACCESS_DENIED;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
				System.out
						.println("SecurityInterceptor - validateCMSSession: close session");
			} else {
				System.out.println("SecurityInterceptor - validateCMSSession: session is null");
			}
		}
	}


	/**
	 * @param token
	 * @param expirationDate 
	 * @param role 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private ServerResponse validateSTBSession(String token, Method method) {
		Session session = null;
		try {
			System.out.println("SecurityInterceptor: find Token STB - Inicio");
			session = sessionFactory.openSession();
			List<IpvodAuthentication> response = session
					.getNamedQuery(IpvodAuthentication.FIND_AUTH_BY_TOKEN)
					.setParameter("token", token).list();
			System.out.println("SecurityInterceptor: find Token STB - Fim");

			if (!response.isEmpty()) {
				IpvodAuthentication auth = response.get(0);
				Date expirationDate = auth.getExpirationDate();
				String role = IpvodConstants.ROLE_STB;
				System.out
						.println("SecurityInterceptor: find Token STB SUCCESS");
				return validateAuth(method, role, expirationDate);
			} else {
				System.out.println("SecurityInterceptor: find Token STB ERROR");
				return ACCESS_DENIED;
			}
		} catch (Exception e) {
			System.out.println("Error validateSTBSession: " + e.getMessage());
			e.printStackTrace();
			return ACCESS_DENIED;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
				System.out
						.println("SecurityInterceptor - validateSTBSession: close session");
			} else {
				System.out.println("SecurityInterceptor - preProcess: session is null");
			}
		}
	}

	private ServerResponse validateAuth(Method method, String role, Date expirationDate) {
		// TOKEN + USUARIO NÃO ENCONTRADO
		if ("".equals(role)) {
			return ACCESS_DENIED;
		}

		// VALIDAÇÃO ROLE TOKEN LOGADO
		if (!roleAllowed(method, role)) {
			return ACCESS_DENIED;
		}

		// VALIDAÇÃO TIMEOUT
		if (isTimedOut(expirationDate)) {
			return TIMEOUT;
		}
		return null;
	}
	
	/**
	 * @param expirationDate 
	 * @return boolean
	 */
	private boolean isTimedOut(Date expirationDate) {
		if (expirationDate != null) {
			// se a data de expiração for depois da data atual
			if (expirationDate.before(new Date())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param method
	 * @param authRole
	 * @return boolean
	 */
	private boolean roleAllowed(Method method, String authRole) {
		if (method.isAnnotationPresent(RolesAllowed.class)) {
			String[] rolesAllowed = method.getAnnotation(RolesAllowed.class)
					.value();
			for (String role : rolesAllowed) {
				if (role.equals(authRole)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
}