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

import br.com.gvt.eng.vod.converter.ConvoyConverter;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodConvoyData;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.vod.facade.ConvoyFacade;

@Stateless
@Path("/convoy")
public class Convoy {

	@EJB
	private ConvoyFacade convoyFacade;

	private ConvoyConverter convoyConverter;

	@PermitAll
	@GET
	@Path("/")
	@Produces("application/json; charset=UTF-8")
	public Response findListConvoy() throws RestException {
		try {
			convoyConverter = new ConvoyConverter();
			// Busca lista de de todos os registros do Convoy
			List<IpvodConvoyData> listConvoyData = convoyFacade.findAll();
			// Bad request
			if (listConvoyData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			return Response.status(200)
					.entity(convoyConverter.getConvoyList(listConvoyData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/execute")
	@Produces("application/json; charset=UTF-8")
	public Response findListConvoyLessDone() throws RestException {
		try {
			convoyConverter = new ConvoyConverter();
			// Busca lista de registros que nao estao com status "done"
			List<IpvodConvoyData> listConvoyData = convoyFacade
					.findAllLessDoneStatus();
			// Bad request
			if (listConvoyData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			return Response.status(200)
					.entity(convoyConverter.getConvoyList(listConvoyData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findConvoyDataById(@PathParam("id") Long convoyId)
			throws RestException {
		try {
			convoyConverter = new ConvoyConverter();
			// Busca lista de registros para processar
			IpvodConvoyData convoyData = convoyFacade.find(convoyId);
			// Bad request
			if (convoyData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			// Retorna o objeto
			return Response.status(200)
					.entity(convoyConverter.toConvoy(convoyData)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados Convoy");
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}/ingest")
	@Produces("application/json; charset=UTF-8")
	public Response findConvoyDataByIngestId(@PathParam("id") Long ingestId)
			throws RestException {
		try {
			convoyConverter = new ConvoyConverter();
			// Busca lista de registros para processar
			List<IpvodConvoyData> listConvoyData = convoyFacade
					.findConvoyDataByIngestId(ingestId);
			// Bad request
			if (listConvoyData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}

			// Retorna o objeto
			return Response.status(200)
					.entity(convoyConverter.getConvoyList(listConvoyData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados Convoy");
		}
	}

	@PermitAll
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response saveDataConvoy(IpvodConvoyData ipvodConvoyData)
			throws RestException {
		try {
			// Bad request
			if (ipvodConvoyData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Salva os dados na base
			convoyFacade.save(ipvodConvoyData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao salvar dados Convoy");
		}
	}

	@PermitAll
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response updateDataConvoy(IpvodConvoyData ipvodConvoyData)
			throws RestException {
		try {
			// Bad request
			if (ipvodConvoyData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodConvoyData convoyData = convoyFacade.find(ipvodConvoyData
					.getConvoyId());

			// Bad request
			if (convoyData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Verifica se a data foi alterada
			if (ipvodConvoyData.getDateStart() == null) {
				ipvodConvoyData.setDateStart(convoyData.getDateStart());
			}

			// Verifica se a data foi alterada
			if (ipvodConvoyData.getDateEnd() == null) {
				ipvodConvoyData.setDateEnd(convoyData.getDateEnd());
			}

			// Verificacao para atualizar com dados de @IpvodMediaAsset
			IpvodMediaAsset mediaAsset = new IpvodMediaAsset();
			mediaAsset.setIpvodAsset(ipvodConvoyData.getIpvodIngestStage()
					.getIpvodAsset());
			mediaAsset.setMediaAssetId(ipvodConvoyData.getIpvodMediaAsset()
					.getMediaAssetId());
			mediaAsset.setUrl(ipvodConvoyData.getIpvodMediaAsset().getUrl());
			mediaAsset.setIpvodMediaType(ipvodConvoyData.getIpvodMediaAsset()
					.getIpvodMediaType());

			// Arrumando as variaveis
			ipvodConvoyData.setIpvodMediaAsset(mediaAsset);

			// Salva os dados na base
			convoyFacade.update(ipvodConvoyData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao atualizar dados Convoy");
		}
	}

	@PermitAll
	@DELETE
	@Path("/delete/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response deleteDataConvoy(@PathParam("id") Long convoyId)
			throws RestException {
		try {
			// Bad request
			if (convoyId == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodConvoyData ipvodConvoy = convoyFacade.find(convoyId);

			// Bad Request
			if (ipvodConvoy == null) {
				throw new RestException(4,
						"Não encontrou nenhum resultado para a pesquisa.");
			}

			// Remove os dados na base
			convoyFacade.delete(ipvodConvoy);

			// Retorna o status
			return Response.status(204).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao deletar dados Convoy");
		}
	}
}