package cn.kejso.Sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	//mybatis配置文件
	public static final  String   Mybatis_config="mybatis_config.xml"; 
	
	//jar文件前缀
	public static String   prefix_jarfile="file:///";
	
	private static String  Jdbc_config;
		
	
	public static String getJdbc_config() {
		if(Jdbc_config!=null&&!Jdbc_config.equals(""))
			return Jdbc_config;
		else
			return null;
	}
	public static void setJdbc_config(String jdbc_config) {
		Jdbc_config = jdbc_config;
		logger.info("set jdbc config file [ {} ] .",jdbc_config);
	}
	
	// 复制表结构
	public static final String  copyTableStructure="SqlMapper.TemplateMapper.copyTableStructure";
	// 复制表数据
	public static final String  copyTableData="SqlMapper.TemplateMapper.copyTableData";
	// 更新一行
	public static final String  updateColumn="SqlMapper.TemplateMapper.updateColumn";
	// 获得所有数据
	public static final String  getAllData="SqlMapper.TemplateMapper.getAllData";
	
}
