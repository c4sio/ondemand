package br.com.gvt.eng.vod.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.gvt.eng.vod.model.IpvodCheckSumData;

@Stateless
public class CheckSumDAO extends GenericDAO<IpvodCheckSumData> {

	public CheckSumDAO() {
		super(IpvodCheckSumData.class);
	}

	/**
	 * Remove o registro da base
	 * 
	 * @param ipvodCheckSumData
	 */
	public void deleteCheckSum(IpvodCheckSumData ipvodCheckSumData) {
		super.delete(ipvodCheckSumData.getCheckSumId(), IpvodCheckSumData.class);
	}

	/**
	 * @param fileName
	 * @return IpvodCheckSumData
	 */
	public IpvodCheckSumData findCheckSumByFileName(String fileName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fileName", fileName);
		return super.findOneResult(IpvodCheckSumData.FIND_BY_FILE_NAME,
				parameters);
	}

}
