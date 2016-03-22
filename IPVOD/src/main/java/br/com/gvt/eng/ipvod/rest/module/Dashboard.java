package br.com.gvt.eng.ipvod.rest.module;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.vo.OverviewVO;
import br.com.gvt.vod.facade.AssetFacade;
import br.com.gvt.vod.facade.PurchaseFacade;
import br.com.gvt.vod.facade.UserFacade;

@Stateless
@Path("/dashboard")
public class Dashboard {
	
	@EJB
	private UserFacade userFacade;
	
	@EJB
	private AssetFacade assetFacade;
	
	@EJB
	private PurchaseFacade purchaseFacade;
	
	@PermitAll
	@GET
	@Path("/overview")
	@Produces("application/json; charset=UTF-8")
	public Response overview() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		
		GregorianCalendar today = new GregorianCalendar();
		/*today.set(Calendar.DAY_OF_MONTH, 30);
		today.set(Calendar.MONTH, 11);
		today.set(Calendar.YEAR, 2014);*/
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		
		GregorianCalendar yesterday = new GregorianCalendar();
		yesterday.setTime(today.getTime());
		yesterday.add(Calendar.MONTH, -1);
		System.out.println(yesterday);
		
		Iterator<Entry<String, long[]>> it = null;
		
		//----------------------------------------------------------------------------------
		realRegions = null;
		
		List<Object> currentMonthNewUsers = userFacade.getTotalNewUsers(today);
		List<Object> lastMonthNewUsers = userFacade.getTotalNewUsers(yesterday);
		float currentMonthNewUsersTotal = getSumValue(currentMonthNewUsers);
		float lastMonthNewUsersTotal = getSumValue(lastMonthNewUsers);
		
		getSumarizedRegions(currentMonthNewUsers, lastMonthNewUsers);
		
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		
		list.add(new OverviewVO(OverviewVO.TYPE_ASSINANTES, Long.toString((long)currentMonthNewUsersTotal), (Float.valueOf((currentMonthNewUsersTotal/zero(lastMonthNewUsersTotal, currentMonthNewUsersTotal)-1)*100)).longValue(), getFormattedRegions()));
		
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonthNewAssets = assetFacade.getTotalNewAssets(today);
		List<Object> lastMonthNewAssets = assetFacade.getTotalNewAssets(yesterday);
		float currentMonthNewAssetsTotal = Float.valueOf(currentMonthNewAssets.get(0).toString());
		float lastMonthNewAssetsTotal = Float.valueOf(lastMonthNewAssets.get(0).toString());
		
		/*getSumarizedRegions(currentMonthNewAssets, lastMonthNewAssets);
		
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}*/
		
		list.add(new OverviewVO(OverviewVO.TYPE_CONTEUDO, Long.toString((long)currentMonthNewAssetsTotal), (Float.valueOf((currentMonthNewAssetsTotal/zero(lastMonthNewAssetsTotal, currentMonthNewAssetsTotal)-1)*100)).longValue()));
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonth = userFacade.getLogonByRegion(today);
		List<Object> lastMonth = userFacade.getLogonByRegion(yesterday);
		float currentMonthTotal = getSumValue(currentMonth);
		float lastMonthTotal = getSumValue(lastMonth);
		
		getSumarizedRegions(currentMonth, lastMonth);
		
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_LOGON, Long.toString((long)currentMonthTotal), (Float.valueOf((currentMonthTotal/zero(lastMonthTotal, currentMonthTotal)-1)*100)).longValue(), getFormattedRegions()));
		
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonthLogon = userFacade.getTotalLogonByRegion(today);
		List<Object> lastMonthLogon = userFacade.getTotalLogonByRegion(yesterday);
		float currentMonthTotalLogon = getSumValue(currentMonthLogon);
		float lastMonthTotalLogon = getSumValue(lastMonthLogon);
		
		getSumarizedRegions(currentMonthLogon, lastMonthLogon);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_HITS, Long.toString((long)currentMonthTotalLogon), (Float.valueOf((currentMonthTotalLogon/zero(lastMonthTotalLogon, currentMonthTotalLogon)-1)*100)).longValue(), getFormattedRegions()));
		
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonthPurchase = purchaseFacade.getPurchaseByRegion(today);
		List<Object> lastMonthPurchase = purchaseFacade.getPurchaseByRegion(yesterday);
		float currentMonthTotalPurchase = getSumValue(currentMonthPurchase);
		float lastMonthTotalPurchase = getSumValue(lastMonthPurchase);
		
		getSumarizedRegions(currentMonthPurchase, lastMonthPurchase);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_PACOTES, Long.toString((long)currentMonthTotalPurchase), (Float.valueOf((currentMonthTotalPurchase/zero(lastMonthTotalPurchase, currentMonthTotalPurchase)-1)*100)).longValue(), getFormattedRegions()));
		
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonthMinutes = userFacade.getMinutesPlayedByRegion(today);
		List<Object> lastMonthMinutes = userFacade.getMinutesPlayedByRegion(yesterday);
		float currentMonthTotalMinutes = getSumValue(currentMonthMinutes)/today.get(Calendar.DAY_OF_MONTH);
		float lastMonthTotalMinutes = getSumValue(lastMonthMinutes)/today.get(Calendar.DAY_OF_MONTH);
		
		long hours = TimeUnit.MINUTES.toHours(Float.valueOf(currentMonthTotalMinutes).longValue());
		
		getSumarizedRegions(currentMonthMinutes, lastMonthMinutes);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{((int) TimeUnit.MINUTES.toHours(valueCurrent/today.get(Calendar.DAY_OF_MONTH))), percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_AUDIENCIA, String.valueOf(hours), (Float.valueOf((currentMonthTotalMinutes/zero(lastMonthTotalMinutes, currentMonthTotalMinutes)-1)*100)).longValue(), getFormattedRegions()));
		
		return Response.status(200).entity(list).build();
	}
	
	public static float getSumValue(List<Object> list) {
		float total = 0;
		for(int i=0; i<list.size(); i++) {
			total += ((BigDecimal)((Object[])list.get(i))[1]).intValue();
		}
		return total;
	}
	
	public static float zero(float number, float number2) {
		
		return (number == 0 ? 100 : number);
	}
	
	@PermitAll
	@GET
	@Path("/test")
	@Produces("application/json; charset=UTF-8")
	public Response test() {
		return Response.status(200).entity("").build();
	}
	
	private static Map<String, long[]> getSumarizedRegions(List<Object> currentResults, List<Object> lastResults) {
		
		for(int i=0; i<getRegions().size(); i++) {
			
			float current = getRegionValue(currentResults, getRegions().get(i));
			float last = getRegionValue(lastResults, getRegions().get(i));
			
			String formattedRegion = getRealRegion(getRegions().get(i));
			long valueCurrent = getFormattedRegions().get(formattedRegion)[0];
			long valueLast = getFormattedRegions().get(formattedRegion)[1];
			//long valuePercent = getFormattedRegions().get(formattedRegion)[1];
			
			System.out.println("formattedRegion: " + formattedRegion);
			System.out.println("current: " + current);
			System.out.println("last: " + last);
			
			getFormattedRegions().put(new String(formattedRegion), new long[]{(valueCurrent + Float.valueOf(current).longValue()), (valueLast + Float.valueOf(last).longValue())});
		}
		
		
		return getFormattedRegions();
		
	}
	
	public static float getRegionValue(List<Object> list, String region) {
		
		for(int i=0; i<list.size(); i++) {
			if(((Object[])list.get(i))[0].toString().equals(region)) {
				System.out.println(((Object[])list.get(i))[1]);
				return ((BigDecimal)((Object[])list.get(i))[1]).floatValue();
			}
		}
		
		return 0;
	}
	
	private static List<String> regions;
	private static Map<String, long[]> realRegions;
	private static Map<String, String> regionsConversio;
	
	public static List<String> getRegions() {
		
		if(regions == null) {
			
			regions = new ArrayList<String>();
			regions.add("SESPCAPI");
			regions.add("SUL_PR");
			regions.add("SLPRCTBA");
			regions.add("SEMGBHTE");
			regions.add("SUL_MG");
			regions.add("SUL_SC");
			regions.add("SLSCFNLS");
		}
		
		return regions;
	}
	
	private static Map<String, String> getRegionConvertions() {
		
		if(regionsConversio == null) {
			
			regionsConversio = new HashMap<String, String>();
			regionsConversio.put("SESPCAPI", "NORTE");
			regionsConversio.put("SUL_PR", "SUL");
			regionsConversio.put("SLPRCTBA", "SUL");
			regionsConversio.put("SEMGBHTE", "NORDESTE");
			regionsConversio.put("SUL_MG", "CENTRO_OESTE");
			regionsConversio.put("SUL_SC", "SUL");
			regionsConversio.put("SLSCFNLS", "CENTRO_OESTE");
		}
		
		return regionsConversio;
	}
	
	private static String getRealRegion(String region) {
		
		return getRegionConvertions().get(region);
		
	} 

	public static Map<String, long[]> getFormattedRegions() {
	
		if(realRegions == null) {
			realRegions = new HashMap<String, long[]>();
			realRegions.put("SUL", new long[]{0, 0});
			realRegions.put("SUDESTE", new long[]{0, 0});
			realRegions.put("NORTE", new long[]{0, 0});
			realRegions.put("NORDESTE", new long[]{0, 0});
			realRegions.put("CENTRO_OESTE", new long[]{0, 0});
		}
		
		return realRegions;
	}

	@PermitAll
	@GET
	@Path("/overview/newUsers")
	@Produces("application/json; charset=UTF-8")
	public Response overviewNewUsers() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		//----------------------------------------------------------------------------------
		realRegions = null;
		List<Object> currentMonthNewUsers = userFacade.getTotalNewUsers(today);
		List<Object> lastMonthNewUsers = userFacade.getTotalNewUsers(lastMonth);
		float currentMonthNewUsersTotal = getSumValue(currentMonthNewUsers);
		float lastMonthNewUsersTotal = getSumValue(lastMonthNewUsers);
		getSumarizedRegions(currentMonthNewUsers, lastMonthNewUsers);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_ASSINANTES, Long.toString((long)currentMonthNewUsersTotal), (Float.valueOf((currentMonthNewUsersTotal/zero(lastMonthNewUsersTotal, currentMonthNewUsersTotal)-1)*100)).longValue(), getFormattedRegions()));
		
		return Response.status(200).entity(list).build();
	}
	
	@PermitAll
	@GET
	@Path("/overview/newAssets")
	@Produces("application/json; charset=UTF-8")
	public Response overviewNewAssets() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		
		realRegions = null;
		List<Object> currentMonthNewAssets = assetFacade.getTotalNewAssets(today);
		List<Object> lastMonthNewAssets = assetFacade.getTotalNewAssets(lastMonth);
		float currentMonthNewAssetsTotal = Float.valueOf(currentMonthNewAssets.get(0).toString());
		float lastMonthNewAssetsTotal = Float.valueOf(lastMonthNewAssets.get(0).toString());
		
		/*getSumarizedRegions(currentMonthNewAssets, lastMonthNewAssets);
		
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}*/
		
		list.add(new OverviewVO(OverviewVO.TYPE_CONTEUDO, Long.toString((long)currentMonthNewAssetsTotal), (Float.valueOf((currentMonthNewAssetsTotal/zero(lastMonthNewAssetsTotal, currentMonthNewAssetsTotal)-1)*100)).longValue()));
		
		return Response.status(200).entity(list).build();
	}
	
	@PermitAll
	@GET
	@Path("/overview/logonRegion")
	@Produces("application/json; charset=UTF-8")
	public Response overviewLogonRegion() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		
		realRegions = null;
		List<Object> currentMonthLogonByRegion = userFacade.getLogonByRegion(today);
		List<Object> lastMonthLogonByRegion = userFacade.getLogonByRegion(lastMonth);
		float currentMonthTotal = getSumValue(currentMonthLogonByRegion);
		float lastMonthTotal = getSumValue(lastMonthLogonByRegion);
		
		getSumarizedRegions(currentMonthLogonByRegion, lastMonthLogonByRegion);
		
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_LOGON, Long.toString((long)currentMonthTotal), (Float.valueOf((currentMonthTotal/zero(lastMonthTotal, currentMonthTotal)-1)*100)).longValue(), getFormattedRegions()));
		
		
		return Response.status(200).entity(list).build();
	}
	
	@PermitAll
	@GET
	@Path("/overview/totalLogonByRegion")
	@Produces("application/json; charset=UTF-8")
	public Response overviewTotalLogonByRegion() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		
		realRegions = null;
		List<Object> currentMonthLogon = userFacade.getTotalLogonByRegion(today);
		List<Object> lastMonthLogon = userFacade.getTotalLogonByRegion(lastMonth);
		float currentMonthTotalLogon = getSumValue(currentMonthLogon);
		float lastMonthTotalLogon = getSumValue(lastMonthLogon);
		
		getSumarizedRegions(currentMonthLogon, lastMonthLogon);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_HITS, Long.toString((long)currentMonthTotalLogon), (Float.valueOf((currentMonthTotalLogon/zero(lastMonthTotalLogon, currentMonthTotalLogon)-1)*100)).longValue(), getFormattedRegions()));
		
		return Response.status(200).entity(list).build();
	}
	
	@PermitAll
	@GET
	@Path("/overview/purchaseByRegion")
	@Produces("application/json; charset=UTF-8")
	public Response overviewPurchaseByRegion() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		
		realRegions = null;
		List<Object> currentMonthPurchase = purchaseFacade.getPurchaseByRegion(today);
		List<Object> lastMonthPurchase = purchaseFacade.getPurchaseByRegion(lastMonth);
		float currentMonthTotalPurchase = getSumValue(currentMonthPurchase);
		float lastMonthTotalPurchase = getSumValue(lastMonthPurchase);
		
		getSumarizedRegions(currentMonthPurchase, lastMonthPurchase);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{valueCurrent, percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_PACOTES, Long.toString((long)currentMonthTotalPurchase), (Float.valueOf((currentMonthTotalPurchase/zero(lastMonthTotalPurchase, currentMonthTotalPurchase)-1)*100)).longValue(), getFormattedRegions()));
		
		return Response.status(200).entity(list).build();
	}
	
	@PermitAll
	@GET
	@Path("/overview/minutesPlayedByRegion")
	@Produces("application/json; charset=UTF-8")
	public Response overviewMinutesPlayedByRegion() {
		//TODO definir dados a serem mostrados
		ArrayList<OverviewVO> list = new ArrayList<OverviewVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		GregorianCalendar lastMonth = new GregorianCalendar();
		lastMonth.setTime(today.getTime());
		lastMonth.add(Calendar.MONTH, -1);
		
		Iterator<Entry<String, long[]>> it = null;
		
		realRegions = null;
		List<Object> currentMonthMinutes = userFacade.getMinutesPlayedByRegion(today);
		List<Object> lastMonthMinutes = userFacade.getMinutesPlayedByRegion(lastMonth);
		float currentMonthTotalMinutes = getSumValue(currentMonthMinutes)/today.get(Calendar.DAY_OF_MONTH);
		float lastMonthTotalMinutes = getSumValue(lastMonthMinutes)/today.get(Calendar.DAY_OF_MONTH);
		
		long hours = TimeUnit.MINUTES.toHours(Float.valueOf(currentMonthTotalMinutes).longValue());
		
		getSumarizedRegions(currentMonthMinutes, lastMonthMinutes);
		it = getFormattedRegions().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    long valueCurrent = getFormattedRegions().get(pairs.getKey())[0];
		    long valueLast = getFormattedRegions().get(pairs.getKey())[1];
		    long percent = (Float.valueOf((valueCurrent/zero(valueLast, valueCurrent)-1)*100)).longValue();
		    
			getFormattedRegions().put(pairs.getKey().toString(), new long[]{((int) TimeUnit.MINUTES.toHours(valueCurrent/today.get(Calendar.DAY_OF_MONTH))), percent});
		}
		list.add(new OverviewVO(OverviewVO.TYPE_AUDIENCIA, String.valueOf(hours), (Float.valueOf((currentMonthTotalMinutes/zero(lastMonthTotalMinutes, currentMonthTotalMinutes)-1)*100)).longValue(), getFormattedRegions()));
		
		return Response.status(200).entity(list).build();
	}
}
