package org.github.oem.config;

public class PropertyObject {
	
	private String name;
	private String type;
	/**
	 * ��excel �е�������,Ĭ��-1
	 */
	private int colIndex = -1;
	private String value;
	
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int dataIndex) {
		this.colIndex = dataIndex;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
