package br.com.gvt.eng.vod.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.OrFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import br.com.gvt.eng.vod.exception.rest.RestException;
import br.com.gvt.eng.vod.util.ConnectionElasticSearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Stateless
public class ElasticSearchDAO {
	
	public List<Long> searchIdsByText(UriInfo uriInfo) throws RestException {

		String searchIndexes = uriInfo.getQueryParameters().getFirst("q");

		Client client = ConnectionElasticSearch.getConnection();

		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return null;
		}
		
		SearchRequestBuilder req = client.prepareSearch("catalog")
				.setTypes("asset").setSize(50).setSearchType(SearchType.SCAN)
				.setScroll(new TimeValue(60000))
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

		OrFilterBuilder filter = FilterBuilders.orFilter(
				FilterBuilders.queryFilter(QueryBuilders.matchQuery("director", searchIndexes).operator(Operator.AND)),
				FilterBuilders.queryFilter(QueryBuilders.matchQuery("description", searchIndexes).operator(Operator.AND)),
				FilterBuilders.queryFilter(QueryBuilders.matchQuery("episodeName", searchIndexes).operator(Operator.AND)),
				FilterBuilders.queryFilter(QueryBuilders.matchQuery("title", searchIndexes).operator(Operator.AND)),
				FilterBuilders.queryFilter(QueryBuilders.matchQuery("country", searchIndexes).operator(Operator.AND)));
		req.setPostFilter(filter);

		try {
			SearchResponse response = req.execute().actionGet();
			List<Long> ids = new ArrayList<Long>();
			
			while (true) {
				for (SearchHit hit : response.getHits().getHits()) {
					ids.add(new Long(hit.getId()));
				}
				response = client.prepareSearchScroll(response.getScrollId())
						.setScroll(new TimeValue(600000)).execute().actionGet();
				// Break condition: No hits are returned
				if (response.getHits().getHits().length == 0) {
					break;
				}
			}
			client.close();
			if (ids.isEmpty()) {
				throw RestException.getNoContent();
			}
			return ids;
			
		} catch (Exception e) {
			client.close();
			throw e;
		}

	}

	public void updateElasticSearchTerms(Map<String, Integer> value, String id)
			throws RestException {

		Client client = ConnectionElasticSearch.getConnection();
		
		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return;
		}
		
		try {
			Gson gson = new GsonBuilder().serializeNulls().create();
			String json = "{\"terms\":" + gson.toJson(value.keySet()) + "}";
			
			client.prepareIndex("ipvod", "terms", id).setSource(json).execute().actionGet();
			client.close();
		} catch (Exception e) {
			client.close();
			throw e;
		}
	}

	public String getSearchTerms(String id) throws RestException {

		Client client = ConnectionElasticSearch.getConnection();
		
		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return null;
		}
		
		try {
			GetResponse resp = client.prepareGet().setIndex("ipvod")
					.setType("terms").setId(id).execute().actionGet();
			if (resp.getSourceAsString() == null) {
				throw RestException.BAD_REQUEST;
			}
			client.close();
			return resp.getSourceAsString().replace("{\"terms\":", "")
					.replace("}", "");
		} catch (Exception e) {
			client.close();
			throw e;
		}
	}
	
	public void saveElasticSearchAsset(Long id, String ipvodAsset) throws RestException {
		Client client = ConnectionElasticSearch.getConnection();
		
		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return;
		}
		
		try {
			client.prepareIndex("ipvod", "temp", id.toString()).setSource(ipvodAsset).execute().actionGet();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			client.close();
		}
	}

	public String getElasticSearchAsset(String id) throws RestException {

		Client client = ConnectionElasticSearch.getConnection();
		
		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return null;
		}
		
		try {
			GetResponse resp = client.prepareGet().setIndex("ipvod")
					.setType("temp").setId(id).execute().actionGet();
			client.close();
			if (resp.getSourceAsString() == null) {
				return null;
			}
			
			return resp.getSourceAsString();
		} catch (Exception e) {
			client.close();
			throw e;
		}
	}
	
	public void deleteElasticSearchAsset(String id) throws RestException {
		Client client = ConnectionElasticSearch.getConnection();
		
		if (((TransportClient) client).connectedNodes().isEmpty()) {
			client.close();
			return;
		}
		
		try {
			client.prepareDelete().setIndex("ipvod")
				.setType("temp").setId(id).execute().actionGet();
			client.close();
		} catch (Exception e) {
			client.close();
			throw e;
		}
	}
}
