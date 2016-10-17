package br.com.gvt.eng.convoy.rest.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.json.JSONArray;
import br.com.gvt.eng.convoy.json.JSONObject;
import br.com.gvt.eng.convoy.rest.ConvoyDataRest;
import br.com.gvt.eng.convoy.util.Util;
import br.com.gvt.eng.convoy.vo.IpvodConvoyData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ConvoyDataRestImpl implements ConvoyDataRest {

	static Logger logger = Logger.getLogger(ConvoyDataRestImpl.class.getName());

	private HttpClient httpClient;

	@Override
	public void saveConvoyData(IpvodConvoyData ipvodConvoyData) {
		try {
			logger.info("Salvando dados de arquivo "
					+ ipvodConvoyData.getNameFile());

			httpClient = HttpClientBuilder.create().build();
			HttpPut putRequest = new HttpPut(Util.getURIIpvod() + "convoy/save");
			putRequest.addHeader("Content-Type", "application/json");

			Gson gson = new Gson();
			String data = gson.toJson(ipvodConvoyData);
			System.out.println("Salva Dados: " + data);
			logger.info("Salva Dados: " + data);
			putRequest.setEntity(new StringEntity(data, ContentType.create(
					"application/json", "UTF-8")));

			// Executa a acao
			HttpResponse response = httpClient.execute(putRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao salvar dados do Convoy na base: ", e);
		}
	}

	@Override
	public IpvodConvoyData update(IpvodConvoyData ipvodConvoyData) {
		IpvodConvoyData convoyData = new IpvodConvoyData();
		try {
			logger.info("Atualizando dados de arquivo "
					+ ipvodConvoyData.getNameFile());

			httpClient = HttpClientBuilder.create().build();
			HttpPut putRequest = new HttpPut(Util.getURIIpvod()
					+ "convoy/update");
			putRequest.addHeader("Content-Type", "application/json");

			Gson gson = new Gson();
			String data = gson.toJson(ipvodConvoyData);
			System.out.println("Atualiza Dados: " + data);
			logger.info("Atualiza Dados: " + data);
			putRequest.setEntity(new StringEntity(data, ContentType.create(
					"application/json", "UTF-8")));

			// Executa a acao
			HttpResponse response = httpClient.execute(putRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao atualizar dados do Convoy na base: ", e);
		}
		return convoyData;
	}

	@Override
	public void delete(Long convoyId) {
		try {
			logger.info("Deletando registro Convoy - ID: " + convoyId);

			httpClient = HttpClientBuilder.create().build();
			HttpDelete deleteRequest = new HttpDelete(Util.getURIIpvod()
					+ "convoy/delete/" + convoyId);
			deleteRequest.addHeader("Content-Type", "application/json");

			// Executa a acao
			HttpResponse response = httpClient.execute(deleteRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar dados do Convoy na base: ", e);
		}
	}

	@Override
	public List<IpvodConvoyData> findAllLessDone() {
		List<IpvodConvoyData> listConvoyData = new ArrayList<IpvodConvoyData>();
		try {
			logger.info("Buscar registros do Convoy na base.");

			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIIpvod()
					+ "convoy/execute");
			getRequest.addHeader("Content-Type", "application/json");

			// Executa a acao
			HttpResponse response = httpClient.execute(getRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				return listConvoyData;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			String json = sb.toString();
			JSONArray jsonArray = new JSONArray(json);
			JSONObject jsonObject = null;

			Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
					Date.class, new DateTypeAdapter()).create();

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				listConvoyData.add(gson.fromJson(jsonObject.toString(),
						IpvodConvoyData.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao buscar dados do Convoy na base: ", e);
		}
		return listConvoyData;
	}

	@Override
	public IpvodConvoyData find(Long entityID) {
		IpvodConvoyData ipvodConvoyData = new IpvodConvoyData();
		try {
			logger.info("Buscar registro do Convoy na base - ID: " + entityID);
			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIIpvod() + "convoy/"
					+ entityID);
			getRequest.addHeader("Content-Type", "application/json");
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
			ipvodConvoyData = gson.fromJson(jsonObject.toString(),
					IpvodConvoyData.class);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao buscar registro do Convoy na base: ", e);
		}
		return ipvodConvoyData;
	}

	@Override
	public boolean checkEndOfProcess(Long ingestID) {
		List<IpvodConvoyData> listConvoyData = new ArrayList<IpvodConvoyData>();
		try {
			logger.info("Buscar registro do Convoy na base - ingestID: "
					+ ingestID);
			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIIpvod() + "convoy/"
					+ ingestID + "/ingest");
			getRequest.addHeader("Content-Type", "application/json");

			// Executa a acao
			HttpResponse response = httpClient.execute(getRequest);

			// Verifica o retorno
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				return false;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			String json = sb.toString();
			JSONArray jsonArray = new JSONArray(json);
			JSONObject jsonObject = null;

			Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
					Date.class, new DateTypeAdapter()).create();

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				listConvoyData.add(gson.fromJson(jsonObject.toString(),
						IpvodConvoyData.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao buscar registro do Convoy na base: ", e);
			return false;
		}
		return verifyAllRegisters(listConvoyData);
	}

	/**
	 * @param listConvoyData
	 * @return boolean
	 */
	private boolean verifyAllRegisters(List<IpvodConvoyData> listConvoyData) {
		boolean finished = true;
		try {
			logger.info("Verifica se todos os registros com mesmo IngestId foram executados com sucesso.");
			for (IpvodConvoyData ipvodConvoyData : listConvoyData) {
				if (!ipvodConvoyData.getStatusConvoy().equalsIgnoreCase("done")) {
					finished = false;
				}
			}
		} catch (Exception e) {
			logger.error(
					"Erro ao verificar final do processo arquivos Convoy: ", e);
		}
		return finished;
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