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
	  $('#ems-div').css('height','84%');
	  $('#btn-open').hide(); 
	  $('#btn-close').show();
	  $("#element-div").draggable();
	  $("#ems-div").draggable();
	  $("#lst-div").draggable();
	  $("#scheme-div").draggable();
	  $("#tbname-dialog").draggable();
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
	  //下拉框选择操作 - 角色选择
	  $('#subTbId-ul li').on('click', function(){
	    $('#subTbId-btn').text($(this).text());    	
	    $('#subTbId').val($(this).attr("value"));    	    	
	  });
	  redraw();
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
				  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
			  },
			  error: function(XMLHttpRequest, textStatus, errorThrown){
				  console.warn(XMLHttpRequest.responseText);		  
			  }
		});
	  }
  }
  
  //元素上移一位
  function moveUp(id){
	  $.ajax({
		  type: 'GET',
		  url: "/tb/moveup/${tbId}/"+id,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
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
		  url: "/tb/movedown/${tbId}/"+id,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
	
  //根据选中的不同位置刷新表单元素
  function fresh(scope){
	  gscope = scope;
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
  }
  
  //删除表单中的元素
  function remove(id){
	  $.ajax({
		  type: 'GET',
		  url: "/tb/remove/${tbId}/"+id,		  
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
		  },
		  error: function(XMLHttpRequest, textStatus, errorThrown){
			  console.warn(XMLHttpRequest.responseText);		  
		  }
	});
  }
  
   //弹出操作菜单
  function showPos(event,id,scope) {
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
	  
	  if(id != "") {
		  $("#id").val(id);			 
		  var ems = ${tbems};
		  $.each(ems,function(key,element){
			  if(element.id == id){
				  setElement(element);
				  $("#fieldName").prop("readonly", true);
				  $("#saveEm").html('保存');
			  }
		  });
	  }else{
		  $("#myForm")[0].reset();
		  $("#element-name").text('');
		  $("#fieldName").prop("readonly", false);
		  $("#saveEm").html('新增');
		  $("#id").val(""); 
	  }
  }
  
   //内部方法，完成字段赋值功能
  function setElement(element){	   
	  $("#id").val(element.id); 
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
	  $("#defaultValue").val(element.defaultValue);
	  $("#defaultValueFrom").val(element.defaultValueFrom);
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
  
  //表单列数设置，默认必须是2列。
  function saveLayout(){	  		  
	  var headCols=$("#headCols").val();		  
	  if (!/^[1-9]$/.test(headCols)) {
		  headCols="";
	  }		
	  $("#headCols").val(headCols);
	  var bodyCols=$("#bodyCols").val();		  
	  if (!/^[1-9]$/.test(bodyCols)) {
		  bodyCols="2";
	  }		
	  $("#bodyCols").val(bodyCols);
	  var footCols=$("#footCols").val();		  
	  if (!/^[1-9]$/.test(footCols)) {
		  footCols="";
	  }		
	  $("#footCols").val(footCols);
	  
	  $.ajax({
		  type: 'GET',
		  url: "/tb/savelayout/${tbId}",
		  data:{headCols:headCols,bodyCols:bodyCols,footCols:footCols},
		  dataType: 'json',
		  success: function(data){
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
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
		  url: "/tb/setlist/${tbId}",
		  data:{checkedIds:checkedIds},
		  dataType: 'json',
		  success: function(data){
			  $("#lst-div").hide();
			  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
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
		  $('#tbname-dialog').show();
	  }else{		  
		  $.ajax({
			  type: 'GET',
			  url: "/tb/createtable/${tbId}",
			  data:{tbName:tbName},
			  dataType: 'json',
			  success: function(data){
				  if(data.code == '1'){					
					  $('#tbname-dialog').hide();	
					  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&fieldsetting="+gfieldsetting;
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
  
  //库表检测
  function checkTableScheme(){		  
	  $.ajax({
		  type: 'GET',
		  url: "/tb/getTableScheme/${tbId}",		  			 
		  dataType: 'json',
		  success: function(data){
			  var li;
			  var alarm = false;
			  $("#scheme-lst").empty();
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

  //切换表单的展示范围
  function changeScope(){
	  scope = $("#tscope").val();
	  location.href="/tb/tabledefination/${tbId}?scope="+scope+"&fieldsetting="+gfieldsetting;
  }
  
  //设置子表
  function setSubTable(){      
	  $.ajax({
		  type: 'GET',
		  url: "/tb/setSubTable/${tbId}/"+$("#subTbId").val()+"/"+$("#tscope").val(),
		  dataType: 'json',
		  success: function(data){
			  $("#subTable-div").hide();			  
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
		  url: "/tb/redraw/${tbId}/"+$("#tscope").val()+"/"+gfieldsetting,
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
		  url: "/tb/review/${tbId}",
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
    		  url: "/tb/setTemplate/${tbId}",
    		  data: {template:template},			  
    		  dataType: 'json',
    		  success: function(data){	 
    			  if(data){
    				  if(template=="组建"){
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
	  if(newFieldType == '组件' || newFieldType == '子表'){
		  $.ajax({
    		  type: 'GET',
    		  url: "/tb/getPlugIns",
    		  data: {newFieldType:newFieldType},			  
    		  dataType: 'json',
    		  success: function(data){	 
    			  $("#newFieldDataType").empty();
    			  var newFieldDataType =  $("#newFieldDataType").val();
    			  $.each(data,function (index, brief) {
    				  var selected = "";
    				  if(newFieldDataType == brief.stbId+","+brief.newFieldDataType) selected = "selected";
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
	  }
  }
</script>
<c:set var="cols" value="${style}"/>
<div style="padding:0em;margin:0px;padding-top: 1.4%;">
  	<div style="background:#f8f9fa;">
  		<span class="mt-5" style="font-size: 1.35rem;margin-left:2%">表单定义 </span>
  		<span class="small-btn" style="background:#42a288;font-weight:bold;color: #ffc107;" onclick="location.href='/tb/tablecenter'">&nbsp;⬅&nbsp;</span>  		  		
  		<span style="margin-left:2%;"><span style="color:#71d2f1">❒</span>模板&nbsp;<select style="font-size:.78rem;" id="template" onchange="changeTemplate();"><option value="表">表</option><option value="子表">子表</option><option value="组件">组件</option></select></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="$('#setting-div').show();"><span style="color:#0c8e2a;">❆</span>布局 </span> 
  		<span style="margin-left:0.3%;">|</span>   		
  		<span style="margin-left:0.3%;"><select style="font-size:.78rem;" id="tscope" onchange="changeScope();"><option value="head">表头</option><option value="body">表体</option><option value="foot">表尾</option></select></span>  		  		
  		<span style="margin-left:0.3%;font-size:.78rem;" id="layout"></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="showFieldSetting();" id="fieldsetting"><span style="color: #90790a;">⤧</span>元素</span>
  		<span style="margin-left:0.3%;">|</span>   		
  		<c:if test="${empty brief.name }" > 		
			<span id="db1" style="cursor:pointer;" title="生成库表数据" onclick="createTable();"><img src="/img/wf_btn12.PNG"></span>
		</c:if>
		<c:if test="${!empty brief.name }" > 		
			<span id="db2" style="cursor:pointer;" title="重构库表数据" onclick="createTable();"><img src="/img/wf_btn13.PNG"></span>
		</c:if> 
		<span id="dbcheck" style="cursor:pointer;" title="库表检测" onclick="checkTableScheme();"><img src="/img/wf_btn15.PNG"></span>		
  		<span style="margin-left:0.3%;">|</span> 
  		<div style="float: right; margin-right: 24%;margin-top: 8px;" > 
  			<span style="cursor:pointer;" onclick="$('#brief-div').show();"><span style="font-weight:bold;color:RED">➿</span> |</span> 				  		
	  		<span style="cursor:pointer;" onclick="review();"><span style="font-weight:bold;color:#152505;">⇱</span>预览 |</span>		  		
	  		<span style="cursor:pointer;" onclick="$('#lst-div').show();"><span style="font-weight:bold;color:#152505;">✋</span>列表</span>	  		
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
       	<input type="text" name="tableName" id="tableName" class="form-control" placeholder="表单名称"  style="text-align:center;font-weight:bold;font-size:2.2rem;" value="${brief.tableName }" onkeypress="setTableName(event);" />
	</div>
	<!-- 展示表单选中范围信息 -->
	<div style="text-align:center;margin-top:7%;margin-left:2%;margin-right:2%">
		<div class="table-container" style="margin-left:0px" id="table-container">
			${table}			 		
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
					<p style="margin-bottom: 0.1rem;"><input type="checkbox" id="orgemId" name="orgemId" value="${element.emId}" <c:if test="${! empty element.tbId }">checked</c:if>><span style="font-size:0.8rem">&nbsp;${element.labelName}</span></p>
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
					<p style="margin-bottom: 0.1rem;"><input type="checkbox" id="lstId" name="lstId" value="${element.emId}-${element.tbId}" <c:if test="${element.list eq '有效' }">checked</c:if>><span style="font-size:0.8rem">&nbsp;${element.labelName}</span></p>
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
<div id="element-div" class="node-mask opacity" style="display:none;height:85%;width:32%;">
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
			
			<input type="hidden" id="id" name="id" value="">
			<input type="hidden" id="escope" name="escope" value="${scope}">			
	  		<div class="form-group-dialog">	  			  			       
		        <label >元素名称：</label>
		        <input name="newLabelName" id="newLabelName" class="form-control-one-line mx-sm-2" required autofocus style="width:25%"/>
		        <label >字段名称：</label>
		        <input name="fieldName" id="fieldName" class="form-control-one-line" readOnly style="width:25%"/>
		    </div>
		  	<div class="form-group-dialog">	    		       
		        <label>字段类型：</label>
		        <select name="newFieldDataType" id="newFieldDataType" class="form-control-one-line mx-sm-2" style="width:25%" > 
		        	<option value="String">字符串</option>
			        <option value="Date">日期</option>		       
		        </select>       
		        <label >操作方式：</label>
			    <select name="newFieldType" id="newFieldType" class="form-control-one-line" required style="width:25%" onChange="changeNewFieldType();">
			        <option>输入框</option>
			        <option>下拉框</option>
			        <option>单选框</option>
			        <option>多选框</option>
			        <option>文本框</option>
			        <option>标签</option>
			        <option>组件</option>
			        <option>子表</option>
			    </select>			        		       	        		       
		    </div>
		 
			<div class="form-group-dialog">				
			    <label >数据长度： </label>
		        <input name="newLength" id="newLength" class="form-control-one-line mx-sm-2"  style="width:25%" />	
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
		        <label >字段初值：</label>
		        <input name="defaultValue" id="defaultValue" class="form-control-one-line" style="width:25%"/>		        		        	        			        		      
		    </div>
		    <div class="form-group-dialog">		        
		        <label >初值来源：</label>
		        <input name="defaultValueFrom" id="defaultValueFrom" class="form-control-one-line  mx-sm-2" style="width:60%"/>			        		       
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
		        <input name="formula" id="formula" class="form-control-one-line  mx-sm-2" style="width:60%" readOnly/>			        		       
		    </div>		    
		    <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px;">
		   	   <button class="btn btn-lg btn-primary-dialog" style="margin-right:20px;" type="submit" id="saveEm">保存</button>
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
    <input type="text" id="tbName" name="tbName" class="form-control-one-line" placeholder="表单名称录入" >     
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
    	<label>表头列</label>：<input id="headCols" value="${headCols}"><br>
    	<label>表体列</label>：<input id="bodyCols" value="${bodyCols }"><br>
    	<label>表尾列</label>：<input id="footCols" value="${footCols }"><br>
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div class="btn-confirm-dialog" style="margin:5px;width:70px">
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
    <div class="btn-confirm-dialog" style="margin:5px;width:70px">
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
    	<label style="font-size:.78rem;">表单名称</label>：${brief.tableName }<br>
    	<label style="font-size:.78rem;">表单模板</label>：${brief.template }<br>
    	<label style="font-size:.78rem;">数据表名</label>：${brief.name }<br>
    	<hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr> 
    	<c:forEach var="layout" items="${layouts}" varStatus="status">
    		<c:choose>
    			<c:when test="${layout.scope eq 'head'}">
    				<label style="font-size:.78rem;">表头</label>
    			</c:when>
    			<c:when test="${layout.scope eq 'body'}">
    				<label style="font-size:.78rem;">表体</label>
    			</c:when>
    			<c:when test="${layout.scope eq 'foot'}">
    				<label style="font-size:.78rem;">表尾</label>
    			</c:when>
    		</c:choose>
  			：${layout.cols }列 <c:if test="${!empty layout.stbId }"><label style="font-size:.78rem;">[外子表：${layout.stableName }]</label></c:if><br>
  		</c:forEach>    	
    </div>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>    
</div>