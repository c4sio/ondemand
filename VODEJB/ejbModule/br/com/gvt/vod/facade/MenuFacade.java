package br.com.gvt.vod.facade;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodVisualMenu;

@Local
public interface MenuFacade {

	public abstract void save(IpvodVisualMenu ipvodMenu);

	public abstract void delete(IpvodVisualMenu ipvodMenu);

	public abstract IpvodVisualMenu update(IpvodVisualMenu ipvodMenu);

	public abstract List<IpvodVisualMenu> findAll();

	public abstract IpvodVisualMenu find(long menuId);
	
	public abstract List<IpvodVisualMenu> findMenuHierarchy();
	
	public abstract List<IpvodVisualMenu> findMenuActive();
	
	public abstract void toDisableMenu(IpvodVisualMenu ipvodMenu);
	
	public abstract void toEnableMenu(IpvodVisualMenu ipvodMenu);

	public abstract void update(List<IpvodVisualMenu> ipvodMenu);
	
	public abstract IpvodVisualMenu findMenuAssetPagination(long menuId, Long userId,  Map<String, Object> parameters);

	public abstract IpvodVisualMenu find(long menuId, Long userId);
	
	public abstract List<IpvodVisualMenu> findByAssetId(long assetId);

	public abstract IpvodVisualMenu findMenuSTB(long menuId, Long userId);

	public abstract IpvodVisualMenu findMenuSTB(long menuId);
	
	public abstract IpvodVisualMenu getOnDemand(Long userId);
	
	public abstract IpvodVisualMenu getCatchup(Long userId);
	
	public abstract List<IpvodVisualMenu> findMenuCategorize();
	
}
