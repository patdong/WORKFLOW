<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>  
  //页面设置全局变量
  var gscope = "${scope}";
  var gfieldsetting = "${fieldsetting}";
  $( function() {
	  //初始化元素窗口默认为隐藏
	  $('#tb-span').removeClass('span-btn').addClass('span-highlight-btn');
	  $('#ems-span').removeClass('span-highlight-btn').addClass('span-btn');
	  $('#tbems-body').show();
	  $('#ems-body').hide();
	  $('#ems-foot').hide();	  
	  $('#ems-dialog').show();
	  $('#ems-div').css('height','80%');
	  $('#btn-open').hide(); 
	  $('#btn-close').show();
	  $("#element-div").draggable();
	  $("#ems-div").draggable();
	  $("#lst-div").draggable();
	  $("#scheme-div").draggable();
	  $("#tbname-dialog").draggable();
	  $("#confirm-dialog").dialog();
	  $('#confirm-dialog').dialog('close');
	  $("#formula-div").draggable();
	  //设置表单位置的radiobox值
	  var $scope = $('input:radio[name=scope]');	 
	  $scope.filter('[value=${scope}]').prop('checked', true);
	  //设置风格默认值
	  $("#style").val("${style}");
	  //设置字段设置按钮不可见
	  $(".btn-edit-pointer").hide();	  
	 
	  //设置布局信息	  
	  $("#layout").text("${layout}");
	  $("#tscope").val("${scope}");
	  //设置模板
	  $("#template").val("${brief.template}");
	  changeTemplate();
	  //下拉框选择操作 - 角色选择
	  $('#subTbId-ul li').on('click', function(){
	    $('#subTbId-btn').text($(this).text());    	
	    $('#subTbId').val($(this).attr("value"));    	    	
	  });
	  redraw();
  });
  
  //保存多个元素设置
  function saveElements(){	 
	  var checkedIds = [];
	  if($("#ems-body").is(":visible")){
	    	//统计选中的记录
    	  $('input:checkbox[name=orgemId]:checked').each(function(k){ 
    		  checkedIds.push($(this).val());	    		   	
    	  })
		  $.ajax({
			  type: 'GET',
			  url: "${path}/tb/savecheckedelements/${tbId}",
			  data:{checkedIds:checkedIds,scope:gscope},
			  dataType: 'json',
			  success: function(data){
				  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
			  },
			  error: function(XMLHttpRequest, textStatus, errorThrown){
				  console.warn(XMLHttpRequest.responseText);		  
			  }
		});
	  }
  }
  
  //保存单个元素的设置
  function saveElement(){
	  $('#myForm').valid();
	  $.validator.messages.required = "此项为必输项！";
	  $.validator.messages.number = "请输入数字！";	  
	  $('#myForm').attr('action','${path}/tb/saveElement/${tbId}');
	  $('#myForm').submit();
  } 
  
  //元素上移一位
  function moveUp(id){
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/moveup/${tbId}/"+id+"/"+gscope,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //元素下移一位
  function moveDown(id){
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/movedown/${tbId}/"+id+"/"+gscope,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
	
  //根据选中的不同位置刷新表单元素
  function fresh(scope){
	  gscope = scope;
	  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
  }
  
  //删除表单中的元素
  function remove(id){
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/remove/${tbId}/"+id,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
   //弹出元素信息操作菜单
  function showPos(event,id) {
	  $('#ems-div').hide();
	  var el, x, y;
	  el = document.getElementById('element-div');	  
	  
	  if (window.event) {
		  x = window.event.clientX + document.documentElement.scrollLeft + document.body.scrollLeft;
		  y = window.event.clientY + document.documentElement.scrollTop + document.body.scrollTop;
	  }
	  else {
		  x = event.clientX + window.scrollX;
		  y = event.clientY + window.scrollY;
	  }
	  x -= 550; 
	  
	  if(y>200) y = 60;
	  
	  //el.style.left = //x + "px";
	  el.style.right = "5px";
	  el.style.top = "14%";//y + "px";
	  el.style.display = "block";
	  
	  if(id != "") {
		  $("#id").val(id);			 
		  var ems = ${tbems};
		  $.each(ems,function(key,element){
			  if(element.id == id){
				  setElement(element);				  
				  $("#saveEm").html('保存');
			  }
		  });
	  }else{
		  $("#myForm")[0].reset();
		  $("#element-name").text('');		  
		  $("#saveEm").html('新增');
		  $("#id").val(""); 
	  }
  }
  
   //内部方法，完成字段赋值功能
  var newFieldDataType = "String";
  function setElement(element){
	  //清空校验的异常信息
	  var validator = $("#myForm").validate();
	  validator.resetForm();
	  $("#id").val(element.id); 
	  $("#element-name").text(element.newLabelName);	  
	  $("#newLabelName").val(element.newLabelName);	  
	  $("#position").val(element.position);
	  $("#newFieldName").val(element.newFieldName);
	  $("#newUnit").val(element.newUnit);	  
	  $("#newFieldDataType").val(element.newFieldDataType);
	  $("#newFieldType").val(element.newFieldType);
	  $("#rowes").val(element.rowes);
	  $("#cols").val(element.cols);
	  $("#width").val(element.width);
	  $("#newFunctionName").val(element.newFunctionName);
	  $("input[name=functionBelongTo][value=" + element.functionBelongTo + "]").attr('checked', 'checked');
	  $("#newHiddenFieldName").val(element.newHiddenFieldName);
	  $("#newLength").val(element.newLength);
	  $("#newDataContent").val(element.newDataContent);
	  $("#formula").val(element.formula);
	  $("#defaultValue").val(element.defaultValue);
	  $("#defaultValueFrom").val(element.defaultValueFrom);
	  newFieldDataType = element.newFieldDataType;	  
	  changeNewFieldType();
   }
   
  //设置表单名称
  function setTableName(event){
		 if (event.keyCode == 13) {	
	        var tableName = $("#tableName").val();
	        var tbId = $("#tbId").val();
	        if($.trim(tableName) != ""){
	        	$.ajax({
	      		  type: 'GET',
	      		  url: "${path}/tb/setTableName/"+tbId,
	      		  data: {tableName:tableName},			  
	      		  dataType: 'json',
	      		  success: function(data){
	      			  if(!data){
	      				  alert("设置不成功!");
	      			      $("#tableName").val("${brief.tableName}");
	      			  }
	      		  },
	      		  error: function(XMLHttpRequest, textStatus, errorThrown){
	      			  console.warn(XMLHttpRequest.responseText);			  
	      		  }
	      	});
	        }
	    }
	}
  
  //表单列数设置，默认必须是2列。
  function saveLayout(){	  		  
	  var headCols=$("#headCols").val();
	  var headBorder = $("#headBorder").val();
	  if (!/^[1-9][0-9]*$/.test(headCols)) {
		  headCols="";
	  }		
	  $("#headCols").val(headCols);
	  var bodyCols=$("#bodyCols").val();
	  var bodyBorder = $("#bodyBorder").val();
	  if (!/^[1-9][0-9]*$/.test(bodyCols)) {
		  bodyCols="2";
	  }		
	  $("#bodyCols").val(bodyCols);
	  var footCols=$("#footCols").val();
	  var footBorder = $("#footBorder").val();
	  if (!/^[1-9][0-9]*$/.test(footCols)) {
		  footCols="";
	  }		
	  $("#footCols").val(footCols);
	  
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/savelayout/${tbId}",
		  data:{headCols:headCols,
			    bodyCols:bodyCols,
			    footCols:footCols,
			    headBorder:headBorder,
			    bodyBorder:bodyBorder,
			    footBorder:footBorder
			   },
		  dataType: 'json',
		  success: function(data){
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});		  
	
  }
  
  //显示设置字段页面
  function showFieldSetting(){
	  //设置字段设置按钮高亮
	  if(gfieldsetting == "yes") gfieldsetting = "no";
	  else gfieldsetting = "yes";	  
	  redraw();
  }
  
  //设置表单显示的字段
  function setList(){
	  var checkedIds = [];	  
      //统计选中的记录
   	  $('input:checkbox[name=lstId]:checked').each(function(k){ 
   		  checkedIds.push($(this).val());	    		   	
   	  })
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/setlist/${tbId}",
		  data:{checkedIds:checkedIds},
		  dataType: 'json',
		  success: function(data){
			  $("#lst-div").hide();
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //生成库表数据
  function createTable(){
	  //表单验证
	  $("#myForm1").valid();
	  var tbName = $("#tbName").val();	  
	  if("${brief.name}" == "" && tbName == ""){		  
		  $('#tbname-dialog').show();		  
	  }else{		  
		  $.ajax({
			  type: 'GET',
			  url: "${path}/tb/createtable/${tbId}",
			  data:{tbName:tbName},
			  dataType: 'json',
			  success: function(data){
				  if(data.code == '1'){					
					  $('#tbname-dialog').hide();	
					  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
				  }
				  if(data.code == '0'){
					  $('#alert-dialog').show();
					  $("#alert-msg").text(data.message+"请联系管理员.");
					  $("#tbName").val("");
				  }
			  },
			  error: function(XMLHttpRequest, textStatus, errorThrown){
				  console.warn(XMLHttpRequest.responseText);		  
			  }
			});		 
	  }
  }
  
  //表重命名
  function renameTable(){
	  $('#tbname-dialog').show();
  }
  
   //删除表确认
  function dropTableConfirm(){
	  $('#confirm-dialog').dialog('open');
  }
  //删除表
  function dropTable(){
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/droptable/${tbId}",			  
		  dataType: 'json',
		  success: function(data){
			  if(data.code == '1'){				  
				  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
			  }
			  if(data.code == '0'){
				  $('#alert-dialog').show();
				  $("#alert-msg").text(data.message+"请联系管理员.");				  
			  }	  
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);			  
		  }
	});  
  }
  //库表检测
  function checkTableScheme(){		  
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/getTableScheme/${tbId}",		  			 
		  dataType: 'json',
		  success: function(data){
			  //判重
			  var duplicateEle = [];
			  for(var i=0;i<data.length;i++){
				  var count=0;
				  var exist = false
				  for(var j=0;j<data.length;j++){
					  if(data[i].newFieldName == data[j].newFieldName) {
						  count++;						  
						  for(var k=0;k<duplicateEle.length;k++){
							  if(duplicateEle[k] == data[i].newFieldName) exist = true;
						  }
					  }
				  }
				  if(count > 1){					  
					  if(!exist) duplicateEle.push(data[i].newFieldName);
				  }
			  }			  
			  var li;
			  var alarm = false;
			  $("#scheme-lst").empty();
			  $.each(data,function (index, element) {
			      li = "<tr>";
			      if(element.newFieldName==null){
			      	li += "<td><span style='color:red;font-weight:bold;'>❗<span></td>"; 	
			      	alarm = true;
			      }else{
			    	  //判重
			    	  var duplicate = false;			    	  
			    	  for(var i=0;i<duplicateEle.length;i++){			    		  
			    		  if(element.newFieldName == duplicateEle[i]) duplicate = true;
			    	  }
			    	  if(duplicate) {
			    		  li += "<td><span style='color:red;font-weight:bold;'>❗<span>"+element.newFieldName+"</td>";
			    		  alarm = true;
			    	  }
			    	  else li += "<td>"+element.newFieldName+"</td>"
			      }
			      if(element.newLabelName==null){
			      	li += "<td><span style='color:red;'>❗<span></td>"; 
			      	alarm = true;
			      }else{
			    	  li += "<td>"+element.newLabelName+"</td>"
			      }
			      if(element.newFieldDataType==null){
			      	li += "<td><span style='color:red;'>❗<span></td>";
			      	alarm = true;
			      }else{
			    	  li += "<td>"+element.newFieldDataType+"</td>"
			      }
			      if(element.newLength==null ){
			    	  if(element.newFieldDataType == "String"){
				      	li += "<td><span style='color:red;'>❗<span></td>";
				      	alarm = true;
			      	  }
			      }else{
			    	  li += "<td>"+element.newLength+"</td>"
			      }
			       
			      li += "</tr>";
				        
				  $("#scheme-lst").append(li);				 
			  });
			  if(!alarm) $("#scheme-msg").text("库表检测 - 检测成功.");
			  else $("#scheme-msg").text("库表检测 - 检测失败,请关注标红列!");
			  $("#scheme-div").show();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }

  //切换表单的展示范围
  function changeScope(){
	  scope = $("#tscope").val();
	  location.href="${path}/tb/tabledefination/${tbId}?scope="+scope+"&fieldsetting="+gfieldsetting;
  }
  
  //设置子表
  function setSubTable(){      
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/setSubTable/${tbId}/"+$("#subTbId").val()+"/"+$("#tscope").val(),
		  dataType: 'json',
		  success: function(data){
			  $("#subTable-div").hide();
			  location.href="${path}/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	}); 
  }
  
  //重新绘制表单
  function redraw(){
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/redraw/${tbId}/"+$("#tscope").val()+"/"+gfieldsetting,
		  dataType: 'json',
		  success: function(data){
			  $("#table-container").empty();
			  $("#table-container").html(data.message);
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	}); 
  }
  //预览
  function review(){	  
	  $.ajax({
		  type: 'GET',
		  url: "${path}/tb/review/${tbId}",
		  dataType: 'json',
		  success: function(data){			  
			  $("#table-reviewer").empty();
			  $("#table-reviewer").html(data.message);
			  $("#review-div").show();
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	}); 
  }
  
  //修改模板
  function changeTemplate(){
	  var template = $("#template").val();
      if($.trim(template) != ""){
      	$.ajax({
    		  type: 'GET',
    		  url: "${path}/tb/setTemplate/${tbId}",
    		  data: {template:template},			  
    		  dataType: 'json',
    		  success: function(data){	 
    			  if(data){
    				  if(template=="组件"){
    					  $('#db1').hide();
    					  $('#db2').hide();
    					  $('#dbcheck').hide();
    				  }else{
    					  $('#db1').show();
    					  $('#db2').show();
    					  $('#dbcheck').show();
    				  }
    			  }
    		  },
    		  error: function(XMLHttpRequest, textStatus, errorThrown){
    			  console.warn(XMLHttpRequest.responseText);			  
    		  }
    	});
  		}
  }
  
  //元素类型切换
  function changeNewFieldType(){
	  var newFieldType = $("#newFieldType").val();
	  if(newFieldType == '组件' || newFieldType == '子表' || newFieldType == '标签'){
		  $("#newFieldName").removeAttr("required");
	  }else{
		  $("#newFieldName").attr("required","true");
	  }
	  if(newFieldType == '组件' || newFieldType == '子表'){
		  $.ajax({
    		  type: 'GET',
    		  url: "${path}/tb/getPlugIns",
    		  data: {newFieldType:newFieldType},			  
    		  dataType: 'json',
    		  success: function(data){	 
    			  $("#newFieldDataType").empty();    			  
    			  $.each(data,function (index, brief) {
    				  var selected = "";
    				  if(newFieldDataType == brief.tableName) selected = "selected";
    				  $("#newFieldDataType").append("<option "+selected+" value='"+brief.tbId+","+brief.tableName+"'>"+brief.tableName+"</option>");
    			  })    			  
    		  },
    		  error: function(XMLHttpRequest, textStatus, errorThrown){
    			  console.warn(XMLHttpRequest.responseText);			  
    		  }
    	});
		  
	  }else{		   
		  $("#newFieldDataType").empty();
		  $("#newFieldDataType").append("<option value='String'>字符串</option>");
		  $("#newFieldDataType").append("<option value='Date'>日期</option>");
		  $("#newFieldDataType").append("<option value='DateTime'>时间日期</option>");
		  $("#newFieldDataType").append("<option value='Number'>数字</option>");
		  $("#newFieldDataType").append("<option value='Money'>金额</option>");		  
		  $("#newFieldDataType").val(newFieldDataType);
	  }
  }
  //设置计算公式 
  var exprs = [,,];
  function setFormula(item,position){
	  //重置及初始化	 
	  const formuladata = $("#formuladata").val();
	  if(formuladata == "") exprs = [,,];
	  else{
		  const regular = /(\S+)([*|/|+|-])(\S+)/;		
		  const matchObj = regular.exec(formuladata);
		  if(matchObj != null){
			  exprs[0] = matchObj[1];
			  exprs[1] = matchObj[2];
			  exprs[2] = matchObj[3];
		  }
	  }
	  
	  switch(position){
	  case 1:
		  if(exprs[0] == null) exprs[0] = item.value;
		  if(exprs[1] == null && exprs[2] == null){			  
			  exprs[0] = item.value;
		  }
		  if(exprs[1] != null && exprs[2] == null){			  
			  exprs[2] = item.value;
		  }			
		break;
	  case 2:
		  if(exprs[0] != null && exprs[1] == null) exprs[1] = item.value;
		break;
	  case 3:		  
		  if(exprs[0] != null && (exprs[1] == null || exprs[2] == null)) {
			  exprs[1] = item.value.substring(0,1);
			  exprs[2] = item.value.substring(1);
		  }
		break;
	  }
	  var express = "";
	  for(var i=0;i<exprs.length;i++){
		  if(exprs[i] == null) exprs[i] = "";
		  express += elem[i];
	  }
	  $("#formuladata").val(express);	  	  
  }
</script>
<c:set var="cols" value="${style}"/>
<div style="padding:0em;margin:0px;padding-top: 1.4%;">
  	<div style="background:#f8f9fa;">
  		<span class="mt-5" style="font-size: 1.35rem;margin-left:2%">表单定义 </span>
  		<span class="small-btn" style="background:#42a288;font-weight:bold;color: #ffc107;" onclick="location.href='${path}/tb/tablecenter'">&nbsp;⬅&nbsp;</span>  		  		
  		<span style="margin-left:2%;"><span style="color:#71d2f1">❒</span>模板&nbsp;<select style="font-size:.78rem;" id="template" onchange="changeTemplate();"><option value="表">表</option><option value="子表">子表</option><option value="组件">组件</option></select></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="$('#setting-div').show();"><span style="color:#0c8e2a;">❆</span>布局 </span> 
  		<span style="margin-left:0.3%;">|</span>   		
  		<span style="margin-left:0.3%;"><select style="font-size:.78rem;" id="tscope" onchange="changeScope();"><option value="表头">表头</option><option value="表体">表体</option><option value="表尾">表尾</option></select></span>  		  		
  		<span style="margin-left:0.3%;font-size:.78rem;" id="layout"></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="showFieldSetting();" id="fieldsetting"><span style="color: #90790a;">⤧</span>编辑</span>
  		<span style="margin-left:0.3%;">|</span>   		
  		<c:if test="${empty brief.name }" > 		
			<span id="db1" style="cursor:pointer;" title="生成库表数据" onclick="createTable();"><img src="${path}/img/wf_btn12.PNG"></span>
		</c:if>
		<c:if test="${!empty brief.name }" > 		
			<span id="db2" style="cursor:pointer;" title="重构库表数据" onclick="createTable();"><img src="${path}/img/wf_btn13.PNG"></span>
			<span style="cursor:pointer;" title="重命名" onclick="renameTable();"><img src="${path}/img/wf_btn18.PNG"></span>
			<span style="cursor:pointer;" title="删除表" onclick="dropTableConfirm();"><img src="${path}/img/wf_btn19.PNG"></span>
		</c:if> 
		<span id="dbcheck" style="cursor:pointer;" title="库表检测" onclick="checkTableScheme();"><img src="${path}/img/wf_btn15.PNG"></span>		
  		<span style="margin-left:0.3%;">|</span> 
  		<div style="float: right; margin-right: 24%;margin-top: 8px;" > 
  			<span style="cursor:pointer;" onclick="$('#brief-div').show();"><span style="font-weight:bold;color:RED">➿</span> |</span> 				  		
	  		<span style="cursor:pointer;" onclick="review();"><span style="font-weight:bold;color:#152505;">⇱</span>预览 |</span>		  		
	  		<span style="cursor:pointer;" onclick="$('#lst-div').show();"><span style="font-weight:bold;color:#152505;">✋</span>列表</span>	  		
  		</div>
  		<div style="float: right; margin-right: -24%;margin-top: 8px;" >
  			<span id="emstool" style="cursor:pointer;" onclick="$('#ems-div').show();"><span style="font-size:18px;font-weight:bold;">😎</span>元素</span>
  		</div>  		  		   		   
  	</div>
  	<div class="line-bottom" ></div>
</div>	
<div style="padding-top:0px;" id="table-body">
<c:set var="cols" value="${style}"/>
<div class="line-table" ></div>
<div id="table-div" class="draw" style="padding-left:1%;bottom:5%;height:78%;top:16%;background-image: url('/img/wf_btn11.PNG'); background-repeat: repeat;">		
	<!-- 表单标题 -->	
	<div style="text-align:center;margin-left:10%;margin-right:10%">
		<label for="tableName" class="sr-only">表单名称</label>				
       	<input type="text" name="tableName" id="tableName" class="form-control" placeholder="表单名称"  style="text-align:center;font-weight:bold;font-size:1.5rem;" value="${brief.tableName }" onkeypress="setTableName(event);" />
	</div>
	<!-- 展示表单选中范围信息 -->
	<div style="text-align:center;margin-top:3%;margin-left:2%;margin-right:2%">
		<div class="table-container" style="margin-left:0px" id="table-container">
			${table}			 		
		</div>		
	</div>
</div>
</div>
<input type="hidden" id="tbId" name="tbId" value="${tbId}"/>
<div id="ems-div" class="ems-mask opacity" style="background-color: #f8f9fa;box-shadow: 1px 6px 4px #d6720f;height:80%;top:14%;" >
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >           
           <span class="span-highlight-btn" onclick="$('#tb-span').removeClass('span-btn').addClass('span-highlight-btn');$('#ems-span').removeClass('span-highlight-btn').addClass('span-btn');$('#tbems-body').show();$('#ems-body').hide();;$('#ems-foot').hide();" id="tb-span">表单元素</span>           
           <span class="span-btn" onclick="$('#ems-span').removeClass('span-btn').addClass('span-highlight-btn');$('#tb-span').removeClass('span-highlight-btn').addClass('span-btn');$('#ems-body').show();$('#tbems-body').hide();$('#ems-foot').show();" id="ems-span">元素集</span>           
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span id="btn-close" class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;display:none;" onclick="$('#btn-open').show(); $('#btn-close').hide(); $('#ems-dialog').hide();$('#ems-div').css('height','6.5%');$('#ems-div').hide();$('#emstool').show();">×</span>
         	<span id="btn-open" class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#btn-open').hide(); $('#btn-close').show(); $('#ems-dialog').show();$('#ems-div').css('height','80%');">√</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;border-top: 1px solid #0c4219;"></hr>    
    <div id="ems-dialog" style="height:77%">
	    <div style="margin-left:5px;padding-left:1px;" >
		    <input type="radio" id="scope" name="scope" value="表头" onclick="fresh('表头');">&nbsp;表头
		    <input type="radio" id="scope" name="scope" value="表体" checked onclick="fresh('表体');">&nbsp;表体
		    <input type="radio" id="scope" name="scope" value="表尾" onclick="fresh('表尾');">&nbsp;表尾 		    
	    </div>
	
		<hr style="margin-top: .1rem; border-top: 1px solid #0c4219;margin-bottom:.1px;"></hr> 
	    <div style="padding: 0px 13px 0px;height: 100%;overflow-y: auto;" id="ems-body">
			<form id="emsForm" class="navbar-form navbar-left" method="get" action="">			
				<c:forEach items="${emList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.1rem;"><input type="checkbox" id="orgemId" name="orgemId" value="${element.emId}" ><span style="font-size:0.8rem">&nbsp;${element.labelName}</span></p>
				</c:forEach>			        			  
		   </form>	  	
		</div>			 
		<div style="padding: 0px 13px 0px;height: 100%;overflow-y: auto;" id="tbems-body" >
			<form id="tbemsForm" class="navbar-form navbar-left" method="get" action="">
				<c:forEach items="${tbemList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.1rem;text-align:left">
						<span style="font-size:0.8rem">&nbsp;${element.newLabelName}(${element.newFieldType})</span>				
						<span style="float:right;">
							<span id="btn-up" class="btn-pointer" onclick="moveUp(${element.id});" title="上移">⬆</span>
							<span id="btn-down" class="btn-pointer" onclick="moveDown(${element.id});" title="下移">⬇</span>
							<span id="btn-up" class="btn-del-pointer" style="padding-top: 4px" onclick="remove(${element.id});" title="删除">✖</span>
						</span>					
					</p>
				</c:forEach>			     			   
		   </form>
		</div>		
		<div id="ems-foot">	
			<hr style="margin-top: .1rem;margin-bottom: .5rem"></hr>
			<div style="margin-bottom:1px;margin-top:1px;margin-left:5px;">
		    	<button class="btn btn-lg btn-primary-dialog buttom-smaller"  onclick="saveElements();">保存</button>
		    </div>
	    </div> 	
	 </div>    
</div>
<!-- 列表设置 -->
<div id="lst-div" class="ems-mask opacity" style="display:none;box-shadow: 1px 6px 4px #d6720f;top:14%;right:21%" >
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >           
           列表元素选择          
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span id="btn-close" class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#lst-div').hide();">×</span>         	
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;border-top: 1px solid #0c4219;"></hr>    
    <div>	    
	    <div style="padding: 0px 13px 0px;height: 300px;overflow-y: auto;" >
			<form id="lstForm" class="navbar-form navbar-left" method="get" action="">			
				<c:forEach items="${tbList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.1rem;"><input type="checkbox" id="lstId" name="lstId" value="${element.id}" <c:if test="${element.list eq '有效' }">checked</c:if>><span style="font-size:0.8rem">&nbsp;${element.newLabelName}</span></p>
				</c:forEach>			        			  
		   </form>	  	
		</div>			 			
		<div id="lst-foot">	
			<hr></hr>
			<div style="margin-bottom:10px;margin-top:10px;margin-left:5px;">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;padding-left:1px;" onclick="setList();">保存</button>
		    </div>
	    </div> 	
	</div>    
</div>


<!-- 元素定义窗口 -->
<div id="element-div" class="node-mask opacity" style="display:none;height:80%;width:22%;">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>元素-[<span id="element-name" style="font-weight:bold;"></span>]设置</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#element-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem;margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;font-size:.8rem;">
		<form id="myForm" class="" method="post" modelAttribute="element" action="">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="id" name="id" value="">
			<input type="hidden" id="escope" name="escope" value="${scope}">			
	  		<div class="form-group-dialog" style="margin-bottom: .2rem;">	  			  			       
		        <label >元素名称:</label>
		        <input name="newLabelName" id="newLabelName" class="form-control-one-line mx-sm-2 form-plugin-smaller" required autofocus style="width:40%"/>
		        <label >位置:</label>
		        <select name="position" id="position" class="form-control-one-line form-plugin-smaller" style="width:14%" >
		        	<option value="左">左</option>
		        	<option value="中">中</option>
		        	<option value="右">右</option>		        	
		        </select>		        		       
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">
		    	<label >字段名称:</label>
		        <input name="newFieldName" id="newFieldName" class="form-control-one-line mx-sm-2  form-plugin-smaller" required style="width:40%"/>
		        <label >单位:</label>
		        <input name="newUnit" id="newUnit" class="form-control-one-line form-plugin-smaller" style="width:14%" />
		    </div>		    		  
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">      
		        <label >元素模型:</label>
			    <select name="newFieldType" id="newFieldType" class="form-control-one-line mx-sm-2 form-plugin-smaller" required style="width:70%" onChange="changeNewFieldType();">
			        <option>输入框</option>
			        <option>下拉框</option>
			        <option>单选框</option>
			        <option>多选框</option>
			        <option>文本框</option>
			        <option>日期</option>
			        <option>文件</option>
			        <option>图片</option>
			        <option>审批意见</option>
			        <option>标签</option>
			        <option>组件</option>
			        <option>子表</option>
			    </select>			        		       	        		       
		    </div>
		 	<div class="form-group-dialog" style="margin-bottom: .2rem;">	    		       
		        <label>字段类型:</label>
		        <select name="newFieldDataType" id="newFieldDataType" class="form-control-one-line mx-sm-2 form-plugin-smaller" style="width:70%" > 
		        	<option value="String">字符串</option>
			        <option value="Date">日期</option>
			        <option value="DateTime">时间日期</option>
			        <option value="Number">数字</option>       
		        </select> 
		    </div>
			<div class="form-group-dialog" style="margin-bottom: .2rem;">				
			    <label >数据长度:</label>
		        <input name="newLength" id="newLength" class="form-control-one-line mx-sm-2 form-plugin-smaller number"  style="width:23%" />		    
		        <label >显示宽度:</label>
		        <input name="width" id="width" class="form-control-one-line form-plugin-smaller number" style="width:22%" />		    
			</div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">		        
		        <label >元素跨行:</label>
		        <input name="rowes" id="rowes" class="form-control-one-line mx-sm-2 form-plugin-smaller number" value="1" style="width:23%" />
		    	    		        
		        <label >元素跨列:</label>
		        <input name="cols" id="cols" class="form-control-one-line form-plugin-smaller number" value="1" style="width:22%"/>			        		       
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">
		    	<label >隐式字段:</label>
		        <input name="newHiddenFieldName" id="newHiddenFieldName" class="form-control-one-line mx-sm-2 form-plugin-smaller" style="width:70%" />
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">
		        <label >字段初值:</label>
		        <input name="defaultValue" id="defaultValue" class="form-control-one-line mx-sm-2 form-plugin-smaller" style="width:70%"/>		        		        	        			        		      
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">		        
		        <label >初值来源:</label>
		        <input name="defaultValueFrom" id="defaultValueFrom" class="form-control-one-line  mx-sm-2 form-plugin-smaller" style="width:70%"/>			        		       
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">	        
		        <label >事件名称:</label>
		        <input name="newFunctionName" id="newFunctionName" class="form-control-one-line  mx-sm-2 form-plugin-smaller" style="width:70%"/>	
		        <!-- <input name="functionBelongTo" id="functionBelongTo" type="radio" value="标签">标签	-->
		        <input type="hidden" name="functionBelongTo" id="functionBelongTo" value="元素">        		       
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">		        
		        <label >级联信息:</label>
		        <input name="newDataContent" id="newDataContent" class="form-control-one-line  mx-sm-2 form-plugin-smaller" style="width:70%"/>			        		       
		    </div>
		    <div class="form-group-dialog" style="margin-bottom: .2rem;">		        
		        <label >计算公式:</label>
		        <input name="formula" id="formula" class="form-control-one-line  mx-sm-2 form-plugin-smaller" style="width:60%" readOnly/>
		        <span style="cursor:pointer;" onclick="$('#formula-div').show();$('#formuladata').val($('#formula').val());">📌</span>			        		       
		    </div>		    
		    <hr style="margin-top: .5rem;margin-bottom: .5rem;"></hr>	         
	   </form>
	   <div style="margin-bottom:10px;margin-top:10px;">
	   	   <button class="btn btn-lg btn-primary-dialog buttom-smaller"  id="saveEm" onClick="saveElement();">保存</button>
	   </div> 	 	   
	</div>    
</div>
<!-- talbe scheme检测 -->
<div id="scheme-div" class="mask opacity" style="display:none;height:66%;left:20%;top:15%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">库表检测</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#scheme-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:80%" >
    <table class="table table-striped table-sm">
    <thead>
      <tr>
        <th>字段名称</th> 
        <th>字段描述</th>
        <th>字段类型</th>
        <th>字段长度</th>
      </tr>
    </thead>
    <tbody id="scheme-lst">
    </tbody>
    </table>
    </div>
</div>
<!-- 表单库表名称录入窗口 -->
<div id="tbname-dialog" class="mask opacity" style="display:none;height:25%;width:20%;left:20%;top:15%">
   <header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">表单名称录入</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#tbname-dialog').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:80%" >
    <form id="myForm1">
    <input type="text" id="tbName" name="tbName" class="form-control-one-line Text" placeholder="表单名称录入" >
    </form>  
    <br><br>
    <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void();"  onclick="createTable();">
		        <span aria-hidden="true">确认</span>		        
		      </a>
	      </div>
	    </li>	    
	</ul>
    </nav>
    </div>    
</div>

<!-- 消息互动窗口 -->
<div id="alert-dialog" class="mask opacity" style="display:none;height:25%;width:20%;left:20%;top:15%">
   <header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">消息窗口</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#alert-dialog').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:80%" >
    <span id="alert-msg"></span>
    <br><br>
    <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void();"  onclick="$('#alert-dialog').hide();">
		        <span aria-hidden="true">确认</span>		        
		      </a>
	      </div>
	    </li>	    
	</ul>
    </nav> 
  	</div>   
</div>

<!-- 表单设置窗口 -->
<div id="setting-div" class="mask opacity" style="display:none;height:36%;left:20%;top:15%;width:30%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">表单设置</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#setting-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:50%" >
    	<label>表头列</label>：<input id="headCols" value="${headCols}" style="width:30%"> &nbsp; 
    	<select id="headBorder">
    		<option value="是" <c:if test="${headBorder eq '是'}">selected</c:if>>有边框</option>
    		<option value="无" <c:if test="${headBorder eq '无'}">selected</c:if>>无边框</option>
    	</select><br>
    	<label>表体列</label>：<input id="bodyCols" value="${bodyCols}" style="width:30%"> &nbsp; 
    	<select id="bodyBorder">
    		<option value="是" <c:if test="${bodyBorder eq '是'}">selected</c:if>>有边框</option>
    		<option value="无" <c:if test="${bodyBorder eq '无'}">selected</c:if>>无边框</option>
    	</select><br>
    	<label>表尾列</label>：<input id="footCols" value="${footCols}" style="width:30%"> &nbsp; 
    	<select id="footBorder">
    		<option value="是" <c:if test="${footBorder eq '是'}">selected</c:if>>有边框</option>
    		<option value="无" <c:if test="${footBorder eq '无'}">selected</c:if>>无边框</option>
    	</select><br>
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div class="btn-confirm-dialog" >
      <a style="color: #e9eef3;" href="javascript:void();"  onclick="saveLayout();">
        <span aria-hidden="true" >确认</span>		        
      </a>
     </div>
</div>

<!-- 子表设置窗口 -->
<div id="subTable-div" class="mask opacity" style="display:none;height:50%;left:20%;top:15%;width:30%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">子表单设置</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#subTable-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:60%" >
    	<div class="navbar" style=" padding: 0rem 0rem;">
	        <div class="navbar-inner">
	            <div class="container" style="padding-left: 0px;">	        		        			        
                <ul class="nav" >			                    
                    <li class="dropdown" id="accountmenu">
                           子表名称： <button type="button" id="subTbId-btn" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="position:overflow;font-size: 1rem;"></button>					  	
                        <input type="hidden" id="subTbId" >
                        <ul class="dropdown-menu" id="subTbId-ul" style="left:82px">		                        	
                        	<c:forEach var="tableBrief" items="${subTbs}" varStatus="status">
					  			<li value="${tableBrief.tbId}" ><a class="dropdown-item" href="#">${tableBrief.tableName}</a></li>
					  		</c:forEach>			                            
                        </ul>
                    </li>
                </ul>
               	</div>
            </div>  
        </div>      
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div class="btn-confirm-dialog">
      <a style="color: #e9eef3;" href="javascript:void();"  onclick="setSubTable();">
        <span aria-hidden="true" >确认</span>		        
      </a>
     </div>
</div>

<!-- 表单预览窗口 -->
<div id="review-div" class="mask opacity" style="display:none;height:80%;left:-7%;top:15%;width:70%;background-color:#f8f9fa">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">⇱表单预览</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#review-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:80%" id="table-reviewer">    	     
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>    
</div>

<!-- 显示表总览窗口 -->
<div id="brief-div" class="mask opacity" style="display:none;height:40%;left:20%;top:15%;width:30%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">表单总览</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#brief-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; " > 
    	<table  border="1px solid" style="border-color: green;">
    		<tr><td style="font-size:.78rem;width:30%">表单名称</td><td style="width:50%">${brief.tableName }</td></tr>
    		<tr><td style="font-size:.78rem;width:30%">表单模板</td><td style="width:50%">${brief.template }</td></tr>
    		<tr><td style="font-size:.78rem;width:30%">数据表名</td><td style="width:50%">${brief.name }</td></tr>
    	</table>    	
    	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr> 
    	<table border="1px solid" style="border-color: red;">    		
    		<c:forEach var="layout" items="${layouts}" varStatus="status">
	    		<tr><td style="font-size:.78rem;width:20%">${layout.scope }</td><td style="width:50%">${layout.cols }列<c:if test="${!empty layout.stbId }"><label style="font-size:.78rem;margin-left:5px;">[外子表：${layout.stableName }]</label></c:if></td></tr>	    		
    		</c:forEach>    		
  		</table>   	
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>    
</div>

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
		      <a style="color: #e9eef3;" href="javascript:void(0);"  onclick="dropTable();">
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

<!-- 计算公式窗口 -->
<div id="formula-div" class="mask opacity" style="display:none;height:60%;left:40%;top:33%;width:35%;background-color:#f8f9fa">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
           <span id="scheme-msg">计算公式设置[仅支持二元计算]</span>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#formula-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;overflow:auto; height:60%" id="table-reviewer"> 
    	<div style="width:40%;height:90%;float:left;font-size:.9rem; "> 
    		<select style="width:100%;height:100%;" multiple onclick="setFormula(this,1);">
	    		<c:forEach var="em" items="${tballems}" varStatus="status">
		    		<option value="$${em.tableName}_${em.newFieldName}">${em.newLabelName}[${em.newFieldDataType }]</option>
	    		</c:forEach> 
	    	</select>
    	</div>
    	<div style="margin-left:10px;width:25%;height:90%;float: left; " >
    		<select style="width:100%;height:100%;font-size:.9rem;" multiple onclick="setFormula(this,2);">
    			<option value="+">加 +</option>
    			<option value="-">减 -</option>
    			<option value="*">乘 *</option>
    			<option value="/">除 /</option>    			
    		</select>    		
    	</div>
    	<div style="width:30%;height:90%;float: right; ">
    		<select style="width:100%;height:100%;font-size:.9rem;" multiple onclick="setFormula(this,3);">
    			<option value="/$n">求平均</option>
    			<option value="*$n">求总数</option>    			
    		</select>
    	</div>    	
    </div>  
    <div>
    		<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    		&nbsp;计算公式：<input id="formuladata" class="form-control-one-line  mx-sm-2" style="width:70%" readOnly>
    		<span style="font-size:20px;weight:bold;cursor:pointer;" onclick="$('#formuladata').val('');" title="清除">🔪</span>
    	</div>  
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div class="btn-confirm-dialog" >
      <a style="color: #e9eef3;" href="javascript:void();"  onclick="$('#formula').val($('#formuladata').val());$('#formula-div').hide();">
        <span aria-hidden="true" >确认</span>		        
      </a>
    </div>     
</div>