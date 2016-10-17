package br.com.gvt.eng.integ.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.BalancerDataDAO;
import br.com.gvt.eng.integ.dao.IngestStageDAO;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.model.IpvodIngestType;
import br.com.gvt.eng.vod.model.IpvodMediaAsset;

@Repository
@Transactional
public class IngestStageDAOImpl implements IngestStageDAO {

	static Logger logger = Logger.getLogger(IngestStageDAOImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private BalancerDataDAO balancerDataDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<IpvodIngestStage> findFilesToExecute() {
		List<IpvodIngestStage> listData = new ArrayList<IpvodIngestStage>();
		try {
			logger.info("Buscando arquivos no ingest para processar");
			Session session = sessionFactory.getCurrentSession();
			listData = (List<IpvodIngestStage>) session
					.createQuery(
							"SELECT ing FROM IpvodIngestStage ing WHERE ing.stageType.id = 4 AND ing.ipvodAsset.assetId is not null order by ing.priority, ing.id asc")
					.list();

			// Verifica se o registro já esta em processamento e retira da lista
			if (!listData.isEmpty()) {
				// Para tratar o lazy Exception
				for (IpvodIngestStage ipvodIngestStage : listData) {
					ipvodIngestStage.getIpvodAsset().getIpvodMediaAssets()
							.size();
				}

				// Busca todos os dados na tabela @IpvodBalancerData
				List<IpvodBalancerData> listBalancerData = balancerDataDAO
						.findAllRegisters();

				// Verifica os registros que ja estao na tabela
				// @IpvodBalancerData e remove da lista
				for (IpvodIngestStage ipvodIngestStage : listData) {
					ipvodIngestStage.getIpvodAsset().setIpvodMediaAssets(
							verifyFiles(ipvodIngestStage.getIpvodAsset()
									.getIpvodMediaAssets(), listBalancerData));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao listar dados de ingest: ", e);
		}
		return listData;
	}

	/**
	 * Verifica se os arquivos de leitura ja estao casdastrados na base
	 * 
	 * @param List
	 *            <IpvodMediaAsset>
	 * @param List
	 *            <IpvodBalancerData>
	 * @return List<IpvodMediaAsset>
	 */
	private List<IpvodMediaAsset> verifyFiles(
			List<IpvodMediaAsset> ipvodMediaAssets,
			List<IpvodBalancerData> results) {
		logger.info("Verificando e removendo arquivos da lista de leitura.");
		List<IpvodMediaAsset> mediaList = new ArrayList<IpvodMediaAsset>(0);
		try {
			// Copia os valores para a nova lista
			mediaList = ipvodMediaAssets;
			String value = null;

			// Verifica os registros
			if (!results.isEmpty()) {
				Iterator<IpvodMediaAsset> ite = ipvodMediaAssets.iterator();
				for (IpvodBalancerData ipvodBalancerData : results) {
					while (ite.hasNext()) {
						value = ite.next().getUrl();
						// Se os valores forem iguais remove da lista
						if (value.equals(ipvodBalancerData.getNameFile())) {
							ite.remove();
							mediaList.remove(ite);
							logger.info("Arquivo "
									+ value
									+ " removido da lista de leitura, pois ja foi incluido na base e criado o job.");
							System.out
									.println("Arquivo "
											+ value
											+ " removido da lista de leitura, pois ja foi incluido na base e criado o job.");
						}
					}
					ite = ipvodMediaAssets.iterator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao verificar se arquivos ja existem na base: ",
					e);
		}
		return mediaList;
	}

	@Override
	public IpvodIngestStage update(IpvodIngestStage ipvodIngestStage) {
		IpvodIngestStage ingestStageValue = new IpvodIngestStage();
		try {
			logger.info("Atualizando dados Ingest " + ipvodIngestStage.getId());
			Session session = sessionFactory.getCurrentSession();
			ingestStageValue = (IpvodIngestStage) session
					.merge(ipvodIngestStage);
		} catch (Exception e) {
			logger.error("Erro ao atualizar dados tabela Ingest - ID "
					+ ipvodIngestStage.getId(), e);
		}
		return ingestStageValue;
	}

	@Override
	public boolean findByIdAndUpdateStage(long ingestId) {
		IpvodIngestStage ipvodIngestStage = new IpvodIngestStage();
		boolean finished = false;
		try {
			logger.info("Busca dados Ingest por ID " + ingestId
					+ " para atualizacao do status");
			Session session = sessionFactory.getCurrentSession();
			ipvodIngestStage = (IpvodIngestStage) session
					.createQuery(
							"SELECT ing FROM IpvodIngestStage ing WHERE ing.id = :ingestId")
					.setParameter("ingestId", ingestId).uniqueResult();
			session.flush();
			session.clear();

			if (ipvodIngestStage != null) {
				IpvodIngestType ipvodIngestType = new IpvodIngestType();
				// Seta o valor 5 (ENCRYPTION) para o registro
				ipvodIngestType.setId(new Long(5));
				ipvodIngestStage.setStageType(ipvodIngestType);

				// Atualiza o registro
				ipvodIngestStage = update(ipvodIngestStage);
			}
			finished = true;
		} catch (Exception e) {
			logger.error("Erro ao buscar dados tabela Ingest - ID " + ingestId,
					e);
		}
		return finished;
	}

	@Override
	public IpvodIngestStage findById(long ingestId) {
		IpvodIngestStage ipvodIngestStage = new IpvodIngestStage();
		try {
			logger.info("Busca dados Ingest por ID " + ingestId);
			Session session = sessionFactory.getCurrentSession();
			ipvodIngestStage = (IpvodIngestStage) session
					.createQuery(
							"FROM IpvodIngestStage ing WHERE ing.id = :ingestId")
					.setParameter("ingestId", ingestId).uniqueResult();
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao buscar dados tabela Ingest - ID " + ingestId,
					e);
		}
		return ipvodIngestStage;
	}

	@Override
	public void updateStagePresetError(long ingestId) {
		IpvodIngestStage ipvodIngestStage = new IpvodIngestStage();
		try {
			logger.info("Busca dados Ingest por ID " + ingestId
					+ " para atualizacao do status 14 (Preset not found)");
			Session session = sessionFactory.getCurrentSession();
			ipvodIngestStage = (IpvodIngestStage) session
					.createQuery(
							"SELECT ing FROM IpvodIngestStage ing WHERE ing.id = :ingestId")
					.setParameter("ingestId", ingestId).uniqueResult();
			session.flush();
			session.clear();

			if (ipvodIngestStage != null) {
				IpvodIngestType ipvodIngestType = new IpvodIngestType();
				// Seta o valor 14 (PRESET NOT FOUND) para o registro
				ipvodIngestType.setId(new Long(14));
				ipvodIngestStage.setStageType(ipvodIngestType);

				// Atualiza o registro
				ipvodIngestStage = update(ipvodIngestStage);
			}
		} catch (Exception e) {
			logger.error("Erro ao buscar dados tabela Ingest - ID " + ingestId,
					e);
		}
	}
}
