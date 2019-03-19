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
          			<td>          				
          				<c:forEach items="${table.wf}" varStatus="idx" step="1" var="wf" >
          					[${wf.wfName}] &nbsp;
          					<c:set var="count" value="${idx.index}"/>
          				</c:forEach>          				
          				<c:if test="${count > 0 }">
          				<span style="color:red;font-weight:bold;cursor:pointer" title="多个流程关联！">!</span>
          				</c:if>
          				<c:set var="count" value="" />
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
