<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
var gwfId = "";
$( function() {
	//初始化
	$("#confirm-dialog").dialog();
	$('#confirm-dialog').dialog('close');
})
function showPos(event,wfId,item) {
	  var el, x, y;
	  el = document.getElementById(item);	  
	  if (window.event) {
		  x = window.event.clientX + document.documentElement.scrollLeft + document.body.scrollLeft;
		  y = window.event.clientY + document.documentElement.scrollTop + document.body.scrollTop;
	  }
	  else {
		  x = event.clientX + window.scrollX;
		  y = event.clientY + window.scrollY;
	  }
	  x -= 2; 
	  y -= 2;
	  if(y>400) y = y-300
	  x = x+22
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  $("#wfId").val(wfId);
	  $("#wfName").val("");
}

//设置流程名称
function setWfName(event){
	 if (event.keyCode == 13) {
        var wfName = $("#wfName").val();
        var wfId = $("#wfId").val();
        if($.trim(wfName) != ""){
        	$.ajax({
      		  type: 'GET',
      		  url: "/wf/setWfName/"+wfId,
      		  data: {wfName:wfName},			  
      		  dataType: 'json',
      		  success: function(data){
      			  if(data){
      				$('#workflow-name').hide();	
      				$("#"+wfId).text(wfName);
      			  }			  
      		  },
      		  error: function(XMLHttpRequest, textStatus, errorThrown){
      			  console.warn(XMLHttpRequest.responseText);			  
      		  }
      	});
        }
    }
}

//设置表单绑定
function setBinging(){	 
      var tbId = $('input[name=tbId]:checked').val();
      var wfId = $("#wfId").val();      
     	$.ajax({
   		  type: 'GET',
   		  url: "/wf/setbinding/"+wfId,
   		  data: {tbId:tbId},			  
   		  dataType: 'json',
   		  success: function(data){
   			  if(data){
   				$('#workflow-binding').hide(); 
   				$("#tb-"+wfId).text("已绑定");
   				location.href="/wf/workflowcenter/"+${page.curPage};
   			  }			  
   		  },
   		  error: function(XMLHttpRequest, textStatus, errorThrown){
   			  console.warn(XMLHttpRequest.responseText);			  
   		  }
   	});
}

//取消表单绑定
function removeBinding(wfId){	        
     	$.ajax({
   		  type: 'GET',
   		  url: "/wf/removebinding/"+wfId,			  
   		  dataType: 'json',
   		  success: function(data){
   			  if(data){   				
   				$("#tb-"+wfId).text("");
   				location.href="/wf/workflowcenter/"+${page.curPage};
   			  }			  
   		  },
   		  error: function(XMLHttpRequest, textStatus, errorThrown){
   			  console.warn(XMLHttpRequest.responseText);			  
   		  }
   	});
}

//启用流程
function startup(wfId){	        
 	$.ajax({
		  type: 'GET',
		  url: "/wf/startup/"+wfId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowcenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}

//停用流程
function shutdown(wfId){	        
 	$.ajax({
		  type: 'GET',
		  url: "/wf/shutdown/"+wfId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowcenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}

//流程删除确认
function removeConfirm(wfId){
	gwfId = wfId;
	$('#confirm-dialog').dialog('open');
}

function remove(){
	$.ajax({
		  type: 'GET',
		  url: "/wf/remove/"+gwfId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowcenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}
</script>
<div class="container" >
	<div id="content">
		<span class="mt-5" style="font-size: 2.5rem;">流程定义</span>	  		   		    
	  	<div style="padding:0em;float:right;padding-top:2%;">
	     	<span class="big-btn" onclick="location.href='/wf/newworkflow'">新建</span>		     	
	  	</div>			
	</div>
	<div class="line"></div>
	<div class="table-responsive" style="padding-top: 20px;">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>#序列</th>
              <th>流程名称</th>
              <th>表单绑定</th>
              <th>有效性</th>
              <th>流程操作</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${page.pageList}" varStatus="i" var="workflow" >
          		<tr>
          			<td><a href="/wf/workflowdefination/${workflow.wfId}">#${workflow.wfId }</a></td>
          			<td><span class="small-btn" style="background-color:#42a288;" onclick="showPos(event,${workflow.wfId },'workflow-name')" >&nbsp;✒&nbsp;</span><span id="${workflow.wfId }">${workflow.wfName }</span></td>
          			<td>          			
          				<c:forEach items="${workflow.tbLst}" varStatus="idx" step="1" var="tb" >          					
          					<c:set var="tn" value="${tb.tableName} ${tn }"/>
          					<c:set var="count" value="${idx.index}"/>
          				</c:forEach>          				
          				<c:if test="${count > 0 }">
          				<span style="color:red;font-weight:bold;cursor:pointer" title="${tn}">警告：被多个表单使用!</span>
          				</c:if>
          				<c:if test="${count == 0 }">
          					${tn}
          				</c:if>
          				<c:set var="count" value="" />
          				<c:set var="tn" value="" />
          			</td>
          			<td>${workflow.status }</td>
          			<td>
          				<c:choose >          				
	          				<c:when test="${empty workflow.tbId }">
	          					<span class="small-btn" style="background-color:#343a40;color:#ffc107;font-weight:bold;cursor:pointer" title="删除" onclick="removeConfirm(${workflow.wfId })">&nbsp;↯&nbsp;</span>
	          				</c:when>
	          				<c:when test="${workflow.status eq '有效' }">
	          					<span class="small-btn" style="background-color:#6c757d;color:#343a40;font-weight:bold;cursor:pointer" title="停用" onclick="shutdown(${workflow.wfId })">&nbsp;⇊&nbsp;</span>
	          				</c:when>
	          				<c:when test="${workflow.status eq '无效' }">
	          					<span class="small-btn" style="background-color:#0dbf36;color:#343a40;font-weight:bold;cursor:pointer" title="启用" onclick="startup(${workflow.wfId })">&nbsp;⇈&nbsp;</span>
	          				</c:when>
          				</c:choose>
          			</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />	
</div>
<!-- 工作流名称设置 -->
<DIV id='workflow-name' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#workflow-name').hide();">×</div>
	<input id="wfName" name="wfName" class="form-control-one-line" style="width: 93%;" onkeypress="setWfName(event);">
	<input type="hidden" name="wfId" id="wfId"> 
</DIV>
<!-- 工作流表单绑定 -->
<div id="workflow-binding" class="popup-width" style="display:none;width:20%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>表单绑定设置</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#workflow-binding').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px; height: 200px;overflow-y: auto;">		
		<c:forEach items="${tbLst }" var="tbrief">
			<p style="margin-bottom: 0rem;">
				<input type="radio" id="tbId" name="tbId" value="${tbrief.tbId}" class="form-element" onclick="setBinging();">${tbrief.tableName}
			</p>
		</c:forEach>
		<hr></hr>        
	</div>    
</div> 

<!-- 删除弹出窗口 -->
<div id="confirm-dialog"  title="确认窗口" >
  <p>流程一旦删除将无法复原！</p>
  <br>
  <div style="display:none" id="delegationDiv">  	
  </div>
  <br>
  <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void(0);"  onclick="remove();">
		        <span aria-hidden="true">确认</span>		        
		      </a>
	      </div>
	    </li>
	    <li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void(0);" onclick="$('#confirm-dialog').dialog('close');">
		        <span aria-hidden="true">取消</span>		        
		      </a>
	      </div>
	    </li>
	</ul>
  </nav>    
</div>
