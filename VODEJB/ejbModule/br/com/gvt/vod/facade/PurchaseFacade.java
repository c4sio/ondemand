package br.com.gvt.vod.facade;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.PurchaseReportVO;
import br.com.gvt.eng.vod.vo.PurchaseVO;
import br.com.gvt.eng.vod.vo.VDRPurchaseReportVO;

@Local
public interface PurchaseFacade {

	/**
	 * @param ipvodPurchase
	 */
	public abstract void save(IpvodPurchase ipvodPurchase);

	/**
	 * @param assetId
	 * @param equipmentId
	 * @return
	 * @throws BusinessException
	 */
	public abstract IpvodPurchase savePurchase(long assetId,
			IpvodEquipment equipmentId) throws BusinessException;

	/**
	 * @param list
	 */
	public abstract void delete(List<IpvodPurchase> list);

	/**
	 * @param ipvodPurchase
	 * @return IpvodPurchase
	 */
	public abstract IpvodPurchase update(IpvodPurchase ipvodPurchase);

	/**
	 * @return List<IpvodPurchase>
	 */
	public abstract List<IpvodPurchase> findAll();

	/**
	 * @param entityID
	 * @return IpvodPurchase
	 */
	public abstract IpvodPurchase find(long entityID);

	/**
	 * @param subscriberId
	 * @param start
	 * @param end
	 * @return List<PurchaseVO>
	 * @throws BusinessException
	 */
	public abstract List<PurchaseVO> findPurchaseExecutions(
			String subscriberId, Date start, Date end) throws BusinessException;

	/**
	 * @param ipvodPurchase
	 */
	public abstract void delete(IpvodPurchase ipvodPurchase);

	/**
	 * @param date
	 * @return List<Object>
	 */
	public abstract List<Object> getPurchaseByRegion(GregorianCalendar date);

	/**
	 * @param assetId
	 * @param userId
	 * @return List<IpvodPurchase>
	 */
	public abstract List<IpvodPurchase> findPurchaseByAssetList(
			List<Long> assetId, Long userId);

	/**
	 * @param ipvodUser
	 * @param ipvodAsset
	 * @return
	 */
	public abstract double getLowerPrice(IpvodUser ipvodUser,
			IpvodAsset ipvodAsset);

	/**
	 * @param ipvodUser
	 * @return Map<Long, Object>
	 */
	public abstract Map<Long, Object> findMyPurchases(IpvodUser ipvodUser);

	/**
	 * @param menuId
	 * @param ipvodUser
	 * @return Map<Long, Map<String, Object>>
	 */
	public abstract Map<Long, Map<String, Object>> findMyPurchases(long menuId,
			IpvodUser ipvodUser);

	/**
	 * @param ipvodUser
	 * @return Object
	 */
	public abstract Object findMyPurchasesCatchup(IpvodUser ipvodUser);

	/**
	 * @param ipvodUser
	 * @return Object
	 */
	public abstract Object findMyPurchasesOnDemandAdult(IpvodUser ipvodUser);

	/**
	 * @param ipvodUser
	 * @return Object
	 */
	public abstract Object findMyPurchasesOnDemand(IpvodUser ipvodUser);

	/**
	 * @param ipvodPurchase
	 * @param ipvodUser
	 * @return IpvodPurchase
	 */
	public abstract IpvodPurchase findPurchase(IpvodPurchase ipvodPurchase,
			IpvodUser ipvodUser);

	/**
	 * @param date
	 * @return List<PurchaseReportVO>
	 * @throws ParseException
	 */
	public abstract List<PurchaseReportVO> findByDate(Date date)
			throws ParseException;

	public abstract List<PurchaseReportVO> findByDateInterval(Date initialDate, Date finalDate) throws ParseException;

	List<VDRPurchaseReportVO> findByDateIntervalVDR(Date initialDate,
			Date finalDate) throws ParseException;

}
