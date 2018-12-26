<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="container" >
	<div id="content">
		<div id="left" style="padding:0em;margin:0px">
		  	<div >
		  		<h1 class="mt-5">表单管理</h1>	  		   		    
		  	</div>
		</div>	
		<div id="right" style="padding:0em;text-align:right;">
	     	<div >
	     		<h1 class="mt-5" >
	     		<img style="cursor:pointer;" onclick="location.href='/tb/newtablebrief'" src="/img/wf_btn1.PNG"></img>
	     		<img style="cursor:pointer;" onclick="location.href='/tb/tabledefination'" src="/img/wf_btn3.PNG"></img>
	     		</h1>	     		
	     	</div>	     	
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
              <th>有效性</th>
              <th>流程操作</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${tbList}" varStatus="i" var="table" >
          		<tr>
          			<td><a href="/tb/tabledefination/${table.tbId}">#${table.tbId }</a></td>
          			<td><span class="btn supply-btn" style="background-color:#42a288" onclick="showPos(event,${table.tbId })" ></span>&nbsp;&nbsp;<span id="${table.tbId }">${table.tableName }</span></td>
          			<td><span class="btn supply-btn" style="background-color:#ce6634" onclick="location.href='/tb/tablecenter'"></span></td>
          			<td>${table.status }</td>
          			<td>删除</td>
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />
</div>
