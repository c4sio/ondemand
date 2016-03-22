package br.com.gvt.vod.facade.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.NotificationDAO;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodNotification;
import br.com.gvt.eng.vod.model.IpvodUserSystem;
import br.com.gvt.eng.vod.util.SendEmail;
import br.com.gvt.vod.facade.NotificationFacade;
import br.com.gvt.vod.facade.UserSystemFacade;

@Stateless
public class NotificationFacadeImp implements NotificationFacade {

	@EJB
	private NotificationDAO notificationDAO;

	@EJB UserSystemFacade userSystemFacade;
	
	@Override
	public void delete(IpvodNotification ipvodNotification) {
		notificationDAO.delete(ipvodNotification);
	}

	@Override
	public List<IpvodNotification> findAll() {
		return notificationDAO.findAll();
	}

	@Override
	public List<IpvodNotification> findByContentProvider(IpvodUserSystem ipvodUserSystem) {
		return notificationDAO.findByContentProvider(ipvodUserSystem);
	}

	@Override
	public List<IpvodNotification> findByRole(String role) {
		return notificationDAO.findByRoleGroup(role);
	}
	
	@Override
	public IpvodNotification findById(Long id) {
		return notificationDAO.find(id);
	}

	@Override
	public void saveNotification(String roleGroup, String subject, String body) {
		saveNotification(null, roleGroup, subject, body);
	}
	
	@Override
	public void saveNotification(IpvodContentProvider ipvodContentProvider,
			String ccRoleGroup, String subject, String body) {
		IpvodNotification notification = new IpvodNotification();
		notification.setIpvodContentProvider(ipvodContentProvider);
		notification.setRoleGroup(ccRoleGroup);
		notification.setTitle(subject);
		notification.setText(body);
		
		saveNotification(notification);
		
	}
	
	@Override
	public void saveNotification(IpvodNotification ipvodNotification) {
		setExpirationDate(ipvodNotification);
		notificationDAO.save(ipvodNotification);
		
		sendEmail(ipvodNotification);
	}
	
	private void sendEmail(IpvodNotification notification){
		List<IpvodUserSystem> listUserEmail = new ArrayList<IpvodUserSystem>();
		
		if (notification.getIpvodContentProvider()!= null) {
			List<IpvodUserSystem> findByContentProvider = userSystemFacade.findByContentProvider(notification.getIpvodContentProvider());
			if (findByContentProvider != null) {
				listUserEmail.addAll(findByContentProvider);
			}
		}
		
		List<IpvodUserSystem> findByRole = userSystemFacade.findByRole(notification.getRoleGroup());
		if (findByRole != null) {
			listUserEmail.addAll(findByRole);
		}
		
		SendEmail sendEmail = new SendEmail();
		try {
			sendEmail.sendEmail(listUserEmail, notification.getText(), notification.getTitle());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setExpirationDate(IpvodNotification ipvodNotification) {
		// DATA DE EXPIRACAO DA MENSAGEM = data atual + 15 dias
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 15);
		ipvodNotification.setExpirationDate(c.getTime());
	}

}