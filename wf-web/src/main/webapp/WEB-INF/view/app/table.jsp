<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script> 
    $(function(){
    	//根据不同的表单风格做处理
    	if("${bizId}" == "") $("#includedContent").load("/app/showcontent/${wf.wfId}/div");
    	else $("#includedContent").load("/app/showcontent/${wf.wfId }/${bizId}/div"); 
    });
    
    //表单保存提交
    function doStorage(){
    	if("${bizId}" == "") $("#myForm").attr("action","/app/save/${wf.wfId}");
    	else $("#myForm").attr("action","/app/save/${wf.wfId}/${bizId}");
    	$("#myForm").submit();
    }
    
    //流程办理完毕提交
    function passWorkflow(){
    	$("#myForm").attr("action","/app/passworkflow/${wf.wfId}/${bizId}");
    	$("#myForm").submit();
    }
    
  	//展示流程信息
    function showWorkflow(){
    	
    }
</script> 

<form id="myForm" method="post" action="">
<input type="hidden" name="wfId" value="${wf.wfId }" />
<input type="hidden" name="bizId" value="${bizId}" />
<input type="hidden" name="nodeName" value="${nodename}" />		
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">✍</h1>
  <div class="btn-toolbar mb-2 mb-md-0">
    <div class="btn-group mr-2">
      <button class="btn btn-sm btn-outline-secondary" type="button" onclick="doStorage();">保存</button> 
      <button class="btn btn-sm btn-outline-secondary" type="reset">重置</button>     
    </div>    
    <c:if test="${!empty bizId}">
    <button class="btn btn-sm btn-outline-secondary" type="button" onclick="passWorkflow();">${nodename}完毕</button>
    </c:if>
    <button class="btn btn-sm btn-outline-secondary" type="button" onclick="showWorkflow();">流程查看</button>
  </div>
</div> 	
<div id="includedContent"></div>	
</form>
  
