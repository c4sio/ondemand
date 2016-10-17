package br.com.gvt.eng.convoy.rest;

import java.util.List;

import br.com.gvt.eng.convoy.vo.IpvodContent;

public interface ContentRest {

	/**
	 * @return List<IpvodContent>
	 */
	public abstract List<IpvodContent> listAllContent();

	/**
	 * @param filename
	 * @return IpvodContent
	 */
	public abstract IpvodContent checkContent(String filename);

	/**
	 * @param ipvodContent
	 */
	public abstract void deleteContent(IpvodContent ipvodContent);

}
