package cn.kejso.Transfrom;

import org.apache.ibatis.session.SqlSession;

import cn.kejso.Sql.SqlUtil;

public class test {
	
	private static SqlSession session;
	
	public static void main(String[] args) {

		session = SqlUtil.getSession();
	}
}
