/**
 * 
 */
package br.com.gvt.vod.facade;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ws.rs.core.UriInfo;

import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPackageType;

@Local
public interface PackageFacade {

	public abstract void save(IpvodPackage ipvodPackage);

	public abstract void delete(IpvodPackage ipvodPackage);

	public abstract IpvodPackage update(IpvodPackage ipvodPackage);

	public abstract List<IpvodPackage> findAll();

	public abstract IpvodPackage find(Long entityID);

	public abstract IpvodPackage findByOtherId(String otherId);

	public abstract boolean addAsset(IpvodPackage ipvodPackage);

	public abstract boolean removeAsset(IpvodPackage ipvodPackage);

	public abstract IpvodPackage find(long packageId, Long userId,
			Map<String, Object> pagination);

	public abstract List<IpvodPackageType> listPackageType();

	public abstract List<IpvodPackage> findResultComplexQuery(UriInfo uriInfo);

	public abstract Long countResultComplexQuery(UriInfo uriInfo);

	public abstract List<IpvodPackage> findPackages(Long userId);
}
