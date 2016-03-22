package br.com.gvt.eng.vod.enums;

public enum PackageHybridEnum {

	SUPER, ULTRA, ULTIMATE;

	public static PackageHybridEnum findPackageNameByCode(int levelNo) {
		switch (levelNo) {
		case 16116:
			return PackageHybridEnum.SUPER;
		case 16113:
			return PackageHybridEnum.ULTRA;
		case 16114:
			return PackageHybridEnum.ULTIMATE;
		default:
			return null;
		}
	}

	public String getPackageName() {
		switch (this) {
		case SUPER:
			return "ipvod_catchup_super";
		case ULTRA:
			return "ipvod_catchup_ultra";
		case ULTIMATE:
			return "ipvod_catchup_ultimate";
		default:
			return "";
		}
	}
}
