package br.com.gvt.eng.ipvod.rest.integration;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.converter.DrmConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodDrmData;
import br.com.gvt.vod.facade.DrmFacade;

@Stateless
@Path("/drm")
public class DRM {

	@EJB
	private DrmFacade drmFacade;

	private DrmConverter drmConverter;

	@PermitAll
	@GET
	@Path("/")
	@Produces("application/json; charset=UTF-8")
	public Response findListDrm() throws RestException {
		try {
			drmConverter = new DrmConverter();
			// Busca lista de registros para processar
			List<IpvodDrmData> listDrmData = drmFacade.findAll();
			// Bad request
			if (listDrmData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			return Response.status(200)
					.entity(drmConverter.getDrmList(listDrmData)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/execute")
	@Produces("application/json; charset=UTF-8")
	public Response findListDrmLessCompleted() throws RestException {
		try {
			drmConverter = new DrmConverter();
			// Busca lista de registros que nao estao com status "done"
			List<IpvodDrmData> listDrmData = drmFacade
					.findAllLessCompletedStatus();
			// Bad request
			if (listDrmData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			return Response.status(200)
					.entity(drmConverter.getDrmList(listDrmData)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}/ingest")
	@Produces("application/json; charset=UTF-8")
	public Response findConvoyDataByIngestId(@PathParam("id") Long ingestId)
			throws RestException {
		try {
			drmConverter = new DrmConverter();
			// Busca lista de registros para processar
			List<IpvodDrmData> listDrmData = drmFacade
					.findDrmDataByIngestId(ingestId);
			// Bad request
			if (listDrmData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			// Retorna o objeto
			return Response.status(200)
					.entity(drmConverter.getDrmList(listDrmData)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados Convoy");
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findDrmDataById(@PathParam("id") Long drmId)
			throws RestException {
		try {
			drmConverter = new DrmConverter();
			// Busca lista de registros para processar
			IpvodDrmData drmData = drmFacade.find(drmId);
			// Bad request
			if (drmData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			// Retorna o objeto
			return Response.status(200).entity(drmConverter.toDrm(drmData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response saveDataDrm(IpvodDrmData ipvodDrmData) throws RestException {
		try {
			// Bad request
			if (ipvodDrmData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Salva os dados na base
			drmFacade.save(ipvodDrmData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao salvar dados DRM");
		}
	}

	@PermitAll
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response updateDataDrm(IpvodDrmData ipvodDrmData)
			throws RestException {
		try {
			// Bad request
			if (ipvodDrmData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodDrmData drmData = drmFacade.find(ipvodDrmData.getDrmId());

			// Verifica se a data foi alterada
			if (ipvodDrmData.getDateStart() == null) {
				ipvodDrmData.setDateStart(drmData.getDateStart());
			}

			// Verifica se a data foi alterada
			if (ipvodDrmData.getDateEndDrm() == null) {
				ipvodDrmData.setDateEndDrm(drmData.getDateEndDrm());
			}

			// Salva os dados na base
			drmFacade.update(ipvodDrmData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao atualizar dados DRM");
		}
	}

	@PermitAll
	@DELETE
	@Path("/delete/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response deleteDataDrm(@PathParam("id") Long drmId)
			throws RestException {
		try {
			// Bad request
			if (drmId == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodDrmData ipvodDrm = drmFacade.find(drmId);

			// Bad Request
			if (ipvodDrm == null) {
				throw new RestException(4,
						"Nao encontrou nenhum resultado para a pesquisa.");
			}

			// Remove os dados na base
			drmFacade.delete(ipvodDrm);

			// Retorna o status
			return Response.status(204).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao atualizar dados DRM");
		}
	}

}