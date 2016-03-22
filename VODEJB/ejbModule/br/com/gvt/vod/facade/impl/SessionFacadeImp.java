package br.com.gvt.vod.facade.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.SessionDAO;
import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodSession;
import br.com.gvt.eng.vod.vo.ExecutionVO;
import br.com.gvt.eng.vod.vo.TopExecutionVO;
import br.com.gvt.eng.vod.vo.TransactionVO;
import br.com.gvt.vod.facade.SessionFacade;

@Stateless
public class SessionFacadeImp implements SessionFacade {

	@EJB
	private SessionDAO sessionDAO;

	@Override
	public void save(IpvodSession ipvodSession) {
		isSessionWithAllData(ipvodSession);
		sessionDAO.save(ipvodSession);
	}

	@Override
	public void delete(IpvodSession ipvodSession) {
		sessionDAO.deleteSession(ipvodSession);
	}

	@Override
	public void delete(List<IpvodSession> ipvodSessions) {
		for (IpvodSession ipvodSession : ipvodSessions) {
			delete(ipvodSession);
		}
	}

	@Override
	public IpvodSession update(IpvodSession ipvodSession) {
		isSessionWithAllData(ipvodSession);
		return sessionDAO.update(ipvodSession);
	}

	@Override
	public List<IpvodSession> findAll() {
		return sessionDAO.findAll();
	}

	@Override
	public IpvodSession find(long entityID) {
		return sessionDAO.find(entityID);
	}

	/**
	 * Validação de campos - Save/Update
	 * 
	 * @param ipvodSession
	 */
	private void isSessionWithAllData(IpvodSession ipvodSession) {
		boolean hasError = false;

		if (ipvodSession == null) {
			hasError = true;
		}

		if (hasError) {
			throw new IllegalArgumentException(
					"The asset is missing data. Check, they should have value.");
		}

	}

	@Override
	public List<TopExecutionVO> listTopExecutions(Integer top) {
		return sessionDAO.listTopExecutions(top);
	}

	@Override
	public List<ExecutionVO> findSubscriberExecutions(String subscriberId,
			Date playTimeStart, Date playTimeEnd) throws BusinessException {
		return sessionDAO.findSubscriberExecutions(subscriberId, playTimeStart,
				playTimeEnd);
	}

	@Override
	public List<TransactionVO> findTransactions(String subscriberId,
			Date playTimeStart, Date playTimeEnd) {
		return sessionDAO.findTransactions(subscriberId, playTimeStart,
				playTimeEnd);
	}

}
