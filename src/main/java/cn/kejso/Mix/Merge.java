package cn.kejso.Mix;

import java.util.List;

import cn.kejso.AbstractTransform;

public class Merge {
	
	private String table;
	
	private String new_table;
	
	private List<String> columns;
	
	private String column_new;
	
	private AbstractTransform  transform;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getNew_table() {
		return new_table;
	}

	public void setNew_table(String new_table) {
		this.new_table = new_table;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getColumn_new() {
		return column_new;
	}

	public void setColumn_new(String column_new) {
		this.column_new = column_new;
	}

	public AbstractTransform getTransform() {
		return transform;
	}

	public void setTransform(AbstractTransform transform) {
		this.transform = transform;
	}
	
}
