package br.com.gvt.vod.facade.impl;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.converter.UserConverter;
import br.com.gvt.eng.vod.dao.EquipmentDAO;
import br.com.gvt.eng.vod.dao.UserDAO;
import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.it.UserVO;
import br.com.gvt.eng.vod.vo.provisioning.User;
import br.com.gvt.vod.facade.EquipmentFacade;
import br.com.gvt.vod.facade.EquipmentTypeFacade;
import br.com.gvt.vod.facade.PackageFacade;
import br.com.gvt.vod.facade.UserFacade;

@Stateless
public class UserFacadeImp implements UserFacade {

	@EJB
	private UserDAO userDAO;

	@EJB
	private EquipmentDAO equipmentDAO;

	@EJB
	protected EquipmentFacade equipmentFacade;

	@EJB
	protected PackageFacade packageFacade;

	@EJB
	protected EquipmentTypeFacade equipmentTypeFacade;

	@Override
	public void save(IpvodUser ipvodUser) {
		isUserWithAllData(ipvodUser);
		ipvodUser.setCreatedAt(new GregorianCalendar());
		userDAO.save(ipvodUser);
	}

	@Override
	public IpvodUser update(IpvodUser ipvodUser) {
		isUserWithAllData(ipvodUser);
		return userDAO.update(ipvodUser);
	}

	@Override
	public List<IpvodUser> findAll() {
		return userDAO.findAll();
	}

	@Override
	public IpvodUser find(long entityID) {
		IpvodUser ipvodUser = new IpvodUser();
		ipvodUser = userDAO.find(entityID);
		// Evita o Lazy Exception
		if (ipvodUser != null) {
			ipvodUser.getIpvodAuthentications().size();
			ipvodUser.getIpvodEquipments().size();
			ipvodUser.getIpvodPackages().size();
			ipvodUser.getIpvodAssets().size();
		}
		return ipvodUser;
	}

	@Override
	public List<IpvodUser> findUserActive() {
		return userDAO.findUserActive();
	}

	@Override
	public void addServicesUser(IpvodUser ipvodUser, List<IpvodPackage> packages) {

		for (IpvodPackage product : packages) {
			addServiceUser(ipvodUser, product);
		}

	}

	@Override
	public void addServiceUser(IpvodUser ipvodUser, IpvodPackage product) {
		ipvodUser.getIpvodPackages().add(product);
		userDAO.update(ipvodUser);
	}

	@Override
	public void addEquipmentsUser(IpvodUser ipvodUser, List<IpvodEquipment> equipments) {
		for (IpvodEquipment equipment : equipments) {
			addEquipmentUser(ipvodUser, equipment);
		}
	}

	@Override
	public void addEquipmentUser(IpvodUser ipvodUser, IpvodEquipment equipment) {
		equipment.setIpvodUser(ipvodUser);
		equipmentDAO.save(equipment);
	}

	@Override
	public void removeServiceUser(String subscriberId, String productId) throws BusinessException {
		// Busca o usuario pelo codigo CRM passado pelo IT
		IpvodUser ipvodUser = userDAO.findUserBySubscriberID(subscriberId);

		if (ipvodUser == null) {
			StringBuilder sb = new StringBuilder(
					"O usuário definido pelo [subscribeID] de ").append(
					subscriberId).append(" não foi encontrado no sistema.");
			throw new BusinessException(String.valueOf(sb));
		}

		// Valida campos ipvodUser
		isUserWithAllData(ipvodUser);

		ipvodUser.setActive(true);

		List<IpvodPackage> ipvodPackages = ipvodUser.getIpvodPackages();

		Iterator<IpvodPackage> it = ipvodPackages.iterator();
		while (it.hasNext()) {
			IpvodPackage ipvodPackage = (IpvodPackage) it.next();
			if (productId.equals(ipvodPackage.getOtherId())) {
				it.remove();
			}
		}

		ipvodUser.setIpvodPackages(ipvodPackages);

		// Atualiza os dados do usuario
		userDAO.update(ipvodUser);
	}

	@Override
	public User getUser(String subscriberID) {
		User user = null;
		try {
			user = new User();
			UserConverter converter = new UserConverter();

			// Busca o usuario pelo codigo CRM passado pelo IT
			IpvodUser ipvodUser = userDAO.findUserBySubscriberID(subscriberID);

			user = converter.toUser(ipvodUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void deleteUser(IpvodUser ipvodUser) {
		userDAO.deleteUser(ipvodUser);
	}

	/**
	 * Valida��o de campos - Save/Update
	 * 
	 * @param IpvodUser
	 */
	private void isUserWithAllData(IpvodUser ipvodUser) {
		boolean hasError = false;
		if (ipvodUser == null)
			hasError = true;

		if (ipvodUser.getCrmCustomerId() == null
				|| "".equals(ipvodUser.getCrmCustomerId().trim()))
			hasError = true;

		if (hasError)
			throw new IllegalArgumentException(
					"The User is missing data. Check, they should have value.");
	}

	@Override
	public IpvodUser findUserBySubscriberID(String subscriberID) {
		return userDAO.findUserBySubscriberID(subscriberID);
	}

	@Override
	public void saveUser(IpvodUser ipvodUser) {
		userDAO.save(ipvodUser);

		if (ipvodUser.getIpvodEquipments() != null) {
			equipmentDAO.saveAll(ipvodUser.getIpvodEquipments());
		}

	}
	
	@Override
	public List<Object> getLogonByRegion(GregorianCalendar date) {
		return userDAO.getLogonByRegion(date);
	}
	
	@Override
	public List<Object> getMinutesPlayedByRegion(GregorianCalendar date) {
		return userDAO.getMinutesPlayedByRegion(date);
	}
	
	@Override
	public List<Object> getTotalLogonByRegion(GregorianCalendar date) {
		return userDAO.getTotalLogonByRegion(date);
	}
	
	@Override
	public List<Object> getTotalNewUsers(GregorianCalendar date) {
		return userDAO.getTotalNewUsers(date);
	}

	@Override
	public UserVO findVO(Long id) {
		return userDAO.findVO(id);
	}

	@Override
	public List<IpvodUser> find(UriInfo uriInfo) {
		return userDAO.findResultComplexQuery(uriInfo);
	}

	@Override
	public Long countResultComplexQuery(UriInfo uriInfo) {
		return userDAO.countResultComplexQuery(uriInfo);
	}
	
}
