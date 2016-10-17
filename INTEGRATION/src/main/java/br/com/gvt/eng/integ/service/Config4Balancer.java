package br.com.gvt.eng.integ.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;

import br.com.gvt.eng.vod.Envivio4BalancerService;
import br.com.gvt.eng.vod.ServicePortType;

public class Config4Balancer {

	static Logger logger = Logger.getLogger(Config4Balancer.class.getName());

	/**
	 * @return Properties
	 * @throws IOException
	 */
	public static ServicePortType get4Balancer() throws ServiceException {
		logger.info("Conexao com servicos 4Balancer");
		Envivio4BalancerService service = new Envivio4BalancerService();
		ServicePortType port = service.getService();
		return port;
	}

}
