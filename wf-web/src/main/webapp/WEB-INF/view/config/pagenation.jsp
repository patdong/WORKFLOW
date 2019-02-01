<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div id="page" style="position:absolute;bottom:3%">
<nav aria-label="Page navigation example">
  <ul class="pagination">  	
    	<c:if test="${! empty page.prePage}">
    		<li class="page-item">
    		  <div class="page-btn">
			      <a style="color: #e9eef3;" href="${page.url}/${page.prePage}" aria-label="Previous">
			        <span aria-hidden="true">&laquo;</span>
			        <span class="sr-only">Previous</span>
			      </a>
		      </div>
		    </li>
    	</c:if>
    	<c:forEach items="${page.bodyPages}" varStatus="i" var="number" >
    		<c:if test="${ !empty number }">
    		<li class="page-item">    			
    			<div <c:if test="${number == page.curPage }"> class="page-highlight-btn" </c:if> <c:if test="${number != page.curPage }"> class="page-btn" </c:if>>    			    			
    				<a style="color: #e9eef3;" href="${page.url}/${number}">${number}</a>
    			</div>
    		</li>
    		</c:if>
    	</c:forEach>
    	<c:if test="${! empty page.nextPage}">
    		<li class="page-item">
    			<div class="page-btn">
			      <a style="color: #e9eef3;" href="${page.url}/${page.nextPage}" aria-label="Next">
			        <span aria-hidden="true">&raquo;</span>
			        <span class="sr-only">Next</span>
			      </a>
		      	</div>
		    </li>
    	</c:if>
  </ul>
</nav>
</div>