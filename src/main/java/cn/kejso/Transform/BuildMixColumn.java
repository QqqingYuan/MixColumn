package cn.kejso.Transform;

import java.io.File;

import cn.kejso.Mix.Adjunction;
import cn.kejso.Mix.Merge;
import cn.kejso.Mix.MixType;
import cn.kejso.Mix.Transform;
import cn.kejso.Sql.Config;
import cn.kejso.Tool.Util;

public class BuildMixColumn {
	
	
	public static void main(String[] args) {
		
		
		if(args.length!=1 && args.length!=2)
		{
			System.out.println("BuildMixColumn read MixColumn's config and transform columns which jdbc-config representation .");
			System.out.println("Usage: java -jar BuildMixColumn.jar  jdbc-config  [transform-jar] .");
		}
		
		
		String jdbcconfig=args[0];
		String jarfile=args[1];

		String jarpath=Config.prefix_jarfile+new File(jarfile).getAbsolutePath();

		Config.setJdbc_config(jdbcconfig);
		
		//mix
		int mixtype = Util.getMixType(jdbcconfig);
		
		switch(mixtype)
		{
			case MixType.transform:
				//transform  column
				Transform mixture=Util.getTransform(jdbcconfig,jarpath);
				MixFactory.transformColumn(mixture);
				break;
				
			case MixType.adjunction:
				// add columns
				Adjunction add=Util.getAdjunction(jdbcconfig);
				MixFactory.AdjunctionTables(add);
				break;
			case MixType.merge:
				// merge columns to create new column
				Merge merge=Util.getMerge(jdbcconfig, jarpath);
				MixFactory.mergeColumn(merge);
				break;
		}
		
			
		

		
	}
}
