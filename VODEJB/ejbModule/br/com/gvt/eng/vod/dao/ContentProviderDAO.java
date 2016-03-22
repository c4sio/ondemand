package br.com.gvt.eng.vod.dao;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodContentProvider;

@Stateless
public class ContentProviderDAO extends GenericDAO<IpvodContentProvider> {

	public ContentProviderDAO() {
		super(IpvodContentProvider.class);
	}

	@SuppressWarnings("unchecked")
	public List<IpvodContentProvider> findProviders() {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("providerId as providerId ");
		hql.append("from IpvodContentProvider ");
		hql.append("group by providerId  ");
		hql.append("order by providerId ");
		Query query = getSession().createQuery(String.valueOf(hql));

		query.setResultTransformer(new AliasToBeanResultTransformer(IpvodContentProvider.class));

		return query.list();
	}

}
