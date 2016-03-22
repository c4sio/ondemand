package br.com.gvt.eng.vod.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonFilter;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonFilter("ErroVO")
public class ErroVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer errorCode;
	private String errorMessage;
	private List<String> missingParams;

	public ErroVO() {
	}

	public ErroVO(String mensagemErro) {
		this.errorMessage = mensagemErro;
	}

	public ErroVO(Integer codigoErro, String message) {
		this.setErrorCode(codigoErro);
		this.errorMessage = message;
	}

	public ErroVO(Integer codigoErro, String message, List<String> missingParam) {
		this.setErrorCode(codigoErro);
		this.errorMessage = message;
		this.missingParams = missingParam;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String mensagemErro) {
		this.errorMessage = mensagemErro;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer codigoErro) {
		this.errorCode = codigoErro;
	}

	public List<String> getMissingParams() {
		return missingParams;
	}

	public void setMissingParams(List<String> missingParams) {
		this.missingParams = missingParams;
	}

}
