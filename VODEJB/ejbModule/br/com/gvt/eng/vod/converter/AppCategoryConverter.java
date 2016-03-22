package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodAppCategory;
import br.com.gvt.eng.vod.vo.AppCategoryVO;

public class AppCategoryConverter {

	/**
	 * @param listApps
	 * @return List<AppCategoryVO>
	 */
	public List<AppCategoryVO> getAppCategoryList(
			List<IpvodAppCategory> listApps) {
		List<AppCategoryVO> listAppCategoryVO = new ArrayList<AppCategoryVO>();
		try {
			if (listApps.isEmpty()) {
				return listAppCategoryVO;
			}

			for (IpvodAppCategory ipvodAppCategory : listApps) {
				listAppCategoryVO.add(toAppCategoryVO(ipvodAppCategory));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppCategoryVO;
	}

	/**
	 * @param ipvodAppCategory
	 * @return AppCategoryVO
	 */
	private AppCategoryVO toAppCategoryVO(IpvodAppCategory ipvodAppCategory) {
		AppCategoryVO appCategoryVO = null;
		AppInfosConverter appInfosConverter = new AppInfosConverter();
		try {
			appCategoryVO = new AppCategoryVO();
			appCategoryVO.setCategoryLabel(ipvodAppCategory.getName());

			if (ipvodAppCategory.getIpvodAppInfos() != null) {
				appCategoryVO.setItems(appInfosConverter
						.getListAppInfoVO(ipvodAppCategory.getIpvodAppInfos()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appCategoryVO;
	}
}
