package br.com.gvt.vod.facade.impl;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.BookmarkDAO;
import br.com.gvt.eng.vod.model.IpvodBookmark;
import br.com.gvt.vod.facade.BookmarkFacade;

@Stateless
public class BookmarkFacadeImp implements BookmarkFacade {

	/**
	 * Entity Manager
	 */
	@EJB
	private BookmarkDAO bookmarkDAO;

	@Override
	public void save(IpvodBookmark ipvodBookmark) {
		ipvodBookmark.setInsertDate(new Date());
		bookmarkDAO.update(ipvodBookmark);
	}

	@Override
	public IpvodBookmark findByAssetUser(Long assetId, Long userId) {
		return bookmarkDAO.getByAssetUser(assetId, userId);
	}

	@Override
	public void delete(IpvodBookmark ipvodBookmark) {
		bookmarkDAO.delete(ipvodBookmark.getBookmarkId());
	}
}