package br.com.gvt.eng.integ.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.gvt.eng.integ.dao.UserSystemDAO;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Repository
@Transactional
public class UserSystemDAOImpl implements UserSystemDAO {

	static Logger logger = Logger.getLogger(UserSystemDAOImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<IpvodUserSystem> findEmail(long contentProviderId) {
		List<IpvodUserSystem> listIpvodUserSystem = new ArrayList<IpvodUserSystem>();
		try {
			logger.info("Buscando dados de email das operadoras");
			Session session = sessionFactory.getCurrentSession();

			listIpvodUserSystem = (List<IpvodUserSystem>) session
					.createQuery(
							"SELECT us FROM IpvodUserSystem us WHERE us.contentProvider.contentProviderId = :contentProviderId")
					.setParameter("contentProviderId", contentProviderId)
					.list();
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Erro ao listar dados de e-mail: ", e);
		}
		return listIpvodUserSystem;
	}

}
