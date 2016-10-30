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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Field;
import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.MixType;
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
	
	
	// 读取操作类型
	public static  int  getMixType(String jdbcfile) 
	{
		Properties prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(Config.getJdbc_config());
			prop.load(in);
		} catch (FileNotFoundException e) {
			logger.warn("jdbc-file {} not found .",jdbcfile);
			return 0;
		} catch (IOException e) {
			logger.warn("jdbc-file {} not open .",jdbcfile);
			return 0;
		}
		
		String mixtype=prop.getProperty("mixcolumn.type").trim();
		
		if(mixtype.equals("adjunction"))
		{
			return MixType.adjunction;
		}else if(mixtype.equals("transform"))
		{
			return MixType.transform;
		}
		
		return 0;
		
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
		mix.setTable(prop.getProperty("mixcolumn.transform.table").trim());
		mix.setNew_table(prop.getProperty("mixcolumn.transform.new_table").trim());
		
		List<String> columns= Arrays.asList(prop.getProperty("mixcolumn.transform.column").trim().split(","));
		List<String> transforms = Arrays.asList(prop.getProperty("mixcolumn.transform.class").trim().split(","));
		
		List<AbstractTransform> trans = new ArrayList<AbstractTransform>();
		List<Field> originalFields = new ArrayList<Field>();
		
		if (columns.size() == transforms.size())
		{
			for(int i=0;i<columns.size();i++)
			{
			
				originalFields.add(new Field(columns.get(i)));
				trans.add(loadJar(transforms.get(i),jarfile));
			}
			
			mix.setOriginalFields(originalFields);
			mix.setTransforms(trans);
		}
		
		
		return mix;
		
	}
	
	// 读取jdbc中配置
	public static  Adjunction  getAdjunction(String jdbcfile) 
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

	//构造赋值等式
	public static List<String>  constructEquals(List<String> columns,Object identity)
	{
		List<String> equals=new ArrayList<String>();
		
		for(String column : columns)
		{
			String equal= column+" = "+"\""+(String) ((HashMap<String,Object>)identity).get(column)+"\"";
			equals.add(equal);
		}
		
		return equals;
	}
	
	//构造赋值等式
	public static List<String>  constructEqualsUseTransform(List<String> columns,Object identity,List<AbstractTransform> trans)
	{
		List<String> equals=new ArrayList<String>();
		
		if(columns.size() == trans.size())
		{
			for(int i=0;i<columns.size();i++)
			{
				String old_value = (String) ((HashMap<String,Object>)identity).get(columns.get(i));
				String new_value = trans.get(i).transform(old_value);
				logger.info(old_value + " ---> {} ",new_value);
				String equal= columns.get(i)+" = "+"\""+new_value+"\"";
				equals.add(equal);
			}
		}
		
		return equals;
	}
	
}
