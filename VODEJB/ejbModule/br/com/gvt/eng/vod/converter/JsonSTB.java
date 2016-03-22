package br.com.gvt.eng.vod.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodVisualMenu;
import br.com.gvt.eng.vod.vo.ErroVO;

public class JsonSTB {
	public static String toJsonSTB(IpvodVisualMenu ipvodVisualMenu) {
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("IpvodVisualMenu", getVisualMenuFilter())
				.addFilter("IpvodAsset", getAssetFilter())
				.addFilter("IpvodCategory", getCategoryFilter())
				.addFilter("IpvodPurchase", getPurchaseFilter())
				.addFilter("IpvodMediaAsset", getMediaAssetFilter())
				.addFilter("IpvodRating", getRatingFilter());
		
		return writeAsString(filters, ipvodVisualMenu);

	}

	public static String toJsonSTBParentMenu(IpvodVisualMenu ipvodVisualMenu) {
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("IpvodVisualMenu", getVisualMenuParentFilter())
				.addFilter("IpvodPackage", getPackageMenuFilter())
				.addFilter("IpvodRating", getRatingFilter());
			
		return writeAsString(filters, ipvodVisualMenu);

	}

	private static BeanPropertyFilter getVisualMenuParentFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("active");
		prop.add("ipvodVisualMenu");
		prop.add("ipvodVisualComponent");
		prop.add("ipvodAssets");
		prop.add("ipvodVisualMenuAsset");
		prop.add("zindex");
		prop.add("avaliableSince");
		prop.add("avaliableUntil");
		prop.add("permanentMenu");
		prop.add("hasAssets");
		prop.add("hasMenus");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	public static String toJsonSTBVisualMenu(List<IpvodVisualMenu> ipvodVisualMenus) {
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("IpvodVisualMenu", getVisualMenuFilterNoAssets())
				.addFilter("IpvodPackage", getPackageFilter())
				.addFilter("IpvodRating", getRatingFilter());

		return writeAsString(filters, ipvodVisualMenus);
	}
	
	public static String toJsonSTB(IpvodPackage ipvodPackage) {
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("IpvodVisualMenu", getVisualMenuFilter())
				.addFilter("IpvodAsset", getAssetFilter())
				.addFilter("IpvodCategory", getCategoryFilter())
				.addFilter("IpvodPackage", getPackageFilter())
				.addFilter("IpvodAssetPackage", getAssetPackageFilter())
				.addFilter("IpvodPurchase", getPurchaseFilter())
				.addFilter("IpvodRating", getRatingFilter())
				.addFilter("IpvodMediaAsset", getMediaAssetFilter());

		return writeAsString(filters, ipvodPackage);
	}
	
	public static String toJsonSTBAssetPackage(List<IpvodAssetPackage> listAssetPackage) {
		FilterProvider filters = new SimpleFilterProvider()
		.addFilter("IpvodAsset", getAssetFilter())
		.addFilter("IpvodPackage", getPackageFilter())
		.addFilter("IpvodAssetPackage", getAssetPackageFilter())
		.addFilter("IpvodCategory", getCategoryFilter())
		.addFilter("IpvodAssetType", getAssetPackageFilter())
		.addFilter("IpvodMediaAsset", getMediaAssetFilter());

		return writeAsString(filters, listAssetPackage);
	}
	
	public static String writeAsString(FilterProvider filters, Object obj) {
		String dtoAsString = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			dtoAsString = mapper.writer(filters).writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dtoAsString;
	}

	private static SimpleBeanPropertyFilter getVisualMenuFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodVisualMenu");
		prop.add("ipvodVisualMenus");
		prop.add("ipvodVisualComponent");
		prop.add("packageMenu");
		prop.add("active");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	
	private static SimpleBeanPropertyFilter getVisualMenuFilterNoAssets() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodVisualMenu");
		prop.add("ipvodVisualComponent");
		prop.add("ipvodAssets");
		prop.add("active");
		prop.add("permanentMenu");
		prop.add("avaliableSince");
		prop.add("avaliableUntil");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	private static SimpleBeanPropertyFilter getAssetFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("creationDate");
		prop.add("billingID");
		prop.add("canResume");
		prop.add("product");
		prop.add("checksum");
		prop.add("ipvodEquipmentTypes");
		prop.add("ipvodAssetPackages");
		prop.add("ipvodCategory2");
		prop.add("ipvodContentProvider");
		prop.add("ipvodVisualMenus");
		prop.add("licenseWindowEnd");
		prop.add("licenseWindowStart");
		prop.add("fileSize");
		prop.add("bitrate");
		prop.add("titleAlternative");
		prop.add("ipvodVisualMenuAsset");
		prop.add("assetInfo");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	private static SimpleBeanPropertyFilter getCategoryFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodAssets1");
		prop.add("ipvodAssets2");
		prop.add("ipvodAssetsCount");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	private static SimpleBeanPropertyFilter getPackageFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodUsers");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	private static SimpleBeanPropertyFilter getPackageMenuFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("dateEnd");
		prop.add("dateStart");
		prop.add("description");
		prop.add("price");
		prop.add("ipvodAssetPackages");
		prop.add("ipvodPackageType");
		prop.add("ipvodUsers");
		prop.add("otherId");
		prop.add("ipvodRating");
		prop.add("zindex");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	
	private static SimpleBeanPropertyFilter getAssetPackageFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodPackage");
		prop.add("assetPackageId");

		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}

	private static BeanPropertyFilter getPurchaseFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodAsset");
		prop.add("ipvodEquipment");
		prop.add("ipvodSessions");
		prop.add("billed");
		prop.add("amountPaid");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	
	private static BeanPropertyFilter getMediaAssetFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("ipvodAsset");
		prop.add("mediaAssetId");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	
	private static BeanPropertyFilter getRatingFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("description");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	
	private static BeanPropertyFilter getErrorFilter() {
		Set<String> prop = new HashSet<String>();
		prop.add("missingParams");
		
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept(prop);
		return theFilter;
	}
	
	public static String toJsonSTB(ErroVO erro) {
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("ErroVO", getErrorFilter());
		
		return writeAsString(filters, erro);

	}

	public static Object toJsonAssetList(List<IpvodAsset> assetList) {
		FilterProvider filters = new SimpleFilterProvider()
		.addFilter("IpvodAsset", getAssetFilter())
		.addFilter("IpvodCategory", getCategoryFilter())
		.addFilter("IpvodPurchase", getPurchaseFilter())
		.addFilter("IpvodMediaAsset", getMediaAssetFilter())
		.addFilter("IpvodRating", getRatingFilter());

		return writeAsString(filters, assetList);
	}

}