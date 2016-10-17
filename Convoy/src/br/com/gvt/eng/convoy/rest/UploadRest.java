package br.com.gvt.eng.convoy.rest;

import br.com.gvt.eng.convoy.vo.IpvodContent;

public interface UploadRest {

	/**
	 * Verifica o status do arquivo no Convoy
	 * 
	 * @param jobId
	 * @return IpvodContent
	 */
	public abstract IpvodContent checkStatusUpload(String jobId);

	/**
	 * Efetua o upload do arquivo no Convoy
	 * 
	 * @param fileName
	 * @param fileToConvoy
	 * @param title
	 * @param pathFile
	 * @return
	 */
	public abstract IpvodContent uploadFile(String fileName,
			String fileToConvoy, String title, String pathFile);

}
