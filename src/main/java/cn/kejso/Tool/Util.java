package cn.kejso.Tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Field;
import cn.kejso.Mix.Mixture;
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
	public static  Mixture  getMixture(String jdbcfile,String jarfile) 
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
		
		Mixture mix = new Mixture();
		mix.setTable(prop.getProperty("mixcolumn.table").trim());
		mix.setNew_table(prop.getProperty("mixcolumn.new_table").trim());
		mix.setOriginalFields(new Field(prop.getProperty("mixcolumn.column").trim()));
		mix.setMappingFields(new Field(prop.getProperty("mixcolumn.column").trim()));
		String classpath=prop.getProperty("mixcolumn.transform").trim();
		mix.setTransform(loadJar(classpath,jarfile));
		
		return mix;
		
	}
}
