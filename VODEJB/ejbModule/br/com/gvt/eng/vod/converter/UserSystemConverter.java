package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodUserSystem;

public class UserSystemConverter {

	/**
	 * @param listUserSystem
	 * @return List<IpvodUserSystem>
	 */
	public List<IpvodUserSystem> getUserSystemList(
			List<IpvodUserSystem> listUserSystem) {
		List<IpvodUserSystem> finalListUserSystem = new ArrayList<IpvodUserSystem>();
		try {
			// Se for nulo retorna um array vazio
			if (listUserSystem == null) {
				return finalListUserSystem;
			}
			// Varre a lista para buscar os dados
			for (IpvodUserSystem ipvodUserSystem : listUserSystem) {
				finalListUserSystem.add(toUserSystem(ipvodUserSystem));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalListUserSystem;
	}

	/**
	 * @param ipvodUserSystem
	 * @return IpvodUserSystem
	 */
	public IpvodUserSystem toUserSystem(IpvodUserSystem ipvodUserSystem) {
		IpvodUserSystem userSystem = null;
		try {
			userSystem = new IpvodUserSystem();
			userSystem.setUserSysId(ipvodUserSystem.getUserSysId());
			userSystem.setUsername(ipvodUserSystem.getUsername());
			userSystem.setEmail(ipvodUserSystem.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userSystem;
	}

}
