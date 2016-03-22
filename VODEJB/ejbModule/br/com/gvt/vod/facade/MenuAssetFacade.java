package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodVisualMenuAsset;

@Local
public interface MenuAssetFacade {

	public abstract void save(IpvodVisualMenuAsset ipvodMenuAsset);

	public abstract void delete(IpvodVisualMenuAsset ipvodMenuAsset);

	public abstract List<IpvodVisualMenuAsset> findByMenuId(long menuId);

	public abstract void clean();

	public abstract List lab();

	public abstract List<IpvodVisualMenuAsset> findAll();

	void clean(long menuId);

	void delete(List<IpvodVisualMenuAsset> menuAssetList);
	
}
