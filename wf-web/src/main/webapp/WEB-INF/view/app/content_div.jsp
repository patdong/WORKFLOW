<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<h1 class="h2" style="text-align:center">${brief.tableName}</h1>	
	<!-- 表单头部 -->
<div style="text-align:center;margin-left:2%;margin-right:2%;top:26%">
	<div class="table-container">					
		<c:forEach items="${headList}" varStatus="i" step="1" var="element" >								
			<c:if test="${i.count % 2 == 1}">
			<div class="row">
			</c:if>						
			<div class="col-6-left">																			  
				${element.newLabelName }
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<input type="text" name="${element.fieldName }" class="form-control-head" value="${bizTable[element.fieldName]}">
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select name="${element.fieldName }" class="form-control-head">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
				</c:choose>						
			</div>				
			<c:if test="${i.count % cols == 0}">
			</div>
			</c:if>
		</c:forEach>	
	</div>								
</div>
<div class="line-gap-table"></div>
<!-- 表单体 -->
<c:set var="cols" value="${brief.cols}"/>
<div style="text-align:center;margin-left:2%;margin-right:2%;margin-top: 7%;">
	<div class="table-container">
		<c:forEach items="${bodyList}" varStatus="i" step="1" var="element" >
			<c:if test="${cols > 1 }">
			<c:if test="${i.count % cols == 1}">
			<div class="row">
			</c:if>		
			</c:if>	
			<c:if test="${cols == '1' }">						
			<div class="row">
			</c:if>		
			<div class="cols body-col-4">																			  
				<label style="width:25%">
					${element.labelName }
				</label>
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<input type="text" name="${element.fieldName }" class="form-control-body" value="${bizTable[element.fieldName]}">
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select name="${element.fieldName }" class="form-control-body">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
				</c:choose>						
			</div>						
			<c:if test="${cols > 1 }">				
			<c:if test="${i.count % cols == 0}">
			</div>
			</c:if>
			</c:if>
			<c:if test="${cols == '1' }">	
			</div>
			</c:if>	
		</c:forEach>				
	</div>
</div>
<div class="line-gap-table"></div>
<!-- 表单尾部 -->
<div style="text-align:center;margin-left:2%;margin-right:2%;margin-top: 5%;">
	<div class="table-container">
		<c:forEach items="${footList}" varStatus="i" step="1" var="element" >
			<c:if test="${cols > 1 }">
			<c:if test="${i.count % 2 == 1}">
			<div class="row">
			</c:if>		
			</c:if>	
			<c:if test="${cols == 1 }">	
			<div class="row">
			</c:if>		
			<div class="col-6-left">																			  
				${element.labelName }
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<input type="text" name="${element.fieldName }" class="form-control-head" value="${bizTable[element.fieldName]}">
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select name="${element.fieldName }" class="form-control-head">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" name="${element.fieldName }" class="form-element" <c:if test="${content eq bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
				</c:choose>							
			</div>				
			<c:if test="${cols > 1 }">				
			<c:if test="${i.count % cols == 0}">
			</div>
			</c:if>
			</c:if>
			<c:if test="${cols == 1 }">	
			</div>
			</c:if>	
		</c:forEach>				
	</div>
</div>	
