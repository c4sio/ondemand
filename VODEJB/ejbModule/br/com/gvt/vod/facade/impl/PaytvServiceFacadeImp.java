package br.com.gvt.vod.facade.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.Stateless;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import br.com.gvt.eng.vod.json.JSONObject;
import br.com.gvt.eng.vod.vo.AppProductVO;
import br.com.gvt.vod.facade.PaytvServiceFacade;

import com.google.gson.Gson;

@Stateless
public class PaytvServiceFacadeImp implements PaytvServiceFacade {

	private HttpClient httpClient;

	@Override
	public AppProductVO findPackageHybridByService(String urlService,
			String SiebelOrder) {
		AppProductVO appProductVO = new AppProductVO();
		try {
			this.httpClient = HttpClientBuilder.create().build();

			HttpGet getRequest = new HttpGet(urlService + SiebelOrder);
			getRequest.addHeader("content-type", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			String json = sb.toString();
			System.out.println(json);

			JSONObject jsonObject = new JSONObject(json);
			appProductVO = gson.fromJson(jsonObject.toString(),
					AppProductVO.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return appProductVO;
	}

}
