package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodNotification;
import br.com.gvt.eng.vod.model.IpvodUserSystem;

@Local
public interface NotificationFacade {

	void delete(IpvodNotification ipvodNotification);
	
	List<IpvodNotification> findAll();
	
	List<IpvodNotification> findByContentProvider(IpvodUserSystem ipvodUserSystem);
	
	List<IpvodNotification> findByRole(String role);
	
	IpvodNotification findById(Long id);
	
	void saveNotification(String roleGroup, String title, String text);
	
	void saveNotification(IpvodContentProvider ipvodContentProvider, String roleGroup, String title, String text);
	
	void saveNotification(IpvodNotification ipvodNotification);
}
