<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
	
    //表单保存提交
    function doStorage(){
    	$("#myForm").valid();
    	//必输项的默认提示信息：
        $.validator.messages.required = "此项为必输项！";
    	
    	if("${model.bizId}" == "") $("#myForm").attr("action","/app/save/${model.wftb.tbId}");
    	else $("#myForm").attr("action","/app/save/${model.wftb.tbId}/${model.bizId}");    	
    	$("#myForm").submit();
    }
    //表单重置
    function doReset(){
    	$("#myForm")[0].reset();
    }
    
    //流程办理完毕提交
    function doAction(){
    	var nextNodeSize = "${model.nextNodeSize}";
    	if(nextNodeSize <= 1){
	    	$("#myForm").attr("action","/app/doaction/${model.wftb.tbId}/${model.bizId}");
	    	$("#myForm").submit();
    	}else{
    		//弹出节点选择框
    		$("#nextNodes-dialog").show();
    	}
    }
    
  	//展示流程信息
    function showWorkflow(){
    	
    }
  	
  	//指定流程节点提交
    function doFixedAction(nodeId){
    	$("#myForm").attr("action","/app/doaction/${model.wftb.tbId}/${model.bizId}/"+nodeId);
    	$("#myForm").submit();    	
    }
  
  	//流程按钮操作
  	function doButton(buttonName){
  		$("#myForm").attr("action","/app/dobutton/${model.wftb.tbId}/${model.bizId}/"+buttonName);
    	$("#myForm").submit();
  	}
</script> 	
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">
  	<label onclick="location.href='/app/list/${model.wftb.tbId}/1'" style="cursor:pointer;color:#dc3545;">⇦</label>
  	✍  	
  </h1>
  <div class="btn-toolbar mb-2 mb-md-0">
  	<c:forEach items="${model.buttons}" varStatus="i" var="button" >
  		<button class="btn btn-sm btn-outline-secondary" type="button" onclick="doButton('${button.actionCodeName}');">${button.actionName}</button> 
  	</c:forEach>
  	&nbsp; 
  	<c:if test="${!empty model.nodeName}"> 	
	    <div class="btn-group mr-2">
	      <button class="btn btn-sm btn-outline-secondary" type="button" onclick="doStorage();">保存</button> 
	      <button class="btn btn-sm btn-outline-secondary" type="button" onclick="doReset()">重置</button>     
	    </div>
    </c:if>
    <c:if test="${!empty model.bizId}">
    	<c:if test="${!empty model.nodeName}">
    		<button class="btn btn-sm btn-outline-secondary" type="button" onclick="doAction();">${model.nodeName}完毕</button>
    	</c:if>
    </c:if>
    <button class="btn btn-sm btn-outline-secondary" type="button" onclick="$('#workflow').show();">流程查看</button>
  </div>
</div> 	
	
<form id="myForm" method="post" action=""> 
<div id="includedContent" style="padding-bottom:20px;">${model.table }</div>
</form>
<!-- 流程跟踪 -->
<div id="workflow" class="draw" style="height:40%;top: 17%;z-index: 999;background:white;display:none;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >流程跟踪 </div>	               
        <div style="position: absolute;top: 1px;right: 15px;">
        	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#workflow').hide();">×</span>
        </div>	      	     
   	</header>
   	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>  
	${model.flowChat}
</div>
<!-- 续办节点选择窗口 -->
<div id="nextNodes-dialog" style="box-shadow: 0px 2px 2px #2ef90a;position: absolute;top:12%;z-index: 999;background:white;right:20px;display:none;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 1px 8px 0px;font-size:12px" >
	 	续办节点&nbsp;
	 	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;padding: 0.15em .2em;" onclick="$('#nextNodes-dialog').hide();">×</span>
	 </div>       	      	    
   	</header>
   	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>   	 	
	<div>		
		<c:forEach items="${model.nextNodes}" varStatus="j" var="node" >		
			<label style="padding: 1px 8px 0px"><button class="btn btn-sm btn-outline-secondary" type="button" onclick="doFixedAction(${node.nodeId});">${node.nodeName}</button></label><br>	
		</c:forEach>
	</div>
</div>
  
