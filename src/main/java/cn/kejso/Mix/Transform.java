package cn.kejso.Mix;

import java.util.List;

import cn.kejso.AbstractTransform;

// transform column in single table.
public class Transform {
	
	private String table;
	private String new_table;
	// multi-column
	private Field originalFields;
	private Field mappingFields;
	//transform
	private AbstractTransform  transform;
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Field getOriginalFields() {
		return originalFields;
	}
	public void setOriginalFields(Field originalFields) {
		this.originalFields = originalFields;
	}
	public Field getMappingFields() {
		return mappingFields;
	}
	public void setMappingFields(Field mappingFields) {
		this.mappingFields = mappingFields;
	}
	public String getNew_table() {
		return new_table;
	}
	public void setNew_table(String new_table) {
		this.new_table = new_table;
	}
	public AbstractTransform getTransform() {
		return transform;
	}
	public void setTransform(AbstractTransform transform) {
		this.transform = transform;
	}
	
	

}
