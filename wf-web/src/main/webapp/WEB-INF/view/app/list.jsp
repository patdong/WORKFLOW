<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">${wf.wfName }</h1>
  <div class="btn-toolbar mb-2 mb-md-0">
    <div class="btn-group mr-2">
      <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='/app/showtable/${wf.wfId}/div'">新建</button>
      <button type="button" class="btn btn-sm btn-outline-secondary">生成Excel</button>
    </div>
    <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle">
      <span data-feather="calendar"></span>
     办理列表
    </button>
  </div>
</div>     

<h3>申请列表</h3>
<div class="table-responsive" >
  <table class="table table-striped table-sm">
    <thead>
      <tr>
        <th>#ID</th>        
        <c:forEach items="${tableList}" varStatus="i" var="tableElement" >
        	<th>${tableElement.newLabelName }</th>
        </c:forEach>
      </tr>
    </thead>
    <tbody>   
      <c:forEach items="${page.pageList}"  var="item" >
      	<tr>
      		<td><a href="/app/showtable/${wf.wfId}/${item['Id']}/div">#${item['Id']}</a></td>
      	<c:forEach items="${item}" var="map" >      		     		
   			<c:forEach items="${tableList}"  var="tableElement" >     				
       		<c:if test="${tableElement.fieldName eq map.key }">	        		
       			<td>${map.value }</td>
       		</c:if>
        	</c:forEach>     		
     	</c:forEach>
     	</tr>
       </c:forEach> 
    </tbody>
  </table>
</div>
<jsp:include page="../pagenation.jsp"  />

  
