package br.com.gvt.vod.facade.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.PackageDAO;
import br.com.gvt.eng.vod.dao.PurchaseDAO;
import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPurchase;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.PurchaseReportVO;
import br.com.gvt.eng.vod.vo.PurchaseVO;
import br.com.gvt.eng.vod.vo.VDRPurchaseReportVO;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.EquipmentFacade;
import br.com.gvt.vod.facade.PurchaseFacade;

@Stateless
public class PurchaseFacadeImp implements PurchaseFacade {

	@EJB
	private PurchaseDAO purchaseDAO;

	@EJB
	protected EquipmentFacade equipmentFacade;

	@EJB
	private AssetFacade assetFacade;

	@EJB
	private PackageDAO packageDAO;

	@Override
	public void save(IpvodPurchase ipvodPurchase) {
		purchaseDAO.save(ipvodPurchase);
	}

	@Override
	public void delete(IpvodPurchase ipvodPurchase) {
		purchaseDAO.deletePurchase(ipvodPurchase);
	}
	
	@Override
	public void delete(List<IpvodPurchase> list) {
		for(IpvodPurchase purchase : list){
			delete(purchase);
		}
	}

	@Override
	public IpvodPurchase update(IpvodPurchase ipvodPurchase) {
		return purchaseDAO.update(ipvodPurchase);
	}

	@Override
	public List<IpvodPurchase> findAll() {
		List<IpvodPurchase> listIpvodPurchase = new ArrayList<IpvodPurchase>();
		listIpvodPurchase = purchaseDAO.findAll();
		// Tratando LazyException
		if (!listIpvodPurchase.isEmpty()) {
			for (IpvodPurchase ipvodPurchase : listIpvodPurchase) {
				ipvodPurchase.getIpvodSessions().size();
			}
		}
		return listIpvodPurchase;
	}

	@Override
	public IpvodPurchase find(long entityID) {
		IpvodPurchase ipvodPurchase = new IpvodPurchase();
		ipvodPurchase = purchaseDAO.find(entityID);
		// Tratando LazyException
		if (ipvodPurchase != null) {
			ipvodPurchase.getIpvodSessions().size();
		}
		return ipvodPurchase;
	}

	@Override
	public List<PurchaseReportVO> findByDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = sdf.format(date);
		sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return purchaseDAO.findPurchasesByDate(sdf.parse(strDate + " 00:00:00"), sdf.parse(strDate + " 23:59:59"));
	}
	
	@Override
	public List<PurchaseReportVO> findByDateInterval(Date initialDate, Date finalDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strInitDate = sdf.format(initialDate);
		String strFinalDate = sdf.format(finalDate);
		sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return purchaseDAO.findPurchasesByDate(sdf.parse(strInitDate + " 00:00:00"), sdf.parse(strFinalDate + " 23:59:59"));
	}
	
	@Override
	public List<VDRPurchaseReportVO> findByDateIntervalVDR(Date initialDate, Date finalDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strInitDate = sdf.format(initialDate);
		String strFinalDate = sdf.format(finalDate);
		sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return purchaseDAO.findPurchasesByDateVDR(sdf.parse(strInitDate + " 00:00:00"), sdf.parse(strFinalDate + " 23:59:59"));
	}
	
	@Override
	public IpvodPurchase savePurchase(long assetId, IpvodEquipment ipvodEquipment) throws BusinessException {
		// Buscar preço do filme - assetId
		IpvodAsset ipvodAsset = new IpvodAsset();
		ipvodAsset = assetFacade.find(assetId);

		if (ipvodAsset == null) {
			throw new BusinessException("Erro - Não encontrou nenhum asset com id "
					+ assetId);
		}
		
		// Insere os dados
		IpvodPurchase ipvodPurchase = new IpvodPurchase();
		ipvodPurchase.setPurchaseDate(new Date());
		ipvodPurchase.setIpvodAsset(ipvodAsset);
		ipvodPurchase.setIpvodEquipment(ipvodEquipment);
		
		IpvodPackage ipvodPackage = packageDAO.getLowerPricePackage(ipvodEquipment.getIpvodUser(), ipvodAsset);
		if (ipvodPackage.getPrice() != null && ipvodAsset.getPrice() >= ipvodPackage.getPrice()) {
			ipvodPurchase.setAmountPaid(ipvodPackage.getPrice());
			ipvodPurchase.setBillingID(ipvodPackage.getOtherId());
		} else { 
			ipvodPurchase.setAmountPaid(ipvodAsset.getPrice());
			ipvodPurchase.setBillingID(ipvodAsset.getBillingID());
		}
		
		ipvodPurchase.setBilled(false);
		//DATA FINAL DE VALIDADE DA COMPRA = NOW + 48H 
		Calendar validUntil = new GregorianCalendar();
		validUntil.add(GregorianCalendar.HOUR, 48);
		ipvodPurchase.setValidUntil(validUntil.getTime());
		// Salva dados purchase
		return purchaseDAO.update(ipvodPurchase);
	}

	@Override
	public List<PurchaseVO> findPurchaseExecutions(String subscriberId,
			Date start, Date end) throws BusinessException {
		return purchaseDAO.findPurchaseExecutions(subscriberId, start, end);
	}
	
	@Override
	public List<Object> getPurchaseByRegion(GregorianCalendar date) {
		return purchaseDAO.getPurchaseByRegion(date);
	}

	@Override
	public List<IpvodPurchase> findPurchaseByAssetList(List<Long> assetId, Long userId) {
		return purchaseDAO.findPurchaseByAssetList(assetId, userId);
	}
	
	public double getLowerPrice(IpvodUser ipvodUser, IpvodAsset ipvodAsset){
		return purchaseDAO.getLowerPrice(ipvodUser, ipvodAsset);
	}

	@Override
	public Map<Long, Object> findMyPurchases(IpvodUser ipvodUser) {
		return purchaseDAO.findAllMyPurchases(ipvodUser);
	}

	@Override
	public Map<Long, Map<String, Object>> findMyPurchases(long menuId, IpvodUser ipvodUser) {
		return purchaseDAO.findMyPurchasesByMenu(menuId, ipvodUser);
	}

	@Override
	public Object findMyPurchasesCatchup(IpvodUser ipvodUser) {
		return purchaseDAO.findMyPurchasesCatchup(ipvodUser);
	}

	@Override
	public Object findMyPurchasesOnDemandAdult(IpvodUser ipvodUser) {
		return purchaseDAO.findMyPurchasesOnDemandAdult(ipvodUser);
	}

	@Override
	public Object findMyPurchasesOnDemand(IpvodUser ipvodUser) {
		return purchaseDAO.findMyPurchasesOnDemand(ipvodUser);
	}

	@Override
	public IpvodPurchase findPurchase(IpvodPurchase ipvodPurchase, IpvodUser ipvodUser) {
		List<Long> assetId = new ArrayList<Long>();
		assetId.add(ipvodPurchase.getIpvodAsset().getAssetId());
		List<IpvodPurchase> purchases = purchaseDAO.findPurchaseByAssetList(assetId , ipvodUser.getUserId());
		IpvodPurchase p = null;
		if (purchases != null && !purchases.isEmpty()) {
			p = purchases.get(0);
		}
		return p;
	}
}
