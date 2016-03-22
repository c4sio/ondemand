package br.com.gvt.eng.vod.exception.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

public class RestException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static RestException BAD_REQUEST = getBadRequest();

	public static RestException NO_CONTENT = getNoContent();

	public static RestException ERROR = getError();
	
	public static RestException FORBIDDEN = getUserForbidden();

	private Integer codigoErro;

	private Status status;

	private List<String> missingParams;

	public RestException(Integer codigoErro, String message) {
		super(message);
		this.setCodigoErro(codigoErro);
	}
	
	public static RestException getBadRequest() {
		RestException exc = new RestException(1, "Missing Parameters.",
				Status.BAD_REQUEST);
		return exc ;
	}
	public static RestException getNoContent() {
		RestException exc = new RestException(2, "Nenhum registro encontrado.",
				Status.NO_CONTENT);
		return exc ;
	}
	
	public static RestException getError() {
		RestException exc = new RestException(3, "Erro na operacao solicitada.",
				Status.INTERNAL_SERVER_ERROR);
		return exc ;
	}

	public static RestException getEquipmentNotFound() {
		RestException exc = new RestException(4, "Equipment not found.",
				Status.FORBIDDEN);
		return exc ;
	}

	public static RestException getUserInactive() {
		RestException exc = new RestException(5, "User inactive.",
				Status.FORBIDDEN);
		return exc ;
	}
	
	public static RestException getUserForbidden() {
		RestException exc = new RestException(6, "Usuario sem permissao para a acao.",
				Status.INTERNAL_SERVER_ERROR);
		return exc ;
	}
	
	public RestException(Integer codigoErro, String message, Status status) {
		super(message);
		this.setCodigoErro(codigoErro);
		this.setStatus(status);
		this.setMissingParams(new ArrayList<String>());
	}
	
	public RestException(Integer codigoErro, String message, Status status, ArrayList<String> missingParams) {
		super(message);
		this.setCodigoErro(codigoErro);
		this.setStatus(status);
		this.missingParams = missingParams;
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestException(RestCodeError codeError, String message, Status status) {
		super(message);
		this.setCodigoErro(codeError.getCode());
		this.setStatus(status);
	}

	public Integer getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(Integer codigoErro) {
		this.codigoErro = codigoErro;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<String> getMissingParams() {
		return missingParams;
	}

	public void setMissingParams(List<String> missingParams) {
		this.missingParams = missingParams;
	}
}
