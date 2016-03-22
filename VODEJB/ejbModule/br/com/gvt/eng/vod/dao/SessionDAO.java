package br.com.gvt.eng.vod.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodSession;
import br.com.gvt.eng.vod.vo.ExecutionVO;
import br.com.gvt.eng.vod.vo.TopExecutionVO;
import br.com.gvt.eng.vod.vo.TransactionVO;

@Stateless
public class SessionDAO extends GenericDAO<IpvodSession> {

	public SessionDAO() {
		super(IpvodSession.class);
	}

	public void deleteSession(IpvodSession ipvodSession) {
		super.delete(ipvodSession.getSessionId(), IpvodSession.class);
	}

	@SuppressWarnings("unchecked")
	public List<TopExecutionVO> listTopExecutions(Integer top) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("a.assetId as assetId, a.title as title, count(*) as totalExecutions ");
		hql.append("from IpvodSession as s ");
		hql.append("left join s.ipvodPurchase as p ");
		hql.append("left join p.ipvodAsset as a ");
		hql.append("left join s.ipvodEventType as et ");
		hql.append("where et.eventTypeId = 1 ");
		hql.append("group by a.assetId, a.title ");
		hql.append("order by totalExecutions desc");

		Query query = getSession().createQuery(String.valueOf(hql));

		if (top != null) {
			query.setFirstResult(0);
			query.setMaxResults(top);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				TopExecutionVO.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ExecutionVO> findSubscriberExecutions(String subscriberId,
			Date playTimeStart, Date playTimeEnd) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("s.playTime as playTime, a.title as title, ");
		hql.append("cp.providerName as provider, c.description as genre, ");
		hql.append("e.mac as mac, s.eventDate as sessionInstantiationTime, ");
		hql.append("s.eventDateEnd as sessionDestructionTime, ");
		hql.append("s.responsecode as responseCode, s.duration as minutesPlayed, ");
		hql.append("a.assetId as assetId, p.purchaseId as purchaseId ");
		hql.append("from IpvodSession as s ");
		hql.append("join s.ipvodPurchase as p ");
		hql.append("join p.ipvodAsset as a ");
		hql.append("join p.ipvodEquipment as e ");
		hql.append("join a.ipvodCategory1 as c ");
		hql.append("join a.ipvodContentProvider as cp ");
		hql.append("join e.ipvodUser u ");
		hql.append("where u.crmCustomerId =:subscriberId ");

		if (playTimeStart != null) {
			hql.append("and s.eventDate >=:playTimeStart ");
		}

		if (playTimeEnd != null) {
			hql.append("and s.eventDateEnd <=:playTimeEnd ");
		}

		hql.append("order by s.eventDate");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("subscriberId", subscriberId);

		if (playTimeStart != null) {
			query.setParameter("playTimeStart", playTimeStart);
		}

		if (playTimeEnd != null) {
			query.setParameter("playTimeEnd", playTimeEnd);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ExecutionVO.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<TransactionVO> findTransactions(String subscriberId,
			Date playTimeStart, Date playTimeEnd) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("s.sessionId as playKey, s.playTime as playTime, ");
		hql.append("s.duration as minutesPlayed, '' as service, ");
		hql.append("e.mac as macAdress, a.title as title, cp.providerName as provider, ");
		hql.append("u.crmCustomerId as customerIdentity ");
		hql.append("from IpvodSession as s ");
		hql.append("join s.ipvodPurchase as p ");
		hql.append("join p.ipvodAsset as a ");
		hql.append("join p.ipvodEquipment as e ");
		hql.append("join a.ipvodCategory1 as c ");
		hql.append("join a.ipvodContentProvider as cp ");
		hql.append("join e.ipvodUser as u ");
		hql.append("where u.crmCustomerId =:subscriberId ");

		if (playTimeStart != null) {
			hql.append("and s.eventDate >=:playTimeStart ");
		}

		if (playTimeEnd != null) {
			hql.append("and s.eventDateEnd <=:playTimeEnd ");
		}

		hql.append("order by s.eventDate");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("subscriberId", subscriberId);

		if (playTimeStart != null) {
			query.setParameter("playTimeStart", playTimeStart);
		}

		if (playTimeEnd != null) {
			query.setParameter("playTimeEnd", playTimeEnd);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				TransactionVO.class));
		return query.list();
	}
}