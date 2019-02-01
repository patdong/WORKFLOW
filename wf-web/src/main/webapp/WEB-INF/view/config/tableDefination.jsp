<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
  //页面设置全局变量
  var gscope = "${scope}";
  var gstyle = "${style}";
  var gfieldsetting = "${fieldsetting}";
  $( function() {
	  //初始化元素窗口默认为隐藏
	  $('#tb-span').removeClass('span-btn').addClass('span-highlight-btn');
	  $('#ems-span').removeClass('span-highlight-btn').addClass('span-btn');
	  $('#tbems-body').show();
	  $('#ems-body').hide();
	  $('#ems-foot').hide();	  
	  $('#ems-dialog').show();
	  $('#ems-div').css('height','84%');
	  $('#btn-open').hide(); 
	  $('#btn-close').show();
	  $("#element-div").draggable();
	  $("#ems-div").draggable();
	  $("#lst-div").draggable();
	  $("#scheme-div").draggable();
	  //设置表单位置的radiobox值
	  var $scope = $('input:radio[name=scope]');	 
	  $scope.filter('[value=${scope}]').prop('checked', true);
	  //设置风格默认值
	  $("#style").val("${style}");
	  //设置字段设置按钮不可见
	  $(".btn-edit-pointer").hide();
	  //设置字段设置按钮高亮	 
	  if(gfieldsetting == "yes"){
		  $("#fieldsetting").css('color','#ffc107').css("background","#12ad43");
		  $(".btn-edit-pointer").show();
	  }
	  //设置列表列风格
	  if(gstyle == '2') $(".cols").removeClass("body-col-6").removeClass("body-col-8").addClass("body-col-4");
	  if(gstyle == '3') $(".cols").removeClass("body-col-8").removeClass("body-col-4").addClass("body-col-6");
	  
	  $("#tbname-dialog" ).dialog();
	  $('#tbname-dialog').dialog('close');	  
	  $("#alert-dialog" ).dialog();
	  $('#alert-dialog').dialog('close');
  });
  
  //保存元素设置
  function saveElements(){	 
	  var checkedIds = [];
	  if($("#ems-body").is(":visible")){
	    	//统计选中的记录
    	  $('input:checkbox[name=orgemId]:checked').each(function(k){ 
    		  checkedIds.push($(this).val());	    		   	
    	  })
		  $.ajax({
			  type: 'GET',
			  url: "/tb/savecheckedelements/${tbId}",
			  data:{checkedIds:checkedIds,scope:gscope},
			  dataType: 'json',
			  success: function(data){
				  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
			  },
			  error: function(XMLHttpRequest, textStatus, errorThrown){
				  console.warn(XMLHttpRequest.responseText);		  
			  }
		});
	  }
  }
  
  //元素上移一位
  function moveUp(emId){
	  $.ajax({
		  type: 'GET',
		  url: "/tb/moveup/${tbId}/"+emId,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //元素下移一位
  function moveDown(emId){
	  $.ajax({
		  type: 'GET',
		  url: "/tb/movedown/${tbId}/"+emId,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
	
  //根据选中的不同位置刷新表单元素
  function fresh(scope){
	  gscope = scope;
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
  }
  
  //删除表单中的元素
  function remove(emId){
	  $.ajax({
		  type: 'GET',
		  url: "/tb/remove/${tbId}/"+emId,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
   //弹出操作菜单
  function showPos(event,emId,scope) {
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
	  
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  
	  $("#emId").val(emId);
	  	  
	  if(scope=="head"){
		  var ems = ${heads};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){
				  setElement(element);
			  }
		  });
	  }
	  if(scope=="body"){
		  var ems = ${bodys};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){				  
				  setElement(element);
			  }
		  });
	  }
	  if(scope=="foot"){
		  var ems = ${foots};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){
				  setElement(element);
			  }
		  });
	  }
  }
  
   //内部方法，完成字段赋值功能
  function setElement(element){
	  $("#emId").val(element.emId); 
	  $("#element-name").text(element.newLabelName);
	  $("#newLabelName").val(element.newLabelName);
	  $("#fieldName").val(element.fieldName);	  
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
   }
   
  //设置表单名称
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
	      		  },
	      		  error: function(XMLHttpRequest, textStatus, errorThrown){
	      			  console.warn(XMLHttpRequest.responseText);			  
	      		  }
	      	});
	        }
	    }
	}
  
  //显示不同风格的表单
  function changeStyle(){
	  var style=$("#style").val();
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+style+"&fieldsetting="+gfieldsetting;
  }
  
  //显示设置字段页面
  function showFieldSetting(){
	  if(gfieldsetting.indexOf("yes")>=0) gfieldsetting = "no";
	  else gfieldsetting = "yes";
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
  }
  
  //保存表单概述信息
  function save(){
	  var style=$("#style").val();
	  var template=$("#template").val();
	  $.ajax({
		  type: 'GET',
		  url: "/tb/savetbrief/${tbId}",
		  data:{template:template,style:style},
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
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
		  url: "/tb/setlist/${tbId}",
		  data:{checkedIds:checkedIds},
		  dataType: 'json',
		  success: function(data){
			  $("#lst-div").hide();
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
  //生成库表数据
  function createTable(){
	  var tbName = $("#tbName").val();
	  if("${brief.name}" == "" && tbName == ""){
		  $('#tbname-dialog').dialog('open');
	  }else{		 
		  $.ajax({
			  type: 'GET',
			  url: "/tb/createtable/${tbId}",
			  data:{tbName:tbName},
			  dataType: 'json',
			  success: function(data){
				  if(data.code == '1'){
					  $('#tbname-dialog').dialog('close');	
					  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+gstyle+"&fieldsetting="+gfieldsetting;
				  }
				  if(data.code == '0'){
					  $('#alert-dialog').dialog('open');
					  $("#alert-msg").text(data.message+"通过库表检测表单信息的健康情况.");
					  $("#tbName").val("");
				  }
			  },
			  error: function(XMLHttpRequest, textStatus, errorThrown){
				  console.warn(XMLHttpRequest.responseText);		  
			  }
		});
	  }
  }
  
  //库表检测
  function checkTableScheme(){	  
	  $.ajax({
		  type: 'GET',
		  url: "/tb/getTableScheme/${tbId}",		  			 
		  dataType: 'json',
		  success: function(data){
			  var li;
			  var alarm = false;
			  $.each(data,function (index, element) {
			      li = "<tr>";
			      if(element.fieldName==null){
			      	li += "<td><span style='color:red;font-weight:bold;'>❗<span></td>"; 	
			      	alarm = true;
			      }else{
			    	  li += "<td>"+element.fieldName+"</td>"
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
 
</script>
<c:set var="cols" value="${style}"/>
<div style="padding:0em;margin:0px;padding-top: 1.4%;">
  	<div style="background:#f8f9fa;">
  		<span class="mt-5" style="font-size: 1.75rem;margin-left:2%">表单定义 </span>
  		<span class="small-btn" style="background:#42a288;font-weight:bold;color: #ffc107;" onclick="location.href='/tb/tablecenter'">&nbsp;⬅&nbsp;</span>
  		<span style="margin-left:2%;">模板&nbsp;<select style="font-size:.78rem;" id="template"><option value="bootstrap">标准</option></select></span> 
  		<span style="margin-left:0.3%;">|</span> 
  		<span style="margin-left:0.3%;">风格&nbsp;<select style="font-size:.78rem;" id="style" onchange="changeStyle();"><option value="2">双列</option><option value="3">三列</option></select></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="showFieldSetting();" id="fieldsetting">字段设置</span>
  		<span style="margin-left:0.3%;">|</span> 
  		<c:if test="${empty brief.name }" > 		
			<span style="cursor:pointer;" title="生成库表数据" onclick="createTable();"><img src="/img/wf_btn12.PNG"></span>
		</c:if>
		<c:if test="${!empty brief.name }" > 		
			<span style="cursor:pointer;" title="重构库表数据" onclick="saveTable();"><img src="/img/wf_btn13.PNG"></span>
		</c:if> 
		<span style="cursor:pointer;" title="库表检测" onclick="checkTableScheme();"><img src="/img/wf_btn15.PNG"></span>		
  		<span style="margin-left:0.3%;">|</span> 
  		<div style="float: right; margin-right: 24%;margin-top: 1%;" >  			
	  		<span style="cursor:pointer;" onclick="save();"><span style="font-weight:bold;color:#152505;">✍</span>保存
	  			<span style="color:#dc3545;box-shadow: 1px 4px 4px #826a22;background-color:black;"><c:if test="${empty brief.name }">❗</c:if></span>	  		
	  		</span>	
	  		<span style="cursor:pointer;" onclick="review();"><span style="font-weight:bold;color:#152505;">⇱</span>预览 |</span>		  		
	  		<span style="cursor:pointer;" onclick="$('#lst-div').show();"><span style="font-weight:bold;color:#152505;">✋</span>列表设置</span>	  		
  		</div>  		  		   		   
  	</div>
  	<div class="line-bottom" ></div>
</div>	
<div style="padding-top:0px;">	
	<div class="line-table" ></div>
	<div id="table-div" class="draw" style="padding-left:1%;bottom:5%;height:78%;top:16%;background-image: url('/img/wf_btn11.PNG'); background-repeat: repeat;">		
		<!-- 表单标题 -->	
		<div style="text-align:center;margin-left:10%;margin-right:10%">
			<label for="tableName" class="sr-only">表单名称</label>				
        	<input type="text" name="tableName" id="tableName" class="form-control" placeholder="表单名称"  style="text-align:center;font-weight:bold;font-size:2.2rem;" value="${brief.tableName }" onkeypress="setTableName(event);" />
		</div>
		<!-- 表单头部 -->
		<div style="text-align:center;margin-left:2%;margin-right:2%;top:25%">
			<div class="table-container">					
				<c:forEach items="${headList}" varStatus="i" step="1" var="element" >								
					<c:if test="${i.count % 2 == 1}">
					<div class="row">
					</c:if>						
					<div class="col-6-left">																			  
						${element.newLabelName }
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-head">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-head">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'head');" title="编辑">⤧</span>
						</c:if>
					</div>				
					<c:if test="${i.count % cols == 0}">
					</div>
					</c:if>
				</c:forEach>	
			</div>								
		</div>
		<div class="line-gap-table"></div>
		<!-- 表单体 -->
		<div style="text-align:center;margin-left:2%;margin-right:2%;margin-top: 7%;">
			<div class="table-container">
				<c:forEach items="${bodyList}" varStatus="i" step="1" var="element" >
					<c:if test="${cols > 1 }">
					<c:if test="${i.count % cols == 1}">
					<div class="row">
					</c:if>		
					</c:if>	
					<c:if test="${cols == '1' }">						
					<div class="row">
					</c:if>		
					<div class="cols body-col-4">																			  
						<label style="width:25%">
							${element.newLabelName }
						</label>
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-body">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-body">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span>
						</c:if>	
					</div>						
					<c:if test="${cols > 1 }">				
					<c:if test="${i.count % cols == 0}">
					</div>
					</c:if>
					</c:if>
					<c:if test="${cols == '1' }">	
					</div>
					</c:if>	
				</c:forEach>				
			</div>
		</div>
		<div class="line-gap-table"></div>
		<!-- 表单尾部 -->
		<div style="text-align:center;margin-left:2%;margin-right:2%;margin-top: 5%;">
			<div class="table-container">
				<c:forEach items="${footList}" varStatus="i" step="1" var="element" >
					<c:if test="${cols > 1 }">
					<c:if test="${i.count % 2 == 1}">
					<div class="row">
					</c:if>		
					</c:if>	
					<c:if test="${cols == 1 }">	
					<div class="row">
					</c:if>		
					<div class="col-6-left">																			  
						${element.newLabelName }
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-head">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-head">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>	
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'foot');" title="编辑">⤧</span>
						</c:if>
					</div>				
					<c:if test="${cols > 1 }">				
					<c:if test="${i.count % cols == 0}">
					</div>
					</c:if>
					</c:if>
					<c:if test="${cols == 1 }">	
					</div>
					</c:if>	
				</c:forEach>				
			</div>
		</div>		
	</div>
</div>
<input type="hidden" id="tbId" name="tbId" value="${tbId}"/>
<div id="ems-div" class="ems-mask opacity" style="background-color: #f8f9fa;box-shadow: 1px 6px 4px #d6720f;top:10%;bottom: 1%" >
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >           
           <span class="span-highlight-btn" onclick="$('#tb-span').removeClass('span-btn').addClass('span-highlight-btn');$('#ems-span').removeClass('span-highlight-btn').addClass('span-btn');$('#tbems-body').show();$('#ems-body').hide();;$('#ems-foot').hide();" id="tb-span">表单元素</span>           
           <span class="span-btn" onclick="$('#ems-span').removeClass('span-btn').addClass('span-highlight-btn');$('#tb-span').removeClass('span-highlight-btn').addClass('span-btn');$('#ems-body').show();$('#tbems-body').hide();$('#ems-foot').show();" id="ems-span">元素集</span>           
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span id="btn-close" class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;display:none;" onclick="$('#btn-open').show(); $('#btn-close').hide(); $('#ems-dialog').hide();$('#ems-div').css('height','6.5%');">×</span>
         	<span id="btn-open" class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#btn-open').hide(); $('#btn-close').show(); $('#ems-dialog').show();$('#ems-div').css('height','80%');">√</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;border-top: 1px solid #0c4219;"></hr>    
    <div id="ems-dialog" style="height:70%">
	    <div style="margin-left:5px;padding-left:1px;" >
		    <input type="radio" id="scope" name="scope" value="head" onclick="fresh('head');">&nbsp;表头
		    <input type="radio" id="scope" name="scope" value="body" checked onclick="fresh('body');">&nbsp;表体
		    <input type="radio" id="scope" name="scope" value="foot" onclick="fresh('foot');">&nbsp;表尾 		    
	    </div>
	
		<hr style="margin-top: .1rem; border-top: 1px solid #0c4219;"></hr> 
	    <div style="padding: 0px 13px 0px;height: 100%;overflow-y: auto;" id="ems-body">
			<form id="emsForm" class="navbar-form navbar-left" method="get" action="">			
				<c:forEach items="${emList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.5rem;"><input type="checkbox" id="orgemId" name="orgemId" value="${element.emId}" <c:if test="${! empty element.tbId }">checked</c:if>>&nbsp;${element.labelName}</p>
				</c:forEach>			        			  
		   </form>	  	
		</div>			 
		<div style="padding: 0px 13px 0px;height: 100%;overflow-y: auto;" id="tbems-body" >
			<form id="tbemsForm" class="navbar-form navbar-left" method="get" action="">
				<c:forEach items="${tbemList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.5rem;text-align:left">
						&nbsp;${element.labelName}					
						<span style="float:right;">
							<span id="btn-up" class="btn-pointer" onclick="moveUp(${element.emId});" title="上移">⬆</span>
							<span id="btn-down" class="btn-pointer" onclick="moveDown(${element.emId});" title="下移">⬇</span>
							<span id="btn-up" class="btn-del-pointer" onclick="remove(${element.emId});" title="删除">✖</span>
						</span>					
					</p>
				</c:forEach>			     			   
		   </form>
		</div>		
		<div id="ems-foot">	
			<hr></hr>
			<div style="margin-bottom:10px;margin-top:10px;margin-left:5px;">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;padding-left:1px;" onclick="saveElements();">保存</button>
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
    <div style="height:70%">	    
	    <div style="padding: 0px 13px 0px;height: 100%;overflow-y: auto;" >
			<form id="lstForm" class="navbar-form navbar-left" method="get" action="">			
				<c:forEach items="${tbList}" varStatus="i" var="element" >
					<p style="margin-bottom: 0.5rem;"><input type="checkbox" id="lstId" name="lstId" value="${element.emId}-${element.tbId}" <c:if test="${element.list eq '有效' }">checked</c:if>>&nbsp;${element.labelName}</p>
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


<!-- 节点定义窗口 -->
<div id="element-div" class="node-mask opacity" style="display:none;height:83%;width:32%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>元素-[<span id="element-name" style="font-weight:bold;"></span>]设置</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#element-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr ></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="myForm" class="" method="post" modelAttribute="element" action="/tb/saveElement/${tbId}">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="emId" name="emId" value="">			
	  		<div class="form-group-dialog">	  			  			       
		        <label >元素名称：</label>
		        <input name="newLabelName" id="newLabelName" class="form-control-one-line mx-sm-2" required autofocus style="width:25%"/>
		        <label >字段名称：</label>
		        <input name="fieldName" id="fieldName" class="form-control-one-line" required readOnly style="width:25%"/>
		    </div>
		  	<div class="form-group-dialog">	    		       
		        <label>字段类型：</label>
		        <select name="newFieldDataType" id="newFieldDataType" class="form-control-one-line mx-sm-2" required style="width:25%" > 
		        	<option value="String">字符串</option>
			        <option value="Date">日期</option>		       
		        </select>       
		        <label >操作方式：</label>
			    <select name="newFieldType" id="newFieldType" class="form-control-one-line" style="width:25%">
			        <option>输入框</option>
			        <option>下拉框</option>
			        <option>单选框</option>
			        <option>多选框</option>
			        <option>文本框</option>
			    </select>			        		       	        		       
		    </div>
		 
			<div class="form-group-dialog">				
			    <label >数据长度： </label>
		        <input name="newLength" id="newLength" class="form-control-one-line mx-sm-2" required style="width:25%" />	
		        <label >元素宽度：</label>
		        <input name="width" id="width" class="form-control-one-line" style="width:25%" />		    
			</div>
		    <div class="form-group-dialog">		        
		        <label >元素跨行：</label>
		        <input name="rowes" id="rowes" class="form-control-one-line mx-sm-2" value="1" style="width:25%" />		    		        
		        <label >元素跨列：</label>
		        <input name="cols" id="cols" class="form-control-one-line" value="1" style="width:25%"/>			        		       
		    </div>
		    <div class="form-group-dialog">
		    	<label >隐式字段：</label>
		        <input name="newHiddenFieldName" id="newHiddenFieldName" class="form-control-one-line mx-sm-2" style="width:25%" />		        		        	        			        		      
		    </div>
		    <div class="form-group-dialog">	        
		        <label >事件名称：</label>
		        <input name="newFunctionName" id="newFunctionName" class="form-control-one-line  mx-sm-2" style="width:45%"/>	
		        <input name="functionBelongTo" id="functionBelongTo" type="radio" value="标签">标签	
		        <input name="functionBelongTo" id="functionBelongTo" type="radio" value="元素">元素        		       
		    </div>
		    <div class="form-group-dialog">		        
		        <label >级联信息：</label>
		        <input name="newDataContent" id="newDataContent" class="form-control-one-line  mx-sm-2" style="width:60%"/>			        		       
		    </div>
		    <div class="form-group-dialog">		        
		        <label >计算公式：</label>
		        <input name="formula" id="formula" class="form-control-one-line  mx-sm-2" style="width:60%"/>			        		       
		    </div>	
		    <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px;">
		   	   <button class="btn btn-lg btn-primary-dialog" style="margin-right:20px;" type="submit">保存</button>
		    </div> 	   
	   </form>	   
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
<div id="tbname-dialog"  title="表单名称录入" >
  <input type="text" id="tbName" name="tbName" class="form-control-one-line" placeholder="表单名称录入"> 
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

<!-- 消息互动窗口 -->
<div id="alert-dialog"  title="消息弹出框" >
  <span id="alert-msg"></span>
  <br><br>
  <nav aria-label="Page navigation example">
  	<ul class="pagination">  	    
   		<li class="page-item">
   		  <div class="btn-confirm-dialog">
		      <a style="color: #e9eef3;" href="javascript:void();"  onclick="$('#alert-dialog').dialog('close');">
		        <span aria-hidden="true">确认</span>		        
		      </a>
	      </div>
	    </li>	    
	</ul>
  </nav>    
</div>
