package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.core.UriInfo;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.gvt.eng.vod.constants.IpvodConstants;
import br.com.gvt.eng.vod.model.IpvodContentProvider;
import br.com.gvt.eng.vod.model.IpvodIngestStage;
import br.com.gvt.eng.vod.util.Filter;
import br.com.gvt.eng.vod.util.FilterRules;
import br.com.gvt.eng.vod.vo.BalancerVO;
import br.com.gvt.eng.vod.vo.CheckSumVO;
import br.com.gvt.eng.vod.vo.ConvoyVO;
import br.com.gvt.eng.vod.vo.DrmVO;
import br.com.gvt.eng.vod.vo.IngestVO;

@Stateless
public class IngestDAO extends GenericDAO<IpvodIngestStage> {

	public IngestDAO() {
		super(IpvodIngestStage.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodIngestStage
	 */
	public void deleteIngest(IpvodIngestStage ipvodIngestStage) {
		super.delete(ipvodIngestStage.getId(), IpvodIngestStage.class);
	}

	public List<IpvodIngestStage> getByAssetInfo(String assetInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetInfo", assetInfo);
		return super.findResultByParameter(
				IpvodIngestStage.FIND_LIST_BY_ASSET_INFO, parameters);
	}

	public IpvodIngestStage getByAssetInfoStageType(String assetInfo,
			Long stageType) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetInfo", assetInfo);
		parameters.put("stageType", stageType);
		return super.findOneResult(IpvodIngestStage.FIND_BY_ASSET_STAGE,
				parameters);
	}

	public int clearIngestByAssetInfo(String assetInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetInfo", assetInfo);
		return super.deleteByParameter(
				IpvodIngestStage.CLEAR_INGEST_BY_ASSET_INFO, parameters);
	}

	public IpvodIngestStage findByAssetId(Long assetId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetId", assetId);

		List<IpvodIngestStage> ingests = super.findResultByParameter(
				IpvodIngestStage.FIND_INGEST_BY_ASSET_ID, parameters);
		if (ingests != null && ingests.size() > 0) {
			return ingests.get(0);
		}
		return null;
	}

	/**
	 * Busca arquivos para serem processados
	 * 
	 * @return List<IpvodIngestStage>
	 */
	public List<IpvodIngestStage> findFilesToExecute(long stageTypeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("stageTypeId", stageTypeId);
		return super.findResultByParameter(
				IpvodIngestStage.FIND_FILES_TO_EXECUTE, parameters);
	}

	/**
	 * Busca dados para serem mostrados na tela de revisao Ingestao IPVOD-CMS
	 * 
	 * @return List<IpvodIngestStage>
	 */
	@SuppressWarnings("unchecked")
	public List<IngestVO> findAllDataIngestForRevision(UriInfo uriInfo,
			IpvodContentProvider ipvodContentProvider) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("ins.id as idIngest, ");
		hql.append("ins.stageType.id as stageType, ");
		hql.append("asset.assetId as assetId, ");
		hql.append("asset.title as assetTitle, ");
		hql.append("ins.assetName as assetName, ");
		hql.append("assetCategory.description as categoryDescription, ");
		hql.append("assetCategory.categoryId as assetCategoryId, ");
		hql.append("ins.dtInsert as dtInsertIngest, ");
		hql.append("ins.dtChange as dtChangeIngest, ");
		hql.append("cast(ins.result as boolean) as resultOK, ");
		hql.append("ins.priority as priority, ");
		hql.append("asset.languages as language, ");
		hql.append("asset.billingID as billingId, ");
		hql.append("asset.price as price ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("inner join ins.ipvodAsset as asset ");
		hql.append("left join ins.ipvodAsset.ipvodCategory1 as assetCategory ");
		hql.append("where ");
		hql.append("ins.stageType.id = 7 ");
		hql.append("and (asset.isRevised is null or asset.isRevised = 0) ");
		hql.append("and asset.licenseWindowEnd > sysdate ");
		if (ipvodContentProvider != null) {
			hql.append(" and asset.ipvodContentProvider.contentProviderId = "
					+ ipvodContentProvider.getContentProviderId());
		}

		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_NOT_CATEGORIZED) != null) {
			hql.append(" and asset.ipvodVisualMenuAsset is empty ");
		}

		Map<String, Object> parameters = parseUriInfo(uriInfo);
		Filter filter = (Filter) parameters.get("filters");
		if (filter != null && !filter.getRules().isEmpty()) {
			if (filter.getGroupOp().equals("AND")) {
				for (FilterRules rule : filter.getRules()) {
					hql.append(" and  upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
			} else {
				StringBuilder orClause = new StringBuilder();
				orClause.append(" and ( ");
				for (int i = 0; i < filter.getRules().size(); i++) {
					FilterRules rule = filter.getRules().get(i);
					if (i > 0) {
						orClause.append(" or ");
					}
					orClause.append(" upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
				orClause.append(" ) ");
				hql.append(orClause);
			}
		}
		hql.append(" order by ins.id desc, ins.priority asc");

		Query query = getSession().createQuery(String.valueOf(hql));

		setPagination(query, uriInfo);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				IngestVO.class));
		return query.list();
	}

	public Long countAllDataIngestForRevision(
			IpvodContentProvider ipvodContentProvider, UriInfo uriInfo) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("count(ins.id) ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("inner join ins.ipvodAsset as asset ");
		hql.append("inner join ins.ipvodAsset.ipvodCategory1 as assetCategory ");
		hql.append("where ");
		hql.append("ins.stageType.id = 7 ");
		hql.append("and (asset.isRevised is null or asset.isRevised = 0) ");
		hql.append("and asset.licenseWindowEnd > sysdate ");
		if (ipvodContentProvider != null) {
			hql.append(" and asset.ipvodContentProvider.contentProviderId = "
					+ ipvodContentProvider.getContentProviderId());
		}

		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_NOT_CATEGORIZED) != null) {
			hql.append(" and asset.ipvodVisualMenuAsset is empty ");
		}

		Map<String, Object> parameters = parseUriInfo(uriInfo);
		Filter filter = (Filter) parameters.get("filters");
		if (filter != null && !filter.getRules().isEmpty()) {
			if (filter.getGroupOp().equals("AND")) {
				for (FilterRules rule : filter.getRules()) {
					hql.append(" and  upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
			} else {
				StringBuilder orClause = new StringBuilder();
				orClause.append(" and ( ");
				for (int i = 0; i < filter.getRules().size(); i++) {
					FilterRules rule = filter.getRules().get(i);
					if (i > 0) {
						orClause.append(" or ");
					}
					orClause.append(" upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
				orClause.append(" ) ");
				hql.append(orClause);
			}
		}

		Query query = getSession().createQuery(String.valueOf(hql));
		return (Long) query.uniqueResult();
	}

	/**
	 * Busca dados de @IpvodIngestStage pelo AssetInfo onde o result e igual a
	 * ZERO
	 * 
	 * @param assetInfo
	 * @return IpvodIngestStage
	 */
	public IpvodIngestStage findRegisterWithErrorByAssetInfo(String assetInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetInfo", assetInfo);
		return super.findOneResult(
				IpvodIngestStage.FIND_DATA_ERROR_BY_ASSET_INFO, parameters);
	}

	/**
	 * Busca dados de @IpvodIngestStage pelo pathFile
	 * 
	 * @param pathfile
	 * @return IpvodIngestStage
	 */
	public IpvodIngestStage getDataByPathFile(String pathfile) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pathfile", pathfile);
		return super.findOneResult(IpvodIngestStage.FIND_DATA_BY_PATH_FILE,
				parameters);
	}

	/**
	 * Busca dados de @IpvodIngestStage pelo assetInfo
	 * 
	 * @param assetInfo
	 * @return IpvodIngestStage
	 */
	public IpvodIngestStage getDataByAssetInfo(String assetInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assetInfo", assetInfo);
		return super.findOneResult(IpvodIngestStage.FIND_DATA_BY_ASSET_INFO,
				parameters);
	}

	/**
	 * Lista dados @IpvodIngestStage
	 * 
	 * @return List<IngestVO>
	 */
	@SuppressWarnings("unchecked")
	public List<IngestVO> findDataIngestBeforePlataform(UriInfo uriInfo,
			IpvodContentProvider ipvodContentProvider) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("ins.id as idIngest, ");
		hql.append("ins.stageType.id as stageType, ");
		hql.append("ins.assetInfo as assetInfo, ");
		hql.append("ins.adicionalInfo as adicionalInfo, ");
		hql.append("asset.assetId as assetId, ");
		hql.append("asset.title as assetTitle, ");
		hql.append("ins.assetName as assetName, ");
		hql.append("ins.dtInsert as dtInsertIngest, ");
		hql.append("ins.dtChange as dtChangeIngest, ");
		hql.append("cast(ins.result as boolean) as resultOK, ");
		hql.append("ins.priority as priority ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodAsset as asset ");
		hql.append("where ");
		hql.append("ins.stageType.id < 7 ");
		if (ipvodContentProvider != null) {
			hql.append(" and asset.ipvodContentProvider.contentProviderId = "
					+ ipvodContentProvider.getContentProviderId());
		}

		Map<String, Object> parameters = parseUriInfo(uriInfo);
		Filter filter = (Filter) parameters.get("filters");
		if (filter != null && !filter.getRules().isEmpty()) {
			if (filter.getGroupOp().equals("AND")) {
				for (FilterRules rule : filter.getRules()) {
					hql.append(" and  upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
			} else {
				StringBuilder orClause = new StringBuilder();
				orClause.append(" and ( ");
				for (int i = 0; i < filter.getRules().size(); i++) {
					FilterRules rule = filter.getRules().get(i);
					if (i > 0) {
						orClause.append(" or ");
					}
					orClause.append(" upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
				orClause.append(" ) ");
				hql.append(orClause);
			}
		}

		hql.append(" order by ins.id desc, ins.priority asc");
		Query query = getSession().createQuery(String.valueOf(hql));

		setPagination(query, uriInfo);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				IngestVO.class));
		return query.list();
	}

	public Long countDataIngestBeforePlataform(
			IpvodContentProvider ipvodContentProvider, UriInfo uriInfo) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("count(ins.id) ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodAsset as asset ");
		hql.append("where ");
		hql.append("ins.stageType.id < 7 ");
		if (ipvodContentProvider != null) {
			hql.append(" and asset.ipvodContentProvider.contentProviderId = "
					+ ipvodContentProvider.getContentProviderId());
		}

		Map<String, Object> parameters = parseUriInfo(uriInfo);
		Filter filter = (Filter) parameters.get("filters");
		if (filter != null && !filter.getRules().isEmpty()) {
			if (filter.getGroupOp().equals("AND")) {
				for (FilterRules rule : filter.getRules()) {
					hql.append(" and  upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
			} else {
				StringBuilder orClause = new StringBuilder();
				orClause.append(" and ( ");
				for (int i = 0; i < filter.getRules().size(); i++) {
					FilterRules rule = filter.getRules().get(i);
					if (i > 0) {
						orClause.append(" or ");
					}
					orClause.append(" upper(" + rule.getField() + ") like '%"
							+ rule.getData().toUpperCase() + "%' ");
				}
				orClause.append(" ) ");
				hql.append(orClause);
			}
		}

		Query query = getSession().createQuery(String.valueOf(hql));
		return (Long) query.uniqueResult();
	}

	/**
	 * Lista dados @IpvodBalancerData
	 * 
	 * @param idIngest
	 * @return List<BalancerVO>
	 */
	@SuppressWarnings("unchecked")
	public List<BalancerVO> findData4Balancer(Long idIngest) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("bad.id as idBalancer, ");
		hql.append("bad.nameFile as nameFile, ");
		hql.append("bad.status as statusBalancer, ");
		hql.append("bad.percentComp as percentCompBalancer, ");
		hql.append("bad.dateEnd as dateEndBalancer ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodBalancer as bad ");
		hql.append("where ");
		hql.append("ins.id = " + idIngest);
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(
				BalancerVO.class));
		return query.list();
	}

	/**
	 * Lista dados @IpvodDrmData
	 * 
	 * @param idIngest
	 * @return List<DrmVO>
	 */
	@SuppressWarnings("unchecked")
	public List<DrmVO> findDataDrm(Long idIngest) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("drm.drmId as drmId, ");
		hql.append("drm.nameFile as nameFile, ");
		hql.append("drm.statusDrm as statusDrm, ");
		hql.append("drm.percentCompDrm as percentCompDrm, ");
		hql.append("drm.dateEndDrm as dateEndDrm ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodDrm as drm ");
		hql.append("where ");
		hql.append("ins.id = " + idIngest);
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(DrmVO.class));
		return query.list();
	}

	/**
	 * Lista dados @IpvodConvoyData
	 * 
	 * @param idIngest
	 * @return List<ConvoyVO>
	 */
	@SuppressWarnings("unchecked")
	public List<ConvoyVO> findDataConvoy(Long idIngest) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("conv.convoyId as convoyId, ");
		hql.append("conv.nameFile as nameFile, ");
		hql.append("conv.statusConvoy as statusConvoy, ");
		hql.append("conv.percentCompConvoy as percentCompConvoy, ");
		hql.append("conv.dateEnd as dateEndConvoy ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodConvoy as conv ");
		hql.append("where ");
		hql.append("ins.id = " + idIngest);
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConvoyVO.class));
		return query.list();
	}

	/**
	 * Lista dados @IpvodCheckSumData
	 * 
	 * @param idIngest
	 * @return List<CheckSumVO>
	 */
	@SuppressWarnings("unchecked")
	public List<CheckSumVO> findDataCheckSum(Long idIngest) {
		// HQL dos dados, todos os campos devem ter o mesmo nome dos atributos
		// no IngestVO
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("check.checkSumId as checkSumId, ");
		hql.append("check.fileName as fileName, ");
		hql.append("check.checkSumSend as checkSumSend, ");
		hql.append("check.checkSumResult as checkSumResult, ");
		hql.append("check.statusFile as statusFile ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("left outer join ins.ipvodCheckSumData as check ");
		hql.append("where ");
		hql.append("ins.id = " + idIngest);
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CheckSumVO.class));
		return query.list();
	}

	/**
	 * @param query
	 * @param uriInfo
	 */
	public void setPagination(Query query, UriInfo uriInfo) {

		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_PAGE_NUMBER) != null) {
			int pageNumber = Integer.parseInt((String) uriInfo
					.getQueryParameters().getFirst(
							IpvodConstants.URLPARAM_PAGE_NUMBER));

			int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
			if (uriInfo.getQueryParameters().getFirst(
					IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
				maxResults = Integer.parseInt((String) uriInfo
						.getQueryParameters().getFirst(
								IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
			}

			query.setMaxResults(maxResults);
			query.setFirstResult((pageNumber * maxResults) - maxResults);

		}

		if (uriInfo.getQueryParameters().getFirst(
				IpvodConstants.URLPARAM_FIRST_INDEX) != null) {
			int firstResult = Integer.parseInt((String) uriInfo
					.getQueryParameters().getFirst(
							IpvodConstants.URLPARAM_FIRST_INDEX));

			int maxResults = IpvodConstants.REGISTERS_PER_PAGE;
			if (uriInfo.getQueryParameters().getFirst(
					IpvodConstants.URLPARAM_REGISTER_PER_PAGE) != null) {
				maxResults = Integer.parseInt((String) uriInfo
						.getQueryParameters().getFirst(
								IpvodConstants.URLPARAM_REGISTER_PER_PAGE));
			}

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
		}
	}

	@SuppressWarnings("unchecked")
	public List<IngestVO> findDataIngestForCleanUp() {
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("ins.id as idIngest, ");
		hql.append("ins.stageType.id as stageType, ");
		hql.append("ins.ipvodAsset.assetId as assetId, ");
		hql.append("ins.assetInfo as assetInfo, ");
		hql.append("ins.adicionalInfo as adicionalInfo, ");
		hql.append("ins.assetName as assetName, ");
		hql.append("ins.cleanup as cleanup, ");
		hql.append("ins.ftpPath as ftpPath ");
		hql.append("from ");
		hql.append("IpvodIngestStage ins ");
		hql.append("where ");
		hql.append("ins.stageType.id = 7 ");
		hql.append("and ins.cleanup = 0 ");
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.setResultTransformer(new AliasToBeanResultTransformer(
				IngestVO.class));
		return query.list();
	}

	public void finishRevision(Long assetId) {
		StringBuilder hql = new StringBuilder();
		hql.append("update ");
		hql.append("IpvodIngestStage ins ");
		hql.append("set ins.stageType.id = 8");
		hql.append("where ");
		hql.append("ins.ipvodAsset.assetId = " + assetId);
		hql.append("order by ins.priority, ins.id asc");
		Query query = getSession().createQuery(String.valueOf(hql));
		query.executeUpdate();
	}
}