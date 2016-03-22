package br.com.gvt.eng.ipvod.rest.integration;

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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.converter.IngestConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAuthenticationSystem;
import br.com.gvt.eng.vod.model.IpvodCheckSumData;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.vo.IngestVO;
import br.com.gvt.vod.facade.AuthenticationSystemFacade;
import br.com.gvt.vod.facade.CheckSumFacade;
import br.com.gvt.vod.facade.IngestFacade;

@Stateless
@Path("/ingest")
public class Ingest {

	@EJB
	private IngestFacade ingestFacade;

	@EJB
	private CheckSumFacade checkSumFacade;

	@EJB
	private AuthenticationSystemFacade authenticationSystemFacade;

	private IngestConverter ingestConverter;

	@PermitAll
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response saveIngest(IpvodIngestStage ingest) throws RestException {
		if (ingest.getId() == null) {
			ingestFacade.save(ingest);

			// Verifica se tem erros de MD5
			if (!ingest.getIpvodCheckSumData().isEmpty()) {
				List<IpvodCheckSumData> listCheckSumData = ingest
						.getIpvodCheckSumData();
				for (IpvodCheckSumData ipvodCheckSumData : listCheckSumData) {
					ingest.setIpvodCheckSumData(null);
					ipvodCheckSumData.setIpvodIngestStage(ingest);
					checkSumFacade.save(ipvodCheckSumData);
				}
			}

			return Response.status(201).entity(ingest).build();
		} else {
			IpvodIngestStage ingestValue = ingestFacade.findIngestById(ingest
					.getId());
			// Verifica se a data foi alterada
			if (ingest.getDtInsert() == null) {
				ingest.setDtInsert(ingestValue.getDtInsert());
			}

			if (ingest.getFtpPath() == null
					|| "".equalsIgnoreCase(ingest.getFtpPath())) {
				ingest.setFtpPath(ingestValue.getFtpPath());
			}

			// Verifica se tem erros de MD5
			if (!ingest.getIpvodCheckSumData().isEmpty()) {
				IpvodCheckSumData checkSumData = null;
				List<IpvodCheckSumData> listCheckSumData = ingest
						.getIpvodCheckSumData();
				for (IpvodCheckSumData ipvodCheckSumData : listCheckSumData) {
					checkSumData = new IpvodCheckSumData();
					checkSumData = checkSumFacade
							.findCheckSumByFileName(ipvodCheckSumData
									.getFileName());

					if (checkSumData != null) {
						checkSumData.setCheckSumResult(ipvodCheckSumData
								.getCheckSumResult());
						checkSumData.setCheckSumSend(ipvodCheckSumData
								.getCheckSumSend());
						checkSumData.setFileName(ipvodCheckSumData
								.getFileName());
						checkSumData.setIpvodIngestStage(ingest);
						checkSumData.setStatusFile(ipvodCheckSumData
								.isStatusFile());
						checkSumFacade.update(checkSumData);
					} else {
						ipvodCheckSumData.setIpvodIngestStage(ingest);
						checkSumFacade.update(ipvodCheckSumData);
					}

				}
			}

			// Atualiza os dados do @IpvodIngestStage
			ingestFacade.update(ingest);
			return Response.status(200).build();
		}
	}

	/**
	 * Busca lista de registros na @IpvodIngestStage pelo assetInfo
	 * 
	 * @param assetInfo
	 * @return List<IpvodIngestStage>
	 * @throws RestException
	 */
	@PermitAll
	@GET
	@Path("/assetInfo/{assetInfo}")
	@Produces("application/json; charset=UTF-8")
	public Response findListIngestByAssetInfo(
			@PathParam("assetInfo") String assetInfo) throws RestException {
		List<IpvodIngestStage> ingests = ingestFacade
				.findListByAssetInfo(assetInfo);
		if (ingests == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200).entity(ingests).build();
	}

	/**
	 * Busca um registro na @IpvodIngestStage
	 * 
	 * @param assetInfo
	 * @return ipvodIngestStage
	 * @throws RestException
	 */
	@PermitAll
	@GET
	@Path("/find/{assetInfo}")
	@Produces("application/json; charset=UTF-8")
	public Response findRegisterIngestByAssetInfo(
			@PathParam("assetInfo") String assetInfo) throws RestException {
		ingestConverter = new IngestConverter();
		IpvodIngestStage ipvodIngestStage = ingestFacade
				.findRegisterByAssetInfo(assetInfo);
		if (ipvodIngestStage == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200)
				.entity(ingestConverter.toIngest(ipvodIngestStage)).build();
	}

	/**
	 * Busca registro na @IpvodIngestStage com ERRO e RESULT igual a ZERO pelo
	 * assetInfo
	 * 
	 * @param assetInfo
	 * @return ipvodIngestStage
	 * @throws RestException
	 */
	@PermitAll
	@GET
	@Path("/{assetInfo}/assetInfo")
	@Produces("application/json; charset=UTF-8")
	public Response findRegisterWithErrorByAssetInfo(
			@PathParam("assetInfo") String assetInfo) throws RestException {
		ingestConverter = new IngestConverter();
		IpvodIngestStage ipvodIngestStage = ingestFacade
				.findRegisterWithErrorByAssetInfo(assetInfo);
		if (ipvodIngestStage == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200)
				.entity(ingestConverter.toIngest(ipvodIngestStage)).build();
	}

	/**
	 * Busca registro na @IpvodIngestStage com ERRO e RESULT igual a ZERO pelo
	 * pathfile
	 * 
	 * @param pathfile
	 * @return ipvodIngestStage
	 * @throws RestException
	 */
	@PermitAll
	@GET
	@Path("/{pathfile}/pathfile")
	@Produces("application/json; charset=UTF-8")
	public Response findByPathFile(@PathParam("pathfile") String pathfile)
			throws RestException {
		ingestConverter = new IngestConverter();
		IpvodIngestStage ipvodIngestStage = ingestFacade
				.findByPathFile(pathfile);
		if (ipvodIngestStage == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200)
				.entity(ingestConverter.toIngest(ipvodIngestStage)).build();
	}

	/**
	 * Busca registros na @IpvodIngestStage pelo stageType
	 * 
	 * @param stageType
	 * @return List<IpvodIngestStage>
	 * @throws RestException
	 */
	@PermitAll
	@GET
	@Path("/type/{stageType:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findDataExecute(@PathParam("stageType") long stageType)
			throws RestException {
		ingestConverter = new IngestConverter();
		// Busca lista de registros para processar.
		List<IpvodIngestStage> ingests = ingestFacade
				.findFilesToExecute(stageType);
		if (ingests == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200)
				.entity(ingestConverter.getIngestList(ingests)).build();
	}

	@PermitAll
	@GET
	@Path("/execution")
	@Produces("application/json; charset=UTF-8")
	public Response findADataIngest(@Context UriInfo uriInfo,
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token)
			throws RestException {
		// Busca lista de registros para processar
		IpvodAuthenticationSystem auth = authenticationSystemFacade
				.findByToken(token);
		if (auth == null) {
			throw RestException.BAD_REQUEST;
		}
		IpvodContentProvider ipvodContentProvider = null;
		if (auth.getIpvodUserSys().getRole()
				.equals(IpvodConstants.ROLE_OPERADORA)) {
			ipvodContentProvider = auth.getIpvodUserSys().getContentProvider();
		}

		List<IngestVO> ingests = ingestFacade.findDataIngest(uriInfo,
				ipvodContentProvider);
		if (ingests == null || ingests.isEmpty()) {
			throw RestException.getNoContent();
		}
		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_PAGE_NUMBER) == null
				&& uriInfo.getQueryParameters().getFirst(
						IpvodConstants.URLPARAM_FIRST_INDEX) == null) {
			// Map<String, Object> m = new HashMap<String, Object>();
			// m.put("list", ingests);
			// m.put("count",
			// ingestFacade.countDataIngest(ipvodContentProvider));
			// return Response.status(200).entity(m).build();
			return Response.status(200).entity(ingests).build();
		} else {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("list", ingests);
			m.put("count",
					ingestFacade.countDataIngest(ipvodContentProvider, uriInfo));
			return Response.status(200).entity(m).build();
		}
	}

	@PermitAll
	@GET
	@Path("/revision")
	@Produces("application/json; charset=UTF-8")
	public Response findAllDataIngestForRevision(@Context UriInfo uriInfo,
			@HeaderParam(IpvodConstants.AUTHORIZATION_PROPERTY_CMS) String token)
			throws RestException {
		IpvodAuthenticationSystem auth = authenticationSystemFacade
				.findByToken(token);
		if (auth == null) {
			throw RestException.BAD_REQUEST;
		}
		IpvodContentProvider ipvodContentProvider = null;
		if (auth.getIpvodUserSys().getRole()
				.equals(IpvodConstants.ROLE_OPERADORA)) {
			ipvodContentProvider = auth.getIpvodUserSys().getContentProvider();
		}

		// Busca lista de registros para revisao apos ingest de conteudo
		List<IngestVO> ingests = ingestFacade.findAllDataIngestForRevision(
				uriInfo, ipvodContentProvider);
		if (ingests == null || ingests.isEmpty()) {
			throw RestException.getNoContent();
		}

		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_PAGE_NUMBER) == null
				&& uriInfo.getQueryParameters().getFirst(
						IpvodConstants.URLPARAM_FIRST_INDEX) == null) {
			return Response.status(200).entity(ingests).build();
		} else {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("list", ingests);
			m.put("count", ingestFacade.countAllDataIngestForRevision(
					ipvodContentProvider, uriInfo));
			return Response.status(200).entity(m).build();
		}
	}

	@PermitAll
	@RolesAllowed({ IpvodConstants.ROLE_ADMIN, IpvodConstants.ROLE_MARKETING,
			IpvodConstants.ROLE_VOC })
	@POST
	@Path("/priority")
	@Produces("application/json; charset=UTF-8")
	@Consumes("application/json")
	public Response updatePriorityIngest(IpvodIngestStage ipvodIngestStage)
			throws RestException {
		try {
			if (ipvodIngestStage.getId() == null
					&& ipvodIngestStage.getPriority() < 0) {
				throw RestException.getError();
			}
			IpvodIngestStage ingestStage = ingestFacade
					.findIngestById(ipvodIngestStage.getId());
			if (ingestStage == null) {
				throw RestException.getNoContent();
			}
			ingestStage.setPriority(ipvodIngestStage.getPriority());
			ingestFacade.update(ingestStage);
			return Response.status(201).build();
		} catch (Exception e) {
			throw RestException.ERROR;
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findIngestByID(@PathParam("id") long id)
			throws RestException {
		ingestConverter = new IngestConverter();
		// Busca registro para processar
		IpvodIngestStage ipvodIngestStage = ingestFacade.findIngestById(id);

		if (ipvodIngestStage == null) {
			throw RestException.getNoContent();
		}
		return Response.status(200)
				.entity(ingestConverter.toIngest(ipvodIngestStage)).build();
	}

	@PermitAll
	@DELETE
	@Path("/delete/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response deleteDataIngest(@PathParam("id") Long ingestId)
			throws RestException {
		try {
			// Bad request
			if (ingestId == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodIngestStage ipvodIngestStage = ingestFacade
					.findIngestById(ingestId);

			// Bad Request
			if (ipvodIngestStage == null) {
				throw new RestException(4,
						"Nao encontrou nenhum resultado para a pesquisa.");
			}

			// Remove os dados na base
			ingestFacade.delete(ipvodIngestStage);

			// Retorna o status
			return Response.status(204).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao remover dados Ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/clean")
	@Produces("application/json; charset=UTF-8")
	public Response findDataForCleanUp() throws RestException {
		// Busca lista de registros para revisao apos ingest de conteudo
		List<IngestVO> listIngests = ingestFacade.findDataIngestForCleanUp();
		if (listIngests == null || listIngests.isEmpty()) {
			throw RestException.getNoContent();
		}
		return Response.status(200).entity(listIngests).build();
	}

	@PermitAll
	@PUT
	@Path("/clean")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response updateDataForCleanup(IngestVO ingestVO)
			throws RestException {
		IpvodIngestStage ingestValue = ingestFacade.findIngestById(ingestVO
				.getIdIngest());
		ingestValue.setCleanup(ingestVO.isCleanup());
		// Atualiza os dados do @IpvodIngestStage
		ingestFacade.update(ingestValue);
		return Response.status(200).build();
	}
}