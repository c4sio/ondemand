package br.com.gvt.eng.vod.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.MethodNotAllowedException;
import org.jboss.resteasy.spi.UnsupportedMediaTypeException;

import br.com.gvt.eng.vod.converter.JsonSTB;
import br.com.gvt.eng.vod.exception.rest.RestCodeError;
import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.vo.ErroVO;

@Provider
public class ExceptionMapperImpl implements ExceptionMapper<Throwable> {

	public ExceptionMapperImpl() {
	}

	@Override
	public Response toResponse(Throwable erro) {
		ErroVO erroVO = null;
		Status status = null;

		if (RestException.class.isInstance(erro)) {
			RestException re = (RestException) erro;
			erroVO = new ErroVO(re.getCodigoErro(), erro.getMessage());
			status = re.getStatus();
			if (re.getMissingParams() != null) {
				erroVO.setMissingParams(re.getMissingParams());
			}
		} else if (UnsupportedMediaTypeException.class.isInstance(erro)) {
			erroVO = new ErroVO(Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), "Content Type inválido para esta requisição.");
			status = Status.UNSUPPORTED_MEDIA_TYPE;
			return Response.status(status).entity(erroVO).build();
		} else if (MethodNotAllowedException.class.isInstance(erro)) {
			erroVO = new ErroVO(405, "Método não permitido para esta requisição.");
			status = Status.UNSUPPORTED_MEDIA_TYPE;
			return Response.status(405).entity(erroVO).build();
		} else {
			erroVO = new ErroVO(RestCodeError.ERRO_INESPERADO.getCode(), "Ocorreu um problema não esperado: "
					+ erro.getMessage());
		}
		
		status = status != null ? status : Status.INTERNAL_SERVER_ERROR;
		
		if (erroVO.getMissingParams() == null || erroVO.getMissingParams().isEmpty()) {
			return Response.status(status).entity(JsonSTB.toJsonSTB(erroVO)).build();
		} else {
			return Response.status(status).entity(erroVO).build();
		}
	}
}