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
    $("#confirm-dialog").dialog();
    $('#confirm-dialog').dialog('close');
    
    //Form必输项控制
    // $("#myForm").valid();
    
    //必输项的默认提示信息：
    //$.validator.messages.required = "此项为必输项！";
    
    //下拉框选择操作 - 角色选择
    $('#role-ul li').on('click', function(){
    	$('#role').text($(this).text()); 
    	$('#roleName').val($(this).text());      	
    	$('#roleId').val($(this).attr("value"));    	    	
    });
    
    showButtons(null);
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
			        + "<h6 class=\"my-0\"><input type=\"checkbox\" name=\"userChecked\" value=\""+user.userId+"\">"+user.userName+"</h6>"
			        + "</div>"
			        + "<span class=\"text-muted\">"+user.currentOrgName+"</span>"
			        + "<span style=\"display:none\">"+user.currentOrgId+"</span>"			        
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
		  $("#user-hidden").append("<input type='hidden' name='users["+idx+"].userName' value='"+$(this).closest("h6").text()+"'>");
		  $("#user-hidden").append("<input type='hidden' name='users["+idx+"].currentOrgName' value='"+$(this).closest("h6").parent().parent().children("span").get(0).innerHTML+"'>");
		  $("#user-hidden").append("<input type='hidden' name='users["+(idx++)+"].currentOrgId' value='"+$(this).closest("h6").parent().parent().children("span").get(1).innerHTML+"'>");
  	  })
	  if(checkedUserNames.length > 0){		  
		  checkedUserNames = checkedUserNames.substring(0,checkedUserNames.length-1);
	  }	  
	  $("#user-div").hide();
	  $("#usersName").val(checkedUserNames);
	  $("#warn4").hide();
  }
  
  
  //选择单位
  function selectOrgs(){
	  var orgName = $("#orgName").val();
	  $.ajax({
		  type: 'GET',
		  url: "${path}/wf/selectOrgs",
		  data: {orgName:orgName},			  
		  dataType: 'json',
		  success: function(data){
			  var li;
			  $.each(data,function (index, org) {
			      li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\">"
			        + "<div>"
			        + "<h6 class=\"my-0\"><input type=\"radio\" name=\"orgChecked\" value=\""+org.orgId+"\">"+org.orgName+"</h6>"
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
	  if(cNodeId == 0) el = document.getElementById('PopUp-2');
	  else if(cNodeId > 0) el = document.getElementById('PopUp-1');
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
	  y = y+15
	  if(y>500) y=410;
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
		      $('#nType').val(node.nType);
		      if(node.nodeAction != null){			      	    
			      $('#action').val(node.nodeAction.actionCodeName); 
		      }
		      $("input[name=status][value="  + node.status + "]").prop('checked', true);
		      $('#timeLimit').val(node.timeLimit);
		      		
		      if(node.role != null){
			      $('#role').text(node.role.roleName); 
			      $('#roleName').val(node.role.roleName);    	
			      $('#roleId').val(node.role.roleId);
			      $("input[name=noder][value=办理角色]").prop('checked', true);
			      $('#user-sel').hide();$('#role-sel').show();$('#org-sel').show();
		      }

		      if(node.users != null && node.users != ""){		    	  
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
		    	  $("input[name=noder][value=办理人]").prop('checked', true);
		    	  $('#user-sel').show();$('#role-sel').hide();$('#org-sel').hide();
		      }
		      
		      if(node.org != null){
		    	  $("#org").val(node.org.orgName);
		    	  $("#orgId").val(node.org.orgId);
		      }
		      
		      //节点按钮显示处理
		      if(node.buttons != null){
		    	  //先删除div下的元素，然后再添加
	    		  var el = document.getElementById('button-hidden');
	    		  while( el.hasChildNodes() ){
	    		      el.removeChild(el.lastChild);
	    		  }
	    		  var checkedButtons = "";
		    	  $.each(node.buttons,function(key,action){			    		  
		    		  checkedButtons += action.actionName+",";		    		  
	    			  $("#button-hidden").append("<input type='hidden' name='buttons["+key+"].actionName' value='"+action.actionName+"'>");		
	    			  $("#button-hidden").append("<input type='hidden' name='buttons["+key+"].actionCodeName' value='"+action.actionCodeName+"'>");		 		  	    	  	  		    		   		    		  
		    	  });
		    	  if(checkedButtons.length > 0){		  
		    		  checkedButtons = checkedButtons.substring(0,checkedButtons.length-1);
	    		  }	
		    	  $("#button").val(checkedButtons);		    	    	 
		      }
		      showButtons(node);
		  }
	  });
  }
  
  //增加节点操作按钮的显示页面
  function showButtons(node){
	  var buttons = ${buttons};
	  $("#button-lst").empty();
      var li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\" style=\"height: 40px;background-color: #adcabc;\">"             
  		  +"<div>"
    	  +"<h6 class=\"my-0\">&nbsp;操作按钮名称</h6>"               
          +"</div>"
          +"<span class=\"text-muted\">操作按钮</span>"
          +"<span class=\"text-muted\">选择</span>"
          +"</li>";
      $("#button-lst").append(li);      
      $.each(buttons,function (index, action) {
    	  var checked = "";
    	  if(node != null){
	    	  $.each(node.buttons,function(key,item){	
	    		  if(action.actionId == item.actionId) {
	    			  checked = "checked";
	    			  return;
	    		  }
	    	  })
    	  }
		  li = "<li class='list-group-item d-flex justify-content-between lh-condensed' style='height: 40px;'> "
            	  +"<span class=\"text-muted\">"+action.actionName+"</span> "
            	  +"<span class=\"text-muted\">"+action.actionCodeName+"</span> "
            	  +"<span class=\"text-muted\"><input type=\"checkbox\" name=\"buttonChecked\" "+checked+" value=\""+action.actionCodeName+"\"></span> "
          		  +"</li> ";
          $("#button-lst").append(li);
	  })
  }
  
  //删除指定节点的连接
  function delLinkConfirm(){
	  $("#link-div").show();
	  $("#link-lst").empty();
	  var li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\" style=\"height: 40px;background-color: #adcabc;\">"             
  		  +"<div>"
    	  +"<h6 class=\"my-0\">&nbsp;连接节点名称</h6>"               
          +"</div>"         
          +"<span class=\"text-muted\">选择</span>"
          +"</li>";
      $("#link-lst").append(li);
      var nodes = ${nodes};
	  $.each(nodes,function(key,node){
		  if(node.nodeId == clickedNodeId){			  
			  var sufNodes = node.sufNodes;					
			  if(sufNodes.length > 0){				  
				  $.each(sufNodes,function (index, data) {	
					  li = "<li class='list-group-item d-flex justify-content-between lh-condensed' style='height: 40px;'> "
		            	  +"<span class=\"text-muted\">"+data.nodename+"</span> "
		            	  +"<span class=\"text-muted\"><input type=\"checkbox\" name=\"linkChecked\" checked value=\""+data.nodeId+"\"></span> "
		          		  +"</li> ";
					  $("#link-lst").append(li);
				  })				 			 
			  }
		  }
	  });
  }
  
  //取消节点连接
  function delNodeLink(){
	  var linkNodeIds = "";
	  $('input:checkbox[name=linkChecked]:checked').each(function(k){
		  linkNodeIds += $(this).val()+",";
  	  })
	  if(linkNodeIds.length > 0){		 
		  linkNodeIds = linkNodeIds.substring(0,linkNodeIds.length-1);
	  }	
	  var url = "";
	  if(linkNodeIds == "") {
		  url = '${path}/wfnode/delNodeLink/'+clickedNodeId;
	  }else{
		  url = '${path}/wfnode/delNodeLink/'+clickedNodeId+'/'+linkNodeIds;
	  }
	  $("#link-div").hide();
	  $.ajax({
		  type: 'GET',
		  url: url,
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/wf/workflowdefination/"+$("#wfId").val();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  //保留节点，删除节点后续链删除确认
  function delConfirm(){
	  $("#confirm-dialog").dialog("open");	  
	  $("#delegationDiv").empty();
	  var nodes = ${nodes};
	  $.each(nodes,function(key,node){
		  if(node.nodeId == clickedNodeId){			  
			  var preNodes = node.preNodes;	
			  if(preNodes.length > 0){
				  var sel = "接管节点：<select id='delegationNodeId' name='delegationNodeId' class='form-control-one-line' style='width:150px'>";
				  sel +="<option value=''>不设接管节点</option>";
				  $.each(preNodes,function (index, data) {	
				  	sel +="<option value='"+data.nodeId+"'>"+data.nodename+"</option>";
				  })
				  sel +="</select>";
				  $("#delegationDiv").append(sel);
				  $("#delegationDiv").show();
			  }
		  }
	  });	 
  }
  //删除节点 -- 直接删除
  function delNode(){
	  $("#confirm-dialog").dialog("close");
	  var delegationNodeId = $("#delegationNodeId").val();
	  var url = "";
	  if(delegationNodeId == "" || typeof(delegationNodeId) == 'undefined') url="${path}/wfnode/delNode/"+clickedNodeId;
	  else url="${path}/wfnode/delNode/"+clickedNodeId+"/"+delegationNodeId;
	  $.ajax({
		  type: 'GET',
		  url: url,
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/wf/workflowdefination/"+$("#wfId").val();
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
		  url: "${path}/wfnode/frozenNode/"+clickedNodeId,		  	 
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/wf/workflowdefination/"+$("#wfId").val();
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
		  url: "${path}/wfnode/unfrozenNode/"+clickedNodeId,		  	 
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/wf/workflowdefination/"+$("#wfId").val();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //设置流程名称
  function setWfName(event){
		 if (event.keyCode == 13) {	
	        var wfName = $("#wfName").val();
	        var wfId = $("#wfId").val();
	        if($.trim(wfName) != ""){
	        	$.ajax({
	      		  type: 'GET',
	      		  url: "${path}/wf/setWfName/"+wfId,
	      		  data: {wfName:wfName},			  
	      		  dataType: 'json',
	      		  success: function(data){	      			  		 
	      		  },
	      		  error: function(XMLHttpRequest, textStatus, errorThrown){
	      			  console.warn(XMLHttpRequest.responseText);			  
	      		  }
	      	});
	        }
	    }
	}
  
  //节点保存
  function saveNode(){
	  //节点必输项的判断
	  var goon = true;
	  if($("#nodename").val() == "") {
		  $("#warn1").show();
		  goon = false;
	  }			 
	  if($("#nType").val() == "") {
		  $("#warn2").show();		  
		  goon = false;
	  }	
	  if($("#action").val() == "") {
		  $("#warn3").show();
		  goon = false;
	  }	
	  if(!$("input:radio[name=noder]").is(':checked')){
		 $("#warn4").show();
		 goon = false; 
	  }
	  if($('input:radio[name=noder]:checked').val() == '办理人'){			 
		  if($("#usersName").val() == ""){
			  $("#warn5").show();
			  goon = false;
		  }			  
	  }
	  if($('input:radio[name=noder]:checked').val() == '办理角色'){			 
		  if($("#roleId").val() == ""){
			  $("#warn6").show();
			  goon = false;
		  }			  
	  }
	  if(!goon) return false;
	  $("#myForm").submit();
  }
  
  //关闭节点窗口，对节点中的数据还原成初始状态
  function closeNode(){
	  $('#node-div').hide();
	  $("#warn1").hide();
	  $("#warn2").hide();
	  $("#warn3").hide();
	  $("#warn4").hide();
	  $("#warn5").hide();
	  $("#warn6").hide();
	  $("#nType").val("");	  	  
	  $("#action").val("");
	  showButtons(null);	  
  }
  
  //输入框键输入
  function nodeKeydown(item){
	  $("#"+item).hide();
  }
  
  //操作协助操作
  function selectedButton(){
	  var buttonActions = "";
	  var idx = 0;
	  var name = "";
	  var codeName = "";
	  $("#button-hidden").empty();
	  $('input:checkbox[name=buttonChecked]:checked').each(function(k){
		  name = $(this).parent().parent().children("span").get(0).innerHTML;	
		  codeName = $(this).parent().parent().children("span").get(1).innerHTML;
		  buttonActions += name+",";
		  $("#button-hidden").append("<input type='hidden' name='buttons["+idx+"].actionName' value='"+name+"'>");
		  $("#button-hidden").append("<input type='hidden' name='buttons["+idx+"].actionCodeName' value='"+codeName+"'>");
		  idx++;
  	  })
	  if(buttonActions.length > 0){		 
		  buttonActions = buttonActions.substring(0,buttonActions.length-1);
	  }	 
	  $("#button-div").hide();
	  $("#button").val(buttonActions);
  }
  
  //设置节点关联
  function showNodeNodes(){
	  var wfId = $("#wfId").val();
	  $("#nodeNodeId").val(clickedNodeId);
	  $.ajax({
  		  type: 'GET',
  		  url: "${path}/wf/getsufnodes/"+clickedNodeId+"/"+wfId, 
  		  dataType: 'json',
  		  success: function(data){ 
  			$("#sufNodeId").empty();
  			$("#sufNodeId").append("<option>后置节点</option>");
  			$.each(data,function (index,node) {
  				$("#sufNodeId").append("<option value='"+node.nodeId+"'>"+node.nodeName+"</option");
  			});
  			$('#node-nodes-div').show();
  		    $('#nodesForm')[0].reset();
  		  },
  		  error: function(XMLHttpRequest, textStatus, errorThrown){
  			  console.warn(XMLHttpRequest.responseText);			  
  		  }
  	});	 
  }
  
  //打开字段设置窗口
  function openTableElements(tbId){
	  var wfId = $("#wfId").val();
	  $("#fields-div").show();
	  $("#fields-lst").empty();
	  var li = "<li class=\"list-group-item d-flex justify-content-between lh-condensed\" style=\"height: 40px;background-color: #adcabc;\">"             
  		  +"<div>"
    	  +"<h6 class=\"my-0\">&nbsp;字段名称</h6>"               
          +"</div>"         
          +"<span class=\"text-muted\">是否可编辑</span>"
          +"<span class=\"text-muted\">是否必输项</span>"
          +"</li>";
      $("#fields-lst").append(li);
	  $.ajax({
  		  type: 'GET',
  		  url: "${path}/wf/gettableelements/"+wfId+"/"+clickedNodeId+"/"+tbId, 
  		  dataType: 'json',
  		  success: function(data){  			
  			var checked="";
  			var rqchecked = "";
  			$.each(data,function (index,table) { 
  				checked="";
				rqchecked = "";
  				if(table.readOnly) checked="checked";  				
  				if(table.required == '是') rqchecked = "checked";  				
  				li = "<li class='list-group-item d-flex justify-content-between lh-condensed' style='height: 40px;'> "
	            	  +"<span class=\"text-muted\">"+table.newLabelName+"</span>"	            	  
	            	  +"<span class=\"text-muted\"><input type=\"checkbox\" name=\"fieldChecked\" "+checked+" value=\""+table.id+"\"></span>"	            	 
	            	  +"<span class=\"text-muted\"><input type=\"checkbox\" name=\"required\" "+rqchecked+" value=\""+table.id+"\"></span>"
	          		  +"</li> ";
  				$("#fields-lst").append(li);  				
  			});			
  		  },
  		  error: function(XMLHttpRequest, textStatus, errorThrown){
  			  console.warn(XMLHttpRequest.responseText);			  
  		  }
  	  });	  
  }
  
  //保存流程节点字段
  function setTableElements(){
	  var wfId = $("#wfId").val();
	  var ids = "";
	  var requiredIds = "";
	  $("#button-hidden").empty();
	  $('input:checkbox[name=fieldChecked]:checked').each(function(k){		  
		  ids += $(this).val()+",";		  
  	  })
  	  if(ids.length > 0){
  		ids = ids.substring(0,ids.length-1);		  
	  }
	  $('input:checkbox[name=required]:checked').each(function(k){		  
		  requiredIds += $(this).val()+",";		  
  	  })
  	  if(requiredIds.length > 0){
  		requiredIds = requiredIds.substring(0,requiredIds.length-1);		  
	  }
	  $.ajax({
  		  type: 'GET',
  		  url: "${path}/wf/settableelements/"+wfId+"/"+clickedNodeId,
  		  data: {ids:ids,requiredIds:requiredIds},			  
  		  dataType: 'json',
  		  success: function(data){ 
  			  $("#fields_message").text("配置完成。");
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
		  		<h3 class="mt-5">流程定义 
		  		 	<span class="small-btn" style="background:#42a288;font-weight:bold;color: #ffc107;" onclick="location.href='${path}/wf/workflowcenter'">&nbsp;⬅&nbsp;</span>
		  		 	<input type="text" name="wfName" id="wfName" placeholder="流程名称"  style="text-align:center;font-size:1.5rem;width:30%" value="${wf.wfName }" onkeypress="setWfName(event);" />		  		 	
		  		</h3>		  		  		   		    
		  	</div>
		</div>			
	</div>
	<div class="line"></div>
	<div id="workflow" class="draw" style="margin-top: 12px;width:82%">		
		 ${flowchat}		
	</div>
</div>	
<!-- 节点定义窗口 -->
<div id="node-div" class="node-mask opacity" style="display:none;height:76%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>节点定义</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="closeNode();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="myForm" class="navbar-form navbar-left" method="post" modelAttribute="node" action="/wfnode/savenode">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="wfId" name="wfId" value="${wf.wfId}">
			<input type="hidden" id="nodeId" name="nodeId" value="">
			<input type="hidden" id="preNodeId" name="preNodeId" value="">
	  		<div class="popup-form-group">		        
		        <label for="nodename" class="sr-only">节点名称</label>
		        <input name="nodename" id="nodename" class="form-control-one-line" autofocus placeholder="节点名称" onkeydown="nodeKeydown('warn1');" style="width:75%"/>	
		        <label style="display:none;color:red;font-weight:bold;" id="warn1">!</label>		        		       
		    </div>
		    <div class="popup-form-group">
		    	<select name="nType" id="nType" class="form-control-one-line" onclick="nodeKeydown('warn2');" style="width:75%">
		    		<option value="">节点属性</option>
		    		<option value="串行">串行</option>
		    	</select>
		        <label style="display:none;color:red;font-weight:bold;" id="warn2">!</label>
		    </div>		    
	        <div class="popup-form-group">
	        	<select name="nodeAction.actionCodeName" id="action" class="form-control-one-line" onclick="nodeKeydown('warn3');" style="width:75%">
		    		<option value="">节点行为</option>
		    		<c:forEach var="item" items="${actions}" varStatus="status">
			  			<option value="${item.actionCodeName}" >${item.actionName}</option>
			  		</c:forEach>	
		    	</select>
		    	<label style="display:none;color:red;font-weight:bold;" id="warn3">!</label>        			        		          			        			     
	        </div>	
	        <div class="popup-form-group">
	        	<label for="button" class="sr-only">操作协助</label>
		        <input id="button" name="button.actionName" class="form-control-one-line" placeholder="操作协助" style="width:75%" />			        
		        <span style="cursor:pointer" onclick="$('#button-div').show();">操作</span>	
		        <div id="button-hidden"></div>	        		        	      
		    </div>
	        <div class="popup-form-group">	
		        <label for="timeLimit" class="sr-only">节点时效</label>
		        <input id="timeLimit" name="timeLimit" class="form-control-one-line" placeholder="节点时效"  style="width:75%"/>&nbsp;小时        			        
	        </div>		 
		    <hr style="margin-top: .3rem; margin-bottom: .3rem"></hr>
	        <div class="popup-form-group"> 
		    	<label for="nodeStatus" class="sr-only">节点办理人</label> 
		        <input type="radio" name="noder" value="办理人" onclick="nodeKeydown('warn2'); $('#user-sel').show();$('#role-sel').hide();$('#org-sel').hide();">&nbsp;办理人
		        <input type="radio" name="noder" value="办理角色" onclick="nodeKeydown('warn2'); $('#role-sel').show();$('#org-sel').show();$('#user-sel').hide();">&nbsp;办理角色
		        <label style="display:none;color:red;font-weight:bold;" id="warn4">!</label>		        
	        </div>		        	     
	        <div class="popup-form-group" id="user-sel" style="display:none;">
	        	<label for="usersName" class="sr-only">办理人</label>
		        <input id="usersName" name="usersName" class="form-control-one-line" placeholder="办理人" style="width:75%" />			        
		        <span style="cursor:pointer" onclick="$('#user-div').show();">用户</span>
		        <label style="display:none;color:red;font-weight:bold;" id="warn5">!</label>
		        <div id="user-hidden"></div>		        
		    </div>
		    <div class="popup-form-group" id="role-sel" style="display:none;">		    	
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
							  			<li value="${item.roleId}" onclick="nodeKeydown('warn6');"><a class="dropdown-item" href="#">${item.roleName}</a></li>
							  		</c:forEach>			                            
		                        </ul>
		                    </li>
		                </ul>
		               	</div>
		            </div>
		            <label style="display:none;color:red;font-weight:bold;" id="warn6">!</label>
		        </div>						       		        
		     </div>
		     <div class="popup-form-group" id="org-sel" style="display:none;">
		        <label for="org" class="sr-only">所在单位</label>
		        <input id="org" name="org.orgName" class="form-control-one-line" placeholder="所在单位" style="width:75%" />
				<input type="hidden" id="orgId" name="org.orgId" />
				<span style="cursor:pointer" onclick="$('#org-div').show();">单位</span>				        
	        </div> 
	        <hr style="margin-top: .3rem; margin-bottom: .3rem"></hr>  		       
		    <div class="popup-form-group"> 
		    	<label for="nodeStatus" class="sr-only">节点状态</label> 
		        <input type="radio" id="status" name="status" value="有效" checked>&nbsp;有效
		        <input type="radio" id="status" name="status" value="无效">&nbsp;无效			        
	        </div>
	        <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;" type="button" onclick="saveNode();" >保存</button>
		   </div> 			   
	   </form>
	</div>
    
</div>

<!-- 节点关系配置 -->
<div id="node-nodes-div" class="node-mask opacity" style="display:none;height:40%;top: 30%;">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>为节点-[<span id="node-nodes-nodename" style="font-weight:bold;"></span>]&nbsp;建立关联关系</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#node-nodes-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="nodesForm" class="navbar-form navbar-left" method="post" action="/wfnode/savesufnode/${wf.wfId}">
			<input type="hidden" id="nodeNodeId" name="nodeNodeId" value="">	        
	        <div class="popup-form-group">
	            <select name="sufNodeId" id="sufNodeId" class="form-control-one-line" required >
			        <option>后置节点</option>			       
			    </select>	        		          			        			      
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
<DIV id='PopUp-2' class="popup" >
    <div class="popup-close" style="cursor:pointer;" onclick="$('#PopUp-2').hide();">×</div>
    <c:if test="${! empty wf.tbLst }">
		<SPAN style="cursor:pointer;" onclick="$('#PopUp-2').hide();openTableElements(${wf.tbId});">字段授权</SPAN>
	</c:if>
</DIV>
<DIV id='PopUp' class="popup" >
    <div class="popup-close" style="cursor:pointer;" onclick="$('#PopUp').hide();">×</div>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp').hide();$('#node-div').show();$('#myForm')[0].reset();">新增</SPAN>
</DIV>
<DIV id='PopUp-1' class="popup-width" style="z-index:99">
	<div class="popup-close" style="cursor:pointer;" onclick="$('#PopUp-1').hide();">×</div>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();$('#node-div').show();$('#myForm')[0].reset();">新增</SPAN>
	<hr style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></hr>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();$('#node-div').show();showNode();">修改</SPAN>	
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();showNodeNodes();">关联</SPAN>
	<hr style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></hr>	
	<!-- 以下功能暂缓，业务还没想清楚
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();frozenNode();" id="frozen">冻结</SPAN>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();unfrozenNode();" id="unfrozen">解冻</SPAN>
	-->
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();delLinkConfirm();">取消关联</SPAN>	
	<p style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></p>
	<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();delConfirm();">删除节点</SPAN>
	<!-- 只有流程配置了表，才会出现字段授权功能 -->
	<c:if test="${! empty wf.tbLst }">
		<hr style="margin-top: 0.3rem;margin-bottom: 0.3rem;"></hr>
		<SPAN style="cursor:pointer;" onclick="$('#PopUp-1').hide();openTableElements(${wf.tbId});">字段授权</SPAN>
	</c:if>
</DIV>
<!-- 删除弹出窗口 -->
<div id="confirm-dialog"  title="确认窗口" >
  <p>确认删除节点吗？</p>
  <br>
  <div style="display:none" id="delegationDiv">  	
  </div>
  <br>
  <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void(0);"  onclick="delNode();">
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
<!-- 操作协助选择窗口 -->
<div id="button-div" class="mask opacity" style="display:none">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <label>操作协助选择</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#button-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;" >
         <h5 class="d-flex justify-content-between align-items-center mb-2">
           <span class="text-muted">返回结果</span>            
           <span class="badge badge-secondary badge-pill" style="cursor:pointer;" onclick="selectedButton();">√</span>
         </h5>
         <div style="height: 300px;overflow-y: auto;">
          <ul class="list-group mb-3" id="button-lst"></ul>          	
         </div>
       </div>
</div>

<!-- 取消连接窗口 -->
<div id="link-div" class="mask opacity" style="display:none">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <label>选择需要取消的连接(<span style="color:red">把√去掉!  单连接此操作无效</span>)</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#link-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;" >
         <h5 class="d-flex justify-content-between align-items-center mb-2">
           <span class="text-muted">返回结果</span>            
           <span class="badge badge-secondary badge-pill" style="cursor:pointer;" onclick="delNodeLink();">确定</span>
         </h5>
         <div style="height: 300px;overflow-y: auto;">
          <ul class="list-group mb-3" id="link-lst"></ul>          	
         </div>
       </div>
</div>

<!-- 字段授权窗口 -->
<div id="fields-div" class="mask opacity" style="display:none">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <label>设置节点字段是否可编辑</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#fields-div').hide();$('#fields_message').text('');">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;" >
         <h5 class="d-flex justify-content-between align-items-center mb-2">
           <span class="text-muted">字段列表</span>   
           <span id="fields_message"></span>         
           <span class="badge badge-secondary badge-pill" style="cursor:pointer;" onclick="setTableElements();">确定</span>
         </h5>
         <div style="height: 270px;overflow-y: auto;">
          <ul class="list-group mb-3" id="fields-lst"></ul>          	
         </div>
       </div>
</div>