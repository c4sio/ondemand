package br.com.gvt.eng.integ.dao;

import java.io.File;
import java.util.List;

import javax.ejb.Local;

@Local
public interface FilesDAO {

	/**
	 * Verifica se os arquivos de leitura ja estao casdastrados na base
	 * 
	 * @param listFiles
	 * @return List<File>
	 */
	public abstract List<File> verifyFilesExist(List<File> listFiles);

	/**
	 * Busca arquivos na pasta passada
	 * 
	 * @param path
	 * @return List<File>
	 */
	public abstract List<File> searchFiles(String path);

	/**
	 * Remover arquivo da pasta
	 * 
	 * @param file
	 */
	public abstract void removeFile(String inFilesPath, String nameFile);

}
