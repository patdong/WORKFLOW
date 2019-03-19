<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<script>

</script>
<div class="container" >
	<div id="content">
		<span class="mt-5" style="font-size: 2.5rem;">系统设置中心</span>	  		   		    
	  	<div style="padding:0em;float:right;padding-top:2%;">
	     	<span class="big-btn" onclick="location.href='#'">新建</span>	     	
	  	</div>			
	</div>
	<div class="line"></div>
	<div class="table-responsive" style="padding-top: 20px;">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>参数名称</th>
              <th>参数值</th>
              <th>参数描述</th>              
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${confs}" varStatus="i" var="conf" >
          		<tr>
          			<td>${conf.name}</td>
          			<td>${conf.value}</td>
          			<td>${conf.note}</td>          			
          		</tr>
          	</c:forEach>            
         </tbody>
     	</table>
	</div>
	<jsp:include page="pagenation.jsp"  />
</div>

