<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
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
   			  }			  
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
          			<td><span class="small-btn" style="background-color:#16e81d;" onclick="showPos(event,${workflow.wfId },'workflow-binding')">&nbsp;✓&nbsp;</span>
          				<span id="tb-${workflow.wfId }">
	          				<c:if test="${!empty workflow.tableId }">
	          				${workflow.wftableBrief.tableName }
	          				<span class="small-btn" style="background-color:#ce6634;margin-left:3px;" onclick="removeBinding(${workflow.wfId })">&nbsp;✘&nbsp;</span>
	          				</c:if>
          				</span>
          			</td>
          			<td>${workflow.status }</td>
          			<td>删除</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="../pagenation.jsp"  />	
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
