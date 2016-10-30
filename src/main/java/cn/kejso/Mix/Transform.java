package cn.kejso.Mix;

import java.util.List;

import cn.kejso.AbstractTransform;

// transform column in single table.
public class Transform {
	
	private String table;
	private String new_table;
	// multi-column
	private List<Field> originalFields;
	//transform
	private List<AbstractTransform>  transforms;
	
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
	public List<AbstractTransform> getTransforms() {
		return transforms;
	}
	public void setTransforms(List<AbstractTransform> transforms) {
		this.transforms = transforms;
	}
	public List<Field> getOriginalFields() {
		return originalFields;
	}
	public void setOriginalFields(List<Field> originalFields) {
		this.originalFields = originalFields;
	}
	
	
	

}
