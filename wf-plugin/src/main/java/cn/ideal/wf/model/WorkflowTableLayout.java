package cn.ideal.wf.model;


/**
 * 表单布局pojo
 * @author 郭佟燕
 * @vrsion 2.0
 *
 */
public class WorkflowTableLayout {

	private Long tbId;
	private String scope;
	private Long cols;
	private Long stbId;   //子表编号
	
	
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Long getCols() {
		return cols;
	}
	public void setCols(Long cols) {
		this.cols = cols;
	}
	public Long getStbId() {
		return stbId;
	}
	public void setStbId(Long stbId) {
		this.stbId = stbId;
	}
	
	
}
