<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="container" >
	<div id="content">
		<span class="mt-5" style="font-size: 2.5rem;">表单管理</span>	  		   		    
	  	<div style="padding:0em;float:right;padding-top:2%;">
	     	<span class="big-btn" onclick="location.href='/tb/newtablebrief'">新建</span>	
	     	<span class="big-btn" style="background-color:#5dca0a" onclick="location.href='/tb/newtablebrief'">导入</span>     	
	  	</div>			
	</div>
	<div class="line"></div>
	<div class="table-responsive" style="padding-top: 20px;">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>#序列</th>
              <th>表单名称</th>
              <th>流程绑定</th>
              <th>库表名称</th>
              <th>有效性</th>
              <th>表单操作</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${page.pageList}" varStatus="i" var="table" >
          		<tr>
          			<td><a href="/tb/tabledefination/${table.tbId}">#${table.tbId }</a></td>
          			<td><span class="small-btn" style="background-color:#42a288;" onclick="showPos(event,${table.tbId })" >&nbsp;✒&nbsp;</span><span id="${table.tbId }">${table.tableName }</span></td>
          			<td><span class="small-btn" style="background-color:#ce6634" onclick="location.href='/tb/tablecenter'">&nbsp;✓&nbsp;</span></td>
          			<td>${table.name}</td>
          			<td>${table.status }</td>
          			<td>删除</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />
</div>
