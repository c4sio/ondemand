package br.com.gvt.vod.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.gvt.eng.vod.exception.bo.BusinessException;
import br.com.gvt.eng.vod.model.IpvodAuthentication;
import br.com.gvt.eng.vod.vo.AuthVO;

@Local
public interface AuthenticationFacade {

	public IpvodAuthentication verifyToken(String token, long userID);

	public IpvodAuthentication getAuthByToken(String token);

	public IpvodAuthentication getAuthByUser(long userID);

	public IpvodAuthentication verifyExpiration(String token);

	public String manageToken(AuthVO authVO) throws BusinessException;

	public AuthVO lastAuthenticationEquipment(String serial);

	/**
	 * Gera o dados do token na plataforma DRM
	 * 
	 * @param assetId
	 * @param expirationDate
	 * @param chipId
	 * @return token
	 * @throws BusinessException
	 */
	public abstract String getTokenStreaming(Long assetId, Date expirationDate,
			String chipId) throws BusinessException;

	public String activate(String chipID) throws BusinessException;

	public void delete(List<IpvodAuthentication> ipvodAuthentications);

}
