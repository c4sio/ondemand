package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodAppInfo;
import br.com.gvt.eng.vod.util.Utils;
import br.com.gvt.eng.vod.vo.AppInfoVO;

public class AppInfosConverter {

	/**
	 * @param ipvodAppInfos
	 * @return List<AppInfoVO>
	 */
	public List<AppInfoVO> getListAppInfoVO(List<IpvodAppInfo> ipvodAppInfos) {
		List<AppInfoVO> listAppInfoVO = new ArrayList<AppInfoVO>();
		try {
			if (ipvodAppInfos.isEmpty()) {
				return listAppInfoVO;
			}

			for (IpvodAppInfo appInfo : ipvodAppInfos) {
				listAppInfoVO.add(toAppInfoVO(appInfo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAppInfoVO;
	}

	/**
	 * @param appInfo
	 * @return AppInfoVO
	 */
	private AppInfoVO toAppInfoVO(IpvodAppInfo appInfo) {
		AppInfoVO appInfoVO = null;
		try {
			appInfoVO = new AppInfoVO();

			if (!Utils.isEmptyOrNull(appInfo.getIpvodAppImage().getIcon())) {
				appInfoVO.setIcon(appInfo.getIpvodAppImage().getIcon());
			}

			if (!Utils.isEmptyOrNull(appInfo.getIpvodAppImage()
					.getIconSelected())) {
				appInfoVO.setIconSelected(appInfo.getIpvodAppImage()
						.getIconSelected());
			}

			if (!Utils.isEmptyOrNull(appInfo.getModuleId())) {
				appInfoVO.setModuleId(appInfo.getModuleId());
			}

			if (!Utils.isEmptyOrNull(appInfo.getFitAddress())) {
				appInfoVO.setFitAddress(appInfo.getFitAddress());
			}

			if (!Utils.isEmptyOrNull(appInfo.getAppName())) {
				appInfoVO.setName(appInfo.getAppName());
			}

			appInfoVO.setOrder(appInfo.getOrder());
			appInfoVO.setVersion(appInfo.getVersion());

			if (!Utils.isEmptyOrNull(appInfo.getIpvodEquipmentType()
					.getDescription())) {
				appInfoVO.setEquipment(appInfo.getIpvodEquipmentType()
						.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appInfoVO;
	}

}
