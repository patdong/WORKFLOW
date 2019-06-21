package cn.ideal.wfpf.model;


/**
 * 业务模板pojo
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class TableBizTemplate {

	private Long tempId;
	private String templateName;      //业务模板名称
	private String note;
	
	public Long getTempId() {
		return tempId;
	}
	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
