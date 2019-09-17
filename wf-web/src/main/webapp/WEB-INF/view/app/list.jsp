<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>
	$(function(){
		//设置下拉框列表
		$("#selOperation").val('${model.scope}');
	});
	
  	//根据下拉框的选择获取列表信息
    function findList(){
    	var scope = $("#selOperation").val();
    	location.href="${path}/app/list/${model.wftb.tbId}/"+scope+"/1";
    }
</script>
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">${model.wftb.tableName }</h1>  
  <div class="btn-toolbar mb-2 mb-md-0">  	   
    <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='${path}/app/showinittable/${model.wftb.tbId}/${model.wftb.wfId}'">新建</button>
    <button type="button" class="btn btn-sm btn-outline-secondary">生成Excel</button>&nbsp;
    <select id="selOperation" onchange="findList();">
    	<option>办理列表</option>
    	<option value="approve">申请列表</option>
    	<option value="workflow">流转列表</option>
    </select>       
  </div>   
</div>
   
<c:choose>
	<c:when test="${model.scope eq 'approve' }"><h3>申请列表</h3></c:when>
	<c:when test="${model.scope eq 'workflow' }"><h3>流转列表</h3></c:when>
</c:choose>
<div class="table-responsive" >
  <table class="table table-striped table-sm">
    <thead>
      <tr>
        <th>#ID</th>        
        <c:forEach items="${model.tableList}" varStatus="i" var="tableElement" >
        	<th>${tableElement.newLabelName }</th>
        </c:forEach>
      </tr>
    </thead>
    <tbody>   
      <c:forEach items="${model.page.pageList}"  var="item" >
      	<tr>
      		<td><a href="${path}/app/showtable/${model.wftb.tbId}/${item['Id']}">#${item['Id']}</a></td>
	      	<c:forEach items="${item}" var="map" > 	      	    		     	
	   			<c:forEach items="${model.tableList}"  var="tableElement" >	   				   			
	       		<c:if test="${tableElement.fieldName eq map.key || fn:toLowerCase(tableElement.fieldName) eq map.key  || fn:toUpperCase(tableElement.fieldName) eq map.key}">		       		       	
	       			<td>${map.value }</td>
	       		</c:if>	       		
	        	</c:forEach>     		
	     	</c:forEach>
     	</tr>
       </c:forEach> 
    </tbody>
  </table>
</div>
<jsp:include page="pagenation.jsp"  />

  
