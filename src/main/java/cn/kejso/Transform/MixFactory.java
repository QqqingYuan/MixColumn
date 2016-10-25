package cn.kejso.Transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kejso.Mix.AdjColumn;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.Transform;
import cn.kejso.Sql.Config;
import cn.kejso.Sql.SqlUtil;
import cn.kejso.Tool.Util;

public class MixFactory {
	
	private static Logger logger = LoggerFactory.getLogger(MixFactory.class);
	
	/*
	 * transform single column 
	 * 
	 */
	public static  int  transformSingleColumn(Transform mixture)
	{
		String table=mixture.getTable();
		String column=mixture.getOriginalFields().getName();
		String new_table=mixture.getNew_table();
		AbstractTransform tran=mixture.getTransform();
		SqlSession session=SqlUtil.getSession();
		
		int number = 0;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",table);
		map.put("column",column);
		
		List<Object> urls = session.selectList(Config.getAllData, map);

		
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("table",table);
		create.put("new_table",new_table);
		
		//copy table
		session.update(Config.copyTableStructure,create);
		session.update(Config.copyTableData,create);
		session.commit();
		
		//update
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("table",new_table);
		update.put("column",column);
		
		for(Object one:urls)
		{
			String old_value= (String) ((HashMap<String,Object>)one).get(column);
			int id=(Integer) ((HashMap<String,Object>)one).get("id");
			String new_value=tran.transform(old_value);
			logger.info(old_value + " ---> {} ",new_value);
			update.put("value",new_value);
			update.put("id",id);
			
			session.update(Config.updateColumn, update);
			number++;
			
		}
		session.commit();
		
		session.close();
		
		return number;
	}
	
	/*
	 * Adjunction in two tables. 
	 * 
	 */
	public static  int  AdjunctionTables(Adjunction ad)
	{
		
		
		SqlSession session=SqlUtil.getSession();
		int number = 0;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",ad.getKey_table());
		
		for(String column : ad.getSide_add_columns())
		{
			map.put("column",column);
			// add column
			session.update(Config.addColumn,map);
		}
		
		session.commit();
		
		//copy data
		map.clear();
		map.put("table",ad.getSide_table());
		map.put("identity_column",ad.getSide_identity_column());
		map.put("columns", ad.getSide_add_columns());
		List<Object> side_identitys= session.selectList(Config.getDataFromSideTable, map);
		
		for(Object identity:side_identitys)
		{
			String identity_value= (String) ((HashMap<String,Object>)identity).get(ad.getSide_identity_column());
			map.clear();
			List<String> equals=Util.constructEquals(ad.getSide_add_columns(), identity);
			map.put("key_table",ad.getKey_table());
			map.put("equals",equals);
			map.put("key_identity_column",ad.getKey_identity_column());
			map.put("side_identity_value",identity_value);
			
			session.update(Config.copyDatatoColumn,map);
			number++;
		}
		session.commit();
		session.close();
		
		return number;
	}


	/***
	 * Adjunction in to column
	 * @param mixture
	 * @return
	 */
	public static  int  combineColumns(AdjColumn mixture)
	{
		String table=mixture.getTable();
		String keyColumn=mixture.getKey_column().getName();
		String sideColumn=mixture.getSide_column().getName();
		String new_table=mixture.getNew_table();
		AbstractTransform tran=mixture.getTransform();
		SqlSession session=SqlUtil.getSession();

		int number = 0;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",table);
		map.put("column",keyColumn);
		List<Object> col1 = session.selectList(Config.getAllData, map);
		map.put("column",sideColumn);
		List<Object> col2 = session.selectList(Config.getAllData, map);


		Map<String, Object> create = new HashMap<String, Object>();
		create.put("table",table);
		create.put("new_table",new_table);

		//copy table
		session.update(Config.copyTableStructure,create);
		session.update(Config.copyTableData,create);
		session.commit();

		//update
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("table",new_table);
		update.put("column",keyColumn);

		for(int i=0;i<col1.size();i++)
		{
			String old_value1= (String) ((HashMap<String,Object>)col1.get(i)).get(keyColumn);
			String old_value2= (String) ((HashMap<String,Object>)col2.get(i)).get(sideColumn);
			int id=(Integer) ((HashMap<String,Object>)col1.get(i)).get("id");

			String new_value=tran.transform(old_value1,old_value2);
			logger.info(old_value1 +":"+old_value2+ " ---> {} ",new_value);
			update.put("value",new_value);
			update.put("id",id);

			session.update(Config.updateColumn, update);
			number++;

		}
		session.commit();
		update.put("column",sideColumn);
		session.update(Config.deleteColumn,update);

		session.close();

		return number;
	}


}
