package br.com.gvt.eng.vod.dao;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;

@Stateless
public class VerifyConnectionDAO extends GenericDAO<Object> {

	public VerifyConnectionDAO() {
		super(Object.class);
	}

	public String verifyConnection() {
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = (Date) getSession()
				.createQuery("select sysdate from IpvodAsset").setMaxResults(1)
				.uniqueResult();
		return formatter.format(date);
	}

}
