package br.com.gvt.eng.vod.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.gvt.eng.vod.model.IpvodUserSystem;

public class SendEmail {

	public boolean sendEmail(IpvodUserSystem userEmail,
			String body, String subject) throws IOException {
		
		List<IpvodUserSystem> listUserEmail = new ArrayList<IpvodUserSystem>();
		listUserEmail.add(userEmail);
		return sendEmail(listUserEmail , body, subject);
	}
	
	public boolean sendEmail(List<IpvodUserSystem> listUserEmail,
			String body, String subject) throws IOException {

		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName(PropertiesConfig.getString("email.hostName"));
		email.setSslSmtpPort(PropertiesConfig.getString("email.sslSmtpPort"));
		email.setSSLOnConnect(true);
		email.setStartTLSRequired(false);
		email.setAuthenticator(new DefaultAuthenticator(
				PropertiesConfig.getString("email.authenticator"), 
				PropertiesConfig.getString("email.password")));
		try {
			email.setFrom(PropertiesConfig.getString("email.from"),
					PropertiesConfig.getString("email.name"));
			email.setDebug(true);
			email.setSubject(subject);

			StringBuilder builder = new StringBuilder();

			builder.append("<html>");
			builder.append("<body style=\"background: #FFFFFF;font-family: 'Open Sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;\">");
			builder.append("<h1 style=\"font-weight: normal;\"><img src=\"http://www.gvt.com.br/midiaportal/images/geral/logo_gvt_topo.gif\" alt=\"GVT\" align=\"middle\"><span vertical-align:middle>IPVOD Admin</span></h1>");
			builder.append(body);
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
			return false;
		}
		return true;
	}
	
}
