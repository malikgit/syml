package com.biz.syml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;

import com.biz.syml.StringUtil;

/**
 * Used to read externalized configuration for the Underwriting application. 
 * 
 * In development machine, if you don't want to set bunch of system environment 
 * variable, just put properties file in your home directory (window user need 
 * to create one 'user.home' environment variable first). 
 * 
 * @author xsalefter
 */
public final class UnderwriteAppConfig {

	private static final String SEPARATOR = File.separator;

	private static final String DB_URL = "UW_DB_URL";
	private static final String DB_USERNAME = "UW_DB_USERNAME";
	private static final String DB_PASSWORD = "UW_DB_PASSWORD";
	private static final String OPENERP_URL_1 = "UW_OPENERP_URL_1";
	private static final String OPENERP_URL_2 = "UW_OPENERP_URL_2";
	private static final String OPENERP_PORT = "UW_OPENERP_PORT";
	private static final String OPENERP_DATABASE_NAME = "UW_OPENERP_DATABASE_NAME";
	private static final String OPENERP_USERNAME = "UW_OPENERP_USERNAME";
	private static final String OPENERP_PASSWORD = "UW_OPENERP_PASSWORD";
	private static final String FILOGIX_GATEWAY_URL = "UW_FILOGIX_GATEWAY_URL";
	private static final String MORWEB_GATEWAY_URL = "UW_MORWEB_GATEWAY_URL";
	private static final String FILOGIX_FIRM_CODE = "UW_FILOGIX_FIRM_CODE";
	private static final String FILOGIX_USER_LOGIN = "UW_FILOGIX_USER_LOGIN";

	private static final Logger logger = LoggerFactory.getLogger(UnderwriteAppConfig.class);

	private static UnderwriteAppConfig instance;
	private Properties properties;

	public static UnderwriteAppConfig getInstance() {
		if (instance == null)
			instance = new UnderwriteAppConfig();
		return instance;
	}

	private final String dbURL;
	private final String dbUsername;
	private final String dbPassword;

	private final String openERPURL1;
	private final String openERPURL2;
	private final String openERPPort;
	private final String openERPDatabaseName;
	private final String openERPUsername;
	private final String openERPPassword;

	// Filogix specifics data.
	private final String filogixGatewayURL;
	private final String filogixFirmCode;
	private final String filogixUserLogin;

	// Morweb specifics data.
	private final String morwebGatewayURL;

	// misc
	private final boolean developmentMode;

	private UnderwriteAppConfig() {
		
		try {
			this.loadPropertiesfilefromResourece();
		} catch (FileNotFoundException e) {
			logger.error("error file occured bcoz"+e.getMessage());
		}
		//this.loadPropertiesFromUserHome();

		this.dbURL = getenv(DB_URL);
		this.dbUsername = getenv(DB_USERNAME);
		this.dbPassword = getenv(DB_PASSWORD);
		this.openERPURL1 = getenv(OPENERP_URL_1);
		this.openERPURL2 = getenv(OPENERP_URL_2);
		this.openERPPort = getenv(OPENERP_PORT);
		this.openERPDatabaseName = getenv(OPENERP_DATABASE_NAME);
		this.openERPUsername = getenv(OPENERP_USERNAME);
		this.openERPPassword = getenv(OPENERP_PASSWORD);

		this.filogixGatewayURL = getenv(FILOGIX_GATEWAY_URL);
		this.filogixFirmCode = getenv(FILOGIX_FIRM_CODE);
		this.filogixUserLogin = getenv(FILOGIX_USER_LOGIN);

		this.morwebGatewayURL = getenv(MORWEB_GATEWAY_URL);

		this.developmentMode = this.openERPURL1.contains("dev");

		logger.info("UW configuration value:{}", this.toString());
	}
	private void loadPropertiesfilefromResourece() throws FileNotFoundException{
		
		logger.debug("---------inside load properties form Resource {}:-----------");
		this.properties = new Properties();
		InputStream in = null;
		
		try {
			
			in = Play.application().resourceAsStream("underwrite.properties");
			in=this.getClass().getClassLoader().getResourceAsStream("underwrite.properties");
			
			this.properties.load(in);
			} catch (IOException e) {
				
			logger.debug("while load the properties {}:"+e.getMessage());
		}finally{
			try{
				in.close();
			}catch(IOException e){
				logger.error("Cannot close input stream for file in:{}",e.getMessage());
				
			}
		}
		
	}
	private void loadPropertiesFromUserHome() {
		final String userDir = System.getProperty("user.home");
		final File propertiesFile = new File(userDir + SEPARATOR + "underwrite.properties");
		logger.info(">> 'underwrite.properties' file location: {}", propertiesFile.getAbsolutePath());

		this.properties = new Properties();
		InputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(propertiesFile);
			this.properties.load(fileInputStream);
		} catch (IOException e) {
			logger.error("Cannot load properties file: {}", e);
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				logger.error("Cannot close input stream for file in:{}", propertiesFile.getAbsolutePath());
			}
		}
		
	}

	/**
	 * First, read from environment variable. If not found, will try to get from 
	 * {@link #properties} data. If none of them found, fail-fast: Throw 
	 * {@link RuntimeException}.
	 * @param value environment variable or underwrite.properties file value.
	 * @return String of environment variable.
	 */
	public String getenv(final String value) {
		if (StringUtil.isNullOrEmpty(value)) 
			throw new RuntimeException("'value' parameter in UnderwriteAppConfigTest#getEnv should not contains null or empty string.");

		String result = System.getenv(value);
		if (StringUtil.isNullOrEmpty(result)) {
			logger.warn("Value '{}' is not exist in system environment variable.", value);
			result = this.properties.getProperty(value);
			if (StringUtil.isNullOrEmpty(result)) 
				throw new IllegalStateException("Cannot found variable named '" + value + "' in underwrite.properties file.");
		}

		return result.trim();
	}

	public boolean isDevelopmentMode() {
		return this.developmentMode;
	}

	public boolean isProductionMode() {
		return !isDevelopmentMode();
	}

	public Properties getProperties() {
		return properties;
	}

	public String getDbURL() {
		return dbURL;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getOpenERPURL1() {
		return openERPURL1;
	}

	public String getOpenERPURL2() {
		return openERPURL2;
	}

	public String getOpenERPPort() {
		return openERPPort;
	}

	public String getOpenERPDatabaseName() {
		return openERPDatabaseName;
	}

	public String getOpenERPUsername() {
		return openERPUsername;
	}

	public String getOpenERPPassword() {
		return openERPPassword;
	}

	public String getFilogixGatewayURL() {
		return filogixGatewayURL;
	}

	public String getFilogixFirmCode() {
        return filogixFirmCode;
    }

    public String getFilogixUserLogin() {
        return filogixUserLogin;
    }

    public String getMorwebGatewayURL() {
		return morwebGatewayURL;
	}

	@Override
	public String toString() {
		return "UnderwriteAppConfig [properties=" + properties + 
				", dbURL=" + dbURL + 
				", dbUsername=" + dbUsername + 
				", dbPassword=" + dbPassword + 
				", openERPURL1=" + openERPURL1 + 
				", openERPURL2=" + openERPURL2 + 
				", openERPPort=" + openERPPort + 
				", openERPDatabaseName=" + openERPDatabaseName + 
				", openERPUsername=" + openERPUsername + 
				", openERPPassword=" + openERPPassword + 
				", filogixGatewayURL=" + filogixGatewayURL + 
				", filogixFirmCode=" + filogixFirmCode + 
				", filogixUserLogin=" + filogixUserLogin + 
				", morwebGatewayURL=" + morwebGatewayURL + 
				", deploymentMode=" + (this.developmentMode ? "DEVELOPMENT" : "PRODUCTION") + "]";
	}

}
