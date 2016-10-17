package br.com.gvt.eng.convoy.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.util.Util;

public class FileFunctions {

	static Logger logger = Logger.getLogger(FileFunctions.class.getName());

	public List<File> searchFiles(String path) {
		List<File> filteredFiles = new ArrayList<File>();
		try {
			// O path eh o valor do AssetId concatenado com o tipo Movie/Trailer
			File root = new File(Util.getInFilesPath() + path);
			File[] list = root.listFiles();
			logger.info("Busca arquivos na pasta: " + root.getPath());

			for (File f : list) {
				if (f.isDirectory()) {
					logger.info("Verificando arquivo no caminho: "
							+ f.getAbsolutePath());
					searchFiles(f.getAbsolutePath());
				} else {
					// Arquivos m3u8
					if (f.getName().equalsIgnoreCase(path + ".m3u8")) {
						logger.info("[" + f.getName() + "]");
						filteredFiles.add(f);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Erro buscar arquivos - " + e);
		}

		System.out.println("Encontrou " + filteredFiles.size() + " no caminho "
				+ path);
		logger.info("Encontrou " + filteredFiles.size() + " no caminho " + path);

		return filteredFiles;
	}

	/**
	 * remove os arquivos que ja foram processados
	 * 
	 * @param resource
	 * @throws IOException
	 */
	public void removeFiles(File resource) throws IOException {
		if (resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for (File child : childFiles) {
				removeFiles(child);
			}
		}
		resource.delete();
		logger.info("Arquivo [" + resource.getName() + "] deletado com sucesso.");
	}
}
