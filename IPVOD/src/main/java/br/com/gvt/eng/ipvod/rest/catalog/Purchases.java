package br.com.gvt.eng.ipvod.rest.catalog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.gvt.eng.vod.vo.PurchaseReportVO;
import br.com.gvt.eng.vod.vo.VDRPurchaseReportVO;
import br.com.gvt.vod.facade.PurchaseFacade;

@Stateless
@Path("/purchase")
public class Purchases {
	@EJB
	private PurchaseFacade purchaseFacade;

	@PermitAll
	@GET
	@Path("/{date}")
	@Produces("application/json; charset=UTF-8")
	public Response listPurchasesByDate(@PathParam("date") String date)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PurchaseReportVO> list = purchaseFacade.findByDate(sdf.parse(date));
		return Response.status(200).entity(list).build();
	}

	@PermitAll
	@GET
	@Path("/download/{date}")
	@Produces("text/plain")
	public Response downloadPurchasesByDate(@PathParam("date") String date)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PurchaseReportVO> list = purchaseFacade.findByDate(sdf.parse(date));
		
		File file = new File("teste.csv");
		generateCsvFile(file, list);
		
		return Response.status(200)
				.entity(file )
				.header("Content-Disposition", "attachment; filename=\"Purchases - " + date + ".csv\"")
				.build(); 
	}
	
	@PermitAll
	@GET
	@Path("/download/{initialDate}&{finalDate}")
	@Produces("text/plain")
	public Response downloadPurchasesByDate(@PathParam("initialDate") String initialDate,@PathParam("finalDate") String finalDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PurchaseReportVO> list = purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate));
		
		File file = new File("teste.csv");
		generateCsvFile(file, list);
		
		return Response.status(200)
				.entity(file )
				.header("Content-Disposition", "attachment; filename=\"Purchases - " + initialDate +" to " + finalDate + ".csv\"")
				.build(); 
	}

	private void generateCsvFile(File file, List<PurchaseReportVO> list) {
		try {
			FileWriter writer = new FileWriter(file);

			writer.append("PURCHASE ID");
			writer.append(';');
			writer.append("ASSET ID");
			writer.append(';');
			writer.append("ASSET TITLE");
			writer.append(';');
			writer.append("PURCHASE DATE");
			writer.append(';');
			writer.append("AMOUNT PAID");
			writer.append(';');
			writer.append("BILLED");
			writer.append(';');
			writer.append("CRM CUSTOMER");
			writer.append('\n');

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			for (PurchaseReportVO purchase : list) {
				writer.append(purchase.getPurchaseId().toString());
				writer.append(';');
				writer.append(purchase.getAssetId().toString());
				writer.append(';');
				writer.append(purchase.getTitle());
				writer.append(';');
				writer.append(sdf.format(purchase.getPurchaseDate()));
				writer.append(';');
				writer.append(purchase.getAmountPaid().toString());
				writer.append(';');
				writer.append(purchase.getBilled().toString());
				writer.append(';');
				writer.append(purchase.getCrmCustomerId());
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PermitAll
	@GET
	@Path("/download/vdr/{initialDate}&{finalDate}")
	@Produces("text/plain")
	public Response downloadVDRFormatPurchasesByDate(@PathParam("initialDate") String initialDate,@PathParam("finalDate") String finalDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<VDRPurchaseReportVO> list = purchaseFacade.findByDateIntervalVDR(sdf.parse(initialDate),sdf.parse(finalDate));
		
		File file = new File("teste.csv");
		generateCsvFileVDR(file, list);
		
		return Response.status(200)
				.entity(file )
				.header("Content-Disposition", "attachment; filename=\"Purchases - " + initialDate +" to " + finalDate + ".csv\"")
				.build(); 
	}

	private void generateCsvFileVDR(File file, List<VDRPurchaseReportVO> list) {
		try {
			FileWriter writer = new FileWriter(file);

//			writer.append("PURCHASE ID");
//			writer.append(';');
//			writer.append("ASSET ID");
//			writer.append(';');
//			writer.append("ASSET TITLE");
//			writer.append(';');
//			writer.append("PURCHASE DATE");
//			writer.append(';');
//			writer.append("AMOUNT PAID");
//			writer.append(';');
//			writer.append("BILLED");
//			writer.append(';');
//			writer.append("CRM CUSTOMER");
//			writer.append('\n');

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			for (VDRPurchaseReportVO purchase : list) {
//				A
				writer.append('|');
//				B
				writer.append('|');
//				C
				writer.append('|');
//				D
				writer.append('|');
//				E
				writer.append('|');
//				F
				writer.append('|');
//				event_date
				writer.append(sdf.format(purchase.getPurchaseDate()));
				writer.append('|');
//				amount_paid
				writer.append(purchase.getAmountPaid().toString());
				writer.append('|');
//				Ib
				writer.append('|');
//				amount_paid
				writer.append(purchase.getAmountPaid().toString());
				writer.append('|');
//				K	
				writer.append('|');
			
//				crm_customerID
				writer.append(purchase.getCrmCustomerId());
				writer.append('|');
//				M
				writer.append('|');
//				asset_id
				writer.append(purchase.getAssetId().toString());
				writer.append('|');
//				title
				writer.append(purchase.getTitle());
				writer.append('|');
//				P
				writer.append('|');
//				service_region
				writer.append(purchase.getServiceRegion());
				writer.append('|');
//				R
				writer.append('|');
//				S
				writer.append('|');
//				purchase_date
				writer.append(sdf.format(purchase.getPurchaseDate()));
				writer.append('|');
//				valid_until
				writer.append(sdf.format(purchase.getValidUntil()));
				writer.append('|');
//				V
				writer.append('|');
//				W
				writer.append('|');
//				provider.name
				writer.append(purchase.getProviderName());
				writer.append('|');
//				provider.provider_id
				writer.append(purchase.getProviderId());
				writer.append('|');
//				Z
				writer.append('|');
//				AA
				writer.append('|');
//				MAC
				writer.append(purchase.getMac());
//				AC
				writer.append('|');
//				AD
				writer.append('|');
//				AE
				writer.append('|');
//				AF
				writer.append('|');
//				AG
				writer.append('|');
//				AH
				writer.append('|');
//				AI
				writer.append('|');
//				AJ
				writer.append('|');
//				AK
				writer.append('|');
//				AL
				writer.append('|');

				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Response downloadPurchasesByDate2(@PathParam("initialDate") String initialDate,@PathParam("finalDate") String finalDate)
			throws ParseException {
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
// 		initialDate = "2015-06-01";
// 		finalDate = "2015-07-01";
// 		System.out.println("consulta: " + initialDate + "  - " + finalDate);
		List<PurchaseReportVO> list = purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate));
//		initialDate = "2015-07-02";
//		finalDate = "2015-07-16";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
//		initialDate = "2015-07-17";
//		finalDate = "2015-08-01";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
//		initialDate = "2015-08-02";
//		finalDate = "2015-09-01";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
//		initialDate = "2015-09-02";
//		finalDate = "2015-09-15";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
//		initialDate = "2015-09-16";
//		finalDate = "2015-10-01";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
//		initialDate = "2015-10-02";
//		finalDate = "2015-11-01";
//		System.out.println("consulta: " + initialDate + "  - " + finalDate);
//		list.addAll(purchaseFacade.findByDateInterval(sdf.parse(initialDate),sdf.parse(finalDate)));
		
		File file = new File("teste.csv");
		generateCsvFile(file, list);
		
		return Response.status(200)
				.entity(file )
				.header("Content-Disposition", "attachment; filename=\"Purchases - " + initialDate +" to " + finalDate + ".csv\"")
				.build(); 
	}
}