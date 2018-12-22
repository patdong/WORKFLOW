package cn.ideal.wf.model;
/**
 * 页面翻页器
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Page {
	public final static Long pageSize = 10l; 
	private Long curPage;
	private Long prePage;
	private Long nextPage;
	private Long[] bodyPages;
	
	public Page(Long recordNumber, Long pageNumber){
		if(pageSize.intValue() == 0l) return;
		if(pageNumber <= 0) pageNumber = 1l;
		curPage = pageNumber;
		Long size = 0l;
		//取整
		size = recordNumber / pageSize;
		//取余数
		if(recordNumber % pageSize <= pageSize) size++;
		
        if(size == 0) size = 1l;
        bodyPages = new Long[3];
        if(recordNumber > pageSize) {	               	
	        bodyPages[0] = pageNumber;
	        this.prePage = ((pageNumber - 1) == 0) ? null : pageNumber - 1;
        }
        if(pageNumber+1 <= size) {
        	bodyPages[1] = pageNumber+1;
        	nextPage = pageNumber+1;
        }
        if(pageNumber+2 <= size) bodyPages[2] = pageNumber+2;        
	}
	public Long getPrePage() {
		return prePage;
	}
	public void setPrePage(Long prePage) {
		this.prePage = prePage;
	}
	public Long getNextPage() {
		return nextPage;
	}
	public void setNextPage(Long nextPage) {
		this.nextPage = nextPage;
	}
	public Long[] getBodyPages() {
		return bodyPages;
	}
	public void setBodyPages(Long[] bodyPages) {
		this.bodyPages = bodyPages;
	}
	public Long getCurPage() {
		return curPage;
	}
	public void setCurPage(Long curPage) {
		this.curPage = curPage;
	}
	
	

}
