package br.com.gvt.vod.facade.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.gvt.eng.vod.dao.AuthenticationDAO;
import br.com.gvt.eng.vod.dao.EquipmentDAO;
import br.com.gvt.eng.vod.dao.UserDAO;
import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.vo.AuthVO;
import br.com.gvt.vod.facade.AuthenticationFacade;

@Stateless
public class AuthenticationFacadeImp implements AuthenticationFacade {

	@EJB
	private AuthenticationDAO authenticationDAO;

	@EJB
	private UserDAO userDAO;

	@EJB
	private EquipmentDAO equipmentDAO;

	// private final String vpnAddress = "http://172.25.98.132:6451";
	private final String vpnAddress = "http://192.168.200.2:6451";

	@Override
	public IpvodAuthentication verifyToken(String token, long userID) {
		return authenticationDAO.verifyToken(token, userID);
	}

	@Override
	public IpvodAuthentication getAuthByToken(String token) {
		return authenticationDAO.getAuthByToken(token);
	}

	@Override
	public IpvodAuthentication getAuthByUser(long userID) {
		return authenticationDAO.getAuthByUser(userID);
	}

	@Override
	public IpvodAuthentication verifyExpiration(String token) {
		return authenticationDAO.verifyExpiration(token);
	}

	@Override
	public String manageToken(AuthVO auth) throws BusinessException {

		IpvodEquipment equip = null;
		if (auth.getCaId() != null) {
			equip = equipmentDAO.getEquipmentByCAId(auth.getCaId());
		} else if (auth.getMac() != null) {
			equip = equipmentDAO.getEquipmentByMac(auth.getMac());
		}

		if (equip == null) {
			throw new BusinessException("Equipment not found");
		}

		// FIXME CASO O EQUIPAMENTO NÃO TENHA MAC, ATUALIZA O EQUIPAMENTO
		// (MANTER?)
		if (equip.getMac() == null) {
			equip.setMac(auth.getMac());
			equipmentDAO.save(equip);
		}

		// if (!equip.getAuthInfo().equals(auth.getAuthInfo())) {
		// throw new BusinessException("Equipment not found by auth-info");
		// }

		if (!equip.getIpvodUser().getActive()) {
			throw new BusinessException("User inactive");
		}

		IpvodAuthentication ipvodAuthentication = authenticationDAO
				.getValidAuthByEquipment(equip.getEquipmentId());

		if (ipvodAuthentication == null
				|| !ipvodAuthentication.getIpAddress().equals(auth.getIp())) {
			getNewToken(auth, equip);
		} else {
			auth.setToken(ipvodAuthentication.getToken());
		}

		return auth.getToken();
	}

	/**
	 * @param auth
	 * @param userID
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private boolean isValidToken(AuthVO auth, long userID) {
		IpvodAuthentication ipvodAuthentication = authenticationDAO
				.verifyToken(auth.getToken(), userID);
		if (ipvodAuthentication != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param auth
	 * @param equip
	 */
	private void getNewToken(AuthVO auth, IpvodEquipment equip) {
		String timestamp = new SimpleDateFormat("dd/MM/yyyy hh:mm:SS.sssss")
				.format(new Date());
		String key = equip.getIpvodUser().getCrmCustomerId() + "-"
				+ auth.getIp() + "-" + equip.getMac() + timestamp;
		String token = MD5(key);
		auth.setToken(token);

		persistToken(auth, equip);
	}

	/**
	 * @param authVO
	 * @param userId
	 */
	private void persistToken(AuthVO authVO, IpvodEquipment equip) {
		IpvodAuthentication ipvodAuthentication = new IpvodAuthentication();
		ipvodAuthentication.setIpAddress(authVO.getIp());
		ipvodAuthentication.setToken(authVO.getToken());
		ipvodAuthentication.setConnection(authVO.getConnection());
		ipvodAuthentication.setIpvodUser(equip.getIpvodUser());
		ipvodAuthentication.setEquipment(equip);
		ipvodAuthentication.setAuthDate(new Date());

		// DATA DE EXPIRACAO DO TOKEN STB = data atual + 10 dias
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 10);
		ipvodAuthentication.setExpirationDate(c.getTime());

		authenticationDAO.save(ipvodAuthentication);
	}

	/**
	 * @param md5
	 * @return key
	 */
	private String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AuthVO lastAuthenticationEquipment(String serial) {
		return authenticationDAO.getLastAuthBySerialEquipment(serial);
	}

	@Override
	public void delete(List<IpvodAuthentication> ipvodAuthentications) {
		authenticationDAO.delete(ipvodAuthentications);
	}

	@Override
	/*
	 * Usado no PVR4k para ativar o house holding. see (ABRS-ICD-001 Encryptor
	 * Input ICD_v2.pdf)
	 * /tokenGen/activation/{householdRef}?[appUsername={appUsername
	 * }][&drmEuid={drmEuid}]
	 */
	public String activate(String chipID) throws BusinessException {

		IpvodEquipment equipment = equipmentDAO.getEquipmentByCAId(chipID);
		String crmCustomerID = equipment.getIpvodUser().getCrmCustomerId();

		String url = vpnAddress + "/tokenGen/activation/" + crmCustomerID;

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.addRequestProperty("Accept", "text/plain");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();

		} catch (Exception e) {
			throw new BusinessException("Unable to activate house holding.", e);
		}
	}

	@Override
	public String getTokenStreaming(Long assetId, Date expirationDate,
			String chipId) throws BusinessException {
		SimpleDateFormat expirationDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		String entitlementExpDate = expirationDateFormat.format(expirationDate);

		// TODO - Colocar endereco em properties e devem ser utilizados os
		// dois IPS em cluster
		/*
		 * 172.25.98.132 6451 - 172.25.98.133 6451
		 */
		// String vpnAddress = "http://172.25.98.132:6451";
		String url = vpnAddress + "/tokenGen/authorization/VODStreaming/"
				+ chipId + "?contentID=" + assetId
				+ "_Movie&entitlementExpDate=" + entitlementExpDate
				+ "&appUsername=" + chipId + "&drmEuid=" + chipId;

		try {
			System.out.println("Sending 'GET' request to URL: " + url);

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.addRequestProperty("Accept", "text/plain");

			System.out.println("Response Status: " + con.getResponseCode()
					+ "/" + con.getResponseMessage());

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println("Response: " + response.toString());

			return response.toString();

		} catch (Exception e) {
			throw new BusinessException("Unable generate token streaming.", e);
		}
	}
}