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
	  $('#ems-div').css('height','82%');
	  $('#btn-open').hide(); 
	  $('#btn-close').show();
	  $("#element-div").draggable();
	  
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
  });
  
  //保存元素设置
  function saveElements(){	 
	  var checkedIds = [];
	  if($("#ems-body").is(":visible")){
	    	//统计选中的记录
    	  $('input:checkbox[name=emId]:checked').each(function(k){ 
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
	  }else if($("#tbems-body").is(":visible")){
			//统计选中的记录
	    	$('input:checkbox[name=tbemId]:checked').each(function(k){ 	    		
	    		checkedIds += $(this).val()+",";	    		   	
	    	})
		  $.ajax({
			  type: 'GET',
			  url: "/tb/savetableelementsseq/${tbId}",
			  data:{checkedIds:checkedIds},
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
	  x -= 302; 
	  
	  if(y>200) y = 175;
	  
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  
	  $("#emId").val(emId);
	  	  
	  if(scope=="head"){
		  var ems = ${heads};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){
				  $("#labelNewName").val(element.labelNewName);
				  $("#rowes").val(element.rowes);
				  $("#cols").val(element.cols);
				  $("#width").val(element.width);
				  $("#functionNewName").val(element.functionNewName);
				  $("#element-name").text(element.name);
			  }
		  });
	  }
	  if(scope=="body"){
		  var ems = ${heads};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){
				  $("#labelNewName").val(element.labelNewName);
				  $("#rowes").val(element.rowes);
				  $("#cols").val(element.cols);
				  $("#width").val(element.width);
				  $("#functionNewName").val(element.functionNewName);
				  $("#element-name").text(element.name);
			  }
		  });
	  }
	  if(scope=="foot"){
		  var ems = ${heads};
		  $.each(ems,function(key,element){
			  if(element.emId == emId){
				  $("#labelNewName").val(element.labelNewName);
				  $("#rowes").val(element.rowes);
				  $("#cols").val(element.cols);
				  $("#width").val(element.width);
				  $("#functionNewName").val(element.functionNewName);
				  $("#element-name").text(element.name);
			  }
		  });
	  }
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
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+style+"&fieldsetting="+fieldsetting;
  }
  
  //显示设置字段页面
  function showFieldSetting(){	  
	  location.href="/tb/tabledefination/${tbId}?scope="+gscope+"&style="+style+"&fieldsetting=yes";
  }
</script>
<c:set var="cols" value="${style}"/>
<div style="padding:0em;margin:0px;padding-top: 1.4%;">
  	<div style="background:#f8f9fa;">
  		<span class="mt-5" style="font-size: 1.75rem;margin-left:13.5%">表单定义 </span>
  		<span class="btn supply-btn" style="background:#42a288;" onclick="location.href='/tb/tablecenter'"></span>
  		<span style="margin-left:2%;">模板&nbsp;<select style="font-size:.78rem;"><option>标准</option></select></span> 
  		<span style="margin-left:0.3%;">|</span> 
  		<span style="margin-left:0.3%;">风格&nbsp;<select style="font-size:.78rem;" id="style" onchange="changeStyle();"><option value="2">双列</option><option value="1">单列</option></select></span>
  		<span style="margin-left:0.3%;">|</span>
  		<span style="margin-left:0.3%;cursor:pointer;" onclick="showFieldSetting();" id="fieldsetting">字段设置</span>
  		<span style="margin-left:0.3%;">|</span>
  		<div style="float: right; margin-right: 27.5%;margin-top: 1%;" >
	  		<span style=""><span style="font-weight:bold;color:#152505;">✍</span>保存</span>	
	  		<span style=""><span style="font-weight:bold;color:#152505;">⇱</span>预览</span>	
  		</div>  		  		   		   
  	</div>
  	<div class="line-bottom" ></div>
</div>	
<div class="container" style="padding-top:0px">	
	<div class="line-table" ></div>
	<div id="table-div" class="draw" style="margin-top: 12px;width: 59.5%;height: 75.5%;">
		<form>
			<div style="text-align:center;margin:3% 0 0 5%">
				<label for="tableName" class="sr-only">表单名称</label>				
	        	<input name="tableName" id="tableName" class="form-element" placeholder="表单名称"  style="width:80%;text-align:center;font-weight:bold;font-size:2.2rem;" value="${brief.tableName }" onkeypress="setTableName(event);" />
			</div>
			<table style=" margin:7% 0 0 5%; border:px solid">
				<c:forEach items="${headList}" varStatus="i" step="2" var="element" >
					<tr>
						<td>${element.labelName } <span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'head');" title="编辑">⤧</span> </td>
						<td>
							<c:choose>
								<c:when test="${element.fieldType eq '输入框' }">
									<input name="${element.emId }" class="form-element">
								</c:when>
								<c:when test="${element.fieldType eq '下拉框' }">
									<select name="${element.emId }" class="form-element">
										<c:forEach items="${element.dataContent }" var="content">
											<option>${content}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:when test="${element.fieldType eq '多选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="checkbox" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
								<c:when test="${element.fieldType eq '单选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="radio" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
							</c:choose>							
						</td>
					</tr>
				</c:forEach>			
			</table>
			<div class="line-gap-table"></div>
			<table style=" margin:3% 0 0 5%; border:px solid">
				<c:forEach items="${bodyList}" varStatus="i" step="1" var="element" >
					<c:if test="${i.count % cols != 0}">
					<tr>	
					</c:if>					
						<td>${element.labelName } <span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span> </td>
						<td>
							<c:choose>
								<c:when test="${element.fieldType eq '输入框' }">
									<input name="${element.emId }" class="form-element">
								</c:when>
								<c:when test="${element.fieldType eq '下拉框' }">
									<select name="${element.emId }" class="form-element">
										<c:forEach items="${element.dataContent }" var="content">
											<option>${content}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:when test="${element.fieldType eq '多选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="checkbox" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
								<c:when test="${element.fieldType eq '单选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="radio" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
							</c:choose>	
						</td>
					<c:if test="${i.count % cols == 0}">
					</tr>
					</c:if>
				</c:forEach>			
			</table>
			<div class="line-gap-table"></div>
			<table style=" margin:3% 0 0 5%; border:px solid">
				<c:forEach items="${footList}" varStatus="i" step="1" var="element" >
					<c:if test="${i.count % style != 0}">
					<tr>	
					</c:if>	
						<td>${element.labelName } <span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'foot');" title="编辑">⤧</span> </td>
						<td>
							<c:choose>
								<c:when test="${element.fieldType eq '输入框' }">
									<input name="${element.emId }" class="form-element">
								</c:when>
								<c:when test="${element.fieldType eq '下拉框' }">
									<select name="${element.emId }" class="form-element">
										<c:forEach items="${element.dataContent }" var="content">
											<option>${content}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:when test="${element.fieldType eq '多选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="checkbox" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
								<c:when test="${element.fieldType eq '单选框' }">
									<c:forEach items="${element.dataContent }" var="content">
										<input type="radio" name="${element.emId }" class="form-element">${content}&nbsp;
									</c:forEach>
								</c:when>
							</c:choose>	
						</td>
					<c:if test="${i.count % style == 0}">
					</tr>
					</c:if>
				</c:forEach>			
			</table>
		</form>
	</div>
</div>
<input type="hidden" id="tbId" name="tbId" value="${tbId}"/>
<div id="ems-div" class="ems-mask opacity" style="background-color: #f8f9fa;    box-shadow: 1px 6px 4px #d6720f;top:8%" >
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >           
           <span class="span-highlight-btn" onclick="$('#tb-span').removeClass('span-btn').addClass('span-highlight-btn');$('#ems-span').removeClass('span-highlight-btn').addClass('span-btn');$('#tbems-body').show();$('#ems-body').hide();$('#ems-foot').hide();" id="tb-span">表单元素</span>           
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
					<p style="margin-bottom: 0.5rem;"><input type="checkbox" id="emId" name="emId" value="${element.emId}" <c:if test="${! empty element.tbId }">checked</c:if>>&nbsp;${element.labelName}</p>
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

<!-- 节点定义窗口 -->
<div id="element-div" class="node-mask opacity" style="display:none;height:50%">
	<header>	      
         <div class="form-inline mt-2 mt-md-0" style="padding: 6px 10px 0px;" >
          	<label>元素-[<span id="element-name" style="font-weight:bold;"></span>]设置</label>
         </div>
         <div style="position: absolute;top: 1px;right: 15px;">
         	<span class="badge badge-secondary badge-pill" style="background-color:#46a70a;cursor:pointer;" onclick="$('#element-div').hide();">×</span>
         </div>	      	     
    </header>
    <hr style="margin-top: .5rem; margin-bottom: .5rem;"></hr>
    <div style="padding: 0px 13px 0px;">
		<form id="myForm" class="navbar-form navbar-left" method="post" modelAttribute="element" action="/tb/saveElement">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="emId" name="emId" value="${element.emId}">
			<input type="hidden" id="tbId" name="tbId" value="${brief.tbId}">			
	  		<div class="popup-form-group">		        
		        <label for="labelNewName" class="sr-only">元素名称</label>
		        <input name="labelNewName" id="labelNewName" class="form-control-one-line" required autofocus placeholder="元素名称"  />			        		       
		    </div>		 
		    <div class="popup-form-group">		        
		        <label for="rowes" class="sr-only">元素跨行</label>
		        <input name="rowes" id="rowes" class="form-control-one-line" required autofocus placeholder="元素跨行"  />			        		       
		    </div>
		    <div class="popup-form-group">		        
		        <label for="cols" class="sr-only">元素跨列</label>
		        <input name="cols" id="cols" class="form-control-one-line" required autofocus placeholder="元素跨列"  />			        		       
		    </div>
		    <div class="popup-form-group">		        
		        <label for="width" class="sr-only">元素宽度</label>
		        <input name="width" id="width" class="form-control-one-line" required autofocus placeholder="元素宽度"  />			        		       
		    </div>
		    <div class="popup-form-group">		        
		        <label for="functionNewName" class="sr-only">元素方法</label>
		        <input name="functionNewName" id="functionNewName" class="form-control-one-line" required autofocus placeholder="元素方法"  />			        		       
		    </div>
	        <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px">
		    	<button class="btn btn-lg btn-primary-dialog " style="margin-right:20px;" type="submit" >保存</button>
		   </div> 			   
	   </form>
	</div>
    
</div>
