package cn.ideal.wfpf.model;

/**
 * 系统参数配置
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class SysConfiguration {
	
	private String name;
	private String value;
	private String note;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
