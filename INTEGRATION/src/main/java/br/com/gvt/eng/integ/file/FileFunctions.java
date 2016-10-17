package br.com.gvt.eng.integ.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class FileFunctions {

	static Logger logger = Logger.getLogger(FileFunctions.class.getName());

	/**
	 * @param path
	 * @return List<File>
	 */
	public List<File> searchFiles(String path) {
		List<File> filteredTS = new ArrayList<File>();
		File root = new File(path);
		File[] list = root.listFiles();
		Pattern p = Pattern
				.compile(".*\\.ts|.*\\.mp4|.*\\.mxf|.*\\.mpg|.*\\.mpeg|.*\\.ps|.*\\.mov");
		try {
			for (File f : list) {
				if (f.isDirectory()) {
					logger.info("Verificando arquivo no caminho: "
							+ f.getAbsolutePath());
					searchFiles(f.getAbsolutePath());
				} else {
					Matcher m = p.matcher(f.getName().toLowerCase());
					if (m.matches()) {
						System.out.println("Nome do arquivo encontrado: "
								+ f.getName());
						logger.info("Nome do arquivo encontrado: "
								+ f.getName());
						filteredTS.add(f);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Erro buscar arquivos - " + e);
		}
		return filteredTS;
	}

	/**
	 * @param inFilesPath
	 * @return String
	 */
	public String findLegend(String inFilesPath) {
		String fileLegend = null;
		File root = new File(inFilesPath);
		File[] list = root.listFiles();
		try {
			for (File f : list) {
				if (f.isDirectory()) {
					logger.info("Verificando arquivo de legenda no caminho: "
							+ f.getAbsolutePath());
					searchFiles(f.getAbsolutePath());
				} else {
					// Arquivos podem ser ts / mp4 / mxf /
					if (f.getName().toLowerCase().endsWith(".srt")) {
						fileLegend = f.getName();
					}
				}
			}
		} catch (Exception e) {
			logger.error("Erro buscar arquivos de legenda- " + e);
		}
		return fileLegend;
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
	}
}