package cn.kejso.Sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	//mybatis配置文件
	public static final  String   Mybatis_config="mybatis_config.xml"; 
	
	private static String  Jdbc_config;
	
	public static String getJdbc_config() {
		if(!Jdbc_config.equals("")&&Jdbc_config!=null)
			return Jdbc_config;
		else
			return null;
	}
	public static void setJdbc_config(String jdbc_config) {
		Jdbc_config = jdbc_config;
		logger.info("set jdbc config file [ {} ] .",jdbc_config);
	}
	
}
