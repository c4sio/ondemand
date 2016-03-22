package br.com.gvt.eng.ipvod.rest.catalog;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodTwitterAuth;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.TwitterAuthFacade;

@Stateless
@Path("/twitter")
public class TwitterAuth {
	private final String OAUTH_CONSUMER_KEY = "6lqyxaZHPvXb2U3IFAm1tM8Hn";
	private final String OAUTH_CONSUMER_KEY_SECRET = "iIqjH8nDXo8XE3jsOFOFr5ohVVwOzDhNxg6JzVk56pO42rH5li";
	
	@EJB
	private TwitterAuthFacade twitterAuthFacade;
	
	@EJB
	private AuthenticationFacade authenticationFacade;
	
	@PermitAll
	@GET
	@Path("/auth")
	@Produces("application/json; charset=UTF-8")
	public Response getAuthRequest(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException, TwitterException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		IpvodTwitterAuth ipvodTwitterAuth = twitterAuthFacade.getByUser(auth.getIpvodUser());
		if (ipvodTwitterAuth == null ) {
			ipvodTwitterAuth = new IpvodTwitterAuth();
			ipvodTwitterAuth.setUser(auth.getIpvodUser());
		} else {
			ipvodTwitterAuth.setoAuthAccessToken(null);
			ipvodTwitterAuth.setoAuthAccessTokenSecret(null);
		}
		
		//get request token
      	ConfigurationBuilder builder = new ConfigurationBuilder();
  	    builder = new ConfigurationBuilder();
  	    builder.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
  	    builder.setOAuthConsumerSecret(OAUTH_CONSUMER_KEY_SECRET);
  	    Twitter twitter = new TwitterFactory(builder.build()).getInstance();

      	RequestToken requestToken = twitter.getOAuthRequestToken();

      	//salva url e token de request
      	ipvodTwitterAuth.setUrl(requestToken.getAuthorizationURL());
      	ipvodTwitterAuth.setRequestToken(requestToken.getToken());
      	ipvodTwitterAuth.setRequestTokenSecret(requestToken.getTokenSecret());
      	
      	ipvodTwitterAuth = twitterAuthFacade.save(ipvodTwitterAuth);
      	
      	//retorna id do twitter auth
      	return Response.status(200).entity(ipvodTwitterAuth.getTwitterAuthId()).build();
	}
	
	private IpvodTwitterAuth getIpvodTwitterAuth(String token) throws RestException {
		IpvodAuthentication auth = authenticationFacade.getAuthByToken(token);
		if (auth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("authorization");
			throw exc;
		}
		return twitterAuthFacade.getByUser(auth.getIpvodUser());
	
	}
	
	private Twitter getTwitter(String token) throws RestException {
		//recupera request token da base
		IpvodTwitterAuth ipvodTwitterAuth = getIpvodTwitterAuth(token);
		if (ipvodTwitterAuth == null) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("Twitter Auth");
			throw exc;
		}
		ConfigurationBuilder builder = new ConfigurationBuilder();
  	    builder = new ConfigurationBuilder();
  	    builder.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
      	builder.setOAuthConsumerSecret(OAUTH_CONSUMER_KEY_SECRET);
		builder.setOAuthAccessToken(ipvodTwitterAuth.getoAuthAccessToken());
		builder.setOAuthAccessTokenSecret(ipvodTwitterAuth.getoAuthAccessTokenSecret());
      	return new TwitterFactory(builder.build()).getInstance();
  	}
	
	@PermitAll
	@GET
	@Path("/auth/pin/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getTwitterPinAuthURL(@PathParam("id") Long id) throws RestException, TwitterException {
		IpvodTwitterAuth twitterAuth = twitterAuthFacade.get(id);
		return Response.status(200).entity(twitterAuth.getUrl()).build();
	}

	@PermitAll
	@POST
	@Path("/auth/pin/{pin}")
	@Produces("application/json; charset=UTF-8")
	public Response getAuth(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token, @PathParam("pin") String pin) throws RestException, TwitterException {
		//recupera request token da base
		IpvodTwitterAuth ipvodTwitterAuth = getIpvodTwitterAuth(token);
		
		//prepara chamada pro twitter
		ConfigurationBuilder builder = new ConfigurationBuilder();
	    builder = new ConfigurationBuilder();
	    builder.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
	  	builder.setOAuthConsumerSecret(OAUTH_CONSUMER_KEY_SECRET);
	  	Twitter twitter = new TwitterFactory(builder.build()).getInstance();
	  	RequestToken requestToken = new RequestToken(ipvodTwitterAuth.getRequestToken(), ipvodTwitterAuth.getRequestTokenSecret());

	  	//valida o pin no twitter
	  	AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);

	  	//salva access token na base
	  	ipvodTwitterAuth.setoAuthAccessToken(accessToken.getToken());
	  	ipvodTwitterAuth.setoAuthAccessTokenSecret(accessToken.getTokenSecret());
	  	twitterAuthFacade.update(ipvodTwitterAuth);
	  	
	  	//retorna screenName
      	return Response.status(200).build();
	}

	@PermitAll
	@GET
	@Path("/user")
	@Produces("application/json; charset=UTF-8")
	public Response getSettings(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException, TwitterException {
      	Twitter twitter = getTwitter(token);
      	return Response.status(200)
      			.entity(twitter.verifyCredentials())
      			.build();
	}

	@PermitAll
	@GET
	@Path("/limits")
	@Produces("application/json; charset=UTF-8")
	public Response getLimits(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException, TwitterException {
      	Twitter twitter = getTwitter(token);
      	return Response.status(200)
      			.entity(twitter.getRateLimitStatus())
      			.build();
	}
	
	@PermitAll
	@POST
	@Path("/status")
	@Produces("application/json; charset=UTF-8")
	public Response updateStatus(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token, String status) throws RestException, TwitterException {
      	Twitter twitter = getTwitter(token);
      	Status resp = twitter.updateStatus(status);
      	return Response.status(200).entity(resp).build();
	}
	
	@PermitAll
	@GET
	@Path("/search")
	@Produces("application/json; charset=UTF-8")
	public Response getHashtagTimeline(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token, @Context UriInfo uriInfo) throws RestException, TwitterException {
		String text = uriInfo.getQueryParameters().getFirst("q");
      	Twitter twitter = getTwitter(token);
		Query query = new Query(text);
		QueryResult resp = twitter.search(query );
      	return Response.status(200).entity(resp).build();
	}
	
	@PermitAll
	@POST
	@Path("/logout")
	@Produces("application/json; charset=UTF-8")
	public Response twitterLogout(@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_STB) String token) throws RestException, TwitterException {
		IpvodTwitterAuth ipvodTwitterAuth = getIpvodTwitterAuth(token);
		if (ipvodTwitterAuth == null ) {
			RestException exc = RestException.getBadRequest();
			exc.getMissingParams().add("Twitter Auth");
			throw exc;
		}
		twitterAuthFacade.delete(ipvodTwitterAuth);
//		Twitter twitter = getTwitter(ipvodTwitterAuth);
//		twitter.invalidateOAuth2Token();
		return Response.status(201).build();
	}
}

