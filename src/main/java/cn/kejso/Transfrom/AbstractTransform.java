package cn.kejso.Transfrom;

/*
 * Transform for mysql column .
 * 
 * column type is string.
 * 
 */

public interface AbstractTransform {
	
	int transform(String table,String column);
	
}