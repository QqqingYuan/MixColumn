package cn.kejso.Mix;

import java.util.List;

import cn.kejso.AbstractTransform;

// two table . contain Transform
public class Adjunction {
	
	private String key_table;
	private String side_table;
	private String key_identity_column;
	private String side_identity_column;
	
	private List<String> side_add_columns;
	private String new_key_table;
	
	
	
	public String getKey_table() {
		return key_table;
	}
	public void setKey_table(String key_table) {
		this.key_table = key_table;
	}
	public String getSide_table() {
		return side_table;
	}
	public void setSide_table(String side_table) {
		this.side_table = side_table;
	}
	public String getKey_identity_column() {
		return key_identity_column;
	}
	public void setKey_identity_column(String key_identity_column) {
		this.key_identity_column = key_identity_column;
	}
	public String getSide_identity_column() {
		return side_identity_column;
	}
	public void setSide_identity_column(String side_identity_column) {
		this.side_identity_column = side_identity_column;
	}
	public List<String> getSide_add_columns() {
		return side_add_columns;
	}
	public void setSide_add_columns(List<String> side_add_columns) {
		this.side_add_columns = side_add_columns;
	}
	public String getNew_key_table() {
		return new_key_table;
	}
	public void setNew_key_table(String new_key_table) {
		this.new_key_table = new_key_table;
	}
	
	
}
