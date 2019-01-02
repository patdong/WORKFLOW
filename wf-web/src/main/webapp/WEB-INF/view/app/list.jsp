<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
  <h1 class="h2">${wf.wfName }</h1>
  <div class="btn-toolbar mb-2 mb-md-0">
    <div class="btn-group mr-2">
      <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='/app/table/${wf.wfId}'">新建</button>
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
      <c:forEach items="${page.pageList}" varStatus="i" var="workflow" >
     		<tr>
     			<td><a href="/wf/workflowdefination/${workflow.wfId}">#${workflow.wfId }</a></td>
     			<td><span class="small-btn" style="background-color:#42a288;" onclick="showPos(event,${workflow.wfId },'workflow-name')" >&nbsp;✒&nbsp;</span><span id="${workflow.wfId }">${workflow.wfName }</span></td>
     			<td><span class="small-btn" style="background-color:#16e81d;" onclick="showPos(event,${workflow.wfId },'workflow-binding')">&nbsp;✓&nbsp;</span>
     				<span id="tb-${workflow.wfId }">
      				<c:if test="${!empty workflow.tableId }">
      				已绑定
      				<span class="small-btn" style="background-color:#ce6634;" onclick="removeBinding(${workflow.wfId })">&nbsp;✘&nbsp;</span>
      				</c:if>
     				</span>
     			</td>
     			<td>${workflow.status }</td>
     			<td>删除</td>
     		</tr>
      	</c:forEach> 
    </tbody>
  </table>
</div>
<jsp:include page="../pagenation.jsp"  />

  
