package br.com.gvt.eng.vod.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.util.Filter;
import br.com.gvt.eng.vod.util.FilterRules;

public abstract class GenericDAO<EntityClass> {

	public GenericDAO(Class<EntityClass> entityClass) {
		this.entityClass = entityClass;
	}

	private final static String UNIT_NAME = "VODEJB";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	private Class<EntityClass> entityClass;

	/**
	 * @param Entity
	 */
	public void save(EntityClass Entity) {
		em.persist(Entity);
		// em.flush();
	}

	/**
	 * @param id
	 * @param classe
	 */
	protected void delete(Object id, Class<EntityClass> classe) {
		EntityClass entityToBeRemoved = em.getReference(classe, id);
		em.remove(entityToBeRemoved);
		em.flush();
	}

	/**
	 * @param entity
	 * @return result of update
	 */
	public EntityClass update(EntityClass entity) {
		return em.merge(entity);
	}

	/**
	 * @param entityID
	 * @return result of data
	 */
	public EntityClass find(long entityID) {
		return em.find(entityClass, entityID);
	}

	/**
	 * @return List of data
	 */
	@SuppressWarnings("unchecked")
	public List<EntityClass> findAll() {
		CriteriaQuery<EntityClass> cq = (CriteriaQuery<EntityClass>) em
				.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	/**
	 * @param namedQuery
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected EntityClass findOneResult(String namedQuery,
			Map<String, Object> parameters) {
		EntityClass result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			/*
			 * Method that will populate parameters if they are passed not null
			 * and empty
			 */
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (EntityClass) query.getSingleResult();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected List<EntityClass> findResultByParameterPagination(
			String namedQuery, Map<String, Object> parameters,
			Map<String, Object> pagination) {
		List<EntityClass> result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			if (pagination != null) {
				if (pagination.get(IpvodConstants.URLPARAM_PAGE_NUMBER) != null) {
					int pageNumber = Integer.parseInt((String) pagination
							.get(IpvodConstants.URLPARAM_PAGE_NUMBER));

					int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
					if (pagination
							.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
						maxResults = Integer
								.parseInt((String) pagination
										.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
					}

					query.setMaxResults(maxResults);
					query.setFirstResult((pageNumber * maxResults) - maxResults);

					pagination
							.remove(IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
					pagination.remove(IpvodConstants.URLPARAM_PAGE_NUMBER);

				}

				if (pagination.get(IpvodConstants.URLPARAM_FIRST_INDEX) != null) {
					int firstResult = Integer.parseInt((String) pagination
							.get(IpvodConstants.URLPARAM_FIRST_INDEX));

					int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
					if (pagination
							.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
						maxResults = Integer
								.parseInt((String) pagination
										.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
					}

					query.setFirstResult(firstResult);
					query.setMaxResults(maxResults);

					parameters
							.remove(IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
					parameters.remove(IpvodConstants.URLPARAM_FIRST_INDEX);
				}
			}

			/*
			 * Method that will populate parameters if they are passed not null
			 * and empty
			 */
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (List<EntityClass>) query.getResultList();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected List<EntityClass> findResultByParameter(String namedQuery,
			Map<String, Object> parameters) {
		List<EntityClass> result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			/*
			 * Method that will populate parameters if they are passed not null
			 * and empty
			 */
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (List<EntityClass>) query.getResultList();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param query
	 * @param parameters
	 */
	private void populateQueryParameters(Query query,
			Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	public List<EntityClass> findResultComplexQuery(UriInfo uriInfo) {
		List<EntityClass> result = null;
		try {
			Map<String, Object> parameters = parseUriInfo(uriInfo);

			Session session = (Session) em.getDelegate();
			Criteria criteria = session.createCriteria(entityClass);

			criteria = getPaginationCriteria(criteria, parameters);

			criteria = getComplexQueryCriteria(criteria, parameters);

			result = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Criteria getPaginationCriteria(Criteria criteria,
			Map<String, Object> parameters) {
		if (parameters.get(IpvodConstants.URLPARAM_PAGE_NUMBER) != null) {
			int pageNumber = Integer.parseInt((String) parameters
					.get(IpvodConstants.URLPARAM_PAGE_NUMBER));

			int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
			if (parameters.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
				maxResults = Integer.parseInt((String) parameters
						.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
			}

			criteria.setMaxResults(maxResults);
			criteria.setFirstResult((pageNumber * maxResults) - maxResults);

			parameters.remove(IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
			parameters.remove(IpvodConstants.URLPARAM_PAGE_NUMBER);

		}

		if (parameters.get(IpvodConstants.URLPARAM_FIRST_INDEX) != null) {
			int firstResult = Integer.parseInt((String) parameters
					.get(IpvodConstants.URLPARAM_FIRST_INDEX));

			int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
			if (parameters.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
				maxResults = Integer.parseInt((String) parameters
						.get(IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
			}

			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);

			parameters.remove(IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
			parameters.remove(IpvodConstants.URLPARAM_FIRST_INDEX);
		}

		String order = (String) parameters.get(IpvodConstants.URLPARAM_ORDER);
		if (order != null) {
			String idField = null;
			if (parameters.get(IpvodConstants.URLPARAM_ORDER_BY) != null) {
				idField = (String) parameters
						.get(IpvodConstants.URLPARAM_ORDER_BY);
			} else {
				for (Field field : entityClass.getDeclaredFields()) {
					for (Annotation annotation : field.getAnnotations()) {
						if (annotation.annotationType().getName()
								.equals("javax.persistence.Id")) {
							idField = field.getName();
							break;
						}
					}
					if (idField != null) {
						break;
					}
				}
			}
			if (order.equals("asc")) {
				criteria.addOrder(Order.asc(idField));
			} else {
				criteria.addOrder(Order.desc(idField));
			}
			parameters.remove(IpvodConstants.URLPARAM_ORDER);
		}
		return criteria;
	}

	private String getMethodName(String variable) {
		return "get" + variable.substring(0, 1).toUpperCase()
				+ variable.substring(1);
	}

	private Criteria getComplexQueryCriteria(Criteria criteria,
			Map<String, Object> parameters) throws NoSuchMethodException,
			SecurityException, ParseException {
		Filter filter = (Filter) parameters
				.get(IpvodConstants.URLPARAM_FILTERS);
		if (filter != null && !filter.getRules().isEmpty()) {
			Junction andOrJunction = null;
			if (filter.getGroupOp().equals("AND")) {
				andOrJunction = Restrictions.conjunction();
			} else if (filter.getGroupOp().equals("OR")) {
				andOrJunction = Restrictions.disjunction();
			}
			for (int i = 0; i < filter.getRules().size(); i++) {
				FilterRules rule = filter.getRules().get(i);
				Class<?> returnType = null;

				Method m = null;
				for (String str : rule.getField().split("\\.")) {
					if (m == null) {
						m = entityClass.getMethod(getMethodName(str));
					} else {
						m = returnType.getMethod(getMethodName(str));
					}
					returnType = m.getReturnType();
				}

				Object value = null;
				if (returnType == String.class) {
					value = rule.getData();
				} else if (returnType == Integer.class
						|| returnType == int.class) {
					value = new Integer(rule.getData());
				} else if (returnType == Long.class || returnType == long.class) {
					value = new Long(rule.getData());
				} else if (returnType == Double.class
						|| returnType == double.class) {
					value = new Double(rule.getData());
				} else if (returnType == Date.class) {
					value = new SimpleDateFormat("dd/MM/yyyy").parseObject(rule
							.getData());
				}
				String op = rule.getOp();
				switch (op) {

				case FilterRules.OPERATION_EQUAL:
					if (rule.getField().indexOf(".") > -1) {
						String alias = "alias" + i;
						criteria.createAlias(rule.getField().split("\\.")[0],
								alias, JoinType.INNER_JOIN);
						criteria.add(Restrictions.eq(alias + "."
								+ rule.getField().split("\\.")[1], value));
					} else {
						andOrJunction.add(Restrictions.eq(rule.getField(),
								value));
					}
					break;
				case FilterRules.OPERATION_NOT_EQUAL:
					andOrJunction.add(Restrictions.not(Restrictions.eq(
							rule.getField(), value)));
					break;
				case FilterRules.OPERATION_LESS:
					andOrJunction.add(Restrictions.lt(rule.getField(), value));
					break;
				case FilterRules.OPERATION_LESS_OR_EQUAL:
					andOrJunction.add(Restrictions.le(rule.getField(), value));
					break;
				case FilterRules.OPERATION_GREATER:
					andOrJunction.add(Restrictions.gt(rule.getField(), value));
					break;
				case FilterRules.OPERATION_GREATER_OR_EQUAL:
					andOrJunction.add(Restrictions.ge(rule.getField(), value));
					break;
				case FilterRules.OPERATION_BEGINS_WITH:
					andOrJunction.add(Restrictions.ilike(rule.getField(),
							(String) value, MatchMode.START));
					break;
				case FilterRules.OPERATION_DOES_NOT_BEGIN_WITH:
					andOrJunction.add(Restrictions.not(Restrictions.ilike(
							rule.getField(), (String) value, MatchMode.START)));
					break;
				case FilterRules.OPERATION_ENDS_WITH:
					andOrJunction.add(Restrictions.ilike(rule.getField(),
							(String) value, MatchMode.END));
					break;
				case FilterRules.OPERATION_DOES_NOT_END_WITH:
					andOrJunction.add(Restrictions.not(Restrictions.ilike(
							rule.getField(), (String) value, MatchMode.END)));
					break;
				case FilterRules.OPERATION_CONTAINS:
					if (rule.getField().indexOf(".") > -1) {
						String alias = "alias" + i;
						criteria.createAlias(rule.getField().split("\\.")[0],
								alias, JoinType.INNER_JOIN);
						criteria.add(Restrictions.ilike(alias + "."
								+ rule.getField().split("\\.")[1],
								(String) value, MatchMode.ANYWHERE));
					} else {
						andOrJunction.add(Restrictions.ilike(rule.getField(),
								(String) value, MatchMode.ANYWHERE));
					}

					// if (rule.getField().indexOf(".") > -1) {
					// String alias = "alias" + i;
					// criteria.createAlias(rule.getField(), alias ,
					// JoinType.INNER_JOIN);
					// criteria.add(Restrictions.ilike(alias,
					// (String) value, MatchMode.ANYWHERE));
					// } else {
					// andOrJunction.add(Restrictions.ilike(rule.getField(),
					// (String) value, MatchMode.ANYWHERE));
					// }

					break;
				case FilterRules.OPERATION_DOES_NOT_CONTAIN:
					andOrJunction
							.add(Restrictions.not(Restrictions.ilike(
									rule.getField(), (String) value,
									MatchMode.ANYWHERE)));
					break;
				// case FilterRules.OPERATION_IS_IN:
				// case FilterRules.OPERATION_IS_NOT_IN:
				case FilterRules.OPERATION_IS_NULL:
					andOrJunction.add(Restrictions.isEmpty(rule.getField()));
					break;
				case FilterRules.OPERATION_IS_NOT_NULL:
					andOrJunction.add(Restrictions.isNotEmpty(rule.getField()));
					break;
				}
			}
			criteria.add(andOrJunction);
		}

		parameters.remove(IpvodConstants.URLPARAM_ORDER);
		parameters.remove(IpvodConstants.URLPARAM_ORDER_BY);
		parameters.remove(IpvodConstants.URLPARAM_PAGE_NUMBER);
		parameters.remove(IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
		parameters.remove(IpvodConstants.URLPARAM_FIRST_INDEX);

		Set<String> params = parameters.keySet();
		for (String param : params) {
			if (param != IpvodConstants.URLPARAM_FILTERS) {
				Method f = entityClass.getMethod("get"
						+ param.substring(0, 1).toUpperCase()
						+ param.substring(1));
				Class<?> returnType = f.getReturnType();
				String strParam = (String) parameters.get(param);
				if (returnType == String.class) {
					criteria.add(Restrictions.ilike(param,
							(String) parameters.get(param), MatchMode.ANYWHERE));
				} else if (returnType == Integer.class
						|| returnType == int.class) {
					criteria.add(Restrictions.eq(param, new Integer(strParam)));
				} else if (returnType == Long.class || returnType == long.class) {
					criteria.add(Restrictions.eq(param, new Long(strParam)));
				} else if (returnType == Date.class) {
					criteria.add(Restrictions.eq(param, new SimpleDateFormat(
							"dd/MM/yyyy").parseObject(strParam)));
				}
			}
		}
		return criteria;
	}

	public Long countResultComplexQuery(UriInfo uriInfo) {
		try {
			Map<String, Object> parameters = parseUriInfo(uriInfo);

			Session session = (Session) em.getDelegate();
			Criteria criteria = session.createCriteria(entityClass);

			criteria = getComplexQueryCriteria(criteria, parameters);

			return (Long) criteria.setProjection(Projections.rowCount())
					.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public Map<String, Object> parseUriInfo(UriInfo uriInfo) {
		Map<String, Object> map1 = new HashMap<String, Object>();

		List<String> searchIndexes = uriInfo.getQueryParameters().get(
				IpvodConstants.URLPARAM_SEARCH_INDEX);
		if (searchIndexes != null) {
			for (String searchIndex : searchIndexes) {
				if (searchIndex != null
						&& uriInfo.getQueryParameters().getFirst(searchIndex) != null) {
					map1.put(searchIndex, uriInfo.getQueryParameters()
							.getFirst(searchIndex));
				}
			}
		}

		String rows = uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_REGISTER_PER_PAGE);
		if (rows != null && !rows.equals("-1")) {
			map1.put(IpvodConstants.URLPARAM_REGISTER_PER_PAGE, rows);
			String pageNum = uriInfo.getQueryParameters().getFirst(
					IpvodConstants.URLPARAM_PAGE_NUMBER);
			if (pageNum != null) {
				map1.put(IpvodConstants.URLPARAM_PAGE_NUMBER, pageNum);
			}
		}

		String order = uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_ORDER);
		if (order != null) {
			map1.put(IpvodConstants.URLPARAM_ORDER, order);
		}
		String orderBy = uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_ORDER_BY);
		if (orderBy != null) {
			map1.put(IpvodConstants.URLPARAM_ORDER_BY, orderBy);
		}

		List<String> strList = uriInfo.getQueryParameters().get(
				IpvodConstants.URLPARAM_FILTERS);
		List<Filter> filterList = new ArrayList<Filter>();
		if (strList != null) {
			for (String filter2 : strList) {
				try {
					filterList.add(new ObjectMapper().readValue(filter2,
							Filter.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Filter filter = null;
		for (Filter f : filterList) {
			if (filter == null) {
				filter = f;
			} else {
				filter.getRules().addAll(f.getRules());
			}
		}
		map1.put(IpvodConstants.URLPARAM_FILTERS, filter);
		return map1;
	}

	public EntityManager getEm() {
		return em;
	}

	/**
	 * @param namedQuery
	 * @param parameters
	 */
	protected int deleteByParameter(String namedQuery,
			Map<String, Object> parameters) {

		try {
			Query query = em.createNamedQuery(namedQuery);

			/*
			 * Method that will populate parameters if they are passed not null
			 * and empty
			 */
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			return query.executeUpdate();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}

	protected Session getSession() {
		return (Session) em.getDelegate();
	}
}
