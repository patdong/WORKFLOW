<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
function showPos(event,emId,item) {
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
	  y = y-20
	  x = x+22
	  el.style.left = x + "px";
	  el.style.top = y + "px";
	  el.style.display = "block";
	  $("#emId").val(emId);
	  
}

function setEmLabelName(event){
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
	  	<span class="mt-5" style="font-size: 2.5rem;">元素库</span>	  		   		    
	  	<div style="padding:0em;float:right;padding-top:2%;">
	     	<span class="big-btn" onclick="location.href='${path}/em/newelement'">新建</span>	     	
	  	</div>			
	</div>
	<div class="line"></div>
	<div class="table-responsive" style="padding-top: 20px;">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>#序列</th>
              <th>标签名称</th>
              <th>字段名称</th>
              <th>隐式字段</th>
              <th>绑定方法</th>
              <th>有效性</th>
              <th>级别</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${page.pageList}" varStatus="i" var="element" >
          		<tr>
          			<td><a href="${path}/em/elementdefination/${element.emId}">#${element.emId }</a></td>
          			<td><span class="small-btn" style="background-color:#42a288;" onclick="showPos(event,${element.emId },'em-labelname')" >&nbsp;✒&nbsp;</span>${element.labelName }</td>
          			<td><span class="small-btn" style="background-color:#ce6634" onclick="showPos(event,${element.emId },'em-fieldname')">&nbsp;❒&nbsp;</span>${element.fieldName}</td>
          			<td><span class="small-btn" style="background-color:#bbb8b6" onclick="showPos(event,${element.emId },'em-hiddenname')">&nbsp;❒&nbsp;</span>${element.hiddenFieldName }</td>
          			<td><span class="small-btn" style="background-color:#bace15" onclick="showPos(event,${element.emId },'em-functionname')">&nbsp;❒&nbsp;</span>${element.functionName }</td>
          			<td>${element.status }</td>
          			<td>${element.grade }</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />	
</div>
<input type="hidden" name="emId" id="emId"> 
<DIV id='em-labelname' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#em-labelname').hide();">×</div>
	<input id="labelName" name="labelName" class="form-control-one-line" style="width: 93%;" onkeypress="setEmLabelName(event);">
</DIV>
<DIV id='em-fieldname' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#em-fieldname').hide();">×</div>
	<input id="fieldName" name="fieldName" class="form-control-one-line" style="width: 93%;" onkeypress="setEmLabelName(event);">
</DIV>
<DIV id='em-hiddenname' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#em-hiddenname').hide();">×</div>
	<input id="hiddenName" name="hiddenName" class="form-control-one-line" style="width: 93%;" onkeypress="setEmLabelName(event);">
</DIV>
<DIV id='em-functionname' class="popup" style="width:180px;border: 1px solid #d6e0e0;background-color: #eaeeef;">
    <div class="popup-close" style="cursor:pointer;" onclick="$('#em-functionname').hide();">×</div>
	<input id="functionName" name="functionName" class="form-control-one-line" style="width: 93%;" onkeypress="setEmLabelName(event);">
</DIV>
