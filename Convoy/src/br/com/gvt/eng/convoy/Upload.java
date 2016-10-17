package br.com.gvt.eng.convoy;

import br.com.gvt.eng.convoy.rest.UploadRest;
import br.com.gvt.eng.convoy.rest.impl.UploadRestImpl;

public class Upload {

	public void checkUploadStatus() {
		try {
			UploadRest uploadDAO = new UploadRestImpl();
			String value = null;
			uploadDAO.checkStatusUpload(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// UploadFile

}
