package br.com.gvt.eng.convoy.rest.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import br.com.gvt.eng.convoy.json.JSONObject;
import br.com.gvt.eng.convoy.rest.UploadRest;
import br.com.gvt.eng.convoy.util.Util;
import br.com.gvt.eng.convoy.vo.IpvodContent;
import br.com.gvt.eng.convoy.vo.IpvodVariants;

import com.google.gson.Gson;

public class UploadRestImpl implements UploadRest {

	static Logger logger = Logger.getLogger(UploadRestImpl.class.getName());

	private HttpClient httpClient;

	@Override
	public IpvodContent checkStatusUpload(String jobId) {
		IpvodContent ipvodContent = new IpvodContent();
		try {
			logger.info("Verificando status do arquivo no Convoy - ID " + jobId);
			logger.info("URI Convoy: " + Util.getIPConvoy() + jobId);

			httpClient = HttpClientBuilder.create().disableRedirectHandling()
					.build();
			HttpGet getRequest = new HttpGet(Util.getIPConvoy() + jobId);
			getRequest.addHeader("Content-Type", "application/json");
			getRequest.addHeader("Accept", "application/json");
			getRequest.addHeader("X-Account-Api-Key", Util.getApiKeyConvoy());

			HttpResponse response = httpClient.execute(getRequest);

			System.out.println("Response: "
					+ response.getStatusLine().getStatusCode());
			logger.info("Response: " + response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK
					&& response.getStatusLine().getStatusCode() != HttpStatus.SC_SEE_OTHER) {
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
			e.printStackTrace();
			logger.error("Erro ao verificar o status do job no Convoy: ", e);
		}
		return ipvodContent;
	}

	@Override
	public IpvodContent uploadFile(String fileName, String fileToConvoy,
			String title, String pathFile) {

		IpvodContent ipvodContent = new IpvodContent();
		try {
			logger.info("Executando o upload do arquivo " + fileName
					+ " no Convoy");
			httpClient = HttpClientBuilder.create().build();
			HttpPost postRequest = new HttpPost(Util.getURIConvoy() + "upload");
			postRequest.addHeader("Content-Type", "application/json");
			postRequest.addHeader("Accept", "application/json");
			postRequest.addHeader("X-Account-Api-Key", Util.getApiKeyConvoy());

			IpvodContent content = new IpvodContent();
			content.setFilename(fileName);
			content.setTitle(title);
			content.setContent_type(Util.getMimeTypeConvoy());

			System.out.println("\n\n8=====================D (  |*|  )\n\n");
			System.out.println("[content.setFilename: " + fileName + "]");
			logger.info("[content.setFilename: " + fileName + "]");
			System.out.println("[content.setTitle: " + title + "]");
			logger.info("[content.setTitle: " + title + "]");
			System.out.println("[content.setContent_type: "
					+ Util.getMimeTypeConvoy() + "]");
			logger.info("[content.setContent_type: " + Util.getMimeTypeConvoy()
					+ "]");

			String location = "ftp://" + Util.getFtpUser() + ":"
					+ Util.getFtpPassWord() + "@" + Util.getFtpHost()
					+ Util.getFtpWorkDirectory() + File.separator + pathFile
					+ File.separator + fileName;

			List<IpvodVariants> listVariants = new ArrayList<IpvodVariants>();
			IpvodVariants variants = new IpvodVariants();
			variants.setName(title);
			variants.setLocation(location);
			listVariants.add(variants);
			content.setVariants(listVariants);

			System.out.println("variants.setName: " + title + "]");
			logger.info("variants.setName: " + title + "]");
			System.out.println("[variants.setLocation: " + location + "]");
			logger.info("[variants.setLocation: " + location + "]");
			System.out.println("\n\n8=====================D (  |*|  )\n\n");

			Gson gson = new Gson();
			String data = gson.toJson(content);
			System.out.println("Upload Dados: " + data);
			logger.info("Upload Dados: " + data);
			// StringEntity userEntity = new StringEntity(data);
			// postRequest.setEntity(userEntity);
			postRequest.setEntity(new StringEntity(data, ContentType.create(
					"application/json", "UTF-8")));

			HttpResponse response = httpClient.execute(postRequest);

			System.out.println("Response: "
					+ response.getStatusLine().getStatusCode());
			logger.info("Response: " + response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() != 202) {
				response.getEntity().getContent().close();
				logger.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
				return ipvodContent;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

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
			e.printStackTrace();
			logger.error("Erro ao executar upload de arquivo no Convoy: ", e);
		}
		return ipvodContent;
	}
}