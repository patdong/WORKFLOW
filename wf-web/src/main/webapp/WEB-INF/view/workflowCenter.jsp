<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
function showPos(event,wfId) {
	  var el, x, y;
	  el = document.getElementById('workflow-name');	  
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
	  y = y-20
	  x = x+22
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  $("#wfId").val(wfId);
	  $("#wfName").val("");
}

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
</script>
<div class="container" >
	<div id="content">
		<div id="left" style="padding:0em;margin:0px">
		  	<div >
		  		<h1 class="mt-5">流程定义</h1>	  		   		    
		  	</div>
		</div>	
		<div id="right" style="padding:0em;text-align:right;">
	     	<div ><h1 class="mt-5" style="cursor:pointer;" onclick="location.href='/wf/newworkflow'"><img src="/img/wf_btn1.PNG"></img></h1></div>	     	
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
          	<c:forEach items="${wfList}" varStatus="i" var="workflow" >
          		<tr>
          			<td><a href="/wf/workflowdefination/${workflow.wfId}">#${workflow.wfId }</a></td>
          			<td><span class="btn supply-btn" style="background-color:#42a288" onclick="showPos(event,${workflow.wfId })" ></span>&nbsp;&nbsp;<span id="${workflow.wfId }">${workflow.wfName }</span></td>
          			<td><span class="btn supply-btn" style="background-color:#ce6634" onclick="location.href='/wf/workflowcenter'"></span>${workflow.wfTableId }</td>
          			<td>${workflow.status }</td>
          			<td>删除</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />	
</div>
<DIV id='workflow-name' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#workflow-name').hide();">×</div>
	<input id="wfName" name="wfName" class="form-control-one-line" style="width: 93%;" onkeypress="setWfName(event);">
	<input type="hidden" name="wfId" id="wfId"> 
</DIV>
