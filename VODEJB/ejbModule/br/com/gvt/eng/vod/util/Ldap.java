package br.com.gvt.eng.vod.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

public class Ldap {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Boolean authenticate(String login, String password) {
		
		Hashtable authEnv = new Hashtable(11);
		authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		
		authEnv.put(Context.PROVIDER_URL, "ldap://sv2kdc11");
		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		authEnv.put(Context.SECURITY_PRINCIPAL, login + "@gvt.net.br");
		authEnv.put(Context.SECURITY_CREDENTIALS, password);
		
		if (password.equalsIgnoreCase("")) {
			return Boolean.FALSE;
		}
	
		try {
			
			new InitialDirContext(authEnv);
			System.out.println("Autenticação bem sucedida");
			return Boolean.TRUE;
			
		} catch (Exception e) {
			
			System.out.println("Erro ao autenticar o usuário");
			return Boolean.FALSE;
		}
	}
	
}
