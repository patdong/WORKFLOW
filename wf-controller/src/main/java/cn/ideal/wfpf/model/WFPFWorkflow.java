package cn.ideal.wfpf.model;

/**
 * 工作流pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class WFPFWorkflow implements Serializable {
	private static final long serialVersionUID = -2546295384449824310L;
	private Long wfId;
	private String wfName;
	private Long tbId;
	private String status;
	private Date createdDate;	
	
	private List<TableBrief> tbLst;
	
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public List<TableBrief> getTbLst() {
		return tbLst;
	}
	public void setTbLst(List<TableBrief> tbLst) {
		this.tbLst = tbLst;
	}
	
	
}
