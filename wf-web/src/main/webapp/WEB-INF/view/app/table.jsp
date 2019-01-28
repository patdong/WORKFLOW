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
    	<c:if test="${!empty nodename}">
    		<button class="btn btn-sm btn-outline-secondary" type="button" onclick="passWorkflow();">${nodename}完毕</button>
    	</c:if>
    </c:if>
    <button class="btn btn-sm btn-outline-secondary" type="button" onclick="$('#workflow').show();">流程查看</button>
  </div>
</div> 	
<div id="includedContent"></div>	
</form>
<!-- 流程跟踪 -->
<div id="workflow" class="draw" style="top: 23%;z-index: 999;background:white;display:none;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >流程跟踪 </div>	               
        <div style="position: absolute;top: 1px;right: 15px;">
        	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#workflow').hide();">×</span>
        </div>	      	     
   	</header>
   	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>   	 	
	<table style=" margin:1% 0 0 1%; border:px solid">
		<c:forEach items="${nodetree}" varStatus="i" var="nodes" >
			<tr style="height:75px">
			<c:forEach items="${nodes}" varStatus="j" var="node" >					
				<c:if test="${node.style eq 'user' }">
					<td style="height:40px"><img src="/img/wf_btn4.PNG" style="vertical-align: middle;"></td>
				</c:if>
				<c:if test="${node.style eq 'pointer' }">
					<td><img src="/img/wf_btn6.PNG" style="vertical-align: middle;"></td>
				</c:if>
				<c:if test="${node.style eq 'lpointer' }">
					<td><img src="/img/wf_btn8.PNG" style="vertical-align: middle;"></td>
				</c:if>
				<c:if test="${node.style eq 'rpointer' }">
					<td><img src="/img/wf_btn9.PNG" style="vertical-align: middle;"></td>
				</c:if>
				<c:if test="${node.style eq 'node' }">					
					<td>
						<c:choose>
							<c:when test="${ node.status eq '冻结' }">
								<div class="circle-dotted-text" >
									<font style="font-size:15px">${node.nodename }</font>
								</div>
							</c:when>
							<c:when test="${ node.status eq '有效' }">								
								<c:if test="${ node.passed eq 'passed' }">
									<div class="circle-green-text" >
										<font style="font-size:15px">${node.nodename }</font>
									</div>
								</c:if>
								<c:if test="${empty node.passed}">
									<div class="circle-text" >
										<font style="font-size:15px">${node.nodename }</font>
									</div>
								</c:if>
							</c:when>
						</c:choose>						
					</td>	
				</c:if>
				<c:if test="${node.style eq '^' }">
					<td></td>
				</c:if>			    	
		    </c:forEach>
		    </tr>
		</c:forEach>						
	</table>
</div>
  
