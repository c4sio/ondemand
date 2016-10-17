package br.com.gvt.eng.convoy.rest.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.json.JSONObject;
import br.com.gvt.eng.convoy.rest.ContentRest;
import br.com.gvt.eng.convoy.util.Util;
import br.com.gvt.eng.convoy.vo.IpvodContent;

import com.google.gson.Gson;

public class ContentRestImpl implements ContentRest {

	static Logger logger = Logger.getLogger(ContentRestImpl.class.getName());

	private HttpClient httpClient;

	@Override
	public List<IpvodContent> listAllContent() {
		List<IpvodContent> listContent = new ArrayList<IpvodContent>();
		try {
			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIConvoy() + "content");
			getRequest.addHeader("Content-Type", "application/json");
			getRequest.addHeader("Accept", "application/json");
			getRequest.addHeader("X-Account-Api-Key", Util.getApiKeyConvoy());

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				response.getEntity().getContent().close();
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(line);
			}

			String json = sb.toString();
			JSONObject jsonObject = new JSONObject(json);
			listContent.add(gson.fromJson(jsonObject.toString(),
					IpvodContent.class));

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao listar conteudo Convoy: ", e);
		}
		return listContent;
	}

	@Override
	public IpvodContent checkContent(String filename) {
		IpvodContent ipvodContent = new IpvodContent();
		try {
			logger.info("Verifica dados de conteudo no Convoy para o arquivo "
					+ filename);

			httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(Util.getURIConvoy() + "content/"
					+ filename);
			getRequest.addHeader("Content-Type", "application/json");
			getRequest.addHeader("Accept", "application/json");
			getRequest.addHeader("X-Account-Api-Key", Util.getApiKeyConvoy());

			HttpResponse response = httpClient.execute(getRequest);

			System.out.println("Response: "
					+ response.getStatusLine().getStatusCode());
			logger.info("Response: " + response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				response.getEntity().getContent().close();
				System.out.println(response.getEntity().getContent());
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				return ipvodContent;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(line);
				logger.info(line);
			}

			String json = sb.toString();
			JSONObject jsonObject = new JSONObject(json);
			ipvodContent = gson.fromJson(jsonObject.toString(),
					IpvodContent.class);
		} catch (Exception e) {
			System.out.println("Erro ao buscar content - " + e.getMessage());
			logger.error("Erro ao buscar conteudo Convoy: ", e);
		}
		return ipvodContent;
	}

	@Override
	public void deleteContent(IpvodContent ipvodContent) {
		try {
			httpClient = HttpClientBuilder.create().build();
			HttpDelete deleteRequest = new HttpDelete(Util.getURIConvoy()
					+ "content/" + ipvodContent.getTitle());
			deleteRequest.addHeader("Content-Type", "application/json");
			deleteRequest.addHeader("Accept", "application/json");
			deleteRequest
					.addHeader("X-Account-Api-Key", Util.getApiKeyConvoy());

			HttpResponse response = httpClient.execute(deleteRequest);

			if (response.getStatusLine().getStatusCode() != 204) {
				response.getEntity().getContent().close();
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao remover conteudo Convoy: ", e);
		}
	}
}
