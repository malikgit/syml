package com.syml.couchbase.dao;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFileReader {

	
static	Logger logger=LoggerFactory.getLogger(ConfigurationFileReader.class);
	public static Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			prop.load(ConfigurationFileReader.class.getClassLoader().getResourceAsStream(
					"DevConfig.properties"));
		} catch (Exception e) {
			logger.error("error in Reading config.properties file" + e);
		}
		return prop;
	}
}
