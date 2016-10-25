package cn.kejso.Tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import cn.kejso.Mix.AdjColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Field;
import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.Transform;
import cn.kejso.Sql.Config;

public class Util {
	
	private static Logger logger = LoggerFactory.getLogger(Util.class);
	
	//load jar
	public static AbstractTransform loadJar(String classPath, String jarPath) {
		URLClassLoader cl;
		AbstractTransform impl=null;
		try {
			// 从Jar文件得到一个Class加载器
			cl = new URLClassLoader(new URL[] { new URL(jarPath) });
			// 从加载器中加载Class
			Class<?> c = cl.loadClass(classPath);
			// 从Class中实例出一个对象
			impl = (AbstractTransform) c.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return impl;
	}
	
	// 读取jdbc中配置
	public static  Transform  getTransform(String jdbcfile,String jarfile) 
	{
		Properties prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(Config.getJdbc_config());
			prop.load(in);
		} catch (FileNotFoundException e) {
			logger.warn("jdbc-file {} not found .",jdbcfile);
			return null;
		} catch (IOException e) {
			logger.warn("jdbc-file {} not open .",jdbcfile);
			return null;
		}
		
		Transform mix = new Transform();
		mix.setTable(prop.getProperty("mixcolumn.table").trim());
		mix.setNew_table(prop.getProperty("mixcolumn.new_table").trim());
		mix.setOriginalFields(new Field(prop.getProperty("mixcolumn.column").trim()));
		mix.setMappingFields(new Field(prop.getProperty("mixcolumn.column").trim()));
		String classpath=prop.getProperty("mixcolumn.transform").trim();
		mix.setTransform(loadJar(classpath,jarfile));
		
		return mix;
		
	}
	
	// 读取jdbc中配置
	public static  Adjunction  getAdjunction(String jdbcfile,String jarfile) 
	{
		Properties prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(Config.getJdbc_config());
			prop.load(in);
		} catch (FileNotFoundException e) {
			logger.warn("jdbc-file {} not found .",jdbcfile);
			return null;
		} catch (IOException e) {
			logger.warn("jdbc-file {} not open .",jdbcfile);
			return null;
		}
		
		Adjunction ad = new Adjunction();
		ad.setKey_table(prop.getProperty("mixcolumn.adjunction.key_table").trim());
		ad.setSide_table(prop.getProperty("mixcolumn.adjunction.side_table").trim());
		ad.setKey_identity_column(prop.getProperty("mixcolumn.adjunction.key_identity_column").trim());
		ad.setSide_identity_column(prop.getProperty("mixcolumn.adjunction.side_identity_column").trim());
		ad.setNew_key_table(prop.getProperty("mixcolumn.adjunction.new_key_table").trim());
		
		String add_columns=prop.getProperty("mixcolumn.adjunction.side_add_columns").trim();
		ad.setSide_add_columns(Arrays.asList(add_columns.split(",")));
		
		return ad;
		
	}

	// 读取jdbc中配置
	public static AdjColumn getAdjColumn(String jdbcfile, String jarfile)
	{
		Properties prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(Config.getJdbc_config());
			prop.load(in);
		} catch (FileNotFoundException e) {
			logger.warn("jdbc-file {} not found .",jdbcfile);
			return null;
		} catch (IOException e) {
			logger.warn("jdbc-file {} not open .",jdbcfile);
			return null;
		}

		AdjColumn mix = new AdjColumn();
		mix.setTable(prop.getProperty("mixcolumn.AdjColumn.table").trim());
		mix.setNew_table(prop.getProperty("mixcolumn.AdjColumn.new_table").trim());
		mix.setKey_column(new Field(prop.getProperty("mixcolumn.AdjColumn.key_column").trim()));
		mix.setSide_column(new Field(prop.getProperty("mixcolumn.AdjColumn.side_column").trim()));
		String classpath=prop.getProperty("mixcolumn.AdjColumn.transform").trim();
		mix.setTransform(loadJar(classpath,jarfile));

		return mix;

	}
	
	//构造赋值等式
	public static List<String>  constructEquals(List<String> columns,Object identity)
	{
		List<String> equals=new ArrayList<String>();
		
		for(String column : columns)
		{
			String equal=column+" = "+ (String) ((HashMap<String,Object>)identity).get(column);
			equals.add(equal);
		}
		
		return equals;
	}
	
	
}
