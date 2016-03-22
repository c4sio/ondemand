package br.com.gvt.vod.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodSession;
import br.com.gvt.eng.vod.vo.ExecutionVO;
import br.com.gvt.eng.vod.vo.TopExecutionVO;
import br.com.gvt.eng.vod.vo.TransactionVO;

@Local
public interface SessionFacade {

	public abstract void save(IpvodSession ipvodSession);

	public abstract void delete(IpvodSession ipvodSession);

	public abstract IpvodSession update(IpvodSession ipvodSession);

	public abstract List<IpvodSession> findAll();

	public abstract IpvodSession find(long entityID);

	public abstract List<TopExecutionVO> listTopExecutions(Integer top);

	public abstract List<ExecutionVO> findSubscriberExecutions(
			String subscriberId, Date playTimeStart, Date playTimeEnd)
			throws BusinessException;

	public abstract void delete(List<IpvodSession> ipvodSessions);

	public abstract List<TransactionVO> findTransactions(String subscriberId,
			Date playTimeStart, Date playTimeEnd);

}
