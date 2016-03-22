package br.com.gvt.eng.vod.exception.rest;

public enum RestCodeError {

	ERRO_INESPERADO(1), CAMPOS_OBRIGATORIOS(2), ENTIDADE_EXISTENTE(3), RELACIONAMENTO_INEXISTENTE(4), EQUIPAMENTO_ATIVO(5), OBJETO_DUPLICADO(
			6), SEM_MODIFICACAO(7), FORMATO_INCOMPATIVEL(8), ERRO_SUBSCRIBER(9), ERRO_UPDATE_SUBSCRIBER(10), PENDING_BILLING(
			11), ERRO_CHANNEL_CATALOG(12);

	private RestCodeError(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
