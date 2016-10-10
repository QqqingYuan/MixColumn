package cn.kejso.Transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kejso.AbstractTransform;
import cn.kejso.Mix.Mixture;
import cn.kejso.Sql.Config;
import cn.kejso.Sql.SqlUtil;

public class TransformFactory {
	
	private static Logger logger = LoggerFactory.getLogger(TransformFactory.class);
	
	/*
	 * transform single column 
	 * 
	 */
	public static  int  transformSingleColumn(Mixture mixture)
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
}
