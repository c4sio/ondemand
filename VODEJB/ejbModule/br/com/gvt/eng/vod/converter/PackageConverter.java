package br.com.gvt.eng.vod.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.vod.model.IpvodAssetPackage;
import br.com.gvt.eng.vod.model.IpvodPackage;
import br.com.gvt.eng.vod.model.IpvodPackageType;

public class PackageConverter {

	/**
	 * Converte os dados da tabela IpvodPackage
	 * 
	 * @param ipvodPackage
	 * @return List IpvodPackage
	 */
	public List<IpvodPackage> toPackageList(List<IpvodPackage> ipvodPackage) {
		List<IpvodPackage> listPackage = new ArrayList<IpvodPackage>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodPackage == null) {
				return listPackage;
			}
			// Varre a lista para buscar os dados
			for (IpvodPackage packageData : ipvodPackage) {
				listPackage.add(toPackage(packageData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPackage;
	}

	/**
	 * @param packageData
	 * @return IpvodPackage
	 */
	public IpvodPackage toPackage(IpvodPackage packageData) {
		IpvodPackage ipvodPackage = null;
		List<IpvodAssetPackage> listIpvodAssetPackages = null;
		AssetConverter assetConverter = new AssetConverter();
		try {
			ipvodPackage = new IpvodPackage();
			ipvodPackage.setPackageId(packageData.getPackageId());
			ipvodPackage.setDescription(packageData.getDescription());
			ipvodPackage.setPrice(packageData.getPrice());
			ipvodPackage.setDateStart(packageData.getDateStart());
			ipvodPackage.setDateEnd(packageData.getDateEnd());
			ipvodPackage.setOtherId(packageData.getOtherId());
			ipvodPackage.setIpvodRating(packageData.getIpvodRating());
			
			// Busca os dados de tipo de pacote
			if (packageData.getIpvodPackageType() != null) {
				ipvodPackage.setIpvodPackageType(new IpvodPackageType());
				ipvodPackage.getIpvodPackageType().setDescription(
						packageData.getIpvodPackageType().getDescription());
				ipvodPackage.getIpvodPackageType().setPackageTypeId(
						packageData.getIpvodPackageType().getPackageTypeId());
			}

			// Busca dados de asset
			if (packageData.getIpvodAssetPackages() != null
					&& !packageData.getIpvodAssetPackages().isEmpty()) {

				listIpvodAssetPackages = new ArrayList<IpvodAssetPackage>();
				IpvodAssetPackage ipvodAssetPackage = null;

				for (IpvodAssetPackage assetPackage : packageData
						.getIpvodAssetPackages()) {
					ipvodAssetPackage = new IpvodAssetPackage();
					ipvodAssetPackage.setAssetPackageId(assetPackage
							.getAssetPackageId());
					ipvodAssetPackage.setPrice(assetPackage.getPrice());
					ipvodAssetPackage.setIpvodAsset(assetConverter
							.toAssetNoPurchasesNoMediaAsset(assetPackage.getIpvodAsset()));
					listIpvodAssetPackages.add(ipvodAssetPackage);
				}

				ipvodPackage.setIpvodAssetPackages(listIpvodAssetPackages);
			}

			// Busca dados de usuário
//			if (packageData.getIpvodUsers() != null
//					&& !packageData.getIpvodUsers().isEmpty()) {
//				ipvodPackage.setIpvodUsers(userConverter
//						.toListIpvoduser(packageData.getIpvodUsers()));
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodPackage;
	}
	
	/**
	 * Converte os dados da tabela IpvodPackage
	 * 
	 * @param ipvodPackage
	 * @return List IpvodPackage
	 */
	public List<IpvodPackage> toPackageListNoAssets(List<IpvodPackage> ipvodPackage) {
		List<IpvodPackage> listPackage = new ArrayList<IpvodPackage>();
		try {
			// Se for nulo retorna um array vazio
			if (ipvodPackage == null) {
				return listPackage;
			}
			// Varre a lista para buscar os dados
			for (IpvodPackage packageData : ipvodPackage) {
				listPackage.add(toPackageNoAssets(packageData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPackage;
	}

	/**
	 * @param packageData
	 * @return IpvodPackage
	 */
	public IpvodPackage toPackageNoAssets(IpvodPackage packageData) {
		IpvodPackage ipvodPackage = null;
		try {
			ipvodPackage = new IpvodPackage();
			ipvodPackage.setPackageId(packageData.getPackageId());
			ipvodPackage.setDescription(packageData.getDescription());
			ipvodPackage.setPrice(packageData.getPrice());
			ipvodPackage.setDateStart(packageData.getDateStart());
			ipvodPackage.setDateEnd(packageData.getDateEnd());
			ipvodPackage.setOtherId(packageData.getOtherId());
//			ipvodPackage.setIpvodRating(packageData.getIpvodRating());
			
			// Busca os dados de tipo de pacote
			if (packageData.getIpvodPackageType() != null) {
				ipvodPackage.setIpvodPackageType(new IpvodPackageType());
				ipvodPackage.getIpvodPackageType().setDescription(
						packageData.getIpvodPackageType().getDescription());
				ipvodPackage.getIpvodPackageType().setPackageTypeId(
						packageData.getIpvodPackageType().getPackageTypeId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodPackage;
	}
} 