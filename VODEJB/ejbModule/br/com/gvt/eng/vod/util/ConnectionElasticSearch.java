package br.com.gvt.eng.vod.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * @author GVT
 * 
 */
public class ConnectionElasticSearch {

	// Variavel para identificar a host de conexao
	private static final String HOST = PropertiesConfig
			.getString("elasticsearch.host");

	// Variavel para identificar a porta de conexao - TransportClient
	private static final Integer PORT = Integer.valueOf(PropertiesConfig
			.getString("elasticsearch.port"));

	/**
	 * Classe responsavel pela conexao com o servidor do elasticsearch.
	 * 
	 * @return connection
	 */
	@SuppressWarnings("resource")
	public static Client getConnection() {
		Client client = null;
		try {
			// Propriedades da conexao
			Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", "ipvodsearch")
					.put("client.transport.sniff", true)
					.build();

			// Efetua a conexao
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(HOST,
							PORT));

		} catch (Exception e) {
			e.printStackTrace();
			client.close();
		}
		return client;
	}
}