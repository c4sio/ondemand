package br.com.gvt.eng.ipvod.rest.catalog;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.com.gvt.eng.ipvod.rest.util.FileFunctions;
import br.com.gvt.eng.ipvod.rest.util.ResizeImage;
import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.AssetConverter;
import br.com.gvt.eng.vod.converter.JsonSTB;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.HistoryTypeEnum;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodCategory;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodHistory;
import br.com.gvt.eng.vod.model.IpvodSchedulePublication;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;
import br.com.gvt.eng.vod.util.Filter;
import br.com.gvt.eng.vod.util.FilterRules;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.AssetPackageFacade;
import br.com.gvt.vod.facade.AuthenticationFacade;
import br.com.gvt.vod.facade.AuthenticationSystemFacade;
import br.com.gvt.vod.facade.HistoryFacade;
import br.com.gvt.vod.facade.IngestFacade;
import br.com.gvt.vod.facade.MenuAssetFacade;
import br.com.gvt.vod.facade.MenuFacade;
import br.com.gvt.vod.facade.SchedulePublicationFacade;

import com.google.gson.Gson;

@Stateless
@Path("/asset")
public class Assets {

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private AuthenticationFacade authenticationFacade;

	@EJB
	private AuthenticationSystemFacade authenticationSystemFacade;
	
	@EJB
	private HistoryFacade historyFacade;

	@EJB
	private SchedulePublicationFacade schedulePublicationFacade;
	
	@EJB
	private MenuAssetFacade menuAssetFacade;
	
	@EJB
	private IngestFacade ingestFacade;
	
	@EJB
	private AssetPackageFacade assetPackageFacade;

	@EJB
	private MenuFacade menuFacade;
	
	private AssetConverter assetConverter = new AssetConverter();
	
//	private final String UPLOADED_FILE_PATH = "c:\\img\\";
	private final String UPLOADED_FILE_PATH = "/tmp/upload/";

	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB})
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response listAssets(@Context UriInfo uriInfo, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token ) throws RestException {
		
		if (token != null) {
			return listAssetsCMS(uriInfo, token);
		}
		MultivaluedMap<String, String> search = uriInfo.getQueryParameters();
        if (search != null && !search.isEmpty()) {
			return getAssetComplexQuery(uriInfo);
		}
		List<IpvodAsset> assets = assetFacade.findAll();

		if (assets == null || assets.isEmpty()) {
			throw RestException.getNoContent();
		}
		
		return Response.status(200).entity(assetConverter.toAssetList(assets)).build();
	}

	private Response listAssetsCMS(UriInfo uriInfo, String token) throws RestException {
		IpvodAuthenticationSystem auth = authenticationSystemFacade.findByToken(token);
		if (auth.getIpvodUserSys().getRole().equals(IpvodConstants.ROLE_OPERADORA)) {
			IpvodContentProvider contentProvider = auth.getIpvodUserSys().getContentProvider();

			List<String> strList = uriInfo.getQueryParameters().get(IpvodConstants.URLPARAM_FILTERS);
			List<Filter> filterList = new ArrayList<Filter>();
			if (strList != null) {
				for (String filter2 : strList) {
					try {
						filterList.add(new ObjectMapper().readValue(filter2, Filter.class));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			Filter f = filterList.get(0);
			FilterRules fr = new FilterRules();
			fr.setField("ipvodContentProvider.contentProviderId");
			fr.setData(String.valueOf(contentProvider.getContentProviderId()));
			fr.setOp(FilterRules.OPERATION_EQUAL);
			f.getRules().add(fr);
			List<String> list = new ArrayList<String>();
			list.add(new Gson().toJson(f));
			uriInfo.getQueryParameters().put(IpvodConstants.URLPARAM_FILTERS, list);
		}
		return getAssetComplexQuery(uriInfo);
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB})
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response getAsset(@PathParam("id") Long id, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String cmsToken) throws RestException {
		if (cmsToken != null) {
			return getAssetCMS(id, cmsToken);
		} else {
			IpvodAsset asset = assetFacade.find(id);
			if (asset == null) {
				throw RestException.getNoContent();
			}
			return Response.status(200).entity(assetConverter.toAsset(asset)).build();
		}
	}

	private Response getAssetCMS(Long id, String cmsToken) throws RestException {
		IpvodAsset asset = null;
		Map<String, Object> m = new HashMap<String, Object>();
		try {
			asset = assetFacade.getElasticSearch(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		IpvodSchedulePublication schedule = new IpvodSchedulePublication();
		if (asset == null) {
			asset = assetFacade.find(id);
			if (asset == null) {
				throw RestException.getNoContent();
			}
			m.put("asset", assetConverter.toAssetCMS(asset));
		} else {
			m.put("asset", assetConverter.toAsset(asset));
			schedule = schedulePublicationFacade.getScheduleByAssetId(id);
		}
		m.put("temp", schedule);
		return Response.status(200).entity(m).build();

	}

	/**
	 * _search =
	 * rows = quantidade de linhas
	 * page = numero da pagina
	 * sidx = search index - o campo que vai ser pesquisado
	 * order = asc / desc
	 * '*' = campo referenciado no sidx
	 */
	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC, IpvodConstants.ROLE_OPERADORA, IpvodConstants.ROLE_STB})
	@GET
	@Path("/complex")
	@Produces("application/json; charset=UTF-8")
	public Response getAssetComplexQuery(@Context UriInfo uriInfo) throws RestException {
		try {
			List<IpvodAsset> listAsset = assetFacade.findResultComplexQuery(uriInfo);
			
			Map<String, Object> m = new HashMap<String, Object>();
			String json = (String) JsonSTB.toJsonAssetList(assetConverter.toAssetList(listAsset));
			m.put("list", new Gson().fromJson(json, List.class));
			m.put("count", assetFacade.countResultComplexQuery(uriInfo).intValue());
			return Response.status(200).entity(m).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response updateAsset(@QueryParam("categoryId") Long categoryId, @PathParam("id") Long id, IpvodAsset asset) throws RestException {
		try {
			if (categoryId != null) {
				IpvodCategory category = new IpvodCategory();
				category.setCategoryId(categoryId);
				asset.setIpvodCategory1(category);
			}
			assetFacade.update(asset);
			return Response.status(201).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/updatecategory")
	@Produces("application/json; charset=UTF-8")
	public Response updateCategoryAsset(@QueryParam("categoryId") Long categoryId, @QueryParam("assetId") Long assetId) throws RestException {
		try {

			IpvodAsset asset = assetFacade.find(assetId);

			IpvodCategory category = new IpvodCategory();
			category.setCategoryId(categoryId);
			asset.setIpvodCategory1(category);
			assetFacade.updateNotRevised(asset);

			return Response.status(201).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/updatePartial/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response updatePartialAsset(@PathParam("id") Long assetId, @QueryParam("categoryId") Long categoryId, @QueryParam("billingId") String billingId, @QueryParam("price") Double price, @QueryParam("finishRevision") boolean finishRevision) throws RestException {
		try {

			IpvodAsset asset = assetFacade.find(assetId);
			if (finishRevision == true) {
				assetFacade.update(asset);
				ingestFacade.finishRevision(assetId);
			} else {
				if (categoryId != null) {
					IpvodCategory category = new IpvodCategory();
					category.setCategoryId(categoryId);
					asset.setIpvodCategory1(category);
				}
				if (billingId != null) {
					asset.setBillingID(billingId);
				}
				if (price != null) {
					asset.setPrice(price);
				}
				assetFacade.updateNotRevised(asset);
			}

			return Response.status(201).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}

	@PermitAll
	@GET
	@Path("/word/{word}")
	@Produces("application/json; charset=UTF-8")
	public Response listByInicialWord(@PathParam("word") String word) throws RestException {
		List<IpvodAsset> assets = assetFacade.listAssetByInicialWord(word);

		if (assets == null || assets.isEmpty()) {
			throw RestException.getNoContent();
		}

		return Response.status(200).entity(assetConverter.toAssetJson(assets)).build();
	}

	@PermitAll
	@GET
	@Path("/type/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response listByAssetType(@PathParam("id") Long idAssetType) throws RestException {
		List<IpvodAsset> assets = assetFacade.listAssetType(idAssetType);
		if (assets == null || assets.isEmpty()) {
			throw RestException.getNoContent();
		}
		return Response.status(200).entity(assetConverter.toAssetJson(assets)).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Produces("application/json; charset=UTF-8")
	public Response updateAsset(IpvodAsset ipvodAsset, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token) throws RestException {

		IpvodAuthenticationSystem auth = authenticationSystemFacade.findByToken(token);
		Long userId = auth.getIpvodUserSys().getUserSysId();

		if (auth.getIpvodUserSys().getRole().equals("OPR") && ipvodAsset.getIpvodContentProvider().getContentProviderId() != auth.getIpvodUserSys().getContentProvider().getContentProviderId()) {
			System.out.println(ipvodAsset.getIpvodContentProvider().getContentProviderId() + " != " + auth.getIpvodUserSys().getContentProvider().getContentProviderId());
			RestException.FORBIDDEN.printStackTrace();
			throw RestException.FORBIDDEN;
		}

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		IpvodAsset oldIpvodAsset = assetFacade.find(ipvodAsset.getAssetId());
		Gson gson = new Gson();

		IpvodHistory ipvodHistory = new IpvodHistory();
		ipvodHistory.setChangedAt(new GregorianCalendar());
		ipvodHistory.setType(HistoryTypeEnum.ASSET);

		IpvodUserSystem user = new IpvodUserSystem();

		user.setUserSysId(userId);
		ipvodHistory.setUser(user);
		ipvodHistory.setItemId(ipvodAsset.getAssetId());
		ipvodHistory.setOldValue(gson.toJson(getAssetMap(oldIpvodAsset)));
		IpvodAsset newIpvodAsset = assetFacade.update(ipvodAsset);
		for (IpvodVisualMenu m : ipvodAsset.getIpvodVisualMenus()) {
			if (m.getZindex() == null) {
				IpvodVisualMenuAsset e = new IpvodVisualMenuAsset();
				e.setIpvodAsset(newIpvodAsset);
				e.setIpvodVisualMenu(m);
				e.setZindex(new Long(0));
				menuAssetFacade.save(e);

				IpvodVisualMenu menu = menuFacade.find(m.getMenuId());
				IpvodVisualMenuAsset menuAsset = new IpvodVisualMenuAsset();
				menuAsset.setIpvodAsset(ipvodAsset);
				menu.getIpvodVisualMenuAsset().add(menuAsset);
				assetPackageFacade.insertAssetPackages(menu);
			}
		}
		ipvodHistory.setNewValue(gson.toJson(getAssetMap(newIpvodAsset)));

		historyFacade.save(ipvodHistory);

		if (schedulePublicationFacade.getScheduleByAssetId(ipvodAsset.getAssetId()) != null) {
			cancelTempAsset(ipvodAsset.getAssetId());
		}

		return Response.status(200).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/save/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response saveTempAsset(IpvodSchedulePublication schedule, @HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token, @PathParam("id") Long assetId) throws RestException {

		IpvodAuthenticationSystem auth = authenticationSystemFacade.findByToken(token);
		if (auth == null) {
			throw RestException.BAD_REQUEST;
		}
		schedule.setUser(auth.getIpvodUserSys());
		schedule.setTempDataId(assetId.toString());
		schedule.setData(schedule.getData().replaceAll("\"Vazio\"", "null"));
		schedulePublicationFacade.saveSchedule(schedule);
		assetFacade.saveElasticSearch(assetId, schedule.getData());
		return Response.status(200).build();
	}

	@PermitAll
	@RolesAllowed({IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING, IpvodConstants.ROLE_VOC})
	@POST
	@Path("/cancel/{id}")
	@Produces("application/json; charset=UTF-8")
	public Response cancelTempAsset(@PathParam("id") Long assetId) throws RestException {
		assetFacade.deleteElasticSearch(assetId);
		schedulePublicationFacade.deleteSchedule(assetId);
		return Response.status(200).build();
	}

	private HashMap<Object, Object> getAssetMap(IpvodAsset oldIpvodAsset) {

		HashMap<Object, Object> ipodAssetMap = new HashMap<Object, Object>();

		// ipodAssetMap.put("creationDate", oldIpvodAsset.getCreationDate());
		ipodAssetMap.put("description", oldIpvodAsset.getDescription());
		ipodAssetMap.put("director", oldIpvodAsset.getDirector());
		ipodAssetMap.put("actors", oldIpvodAsset.getActors());
		ipodAssetMap.put("episode", oldIpvodAsset.getEpisode());
		ipodAssetMap.put("billingID", oldIpvodAsset.getBillingID());
		ipodAssetMap.put("episodeName", oldIpvodAsset.getEpisodeName());
		if (oldIpvodAsset.getLicenseWindowStart() != null)
			ipodAssetMap.put("licenseWindowStart", new SimpleDateFormat("dd/MM/yyyy").format(oldIpvodAsset.getLicenseWindowStart()));
		if (oldIpvodAsset.getLicenseWindowEnd() != null)
			ipodAssetMap.put("licenseWindowEnd", new SimpleDateFormat("dd/MM/yyyy").format(oldIpvodAsset.getLicenseWindowEnd()));
		ipodAssetMap.put("price", oldIpvodAsset.getPrice());
		ipodAssetMap.put("releaseYear", oldIpvodAsset.getReleaseYear());
		ipodAssetMap.put("season", oldIpvodAsset.getSeason());
		ipodAssetMap.put("subtitles", oldIpvodAsset.getSubtitles());
		ipodAssetMap.put("languages", oldIpvodAsset.getLanguages());
		ipodAssetMap.put("dubbedLanguage", oldIpvodAsset.getDubbedLanguage());
		ipodAssetMap.put("title", oldIpvodAsset.getTitle());
		ipodAssetMap.put("originalTitle", oldIpvodAsset.getOriginalTitle());
		ipodAssetMap.put("assetInfo", oldIpvodAsset.getAssetInfo());
		ipodAssetMap.put("rating", oldIpvodAsset.getRating());
		ipodAssetMap.put("totalTime", oldIpvodAsset.getTotalTime());
		ipodAssetMap.put("product", oldIpvodAsset.getProduct());
		ipodAssetMap.put("country", oldIpvodAsset.getCountry());
		ipodAssetMap.put("screenFormat", oldIpvodAsset.getScreenFormat());
		ipodAssetMap.put("audioType", oldIpvodAsset.getAudioType());
		ipodAssetMap.put("canResume", oldIpvodAsset.isCanResume());
		ipodAssetMap.put("isHD", oldIpvodAsset.isHD());
		ipodAssetMap.put("isRevised", oldIpvodAsset.getIsRevised());
		ipodAssetMap.put("fileSize", oldIpvodAsset.getFileSize());
		ipodAssetMap.put("checksum", oldIpvodAsset.getChecksum());
		ipodAssetMap.put("bitrate", oldIpvodAsset.getBitrate());
		ipodAssetMap.put("titleAlternative", oldIpvodAsset.getTitleAlternative());

		return ipodAssetMap;
	}

	@PermitAll
	@POST
	@Path("/uploadImg/{assetId}")
	@Consumes("multipart/form-data")
	public Response uploadFile(MultipartFormDataInput input, @PathParam("assetId") String assetId) throws IOException {
		
		
		String fileName = assetId + ".jpg";

		File file = new File(UPLOADED_FILE_PATH);

		if (!file.exists()) {
			System.out.println("crating folder");
			file.mkdirs();
		} else {
			System.out.println("local existe");
		}
		List<InputPart> inputParts = input.getFormDataMap().get("file");

		for (InputPart inputPart : inputParts) {

			try {
//				MultivaluedMap<String, String> header = inputPart.getHeaders();

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				// constructs upload file path
//				fileName = UPLOADED_FILE_PATH + fileName;
				byte[] bytes = IOUtils.toByteArray(inputStream);
				writeFile(bytes, fileName);
				System.out.println("fim write");
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}

		}
		
		//UPDATE DO IPVOD_MEDIA_ASSETX 
		assetFacade.updateMediaAssetImage(assetId);
		System.out.println("Media Asset alterado!");
		return Response.status(200).build();

	}
	
	@PermitAll
	@POST
	@Path("/updateImg/{assetId}")
	public Response uploadFile2(@PathParam("assetId") String assetId) throws IOException {
		assetFacade.updateMediaAssetImage(assetId);
		return Response.status(200).build();
	}
		
	
	private void writeFile(byte[] content, String filename) throws IOException {

		System.out.println("Resizing file");
		File file = new File(filename);

//		if (!file.exists()) {
//			file.createNewFile();
//		} else {
//			file.delete();
//			file.createNewFile();
//		}

		InputStream in = new ByteArrayInputStream(content);
		BufferedImage imBuff = ImageIO.read(in);
		
		ResizeImage resizeImage = new ResizeImage();
		
		BufferedImage scaled = resizeImage.getScaledInstance(imBuff, ResizeImage.DEFAULT_WIDTH, ResizeImage.DEFAULT_HEIGH, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		
		System.out.println("serizeImage.writeJPG");
		resizeImage.writeJPG(scaled, new FileOutputStream(file), 0.85f);
		
		FileFunctions fileFunctions = new FileFunctions();
		try {
			fileFunctions.copyJPGFilesToCluster(file);
		} catch (IOException ioe) {
			System.out.println("erro ao enviar para cluster");
			throw ioe;
		}
	}

}
