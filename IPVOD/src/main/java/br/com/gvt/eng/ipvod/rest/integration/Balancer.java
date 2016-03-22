package br.com.gvt.eng.ipvod.rest.integration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.converter.BalancerConvert;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.eng.vod.model.IpvodPreset;
import br.com.gvt.vod.facade.BalancerFacade;
import br.com.gvt.vod.facade.PresetFacade;

@Stateless
@Path("/balancer")
public class Balancer {

	@EJB
	private BalancerFacade balancerFacade;

	@EJB
	private PresetFacade presetFacade;

	private BalancerConvert balancerConvert;

	@PermitAll
	@GET
	@Path("/")
	@Produces("application/json; charset=UTF-8")
	public Response findListBalancer() throws RestException {
		try {
			balancerConvert = new BalancerConvert();
			// Busca lista de registros para processar
			List<IpvodBalancerData> listBalancerData = balancerFacade.findAll();
			// Bad request
			if (listBalancerData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}
			// Retorna o objeto
			return Response.status(200)
					.entity(balancerConvert.getBalancerList(listBalancerData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/execute")
	@Produces("application/json; charset=UTF-8")
	public Response findAllValuesInProcess() throws RestException {
		try {
			balancerConvert = new BalancerConvert();
			// Busca lista de registros que nao estao com status "Success"
			List<IpvodBalancerData> listBalancerData = balancerFacade
					.findAllValuesInProcess();
			// Bad request
			if (listBalancerData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}
			// Retorna o objeto
			return Response.status(200)
					.entity(balancerConvert.getBalancerList(listBalancerData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}

	@PermitAll
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findBalancerDataById(@PathParam("id") Long balancerId)
			throws RestException {
		try {
			balancerConvert = new BalancerConvert();
			// Busca lista de registros para processar
			IpvodBalancerData balancerData = balancerFacade.find(balancerId);
			// Bad request
			if (balancerData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}
			// Retorna o objeto
			return Response.status(200)
					.entity(balancerConvert.toBalancer(balancerData)).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados Balancer");
		}
	}

	@PermitAll
	@PUT
	@Path("/save")
	@Produces("application/json; charset=UTF-8")
	public Response saveDataBalancer(IpvodBalancerData ipvodBalancerData)
			throws RestException {
		try {
			// Bad request
			if (ipvodBalancerData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Salva os dados na base
			balancerFacade.save(ipvodBalancerData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao salvar dados Balancer");
		}
	}

	@PermitAll
	@PUT
	@Path("/update")
	@Produces("application/json; charset=UTF-8")
	public Response updateDataBalancer(IpvodBalancerData ipvodBalancerData)
			throws RestException {
		try {
			// Bad request
			if (ipvodBalancerData == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodBalancerData balancerData = balancerFacade
					.find(ipvodBalancerData.getId());

			// Verifica se a data foi alterada
			if (ipvodBalancerData.getDateStart() == null) {
				ipvodBalancerData.setDateStart(balancerData.getDateStart());
			}

			// Verifica se a data foi alterada
			if (ipvodBalancerData.getDateEnd() == null) {
				ipvodBalancerData.setDateEnd(balancerData.getDateEnd());
			}

			// Salva os dados na base
			balancerFacade.update(ipvodBalancerData);

			// Retorna o status
			return Response.status(201).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao atualizar dados Balancer");
		}
	}

	@PermitAll
	@DELETE
	@Path("/delete/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response deleteDataBalancer(@PathParam("id") Long balancerId)
			throws RestException {
		try {
			// Bad request
			if (balancerId == null) {
				throw new RestException(1, "Registro invalido");
			}

			// Busca dados na base
			IpvodBalancerData ipvodBalancerData = balancerFacade
					.find(balancerId);

			// Bad Request
			if (ipvodBalancerData == null) {
				throw new RestException(4,
						"Não encontrou nenhum resultado para a pesquisa.");
			}

			// Remove os dados na base
			balancerFacade.delete(ipvodBalancerData);

			// Retorna o status
			return Response.status(204).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao atualizar dados Balancer");
		}
	}

	@PermitAll
	@POST
	@Path("/preset")
	@Produces("application/json; charset=UTF-8")
	public Response findPreset(IpvodAsset ipvodAsset) throws RestException {
		try {
			// Bad request
			if (ipvodAsset == null) {
				throw new RestException(1, "Registro invalido");
			}

			IpvodPreset ipvodPreset = new IpvodPreset();
			// Busca dados de Preset na base
			ipvodPreset = presetFacade.findPresetByParameters(ipvodAsset);

			// Retorna o objeto
			return Response.status(200).entity(ipvodPreset).build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar dados Preset");
		}
	}

	@PermitAll
	@GET
	@Path("/ingest/{ingestId:[0-9][0-9]*}")
	@Produces("application/json; charset=UTF-8")
	public Response findBalancerDataByIngestId(
			@PathParam("ingestId") Long ingestId) throws RestException {
		try {
			balancerConvert = new BalancerConvert();
			List<IpvodBalancerData> listBalancerData = new ArrayList<IpvodBalancerData>();
			// Busca lista de registros para processar
			listBalancerData = balancerFacade
					.findBalancerDataByIngestId(ingestId);

			// Retorna o objeto
			return Response.status(200)
					.entity(balancerConvert.getBalancerList(listBalancerData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados Balancer");
		}
	}
	
	@PermitAll
	@GET
	@Path("/execution")
	@Produces("application/json; charset=UTF-8")
	public Response findAllInExecution() throws RestException {
		try {
			balancerConvert = new BalancerConvert();
			// Busca lista de registros para processar
			List<IpvodBalancerData> listBalancerData = balancerFacade.findAllInExecution();
			// Bad request
			if (listBalancerData == null) {
				throw new RestException(1, "Nenhum registro encontrado");
			}
			// Retorna o objeto
			return Response.status(200)
					.entity(balancerConvert.getBalancerList(listBalancerData))
					.build();
		} catch (Exception e) {
			throw new RestException(1, "Erro ao buscar lista de dados ingest");
		}
	}
}