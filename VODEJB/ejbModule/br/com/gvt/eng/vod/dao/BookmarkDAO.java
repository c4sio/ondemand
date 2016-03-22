package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodBookmark;

@Stateless
public class BookmarkDAO extends GenericDAO<IpvodBookmark> {

	public BookmarkDAO() {
		super(IpvodBookmark.class);
	}

	public IpvodBookmark getByAssetUser(Long assetId, Long userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetId", assetId);
		parameters.put("userId", userId);
		return super.findOneResult(IpvodBookmark.FIND_BY_ASSET_USER,
				parameters);
	}
	
	public void delete (Long id) {
		super.delete(id, IpvodBookmark.class);
	}
}
