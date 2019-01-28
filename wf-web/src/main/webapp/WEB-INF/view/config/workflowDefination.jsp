<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>

<script>
  $( function() {
	//初始化
    $("#user-div").draggable();
    $("#org-div").draggable();
    //$("#resizable").resizable();
    $("#node-div").draggable(); 
    $("#node-nodes-div").draggable();
    $("#confirm-dialog" ).dialog();
    $('#confirm-dialog').dialog('close');
    
    //Form必输项控制
    // $("#myForm").valid();
    
    //必输项的默认提示信息：
    //$.validator.messages.required = "此项为必输项！";
    
    //下拉框选择操作 - 角色选择
    $('#role-ul li').on('click', function(){
    	$('#role').text($(this).text()); 
    	$('#roleName').text($(this).text());    	
    	$('#roleId').val($(this).attr("value"));    	    	
    });
    $('#action-ul li').on('click', function(){    	
    	$('#actionName').text($(this).text());
    	$('#action').val($(this).text()); 
    });
    $('#nType-ul li').on('click', function(){    	
    	$('#nTypeName').text($(this).text());
    	$('#nType').val($(this).text()); 
    });
       
  });
  
  

  //选择用户
  function selectUsers(){
	  var userName = $("#userName").val();
	  $.ajax({
		  type: 'GET',
		  url: "/wf/selectUsers",
		  data: {userName:userName},			  
		  dataType: 'json',
		  success: function(data){
			  var li;
			  $.each(data,function (index, user) {
			      li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\">"
			        + "<div>"
			        + "<h6 class=\"my-0\"><input type=\"checkbox\" name=\"userChecked\" value=\""+user.userId+"\">&nbsp;"+user.userName+"</h6>"
			        + "</div>"
			        + "<span class=\"text-muted\">"+user.currentOrgName+"</span>"
			        + "<span class=\"text-muted\">"+user.orgName+"</span>"
			        + "</li>";
				        
				  $("#user-lst").append(li);				 
			  });
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //确认选择的用户
  function selectedUsers(){
	  var checkedUserIds = "";
	  var checkedUserNames = "";	  
	  var idx = 0;	 
	  
	  //先删除div下的元素，然后再添加
	  var el = document.getElementById('user-hidden');
	  while( el.hasChildNodes() ){
	      el.removeChild(el.lastChild);
	  }
	  $('input:checkbox[name=userChecked]:checked').each(function(k){
		  checkedUserNames += $(this).closest("h6").text()+",";			  
		  $("#user-hidden").append("<input type='hidden' name='users["+idx+"].userId' value='"+$(this).val()+"'>");		
		  $("#user-hidden").append("<input type='hidden' name='users["+idx+++"].userName' value='"+$(this).closest("h6").text()+"'>");		 		  
  	  })
	  if(checkedUserNames.length > 0){		  
		  checkedUserNames = checkedUserNames.substring(0,checkedUserNames.length-1);
	  }	  
	  $("#user-div").hide();
	  $("#usersName").val(checkedUserNames);
  }
  
  
  //选择单位
  function selectOrgs(){
	  var orgName = $("#orgName").val();
	  $.ajax({
		  type: 'GET',
		  url: "/wf/selectOrgs",
		  data: {orgName:orgName},			  
		  dataType: 'json',
		  success: function(data){
			  var li;
			  $.each(data,function (index, org) {
			      li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\">"
			        + "<div>"
			        + "<h6 class=\"my-0\"><input type=\"radio\" name=\"orgChecked\" value=\""+org.orgId+"\">&nbsp;"+org.orgName+"</h6>"
			        + "</div>"
			        + "<span class=\"text-muted\">"+org.currentOrgName+"</span>"
			        + "</li>";
				        
				  $("#org-lst").append(li);				 
			  });
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //确认选择的组织
  function selectedOrgs(){
	  var checkedOrgIds = "";
	  var checkedOrgNames = "";
	  $('input:radio[name=orgChecked]:checked').each(function(k){
		  checkedOrgIds += $(this).val()+",";  
		  checkedOrgNames += $(this).closest("h6").text()+",";		  
  	  })
	  if(checkedOrgIds.length > 0){
		  checkedOrgIds = checkedOrgIds.substring(0,checkedOrgIds.length-1);
		  checkedOrgNames = checkedOrgNames.substring(0,checkedOrgNames.length-1);
	  }	  
	  $("#org-div").hide();
	  $("#org").val(checkedOrgNames);
	  $("#orgId").val(checkedOrgIds);
  }
  //设置选中节点的编号，初始值为0 -- 尚未构建任何节点。
  var clickedNodeId = 0;
  //弹出操作菜单
  function showPos(event,cNodeId,cNodeName,cNodeStatus) {
	  var el, x, y;
	  if(cNodeId > 0) el = document.getElementById('PopUp-1');
	  else el = document.getElementById('PopUp');
	  $("#frozen").show();
	  $("#unfrozen").show();
	  //根据节点状态处理节点操作功能
	  if(cNodeStatus == '冻结'){
		  $("#frozen").hide();
	  }else if(cNodeStatus == '有效'){
		  $("#unfrozen").hide();
	  }else {
		  $("#frozen").hide();
		  $("#unfrozen").hide();
	  }
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
	  y = y+15
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  
	  clickedNodeId = cNodeId;
	  
	  $("#preNodeId").val(cNodeId);
	  $("#node-nodes-nodename").text(cNodeName);
	  //document.getElementById('PopUp').innerHTML = text;
  }

  //点击某一结点显示节点信息
  function showNode(){
	  var nodes = ${nodes};
	  $.each(nodes,function(key,node){
		  if(node.nodeId == clickedNodeId){
			  $("#wfId").val(node.wfId);
			  $("#nodeId").val(node.nodeId);
			  $("#nodename").val(node.nodename);
			  $('#nTypeName').text(node.nType);
		      $('#nType').val(node.nType);
		      $('#actionName').text(node.action);
		      $('#action').val(node.action); 
		      $("input[name=status][value="  + node.status + "]").prop('checked', true);
		      $('#timeLimit').val(node.timeLimit);
		      		
		      if(node.role != null){
			      $('#role').text(node.role.roleName); 
			      $('#roleName').text(node.role.roleName);    	
			      $('#roleId').val(node.role.roleId);
		      }
		      
		      if(node.users != null){
		    	  //先删除div下的元素，然后再添加
	    		  var el = document.getElementById('user-hidden');
	    		  while( el.hasChildNodes() ){
	    		      el.removeChild(el.lastChild);
	    		  }
	    		  var checkedUserNames = "";
		    	  $.each(node.users,function(key,user){	
		    		  checkedUserNames += user.userName+",";
	    			  $("#user-hidden").append("<input type='hidden' name='users["+key+"].userId' value='"+user.userId+"'>");		
	    			  $("#user-hidden").append("<input type='hidden' name='users["+key+"].userName' value='"+user.userName+"'>");		 		  	    	  	  		    		   		    		  
		    	  });
		    	  if(checkedUserNames.length > 0){		  
	    			  checkedUserNames = checkedUserNames.substring(0,checkedUserNames.length-1);
	    		  }	
		    	  $("#usersName").val(checkedUserNames);
		      }

		      if(node.org != null){
		    	  $("#org").val(node.org.orgName);
		    	  $("#orgId").val(node.org.orgId);
		      }
		  }
	  });
  }
  
  //节点删除确认
  function delConfirm(){
	  $("#confirm-dialog").dialog("open");
  }
  
  //删除节点 -- 设置节点为无效
  function delNode(){
	  $("#confirm-dialog").dialog("close");
	  $.ajax({
		  type: 'GET',
		  url: "/wfnode/delNode/"+clickedNodeId,		  	 
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowdefination/"+$("#wfId").val();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //冻结节点 -- 设置节点暂时无效
  function frozenNode(){
	  $.ajax({
		  type: 'GET',
		  url: "/wfnode/frozenNode/"+clickedNodeId,		  	 
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowdefination/"+$("#wfId").val();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //解冻节点 -- 设置节点成有效
  function unfrozenNode(){
	  $.ajax({
		  type: 'GET',
		  url: "/wfnode/unfrozenNode/"+clickedNodeId,		  	 
		  dataType: 'json',
		  success: function(data){
			  location.href="/wf/workflowdefination/"+$("#wfId").val();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
</script>
<div class="container" style="padding-top:5%">
	<div id="content">
		<div style="padding:0em;margin:0px">
		  	<div >
		  		<h3 class="mt-5">流程定义  <span class="small-btn" style="background:#42a288;font-weight:bold;color: yellow;" onclick="location.href='/wf/workflowcenter'">&nbsp;↢&nbsp;</span></h3>		  		  		   		    
		  	</div>
		</div>			
	</div>
	<div class="line"></div>
	<div id="workflow" class="draw" style="margin-top: 12px;">
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
							<div <c:if test="${ node.status eq '冻结' }" > class="circle-dotted-text" </c:if> <c:if test="${ node.status ne '冻结' }" > class="circle-text" </c:if> onclick="showPos(event,${node.nodeId},'${node.nodename }','${node.status}')" >
								<font style="font-size:15px">${node.nodename }</font>
							</div>
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
</div>	
<!-- 节点定义窗口 -->
<div id="node-div" class="node-mask opacity" style="display:none;">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>节点定义</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#node-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="myForm" class="navbar-form navbar-left" method="post" modelAttribute="node" action="/wfnode/savenode">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="wfId" name="wfId" value="${node.wfId}">
			<input type="hidden" id="nodeId" name="nodeId" value="${node.nodeId}">
			<input type="hidden" id="preNodeId" name="preNodeId" value="">
	  		<div class="popup-form-group">		        
		        <label for="nodename" class="sr-only">节点名称</label>
		        <input name="nodename" id="nodename" class="form-control-one-line" required autofocus placeholder="节点名称"  />			        		       
		    </div>		 
		    <div class="popup-form-group"> 
		    	<label for="nodeStatus" class="sr-only">节点状态</label> 
		        <input type="radio" id="status" name="status" value="有效">&nbsp;有效
		        <input type="radio" id="status" name="status" value="无效">&nbsp;无效			        
	        </div>		        	     
	        <div class="popup-form-group" id="user-sel">
	        	<label for="usersName" class="sr-only">办理人</label>
		        <input id="usersName" name="usersName" class="form-control-one-line" placeholder="办理人" style="width:80%" />			        
		        <span style="cursor:pointer" onclick="$('#user-div').show();">用户</span>
		        <div id="user-hidden"></div>
		    </div>
		    <div class="popup-form-group">
		    	<div class="navbar" style=" padding: 0rem 0rem;">
			        <div class="navbar-inner">
			            <div class="container" style="padding-left: 0px;">	        		        			        
		                <ul class="nav" >			                    
		                    <li class="dropdown" id="accountmenu">
		                        <button type="button" id="role" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem;">
							    	&nbsp;角色选择
							  	</button>
							  	<input type="hidden" name="role.roleId" id="roleId"/>
							  	<input type="hidden" name="role.roleName" id="roleName"/>
		                        <ul class="dropdown-menu" id="role-ul">		                        	
		                        	<c:forEach var="item" items="${roles}" varStatus="status">
							  			<li value="${item.roleId}"><a class="dropdown-item" href="#">${item.roleName}</a></li>
							  		</c:forEach>			                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		        </div>						       		        
		     </div>
		     <div class="popup-form-group">
		        <label for="org" class="sr-only">所在单位</label>
		        <input id="org" name="org.orgName" class="form-control-one-line" placeholder="所在单位" style="width:80%" />
				<input type="hidden" id="orgId" name="org.orgId" />
				<span style="cursor:pointer" onclick="$('#org-div').show();">单位</span>				        
	        </div>   		       
		    <div class="popup-form-group">
		        <div class="navbar" style=" padding: 0rem 0rem;">
			        <div class="navbar-inner">
			            <div class="container" style="padding-left: 0px;">		        		        			        
		                <ul class="nav">			                    
		                    <li class="dropdown" id="accountmenu">
		                        <button type="button" id="nTypeName" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem">
							    	&nbsp;节点属性
							  	</button>
							  	<input type="hidden" name="nType" id="nType"/>
		                        <ul class="dropdown-menu" id="nType-ul">
		                        	<li><a class="dropdown-item" href="#">单人</a></li>
							  		<li><a class="dropdown-item" href="#">多人</a></li>							  					                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		        </div>	
		    </div>		    
	        <div class="popup-form-group">
	        	<div class="navbar" style=" padding: 0rem 0rem;">
			        <div class="navbar-inner">
			            <div class="container" style="padding-left: 0px;">		        		        			        
		                <ul class="nav">			                    
		                    <li class="dropdown" id="accountmenu">
		                        <button type="button" id="actionName" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem">
							    	&nbsp;节点行为
							  	</button>
							  	<input type="hidden" name="action" id="action"/>
		                        <ul class="dropdown-menu" id="action-ul">
		                        	<li><a class="dropdown-item" href="#">推进流程</a></li>	                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		        </div>		          			        			       
	        </div>	
	        <div class="popup-form-group">	
		        <label for="timeLimit" class="sr-only">节点时效</label>
		        <input id="timeLimit" name="timeLimit" class="form-control-one-line" placeholder="节点时效"  style="width:80%"/>&nbsp;小时        			        
	        </div>
	        <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;" type="submit" >保存</button>
		   </div> 			   
	   </form>
	</div>
    
</div>

<!-- 节点关系配置 -->
<div id="node-nodes-div" class="node-mask opacity" style="display:none;height:40%;top: 30%;">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>节点-[<span id="node-nodes-nodename" style="font-weight:bold;"></span>]&nbsp;关系定义</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#node-nodes-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="nodesForm" class="navbar-form navbar-left" method="post" modelAttribute="node" action="/wfnode/savenode">	  			      
		    <div class="popup-form-group">
		        <div class="navbar" style=" padding: 0rem 0rem;">
			        <div class="navbar-inner">
			            <div class="container" style="padding-left: 0px;">		        		        			        
		                <ul class="nav">			                    
		                    <li class="dropdown" id="accountmenu">
		                        <button type="button" id="preNodeName" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem">
							    	&nbsp;前置节点
							  	</button>
							  	<input type="hidden" name="preNodeId" id="preNodeId"/>
		                        <ul class="dropdown-menu" id="preNodeId-ul">
		                        	<c:forEach var="item" items="${nodeset}" varStatus="status">
							  			<li value="${item.nodeId}"><a class="dropdown-item" href="#">${item.nodename}</a></li>
							  		</c:forEach>						  					                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		        </div>	
		    </div>		    
	        <div class="popup-form-group">
	        	<div class="navbar" style=" padding: 0rem 0rem;">
			        <div class="navbar-inner">
			            <div class="container" style="padding-left: 0px;">		        		        			        
		                <ul class="nav">			                    
		                    <li class="dropdown" id="accountmenu">
		                        <button type="button" id="sufNodeName" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem">
							    	&nbsp;后置节点
							  	</button>
							  	<input type="hidden" name="sufNodeId" id="sufNodeId"/>
		                        <ul class="dropdown-menu" id="sufNode-ul">
		                        	<c:forEach var="item" items="${nodeset}" varStatus="status">
							  			<li value="${item.nodeId}"><a class="dropdown-item" href="#">${item.nodename}</a></li>
							  		</c:forEach>	                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		        </div>		          			        			       
	        </div>		        
	        <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;" type="submit" >保存</button>
		   </div> 			   
	   </form>
	</div>
    
</div>

<!-- 用户选择窗口 -->
<div id="user-div" class="mask opacity" style="display:none">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <input class="form-control-green mr-sm-2 required" type="text" placeholder="办理人选择" aria-label="办理人选择" id="userName">
           <button class="btn btn-outline-success my-2 my-sm-0" onclick="selectUsers();">选择</button>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#user-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;" >
         <h5 class="d-flex justify-content-between align-items-center mb-2">
           <span class="text-muted">返回结果</span>            
           <span class="badge badge-secondary badge-pill" style="cursor:pointer;" onclick="selectedUsers();">√</span>
         </h5>
         <div style="height: 300px;overflow-y: auto;">
          <ul class="list-group mb-3" id="user-lst">
            <li class="list-group-item d-flex justify-content-between lh-condensed" style="height: 40px;background-color: #adcabc;">              
              <div>
                <h6 class="my-0">&nbsp;姓名</h6>                
              </div>
              <span class="text-muted">直属组织</span>
              <span class="text-muted">所在机构</span>
            </li>                       
          </ul>
         </div>
       </div>
</div>

<!-- 单位选择窗口 -->
<div id="org-div" class="mask opacity" style="display:none">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <input class="form-control-green mr-sm-2 required" type="text" placeholder="角色所在单位选择" aria-label="角色所在单位选择" id="orgName">
           <button class="btn btn-outline-success my-2 my-sm-0" onclick="selectOrgs();">选择</button>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#org-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;" >
         <h5 class="d-flex justify-content-between align-items-center mb-2">
           <span class="text-muted">返回结果</span>            
           <span class="badge badge-secondary badge-pill" style="cursor:pointer;" onclick="selectedOrgs();">√</span>
         </h5>
         <div style="height: 300px;overflow-y: auto;">
          <ul class="list-group mb-3" id="org-lst">
            <li class="list-group-item d-flex justify-content-between lh-condensed" style="height: 40px;background-color: #adcabc;">              
              <div>
                <h6 class="my-0">&nbsp;单位名称</h6>                
              </div>
              <span class="text-muted">直属机构</span>
            </li>                       
          </ul>
         </div>
       </div>
</div>	
<!-- /*onmouseover="document.getElementById('PopUp').style.display = 'none' "*/ -->
<DIV id='PopUp' class="popup" >
    <div class="popup-close" style="cursor:pointer;" onclick="$('#PopUp').hide();">×</div>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp').hide();$('#node-div').show();$('#myForm')[0].reset();">新增</SPAN>
</DIV>
<DIV id='PopUp-1' class="popup-width" >
	<div class="popup-close" style="cursor:pointer;" onclick="$('#PopUp-1').hide();">×</div>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();$('#node-div').show();$('#myForm')[0].reset();">插入</SPAN>
	<hr style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></hr>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();$('#node-div').show();showNode();">修改</SPAN>
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();$('#node-nodes-div').show();$('#nodesForm')[0].reset();">关联</SPAN>
	<hr style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></hr>	
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();frozenNode();" id="frozen">冻结</SPAN>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();unfrozenNode();" id="unfrozen">解冻</SPAN>
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();delConfirm();">删除节点</SPAN>
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();delConfirm();">删除子节点</SPAN>
		
</DIV>
<!-- 删除弹出窗口 -->
<div id="confirm-dialog"  title="确认窗口" >
  <p>确认要删除吗？</p>
  <br>
  <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void();"  onclick="delNode();">
		        <span aria-hidden="true">确认</span>		        
		      </a>
	      </div>
	    </li>
	    <li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void();" onclick="$('#confirm-dialog').dialog('close');">
		        <span aria-hidden="true">取消</span>		        
		      </a>
	      </div>
	    </li>
	</ul>
  </nav>    
</div>

​

