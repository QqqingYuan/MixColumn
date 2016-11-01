package cn.kejso;

import java.util.List;

public interface AbstractTransform {
	
	//String transform(String column_content);
	
	String transform(List<String> columns);
}
