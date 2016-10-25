package cn.kejso.Transform;

import java.io.File;

import cn.kejso.Mix.AdjColumn;
import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.Transform;
import cn.kejso.Sql.Config;
import cn.kejso.Tool.Util;

public class BuildTransform {
	
	
	public static void main(String[] args) {
		
		
		if(args.length!=2)
		{
			System.out.println("BuildTransform read MixColumn's config and transform columns which jdbc-config representation .");
			System.out.println("Usage: java -jar BuildTransform.jar  jdbc-config transform-jar.");
		}
		
		
		String jdbcconfig=args[0];
		String jarfile=args[1];

		String jarpath=Config.prefix_jarfile+new File(jarfile).getAbsolutePath();

		Config.setJdbc_config(jdbcconfig);
		Transform mixture=Util.getTransform(jdbcconfig,jarpath);
		
		//transform single column
		MixFactory.transformSingleColumn(mixture);


		////////combine two column
//		AdjColumn adjcolumn=Util.getAdjColumn(jdbcconfig,jarpath);
//
//		//transform single column
//		MixFactory.combineColumns(adjcolumn);

		
	}
}
