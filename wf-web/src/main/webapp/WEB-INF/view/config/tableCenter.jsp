<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
var gtbId = "";
$( function() {
	//初始化
	$("#confirm-dialog").dialog();
	$('#confirm-dialog').dialog('close');
})

function showPos(event,tbId,item) {
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
	  $("#tbId").val(tbId);
	  $("#tableName").val("");
}

//设置流程名称
function setTableName(event){
	 if (event.keyCode == 13) {
        var tableName = $("#tableName").val();
        var tbId = $("#tbId").val();
        if($.trim(tableName) != ""){
        	$.ajax({
      		  type: 'GET',
      		  url: "/tb/setTableName/"+tbId,
      		  data: {tableName:tableName},			  
      		  dataType: 'json',
      		  success: function(data){
      			  if(data){
      				$('#table-name').hide();	
      				$("#"+tbId).text(tableName);
      			  }			  
      		  },
      		  error: function(XMLHttpRequest, textStatus, errorThrown){
      			  console.warn(XMLHttpRequest.responseText);			  
      		  }
      	});
        }
    }
}

//启用流程
function startup(wfId){	        
 	$.ajax({
		  type: 'GET',
		  url: "/tb/startup/"+wfId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tablecenter/"+${page.curPage};		  
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
		  url: "/tb/shutdown/"+wfId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tablecenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}

//流程删除确认
function removeConfirm(tbId){
	gtbId = tbId;
	$('#confirm-dialog').dialog('open');
}
//删除
function remove(){
	$.ajax({
		  type: 'GET',
		  url: "/tb/remove/"+gtbId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tablecenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}

//表单拷贝
function copy(tbId){
	$.ajax({
		  type: 'GET',
		  url: "/tb/copy/"+tbId,			  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tablecenter/"+${page.curPage};		  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});
}

//设置流程绑定
function setBinging(){
      var wfId = $('input[name=wfId]:checked').val();
      var tbId = $("#tbId").val();      
     	$.ajax({
   		  type: 'GET',
   		  url: "/tb/setbinding/"+tbId,
   		  data: {wfId:wfId},			  
   		  dataType: 'json',
   		  success: function(data){
   			  if(data){
   				$('#workflow-binding').hide(); 
   				$("#tb-"+tbId).text("已绑定");
   				location.href="/tb/tablecenter/"+${page.curPage};		  
   			  }			  
   		  },
   		  error: function(XMLHttpRequest, textStatus, errorThrown){
   			  console.warn(XMLHttpRequest.responseText);			  
   		  }
   	});
}

//取消流程绑定
function removeBinding(tbId){	        
     	$.ajax({
   		  type: 'GET',
   		  url: "/tb/removebinding/"+tbId,			  
   		  dataType: 'json',
   		  success: function(data){
   			  if(data){   				
   				$("#tb-"+tbId).text("");
   				location.href="/tb/tablecenter/"+${page.curPage};		  
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
		<span class="mt-5" style="font-size: 2.5rem;">表单管理</span>	  		   		    
	  	<div style="padding:0em;float:right;padding-top:2%;">
	     	<span class="big-btn" onclick="location.href='/tb/newtablebrief'">新建</span>	
	     	<!-- <span class="big-btn" style="background-color:#5dca0a" onclick="location.href='/tb/newtablebrief'">导入</span> -->
	  	</div>			
	</div>
	<div class="line"></div>
	<div class="table-responsive" style="padding-top: 20px;">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>#序列</th>
              <th>表单名称</th>
              <th>模板</th>
              <th>流程绑定</th>
              <th>库表名称</th>
              <th>已入生产</th>
              <th>有效性</th>
              <th>表单操作</th>              
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${page.pageList}" varStatus="i" var="table" >
          		<tr>
          			<td><a href="/tb/tabledefination/${table.tbId}">#${table.tbId }</a></td>
          			<td><span class="small-btn" style="background-color:#42a288;" onclick="showPos(event,${table.tbId },'table-name')" >&nbsp;✒&nbsp;</span><span id="${table.tbId }">${table.tableName }</span></td>
          			<td>${table.template}</td>
          			<td>
          				<c:if test="${table.template eq '表'}">
          				<span class="small-btn" style="background-color:#16e81d;" onclick="showPos(event,${table.tbId },'workflow-binding')">&nbsp;✓&nbsp;</span>
          				<span id="tb-${table.tbId }">          					
	          				<c:if test="${!empty table.wfId }">
	          					${table.wf.wfName}
	          					<span class="small-btn" style="background-color:#ce6634;margin-left:3px;" onclick="removeBinding(${table.tbId })">&nbsp;✘&nbsp;</span>
	          				</c:if>
          				</span> 
          				</c:if>         				
          			</td>
          			<td>${table.name}</td>
          			<td>          				
          				<c:choose>
          					<c:when test="${table.productCount > 0 }">
          						已入生产
          					</c:when>
          				</c:choose>
          			</td>
          			<td>${table.status }</td>
          			<td>
          				<span class="small-btn" style="background-color:#ffc107;color:#28a745;font-weight:bold;cursor:pointer" title="拷贝" onclick="copy(${table.tbId })">&nbsp;✍&nbsp;</span>
          				<c:choose >	   
          					<c:when test="${empty table.tableName || table.productCount == 0 }">
	          					<span class="small-btn" style="background-color:#343a40;color:#ffc107;font-weight:bold;cursor:pointer" title="删除" onclick="removeConfirm(${table.tbId })">&nbsp;↯&nbsp;</span>
	          				</c:when>       				
	          				<c:when test="${table.status eq '有效' }">
	          					<span class="small-btn" style="background-color:#6c757d;color:#343a40;font-weight:bold;cursor:pointer" title="停用" onclick="shutdown(${table.tbId })">&nbsp;⇊&nbsp;</span>
	          				</c:when>
	          				<c:when test="${table.status eq '无效' }">
	          					<span class="small-btn" style="background-color:#0dbf36;color:#343a40;font-weight:bold;cursor:pointer" title="启用" onclick="startup(${table.tbId })">&nbsp;⇈&nbsp;</span>
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
<!-- 表单名称设置 -->
<DIV id='table-name' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#table-name').hide();">×</div>
	<input id="tableName" name="tableName" class="form-control-one-line" style="width: 93%;" onkeypress="setTableName(event);">
	<input type="hidden" name="tbId" id="tbId"> 
</DIV>
<!-- 删除弹出窗口 -->
<div id="confirm-dialog"  title="确认窗口" >
  <p>表单一旦删除将无法复原！</p>
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

<!-- 工作流表单绑定 -->
<div id="workflow-binding" class="popup-width" style="display:none;width:20%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>流程绑定设置</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#workflow-binding').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px; height: 200px;overflow-y: auto;">		
		<c:forEach items="${wfLst }" var="workflow">
			<p style="margin-bottom: 0rem;">
				<input type="radio" id="wfId" name="wfId" value="${workflow.wfId}" class="form-element" onclick="setBinging();">${workflow.wfName}
			</p>
		</c:forEach>
		<hr></hr>        
	</div>    
</div> 
