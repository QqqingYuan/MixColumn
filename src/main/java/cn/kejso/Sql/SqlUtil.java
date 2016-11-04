package cn.kejso.Sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.Mix.Adjunction;
import cn.kejso.Tool.Util;

public class SqlUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SqlUtil.class);
	private static SqlSessionFactory sessionFactory = null;
	
	// 读取mybatis配置文件
	static {
		Reader reader = null;
		Properties prop = null;
		try {
			reader = Resources.getResourceAsReader(Config.Mybatis_config);
			prop= new Properties();
			FileInputStream in = new FileInputStream(Config.getJdbc_config());
			prop.load(in);
		} catch (IOException e) {
			logger.error("读取数据库配置文件出错!");
			e.printStackTrace();
		}
		//sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		sessionFactory = new SqlSessionFactoryBuilder().build(reader, prop);
	}
	
	public static SqlSession getSession() {
		return sessionFactory.openSession();
	}
	
	//获得一个表的所有列
	public static List<String> getColumns(SqlSession session , String table , String database) {
		
		List<String> columns = new ArrayList<String>();
		
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("table",table);
		create.put("database",database);
		
		List<Object> data = session.selectList(Config.getColumns, create);
		for(Object one : data)
		{
			columns.add((String) ((HashMap<String,Object>)one).get("COLUMN_NAME"));
		}
		
		// delete id
		if(columns.contains("id"))
		{
			columns.remove(columns.indexOf("id"));
		}
		
		return columns;
	}
	
}
