package br.com.gvt.eng.convoy.rest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.json.JSONArray;
import br.com.gvt.eng.convoy.json.JSONObject;
import br.com.gvt.eng.convoy.properties.PropertiesConfig;
import br.com.gvt.eng.convoy.rest.IngestStageRest;
import br.com.gvt.eng.convoy.util.Util;
import br.com.gvt.eng.convoy.vo.IpvodIngestStage;
import br.com.gvt.eng.convoy.vo.IpvodIngestType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IngestStageRestImpl implements IngestStageRest {

	static Logger logger = Logger
			.getLogger(IngestStageRestImpl.class.getName());

	private HttpClient httpClient;

	@Override
	public List<IpvodIngestStage> searchDataForProcess(long stageId) {
		List<IpvodIngestStage> listData = new ArrayList<IpvodIngestStage>();
		try {
			logger.info("Busca registros na base do Ingest - tipo: " + stageId);
			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIIpvod()
					+ "ingest/type/" + stageId);
			getRequest.addHeader("content-type", "application/json");
			// Executa a acao
			HttpResponse response = httpClient.execute(getRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				return listData;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			String json = sb.toString();
			JSONArray jsonArray = new JSONArray(json);
			JSONObject jsonObject = null;

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				listData.add(gson.fromJson(jsonObject.toString(),
						IpvodIngestStage.class));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("Erro ao buscar registros na base do Ingest: ", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao buscar registros na base do Ingest: ", e);
		}
		return listData;
	}

	@Override
	public void updateIngest(IpvodIngestStage ipvodIngestStage) {
		try {
			logger.info("Atualiza registros na base do Ingest - ID: "
					+ ipvodIngestStage.getId());
			httpClient = HttpClientBuilder.create().build();
			HttpPut putRequest = new HttpPut(Util.getURIIpvod() + "ingest");
			putRequest.addHeader("content-type", "application/json");

			Properties prop = PropertiesConfig.getProp();
			int statusFinished = Integer.parseInt(prop
					.getProperty("status.finished"));

			IpvodIngestType ipvodIngestType = new IpvodIngestType();
			// Setando o valor para FINISHED
			ipvodIngestType.setId(statusFinished);
			ipvodIngestStage.setStageType(ipvodIngestType);

			Gson gson = new Gson();
			String data = gson.toJson(ipvodIngestStage);
			System.out.println("Atualiza Dados Ingest: " + data);
			logger.info("Atualiza Dados Ingest: " + data);
			putRequest.setEntity(new StringEntity(data, ContentType.create(
					"application/json", "UTF-8")));

			// Executa a acao
			HttpResponse response = httpClient.execute(putRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK
					&& response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao atualizar registros na base do Ingest: ", e);
		}
	}

	@Override
	public IpvodIngestStage findIngestById(long ingestId) {
		IpvodIngestStage ipvodIngestStage = new IpvodIngestStage();
		try {
			logger.info("Busca registro na base do Ingest - ID: " + ingestId);
			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIIpvod() + "ingest/"
					+ ingestId);
			getRequest.addHeader("content-type", "application/json");
			// Executa a acao
			HttpResponse response = httpClient.execute(getRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			String json = sb.toString();
			JSONObject jsonObject = new JSONObject(json);
			Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
					Date.class, new DateTypeAdapter()).create();
			ipvodIngestStage = gson.fromJson(jsonObject.toString(),
					IpvodIngestStage.class);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao buscar registro por ID na base do Ingest: ",
					e);
		}
		return ipvodIngestStage;
	}

	/**
	 * Adapater para data - Json
	 */
	private static class DateTypeAdapter implements JsonSerializer<Date>,
			JsonDeserializer<Date> {
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return typeOfT == Date.class ? new Date(json.getAsLong())
					: new java.sql.Date(json.getAsLong());
		}

		public JsonElement serialize(Date src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.getTime());
		}
	}
}
