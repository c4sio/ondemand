package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.gvt.eng.vod.model.IpvodAsset;
import br.com.gvt.eng.vod.model.IpvodEquipment;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodUser;
import br.com.gvt.eng.vod.vo.AuthVO;
import br.com.gvt.eng.vod.vo.provisioning.Equipment;
import br.com.gvt.eng.vod.vo.provisioning.Product;
import br.com.gvt.eng.vod.vo.provisioning.User;

public class UserConverter {

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @author Cassiano R. Tesseroli
	 * @since 09/01/2014
	 * 
	 */
	public AuthVO toUserVO(String json) {
		AuthVO result = new AuthVO();
		try {
			result = mapper.readValue(json, AuthVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param ipvodUser
	 * @return User
	 */
	public User toUser(IpvodUser ipvodUser) {
		User user = null;
		try {
			if (ipvodUser != null) {
				user = new User();
				user.setUserId(ipvodUser.getUserId());
				user.setSubscriberId(ipvodUser.getCrmCustomerId());
				user.setServiceRegion(ipvodUser.getServiceRegion());
				user.setAuthInfo(ipvodUser.getAuthInfo());
				user.setActive(ipvodUser.getActive());

				List<Product> products = new ArrayList<Product>();
				List<Equipment> equipments = new ArrayList<Equipment>();

				List<IpvodEquipment> ipvodEquipments = ipvodUser.getIpvodEquipments();

				Equipment equipment = new Equipment();
				for (IpvodEquipment ipvodEquipment : ipvodEquipments) {
					equipment = new Equipment();
					equipment.setCardId(ipvodEquipment.getAuthInfo());
					equipment.setCas(ipvodEquipment.getCas());
					equipment.setMainKey(ipvodEquipment.getMainKey());
					equipment.setType(ipvodEquipment.getIpvodEquipmentType().getEquipmentTypeId());
					equipment.setSerial(ipvodEquipment.getSerial());
					equipments.add(equipment);
				}

				List<IpvodPackage> ipvodPackages = ipvodUser.getIpvodPackages();

				Product product = new Product();
				for (IpvodPackage ipvodPackage : ipvodPackages) {
					product = new Product();
					product.setId(ipvodPackage.getOtherId());
					product.setName(ipvodPackage.getDescription());
					products.add(product);
				}
				
				user.setEquipments(equipments);
				user.setProducts(products);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * @param IpvodUser
	 * @return List<IpvodUser>
	 */
	public List<IpvodUser> toListIpvoduser(List<IpvodUser> IpvodUser) {
		List<IpvodUser> listUser = new ArrayList<IpvodUser>();
		try {
			// Se for nulo retorna um array vazio
			if (IpvodUser == null) {
				return listUser;
			}

			for (IpvodUser userData : IpvodUser) {
				listUser.add(toIpvodUser(userData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listUser;
	}

	/**
	 * @param userData
	 * @return IpvodUser
	 */
	public IpvodUser toIpvodUser(IpvodUser userData) {
		IpvodUser ipvodUser = null;
		try {
			ipvodUser = new IpvodUser();

			ipvodUser.setUserId(userData.getUserId());
			ipvodUser.setCrmCustomerId(userData.getCrmCustomerId());
			ipvodUser.setServiceRegion(userData.getServiceRegion());
			ipvodUser.setAuthInfo(userData.getAuthInfo());
			ipvodUser.setActive(userData.getActive());

			List<IpvodEquipment> equipments = new ArrayList<IpvodEquipment>();
			List<IpvodEquipment> ipvodEquipments = userData
					.getIpvodEquipments();

			IpvodEquipment equipment = null;
			for (IpvodEquipment ipvodEquipment : ipvodEquipments) {
				equipment = new IpvodEquipment();
				equipment.setAuthInfo(ipvodEquipment.getAuthInfo());
				equipments.add(equipment);
			}

			List<IpvodPackage> ipvodPackages = userData.getIpvodPackages();
			List<IpvodPackage> products = new ArrayList<IpvodPackage>();

			IpvodPackage product = null;
			for (IpvodPackage packageData : ipvodPackages) {
				product = new IpvodPackage();
				product.setPackageId(packageData.getPackageId());
				product.setDescription(packageData.getDescription());
				products.add(product);
			}
			
			List<IpvodAsset> ipvodAssets = userData.getIpvodAssets();
			List<IpvodAsset> assets = new ArrayList<IpvodAsset>();
			IpvodAsset ipvodAsset = null;
			for (IpvodAsset assetData : ipvodAssets) {
				ipvodAsset = new IpvodAsset();
				ipvodAsset.setAssetId(assetData.getAssetId());
				ipvodAsset.setTitle(assetData.getTitle());
				assets.add(ipvodAsset);
			}

			ipvodUser.setIpvodAssets(assets);
			ipvodUser.setIpvodEquipments(equipments);
			ipvodUser.setIpvodPackages(products);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodUser;
	}
}
