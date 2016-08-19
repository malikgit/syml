package com.biz.syml;






import com.biz.syml.ExtendedSession;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.Session;

import com.debortoliwines.openerp.api.OpenERPXmlRpcProxy.RPCProtocol;
//import com.syml.domain.BaseBean;
//import com.syml.util.UnderwriteAppConfig;


public class OpenERPSessionUtil {

	private static final Logger logger = LoggerFactory.getLogger(OpenERPSessionUtil.class);

	
	public static void main(String[] args) {
		initSession();
		logger.debug("+++++++++++++++++++++++++++++connect to openerp++++++++++++++++++++++++ ");
	}
	
	public static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
    }

	public static void sslInit() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(new KeyManager[0], new TrustManager[] {(TrustManager) new DefaultTrustManager()}, new SecureRandom());
			SSLContext.setDefault(sslContext);
			logger.debug(">>> SSL Initialization done.");
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			logger.error(">>> Error:{}", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * General contract of this method is that, if you call this instead of 
	 * using <code>new Session( .... )</code>, this method call 
	 * {@link Session#startSession()} automatically, and if failed, will try to 
	 * attempt to connect to second connection.
	 * @return Open ERP {@link Session} object.
	 */
	public static ExtendedSession initSession() {
		UnderwriteAppConfig config = UnderwriteAppConfig.getInstance();
		ExtendedSession session = null;
		boolean isDevelopmentMode = config.isDevelopmentMode();
		RPCProtocol protocol = isDevelopmentMode ? RPCProtocol.RPC_HTTPS : RPCProtocol.RPC_HTTP;

		logger.debug("Will connecting to OpenERP using: {} protocol.", protocol);

		if (isDevelopmentMode) {
			sslInit();
			session = new ExtendedSession(protocol,
        			config.getOpenERPURL1(), 
        			Integer.valueOf(config.getOpenERPPort()), 
        			config.getOpenERPDatabaseName(), 
        			config.getOpenERPUsername(), 
        			config.getOpenERPPassword());
			boolean connected = false;
			while (!connected) {
			try {
				session.startSession();
				connected = true;
			} catch (Exception e) {
				logger.warn(">>> Cannot connect to OpenERP server:{} because: {}", config.getOpenERPURL1(), e.toString());
				connected = false;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					logger.error(">>> Thread interrodooUsername1upted! {}", e1);
					e1.printStackTrace();
				}
				/*sslInit();
				session = new ExtendedSession(protocol,
	        			config.getOpenERPURL2(), 
	        			Integer.valueOf(config.getOpenERPPort()), 
	        			config.getOpenERPDatabaseName(), 
	        			config.getOpenERPUsername(), 
	        			config.getOpenERPPassword());
				try {
					session.startSession();
				} catch (Exception ex) {
					logger.error(">>> Cannot connect to OpenERP server:{}. All Open ERP Server is down.", config.getOpenERPURL2());
					throw new RuntimeException(ex);
				}*/
			}
			}
		} else {
			session = new ExtendedSession(config.getOpenERPURL1(), 
        			Integer.valueOf(config.getOpenERPPort()), 
        			config.getOpenERPDatabaseName(), 
        			config.getOpenERPUsername(), 
        			config.getOpenERPPassword());
			try {
				session.startSession();
			} catch (Exception e) {
				logger.warn(">>> Cannot connect to OpenERP server:{}. Attempt to connect to:{}", config.getOpenERPURL1(), config.getOpenERPURL2());
				session = new ExtendedSession(config.getOpenERPURL2(), 
	        			Integer.valueOf(config.getOpenERPPort()), 
	        			config.getOpenERPDatabaseName(), 
	        			config.getOpenERPUsername(), 
	        			config.getOpenERPPassword());
				try {
					session.startSession();
				} catch (Exception ex) {
					logger.error(">>> Cannot connect to OpenERP server:{}. All Open ERP Server is down.", config.getOpenERPURL2());
					throw new RuntimeException(ex);
				}
			}
		}

		logger.debug(">>> OpenERP Session established.");
		logger.debug("Session  is"+session);
		return session;
	}
	
	/*public static DataSynHelper<BaseBean> getOpenERPHelper() {
        try {
        	ExtendedSession openERPSession = initSession();
			DataSynHelper<BaseBean> helper = new DataSynHelper<BaseBean>();
			helper.setOpenERPSession(openERPSession);
        	return helper;
        } catch (Throwable ex) {
        	logger.error("Cannot create DataSynHelper because:{}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }*/
}


