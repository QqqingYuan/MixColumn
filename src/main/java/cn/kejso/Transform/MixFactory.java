package cn.kejso.Transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.Field;
import cn.kejso.Mix.Merge;
import cn.kejso.Mix.Transform;
import cn.kejso.Mix.Union;
import cn.kejso.Sql.Config;
import cn.kejso.Sql.SqlUtil;
import cn.kejso.Tool.Util;

public class MixFactory {
	
	private static Logger logger = LoggerFactory.getLogger(MixFactory.class);
	
	/*
	 * transform  column 
	 * 
	 */
	public static  int  transformColumn(Transform mixture)
	{
		String table=mixture.getTable();
		
		List<Field> origincolumns=mixture.getOriginalFields();
		
		List<String> fields = new ArrayList<String>();
		
		for(Field one : origincolumns)
		{
			fields.add(one.getName());
		}
		
		
		String new_table=mixture.getNew_table();
		
		List<AbstractTransform> trans=mixture.getTransforms();
		
		SqlSession session=SqlUtil.getSession();
		
		int number = 0;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",table);
		map.put("columns",fields);
		
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
		
		for(Object one:urls)
		{
			List<String> equals=Util.constructEqualsUseTransform(fields, one, trans);
			int id=(Integer) ((HashMap<String,Object>)one).get("id");
			
			update.put("equals",equals);
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
		
		logger.info("copy key_table {} to new_key_table {} .",ad.getKey_table(),ad.getNew_key_table());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",ad.getKey_table());
		map.put("new_table",ad.getNew_key_table());
		
		//copy table
		session.update(Config.copyTableStructure,map);
		session.update(Config.copyTableData,map);
		session.commit();
		
		logger.info("add columns to new_key_table {} .",ad.getNew_key_table());
		map.clear();
		map.put("table",ad.getNew_key_table());
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
			map.put("key_table",ad.getNew_key_table());
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
	
	
	/*
	 * merge  columns 
	 * 
	 */
	
	public static  int  mergeColumn(Merge mixture)
	{
		String table=mixture.getTable();
		
		List<String> columns=mixture.getColumns();
		
		
		String new_table=mixture.getNew_table();
		
		String column_new = mixture.getColumn_new();
		
		AbstractTransform tran=mixture.getTransform();
	
		
		SqlSession session=SqlUtil.getSession();
		
		int number = 0;
		
		//copy table
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("table",table);
		create.put("new_table",new_table);
		session.update(Config.copyTableStructure,create);
		session.update(Config.copyTableData,create);
		session.commit();
		
		//add column
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table",new_table);
		map.put("column",column_new);
		session.update(Config.addColumn,map);
		session.commit();
		
		//get all data
		map.clear();
		map.put("table",new_table);
		map.put("columns",columns);
		List<Object> urls = session.selectList(Config.getAllData, map);

		
		//update
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("table",new_table);
		
		for(Object one:urls)
		{
			List<String> equals=Util.constructEqualsForMerge(columns, column_new,one,tran);
			int id=(Integer) ((HashMap<String,Object>)one).get("id");
			
			update.put("equals",equals);
			update.put("id",id);
			
			session.update(Config.updateColumn, update);
			number++;
			
		}
		session.commit();
		
		session.close();
		
		return number;
	}
	
	
	/*
	 * union tables
	 * 
	 */
	
	public static  int  unionTables(Union mixture)
	{
		String tableA=mixture.getTableA();
		String tableB=mixture.getTableB();
		
		String new_table=mixture.getNew_table();
		
		SqlSession session=SqlUtil.getSession();
		
		int number = 0;
		
		//copy tableA
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("table",tableA);
		create.put("new_table",tableA+"_tmp");
		session.update(Config.copyTableStructure,create);
		session.update(Config.copyTableData,create);
		/*
		// drop id
		create.clear();
		create.put("table",tableA+"_tmp");
		session.update(Config.dropID,create);
		session.commit();
		*/
		
		//copy tableB
		create.clear();
		create.put("table",tableB);
		create.put("new_table",tableB+"_tmp");
		session.update(Config.copyTableStructure,create);
		session.update(Config.copyTableData,create);
		/*
		// drop id
		create.clear();
		create.put("table",tableB+"_tmp");
		session.update(Config.dropID,create);
		session.commit();	
		*/
		
		//add table
		create.clear();
		create.put("tableA",tableA+"_tmp");
		create.put("tableB",tableB+"_tmp");
		session.update(Config.addTable,create);
		session.commit();
		
		// delete table
		create.clear();
		create.put("table",tableB+"_tmp");
		session.update(Config.deleteTable,create);
		session.commit();
		
		//rename table 
		create.clear();
		create.put("old_name",tableA+"_tmp");
		create.put("new_name",new_table);
		session.update(Config.renameTable,create);
		session.commit();
		//add primary id
		create.clear();
		create.put("table",new_table);
		session.update(Config.addID,create);
		session.update(Config.primaryID,create);
		session.commit();
		
		
		session.close();
		
		return number;
	}


	


}
