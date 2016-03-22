package br.com.gvt.vod.facade;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.IpvodBookmark;

@Local
public interface BookmarkFacade {

	void save(IpvodBookmark ipvodBookmark);

	IpvodBookmark findByAssetUser(Long assetId, Long userId);
	
	void delete(IpvodBookmark ipvodBookmark);
}
