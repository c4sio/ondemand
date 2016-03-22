package br.com.gvt.eng.vod.converter;

import br.com.gvt.eng.vod.model.IpvodMediaType;

public class MediaTypeConverter {

	/**
	 * @param ipvodMediaType
	 * @return IpvodMediaType
	 */
	public IpvodMediaType toMediaType(IpvodMediaType ipvodMediaType) {
		IpvodMediaType mediaType = null;
		try {
			if (ipvodMediaType != null) {
				mediaType = new IpvodMediaType();
				mediaType.setDescription(ipvodMediaType.getDescription());
				mediaType.setMediaTypeId(ipvodMediaType.getMediaTypeId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mediaType;
	}

}