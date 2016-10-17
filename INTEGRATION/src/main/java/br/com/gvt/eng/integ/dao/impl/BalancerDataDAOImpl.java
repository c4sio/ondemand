package br.com.gvt.eng.integ.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.BalancerDataDAO;
import br.com.gvt.eng.integ.dao.IngestStageDAO;
import br.com.gvt.eng.integ.dao.IntegrationDAO;
import br.com.gvt.eng.integ.dao.UserSystemDAO;
import br.com.gvt.eng.integ.email.ConfigEmail;
import br.com.gvt.eng.vod.Jobstatusinfo;
import br.com.gvt.eng.vod.model.IpvodBalancerData;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Repository
@Transactional
public class BalancerDataDAOImpl implements BalancerDataDAO {

	static Logger logger = Logger
			.getLogger(BalancerDataDAOImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private IntegrationDAO integrationDAO;

	@Autowired
	private IngestStageDAO ingestStageDAO;

	@Autowired
	private UserSystemDAO userSystemDAO;

	@Override
	public void save(IpvodBalancerData ipvodBalancerData) {
		try {
			logger.info("Salvando dados de arquivo "
					+ ipvodBalancerData.getNameFile());
			Session session = sessionFactory.getCurrentSession();
			session.save(ipvodBalancerData);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao salvar arquivo: ", e);
		}
	}

	@Override
	public IpvodBalancerData update(IpvodBalancerData ipvodBalancerData) {
		IpvodBalancerData balancerValues = new IpvodBalancerData();
		try {
			logger.info("Atualizando dados de arquivo "
					+ ipvodBalancerData.getNameFile());
			Session session = sessionFactory.getCurrentSession();

			// Busca informacao de execucao no 4Balancer
			List<Jobstatusinfo> infos = integrationDAO
					.getInfoJob(ipvodBalancerData.getJobId());

			// Verifica se o status e igual erro e se o e-mail ja foi
			// encaminhado para a operadora
			if (ipvodBalancerData.getStatus().equalsIgnoreCase("Error")
					&& !ipvodBalancerData.isSendMail()) {

				// Busca a lista de emails dos usuarios da operadora
				List<IpvodUserSystem> listUserEmail = new ArrayList<IpvodUserSystem>();
				for (IpvodUserSystem ipvodUserSystem : userSystemDAO
						.findEmail(ingestStageDAO
								.findById(
										ipvodBalancerData.getIpvodIngestStage()
												.getId()).getIpvodAsset()
								.getIpvodContentProvider()
								.getContentProviderId())) {
					listUserEmail.add(ipvodUserSystem);
				}

				// Envia e-mail
				if (!listUserEmail.isEmpty()) {
					ConfigEmail configEmail = new ConfigEmail();
					if (configEmail.sendEmailToList(listUserEmail,
							ipvodBalancerData.getNameFile(), infos)) {
						ipvodBalancerData.setSendMail(true);
					}
				}
			} else if (ipvodBalancerData.getStatus()
					.equalsIgnoreCase("Running")) {
				// Grava dados de percentual de execucao
				for (Jobstatusinfo jobstatusinfo : infos) {
					if (jobstatusinfo.getName().equalsIgnoreCase("completion")) {
						ipvodBalancerData.setPercentComp(Double
								.parseDouble(jobstatusinfo.getValue()));
					}
				}
			}

			// Atualiza os dados na base
			balancerValues = (IpvodBalancerData) session
					.merge(ipvodBalancerData);
		} catch (Exception e) {
			logger.error("Erro ao atualizar arquivo: ", e);
		}
		return balancerValues;
	}

	@Override
	public void delete(IpvodBalancerData ipvodBalancerData) {
		try {
			logger.info("Deletando dados de arquivo "
					+ ipvodBalancerData.getNameFile());
			Session session = sessionFactory.getCurrentSession();
			session.delete(ipvodBalancerData);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao deletar arquivo: ", e);
		}
	}

	@Override
	public IpvodBalancerData findById(long entityID) {
		IpvodBalancerData balancerData = new IpvodBalancerData();
		try {
			logger.info("Busca dados de arquivo id " + entityID);
			Session session = sessionFactory.getCurrentSession();
			balancerData = (IpvodBalancerData) session.load(
					IpvodBalancerData.class, entityID);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao buscar arquivo pelo id: ", e);
		}
		return balancerData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IpvodBalancerData> findAllValuesInProcess() {
		List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
		try {
			logger.info("Busca dados de arquivos na base.");
			Session session = sessionFactory.getCurrentSession();
			long stageType = 4;
			results = session
					.createQuery(
							"SELECT ban FROM IpvodBalancerData ban, IpvodIngestStage ing WHERE ban.ipvodIngestStage.id = ing.id and ban.status <>:status and ing.stageType.id =:stageType order by ban.id, ing.priority asc")
					.setParameter("stageType", stageType)
					.setParameter("status", "success").list();
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao buscar arquivos na base: ", e);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkEndOfProcess(long ingestId) {
		List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
		try {
			logger.info("Busca dados pelo ingestId na base.");
			Session session = sessionFactory.getCurrentSession();
			results = (List<IpvodBalancerData>) session
					.createQuery(
							"FROM IpvodBalancerData ibd WHERE ibd.ipvodIngestStage.id =:ingestId")
					.setParameter("ingestId", ingestId).list();
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao buscar dados por ingestId na base: ", e);
		}
		return verifyAllRegisters(results, ingestId);
	}

	/**
	 * @param results
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean verifyAllRegisters(List<IpvodBalancerData> results,
			long ingestId) {
		boolean finished = true;
		long count = 0;
		try {
			logger.info("Verifica se todos os registros com mesmo IngestId foram executados com sucesso.");

			// Verifica os todos os arquivos atribuidos ao asset
			Session session = sessionFactory.getCurrentSession();
			List<IpvodIngestStage> listFiles = new ArrayList<IpvodIngestStage>();
			listFiles = (List<IpvodIngestStage>) session
					.createQuery(
							"SELECT ing FROM IpvodIngestStage ing, IpvodMediaAsset med WHERE ing.ipvodAsset.assetId = med.ipvodAsset.assetId and med.ipvodMediaType.mediaTypeId in(2,3) and ing.id =:ingestId")
					.setParameter("ingestId", ingestId).list();
			session.flush();
			session.clear();

			// Verifica se todos os dados foram finalizados
			if (results.size() != listFiles.size()) {
				count += 1;
			} else {
				// Verifica se todos os registros foram finalizados com sucesso
				for (IpvodBalancerData ipvodBalancerData : results) {
					if (!ipvodBalancerData.getStatus().equalsIgnoreCase(
							"success")) {
						count += 1;
					}
				}
			}

			if (count > 0) {
				logger.info("Encontrou " + count
						+ " registro(s) que ainda nao foram finalizados!");
				finished = false;
			}
		} catch (Exception e) {
			logger.error("Erro ao verificar final do processo Ingest: ", e);
			finished = false;
		}
		return finished;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IpvodBalancerData> findAllInExecution() {
		List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
		try {
			logger.info("Busca dados de arquivos na base.");
			Session session = sessionFactory.getCurrentSession();
			results = session
					.createQuery(
							"select b from IpvodBalancerData b where b.status not in( 'success', 'error')")
					.list();
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao buscar arquivos na base: ", e);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IpvodBalancerData> findAllRegisters() {
		List<IpvodBalancerData> results = new ArrayList<IpvodBalancerData>();
		try {
			logger.info("findAllRegisters - Busca dados de arquivos na tabela @IpvodBalancerData.");
			Session session = sessionFactory.getCurrentSession();
			results = (List<IpvodBalancerData>) session.createQuery(
					"select b from IpvodBalancerData b").list();
			logger.info("Encontrou " + results.size()
					+ " registro(s) na tabela @IpvodBalancerData.");
			System.out.println("Encontrou " + results.size()
					+ " registro(s) na tabela @IpvodBalancerData.");
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error(
					"findAllRegisters - Erro ao buscar arquivos na tabela @IpvodBalancerData: ",
					e);
		}
		return results;
	}
}