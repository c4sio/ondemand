package br.com.gvt.vod.facade;

import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.model.HistoryTypeEnum;
import br.com.gvt.eng.vod.model.IpvodHistory;

@Local
public interface HistoryFacade {

	public abstract void save(IpvodHistory ipvodHistory);

	public abstract List<IpvodHistory> listAssetType(HistoryTypeEnum type, long itemId);

}
