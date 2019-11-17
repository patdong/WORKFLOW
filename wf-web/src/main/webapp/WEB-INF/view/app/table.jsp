<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
	//初始化页面对象
	var json = ${modeljson};	
	let pageModel = {			
		bizId:json.bizId,
		curNode:json.curNode,
		nextNodes:json.nextNodes,
		nextNodeSize:json.nextNodeSize,
		wfBrief:json.wfBrief,
		buttons:json.buttons,
		table:json.table,
		flowChat:json.flowChat,
		nodes:json.nodes,
		wfts:json.wfts,
		wftb:json.wftb
	};
	
    //表单保存提交
    function doStorage(){
    	$("#myForm").valid();
    	//必输项的默认提示信息：
        $.validator.messages.required = "此项为必输项！";
    	
    	if(!formSpecialValid()){
	    	if(pageModel.bizId == undefined) $("#myForm").attr("action","${path}/app/save/"+pageModel.wftb.tbId+"/"+pageModel.wftb.wfId);
	    	else $("#myForm").attr("action","${path}/app/update/"+pageModel.wftb.tbId+"/"+pageModel.bizId);       	
	    	$("#myForm").submit();
    	}
    }
    //表单重置
    function doReset(){
    	$("#myForm")[0].reset();
    }
    
    //流程办理完毕提交
    function doAction(){ 
    	$("#myForm").valid();
    	//必输项的默认提示信息：
        $.validator.messages.required = "此项为必输项！";
        if(formSpecialValid()) return;
        var bizId = $("#bizId").val();
    	var wfId = $("#wfId").val();
    	var tbId = $("#tbId").val();
    	if(pageModel.curNode.lastUser == "false"){
    		$("#myForm").attr("action","${path}/app/doaction/"+tbId+"/"+bizId+"/"+wfId);
	    	$("#myForm").submit();
    	}else{
	    	if(pageModel.nextNodeSize <= 1){
	    		 //判断是否是后备节点
	    		var node = pageModel.nextNodes;    		
	    		if(node.length > 0 && node[0].backup) {
	    			$("#selectedNodeId").val(node[0].nodeId);    		
	    			$("#user-dialog").show();
	    			//重置
	    			$("#selectUser").find("option:selected").prop("selected", false);    			
	    			$("#selectedUserIds").val("");    			
	    		}else{ 
			    	$("#myForm").attr("action","${path}/app/doaction/"+tbId+"/"+bizId+"/"+wfId);
			    	$("#myForm").submit();
	    		 } 
	    	}else{
	    		//弹出节点选择框
	    		$("#nextNodes-dialog").show();
	    	}
    	}
    }
    
    //流程办理完毕提交并带上选择的用户
    function doActionWithUsers(){
    	var userIds = $("#selectedUserIds").val();
    	var nodeId = $("#selectedNodeId").val();     	
    	if(userIds == "" || nodeId == ""){
    		$("#user-warn").show();
    	}else{
    		$("#myForm").valid();
        	//必输项的默认提示信息：
            $.validator.messages.required = "此项为必输项！";
            if(formSpecialValid()) return;
            var bizId = $("#bizId").val();
        	var wfId = $("#wfId").val();
        	var tbId = $("#tbId").val();
	    	$("#myForm").attr("action","${path}/app/doaction/"+tbId+"/"+bizId+"/"+wfId+"/"+nodeId);    	
	    	$("#myForm").submit();
    	}
    }
    
    
  	//展示流程信息
    function showWorkflow(){
    	
    }
  	
  	//指定流程节点提交
    function doFixedAction(nodeId){
    	$("#myForm").valid();
    	//必输项的默认提示信息：
        $.validator.messages.required = "此项为必输项！";
        if(formSpecialValid()) return;
        
    	//判断是否是后备节点
    	var isStop = false;
    	pageModel.nextNodes.forEach(function(node,index){    		
    		if(node.nodeId == nodeId){    			
    			if(node.backup){
    				$("#selectedNodeId").val(node.nodeId);
    				$("#user-dialog").show();
    				//重置
        			$("#selectUser").find("option:selected").attr("selected", false);
        			$("#selectedUserIds").val("");        			
        			isStop = true;
    			}    			
    		}
    	}) 
    	if(isStop) return;
    	
    	var bizId = $("#bizId").val();
    	var wfId = $("#wfId").val();
    	var tbId = $("#tbId").val();
    	$("#myForm").attr("action","${path}/app/doaction/"+tbId+"/"+bizId+"/"+wfId+"/"+nodeId);
    	$("#myForm").submit();  
    	 
    }
  
  	//流程按钮操作
  	function doButton(buttonName){
  		var bizId = $("#bizId").val();
    	var wfId = $("#wfId").val();
    	var tbId = $("#tbId").val();
  		$("#myForm").attr("action","${path}/app/dobutton/"+tbId+"/"+bizId+"/"+wfId+"/"+buttonName);
    	$("#myForm").submit();
  	}
  	
  //应答按钮弹出窗口操作
  	function showDispenseDialog(buttonName){
  		document.forms['aswForm'].hidBtnActionName.value = buttonName;
  		document.forms['aswForm'].hidActionName.value = "应答";
  		$("#answer-dialog").show();
  	}
  	
  	//应答按钮操作
  	function doDispense(){
  		var bizId = $("#bizId").val();
    	var wfId = $("#wfId").val();
    	var tbId = $("#tbId").val();
    	var buttonName = document.forms['aswForm'].hidBtnActionName.value;
  		$("#aswForm").attr("action","${ctx}/app/dobutton/"+tbId+"/"+bizId+"/"+wfId+"/"+buttonName);
    	$("#aswForm").submit();
  	}
  	
  	//行为消息窗口
  	function showButtonMsgDialog(buttonName){
  		document.forms['msgForm'].hidBtnActionName.value = buttonName;
  		document.forms['msgForm'].hidActionName.value = "行为消息";
  		$("#message-dialog").show();
  	}
  	
  	//行为消息按钮操作
  	function doButtonMsg(){
  		var bizId = $("#bizId").val();
    	var wfId = $("#wfId").val();
    	var tbId = $("#tbId").val();
    	var buttonName = document.forms['msgForm'].hidBtnActionName.value;
  		$("#msgForm").attr("action","${ctx}/app/dobutton/"+tbId+"/"+bizId+"/"+wfId+"/"+buttonName);
    	$("#msgForm").submit();
  	}
  	
  	//初始化
  	function init(){
  		$("#back").attr("onClick","location.href='${path}/app/list/"+pageModel.wftb.tbId+"/1'");
  		$("#table").html(pageModel.table);
  		$("#flowChat").html(pageModel.flowChat);
  		var nextNodes = "";  
  		if(pageModel.nextNodes != undefined){
	  		pageModel.nextNodes.forEach(function(node,index){  			
	  			nextNodes+="<label style='padding: 1px 8px 0px'>"
	  					   +"<button class='btn btn-sm btn-outline-secondary' type='button' "
	  					   +"onclick=\"doFixedAction('"+node.nodeId+"');\">"+node.nodeName
	  					   +"</button></label><br>";
	  		})
  		}
  		$("#nextNodes").html(nextNodes);
  		var buttons = "";  		
  		pageModel.buttons.forEach(function(button){
  			if(button.type == '行为'){
  				buttons += "<button class='u-btn02'  type='button' onclick=\"doButton('"+button.actionCodeName+"');\">"+button.actionName+"</button>";
  			}else if(button.type == '应答'){
  				buttons += "<button class='u-btn02'  type='button' onclick=\"showDispenseDialog('"+button.actionCodeName+"');\">"+button.actionName+"</button>";
  			}else if(button.type == '行为消息'){
  				buttons += "<button class='u-btn02'  type='button' onclick=\"showButtonMsgDialog('"+button.actionCodeName+"');\">"+button.actionName+"</button>";
  			}
  		})
  		
  		buttons += "&nbsp;";
  		if(pageModel.curNode.nodeName != null){
  			buttons += "<div class='btn-group mr-2'>";
  			buttons += "<button class='btn btn-sm btn-outline-secondary' type='button' onclick='doStorage();'>保存</button>";
  			buttons += "<button class='btn btn-sm btn-outline-secondary' type='button' onclick='doReset()'>重置</button>";
  			buttons += "</div>";
  		}
  		if(pageModel.bizId != null && pageModel.curNode.nodeName != null){
  			buttons += "<button class='btn btn-sm btn-outline-secondary' type='button' onclick='doAction();'>"+pageModel.curNode.nodeName+"完毕</button>";
  		}    
  		buttons += "<button class='btn btn-sm btn-outline-secondary' type='button' onclick=\"$('#workflow').show();\">流程查看</button>";
  		$("#buttons").html(buttons);
  	}
  	$(function(){
  		
  		init();
  		
	  	//获取选中的用户
	  	$("#selectUser").click(function(){
	  		$("#user-warn").hide();
	  		if($("#selectUser option:selected").length>0){
	  			var selUserId = "";
	  			$("#selectUser option:selected").each(function(){
	  				selUserId += $(this).val()+",";
	  			})	  			
	  			$("#selectedUserIds").val(selUserId);
	  		}	
	  	})
  	})
</script>
 	
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">
  	<label id="back" style="cursor:pointer;color:#dc3545;">⇦</label>
  	✍  	
  </h1>
  <!-- 按钮展示 -->
  <div id="buttons" class="btn-toolbar mb-2 mb-md-0"></div>
</div> 	
	
<form id="myForm" method="post" action="">
<input type="hidden" id="selectedUserIds" name="selectedUserIds"/>
<input type="hidden" id="selectedNodeId" name="selectedNodeId"/>
<!-- 表单展示 -->
<div id="table" style="padding-bottom:20px;"></div>
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
	<%-- 流程图 --%>
	<div id="flowChat"></div>
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
	<div id="nextNodes">		
		<!-- 续办节点 -->
	</div>
</div>

<!-- 弹出人员选择框 -->
<div id="user-dialog" style="box-shadow: 0px 2px 2px #2ef90a;position: absolute;top:12%;z-index: 999;background:white;right:20px;display:none;width:20%;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 1px 8px 0px;font-size:12px;width:100%;" >
	 	<div style="float:left">续办人员选择</div>
	 	<div style="padding-left:20%;float:right;"><span style="cursor:pointer;background:#b0e0b0;" onclick="doActionWithUsers()">确定</span></div>
	 	<div style="padding-left:20%;float:right"><span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#user-dialog').hide();">×</span></div> 	
	 </div>       	      	    
   	</header>
   	<div id="user-warn" style="display:none;color:red;font-size:14px;text-align:center;margin-top:3px;">续办人员未选择!</div>
   	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>   	 	
	<div style="width:90%;height:90%;float:left;font-size:.9rem;"> 
   		<select style="width:100%;height:100%;" multiple id="selectUser">
    		<option value="1">系统管理员</option>
    		<option value="2">系统管理员2</option>
    	</select>    	    	
   	</div>
</div>

<!-- 图章选择 -->
<div id="stamp-dialog" style="box-shadow: 0px 2px 2px #2ef90a;position: absolute;top:30%;z-index: 999;background:white;left:30%;display:none;width:50%;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 1px 8px 0px;font-size:12px;width:100%;" >
	 	<div style="float:left">图章选择</div>	 	
	 	<div style="padding-left:20%;float:right"><span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#stamp-dialog').hide();">×</span></div> 	
	 </div>       	      	    
   	</header>   
   	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>   	 	
	<div style="width:100%;height:250px;float:center;padding-left:5%;font-size:.9rem;overflow-y:auto;"> 
   		  <img src="/img/stamp.png"/>
   		  <input type="radio" id="stamp" name="stamp" value='/img/stamp.png|表单工作流平台' onclick="selectedStamp(this,'tb_reception_f_stamp','tb_reception_stampurl');" value="/img/stamp.png" /><br/>   		     	
   	</div>
</div>

<!-- 消息类窗口 -->
<div id="answer-dialog" style="box-shadow: 0px 2px 2px #1b91bf;position: absolute;top:6%;z-index: 999;background:white;right:10%;display:none;width:31%;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 1px 8px 0px;font-size:12px;width:100%;" >
	 	<div style="float:left">传阅窗口</div>
	 	<div style="padding-left:20%;float:right;"><span style="cursor:pointer;background:#b0e0b0;" onclick="doAnswer()">确定</span></div>
	 	<div style="padding-left:20%;float:right"><span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#message-dialog').hide();">×</span></div> 	
	 </div>       	      	    
   	</header>   
   	<hr style="margin-top: 1.5rem; margin-bottom: .5rem;"></hr>   	 	
	<div id="opinionLst" style="width:100%;height:290px;padding-top:20px;float:center;padding-left:5%;padding-right:5%;font-size:.9rem;overflow-y:auto;"> 
		<form id="aswForm" >
			<label>传阅内容</label>
			<textarea id="content" name="content" rows=5 cols=45></textarea>
			<label>接收人</label>
			<textarea id="receiveUserNames" name="receiveUserNames" rows=3 cols=38></textarea>
			<input type="hidden" id="receiveUserNamesIds" name="receiveUserNamesIds">
			<input type="hidden" id="hidBtnActionName" name="hidBtnActionName" value="">
			<input type="hidden" name="hidActionName" value="">	
			<span style="cursor:pointer;" onclick="getUser('receiveUserNames');">✍</span>
		</form>   		   		     
   	</div>
</div>

<!-- 行为消息类窗口 -->
<div id="message-dialog" style="box-shadow: 0px 2px 2px #1b91bf;position: absolute;top:10%;z-index: 999;background:white;right:10%;display:none;width:25%;">
	<header>
	 <div class="form-inline mt-2 mt-md-0" style="padding: 1px 8px 0px;font-size:12px;width:100%;" >
	 	<div style="float:left">行为消息类窗口</div>
	 	<div style="padding-left:20%;float:right;">
	 		<span style="cursor:pointer;background:#b0e0b0;" onclick="doButtonMsg()">确定</span>
	 		<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#message-dialog').hide();">×</span>
	 	</div> 	
	 </div>       	      	    
   	</header>   
   	<hr style="margin-top: 1.5rem; margin-bottom: .5rem;"></hr>   	 	
	<div id="opinionLst" style="width:100%;height:215px;padding-top:20px;float:center;font-size:.9rem;overflow-y:auto;"> 
		<form id="msgForm" method="post">
			<p><label>行为消息</label></p>
			<p><textarea id="content" name="content" rows=5 style="width:78%"></textarea></p>			
			<input type="hidden" name="hidBtnActionName" value="">
			<input type="hidden" name="hidActionName" value="">			
			<input type="hidden" name="_csrf" value="${_csrf.token}"/>
		</form>   		   		     
   	</div>
</div>

<!-- 行为类窗口 -->
<div id="button-dialog" style="box-shadow: 0px 2px 2px #1b91bf;position: absolute;top:10%;z-index: 999;background:white;right:10%;display:none;width:25%;">
	<form id="btnForm" method="post">				
		<input type="hidden" name="hidBtnActionName" value="">
		<input type="hidden" name="hidActionName" value="">			
		<input type="hidden" name="_csrf" value="${_csrf.token}"/>
	</form>   		   		     
   
</div>