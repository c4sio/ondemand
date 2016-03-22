package br.com.gvt.eng.vod.converter;

import br.com.gvt.eng.vod.model.IpvodAppMenu;
import br.com.gvt.eng.vod.vo.AppMenuVO;

public class AppMenuConverter {

	/**
	 * @param ipvodAppMenu
	 * @param orderMenu
	 * @return List<AppMenuVO>
	 */
	public AppMenuVO toAppMenuVO(IpvodAppMenu ipvodAppMenu, long orderMenu) {
		AppMenuVO appMenuVO = new AppMenuVO();
		try {
			appMenuVO.setAppName(ipvodAppMenu.getAppName());
			appMenuVO.setFitAddress(ipvodAppMenu.getFitAddress());
			appMenuVO.setModuleId(ipvodAppMenu.getModuleId());
			appMenuVO.setOrder(orderMenu);
			appMenuVO.setIcon(ipvodAppMenu.getIpvodAppImage().getIcon());
			appMenuVO.setIconSelected(ipvodAppMenu.getIpvodAppImage()
					.getIconSelected());
			appMenuVO.setActive(ipvodAppMenu.isActive());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appMenuVO;
	}

}
