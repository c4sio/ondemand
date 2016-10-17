package br.com.gvt.eng.integ.dao.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.BalancerDataDAO;
import br.com.gvt.eng.integ.dao.FilesDAO;
import br.com.gvt.eng.integ.file.FileFunctions;
import br.com.gvt.eng.vod.model.IpvodBalancerData;

@Repository
@Transactional
public class FilesDAOImpl implements FilesDAO {

	static Logger logger = Logger.getLogger(FilesDAOImpl.class.getName());

	@Autowired
	private BalancerDataDAO balancerDataDAO;

	@Override
	public List<File> verifyFilesExist(List<File> listFiles) {
		try {
			logger.info("Verificando e removendo arquivos da lista de leitura.");
			List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
			results = balancerDataDAO.findAllValuesInProcess();

			if (!results.isEmpty()) {
				Iterator<File> ite = listFiles.iterator();
				for (IpvodBalancerData balancerData : results) {
					while (ite.hasNext()) {
						String value = ite.next().getName();
						if (value.equals(balancerData.getNameFile())) {
							ite.remove();
							listFiles.remove(ite);
							logger.info("Arquivo "
									+ value
									+ " removido da lista de leitura, pois ja foi incluido na base e criado o job.");
						}
					}
					ite = listFiles.iterator();
				}
			}
		} catch (Exception e) {
			logger.error("Erro ao verificar se arquivos ja existem na base: ",
					e);
		}
		return listFiles;
	}

	@Override
	public List<File> searchFiles(String path) {
		logger.info("Buscando os arquivos no caminho: " + path);
		List<File> listFiles = new ArrayList<File>();
		try {
			FileFunctions reader = new FileFunctions();
			listFiles = reader.searchFiles(path);
			logger.info("Files founds to import: " + listFiles.size());
			System.out.println("Files founds to import: "
					+ listFiles.size());
		} catch (Exception e) {
			logger.error("Erro ao ler se arquivos na pasta " + path, e);
		}
		return listFiles;
	}

	@Override
	public void removeFile(String inFilesPath, String nameFile) {
		String fileRemoved = null;
		try {
			List<File> listFiles = new ArrayList<File>();
			listFiles = searchFiles(inFilesPath);
			for (File file : listFiles) {
				if (file.getName().equals(nameFile)) {
					fileRemoved = file.getName();
					file.delete();
					logger.info("Arquivo removido " + fileRemoved);
				}
			}
		} catch (Exception e) {
			logger.error("Erro ao remover arquivo " + fileRemoved, e);
		}
	}

}
