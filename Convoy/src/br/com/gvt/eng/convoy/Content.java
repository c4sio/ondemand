package br.com.gvt.eng.convoy;

import java.util.ArrayList;
import java.util.List;

import br.com.gvt.eng.convoy.rest.ContentRest;
import br.com.gvt.eng.convoy.rest.impl.ContentRestImpl;
import br.com.gvt.eng.convoy.vo.IpvodContent;

public class Content {

	/**
	 * @param fileName
	 * @return IpvodContent
	 */
	public IpvodContent findContent(String fileName) {
		IpvodContent ipvodContent = new IpvodContent();
		try {
			ContentRest contentRest = new ContentRestImpl();
			ipvodContent = contentRest.checkContent(fileName);

			if (ipvodContent.getFilename() == null) {
				ipvodContent = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipvodContent;
	}

	/**
	 * @return List<IpvodContent>
	 */
	public List<IpvodContent> listAllContent() {
		List<IpvodContent> listContent = new ArrayList<IpvodContent>();
		try {
			ContentRest contentRest = new ContentRestImpl();
			listContent = contentRest.listAllContent();
			if (listContent.isEmpty()) {
				listContent = new ArrayList<IpvodContent>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listContent;
	}

	/**
	 * @param ipvodContent
	 */
	public boolean deleteContent(String fileName) {
		try {
			ContentRest contentRest = new ContentRestImpl();
			IpvodContent ipvodContent = new IpvodContent();
			ipvodContent = contentRest.checkContent(fileName);

			if (ipvodContent != null) {
				contentRest.deleteContent(ipvodContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
