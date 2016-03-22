package br.com.gvt.eng.vod.dao;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.model.IpvodMediaAsset;
import br.com.gvt.eng.vod.vo.MediaAssetVO;
import br.com.gvt.eng.vod.vo.it.OnDemandContentVO;

@Stateless
public class MediaAssetDAO extends GenericDAO<IpvodMediaAsset> {

	public MediaAssetDAO() {
		super(IpvodMediaAsset.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MediaAssetVO> findVOByAsset(Long idAsset){
		StringBuilder hql = new StringBuilder("select ma.mediaAssetId as mediaAssetId, mt.description as mediaType, ma.url as url ");
		hql.append("from IpvodMediaAsset ma ");
		hql.append("left join ma.ipvodMediaType mt left join ma.ipvodAsset a ");
		hql.append("where ma.ipvodAsset.assetId = :idAsset ");

		Query query = getSession().createQuery(String.valueOf(hql));
		query.setParameter("idAsset", idAsset.longValue());

		query.setResultTransformer(new AliasToBeanResultTransformer(MediaAssetVO.class));

		return query.list();
	}

	public List<OnDemandContentVO> findVOByAsset(List<OnDemandContentVO> l) {
		for(OnDemandContentVO vo : l){
			vo.setMediaAssets(findVOByAsset(vo.getAssetId()));
		}
		return l;
	}

}
