package br.com.gvt.vod.facade;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.it.UserVO;
import br.com.gvt.eng.vod.vo.provisioning.User;

@Local
public interface UserFacade {

	public abstract void save(IpvodUser ipvodUser);

	public abstract IpvodUser update(IpvodUser ipvodUser);

	public abstract List<IpvodUser> findAll();

	public abstract IpvodUser find(long entityID);

	public abstract List<IpvodUser> findUserActive();

	public abstract IpvodUser findUserBySubscriberID(String subscriberID);

	public abstract void removeServiceUser(String subscriberId, String productId) throws BusinessException;

	public abstract User getUser(String entityID);

	public abstract void deleteUser(IpvodUser ipvodUser);

	public abstract void addServicesUser(IpvodUser ipvodUser, List<IpvodPackage> packages);

	public abstract void addServiceUser(IpvodUser ipvodUser, IpvodPackage product);

	public void addEquipmentUser(IpvodUser ipvodUser, IpvodEquipment equipment);

	public void addEquipmentsUser(IpvodUser ipvodUser, List<IpvodEquipment> equipments);

	public abstract void saveUser(IpvodUser ipvodUser);

	public List<Object> getLogonByRegion(GregorianCalendar date);

	public List<Object> getMinutesPlayedByRegion(GregorianCalendar date);

	public List<Object> getTotalLogonByRegion(GregorianCalendar date);

	public List<Object> getTotalNewUsers(GregorianCalendar date);

	public abstract UserVO findVO(Long id);

	public abstract List<IpvodUser> find(UriInfo uriInfo);

	public abstract Long countResultComplexQuery(UriInfo uriInfo);

}
