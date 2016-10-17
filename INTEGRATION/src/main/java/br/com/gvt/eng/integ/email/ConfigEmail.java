package br.com.gvt.eng.integ.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import br.com.gvt.eng.integ.properties.PropertiesConfig;
import br.com.gvt.eng.vod.Jobstatusinfo;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

public class ConfigEmail {

	static Logger logger = Logger.getLogger(ConfigEmail.class.getName());

	public boolean sendEmail(String userEmail, String fileName,
			List<Jobstatusinfo> infos) throws IOException {
		logger.info("Entrou no metodo para enviar e-mail de erro no processo 4Balancer.");

		// Busca arquivo de properties
		Properties prop = PropertiesConfig.getProp();

		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName(prop.getProperty("email.hostName"));
		email.setSslSmtpPort(prop.getProperty("email.sslSmtpPort"));
		email.setSSLOnConnect(true);
		email.setStartTLSRequired(false);
		email.setAuthenticator(new DefaultAuthenticator(prop
				.getProperty("email.authenticator"), prop
				.getProperty("email.password")));
		try {
			email.setFrom(prop.getProperty("email.from"),
					prop.getProperty("email.name"));
			email.setDebug(true);
			email.setSubject("Erro no decoder do arquivo " + fileName);

			StringBuilder builder = new StringBuilder();

			builder.append("<html>");
			builder.append("<body style=\"background: #FFFFFF;font-family: 'Open Sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;\">");
			builder.append("<h1 style=\"font-weight: normal;\"><img src=\"http://www.gvt.com.br/midiaportal/images/geral/logo_gvt_topo.gif\" alt=\"GVT\" align=\"middle\"><span vertical-align:middle>IPVOD Admin</span></h1>");
			builder.append("<h4 style=\"font-weight: lighter; color: #dd5a43!important;border-bottom: 1px solid #CCC;border-bottom-color: #f3cdc6;\">Erro ao inserir o arquivo "
					+ fileName + "</h4>");
			builder.append("<p style=\"color: rgb(57, 57, 57);font-fa °[mily: sans-serif;font-size: 13px;\">");
			builder.append("Causa(s): </br>");
			for (Jobstatusinfo jobstatusinfo : infos) {
				builder.append("<p>" + jobstatusinfo.getValue() + "</p>");
			}
			builder.append("</p>");
			builder.append("</body>");
			builder.append("</html>");
			email.setHtmlMsg(builder.toString());
			email.addTo(userEmail);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			logger.error("Erro ao enviar e-mail - " + e);
			return false;
		}
		return true;
	}

	public boolean sendEmailToList(List<IpvodUserSystem> listUserEmail,
			String fileName, List<Jobstatusinfo> infos) throws IOException {
		logger.info("Entrou no metodo para enviar e-mail de erro no processo 4Balancer.");

		// Busca arquivo de properties
		Properties prop = PropertiesConfig.getProp();

		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName(prop.getProperty("email.hostName"));
		email.setSslSmtpPort(prop.getProperty("email.sslSmtpPort"));
		email.setSSLOnConnect(true);
		email.setStartTLSRequired(false);
		email.setAuthenticator(new DefaultAuthenticator(prop
				.getProperty("email.authenticator"), prop
				.getProperty("email.password")));
		try {
			email.setFrom(prop.getProperty("email.from"),
					prop.getProperty("email.name"));
			email.setDebug(true);
			email.setSubject("Erro no decoder do arquivo " + fileName);

			StringBuilder builder = new StringBuilder();

			builder.append("<html>");
			builder.append("<body style=\"background: #FFFFFF;font-family: 'Open Sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;\">");
			builder.append("<h1 style=\"font-weight: normal;\"><img src=\"http://www.gvt.com.br/midiaportal/images/geral/logo_gvt_topo.gif\" alt=\"GVT\" align=\"middle\"><span vertical-align:middle>IPVOD Admin</span></h1>");
			builder.append("<h4 style=\"font-weight: lighter; color: #dd5a43!important;border-bottom: 1px solid #CCC;border-bottom-color: #f3cdc6;\">Erro ao inserir o arquivo "
					+ fileName + "</h4>");
			builder.append("<p style=\"color: rgb(57, 57, 57);font-fa °[mily: sans-serif;font-size: 13px;\">");
			builder.append("Causa(s): </br>");
			for (Jobstatusinfo jobstatusinfo : infos) {
				builder.append("<p>" + jobstatusinfo.getValue() + "</p>");
			}
			builder.append("</p>");
			builder.append("</body>");
			builder.append("</html>");
			email.setHtmlMsg(builder.toString());

			// Verifica a lista de emails para encaminhar a mensagem de erro
			List<InternetAddress> ListInternetAddress = new ArrayList<InternetAddress>();
			InternetAddress internetAddress = null;

			for (IpvodUserSystem ipvodUserSystem : listUserEmail) {
				internetAddress = new InternetAddress();
				internetAddress.setAddress(ipvodUserSystem.getEmail());
				ListInternetAddress.add(internetAddress);
			}

			email.setTo(ListInternetAddress);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			logger.error("Erro ao enviar e-mail - " + e);
			return false;
		}
		return true;
	}
}
