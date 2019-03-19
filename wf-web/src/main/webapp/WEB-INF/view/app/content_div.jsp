<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<h1 class="h2" style="text-align:center">${model.tableBrief.tableName}</h1>	
	<!-- 表单头部 -->
<c:set var="cols" value="${model.tableBrief.cols}"/>
<div style="text-align:center;margin-left:2%;margin-right:2%;top:26%">
	<div class="table-container">					
		<c:forEach items="${model.headLst}" varStatus="i" step="1" var="element" >								
			<c:if test="${i.count % 2 == 1}">
			<div class="row">
			</c:if>						
			<div class="col-6-left">	
				<c:choose>
					<c:when test="${!empty element.newFunctionName}">
						<a>${element.newLabelName }</a>
					</c:when>
					<c:when test="${empty element.newFunctionName}">
						${element.newLabelName }
					</c:when>
				</c:choose>																		  
				
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<input type="text" id="${element.fieldName }" name="${element.fieldName }" class="form-control-head" value="${model.bizTable[element.fieldName]}">
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select id="${element.fieldName }" name="${element.fieldName }" class="form-control-head">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq model.bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
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
<div style="text-align:center;margin-left:2%;margin-right:2%;margin-top: 7%;">
	<div class="table-container">
		<c:forEach items="${model.bodyLst}" varStatus="i" step="1" var="element" >
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
					<c:choose>
						<c:when test="${!empty element.functionBelongTo  && element.functionBelongTo eq '标签'}">
							<c:if test="${!empty element.newFunctionName}">
								<c:choose>
									<c:when test="${element.newFieldDataType eq 'Date' }">
										<span style="cursor:pointer" onclick="${element.newFunctionName}">${element.newLabelName }</span>
										
									</c:when>
								</c:choose>							
							</c:if>
						</c:when>
						<c:otherwise>
							${element.newLabelName }
						</c:otherwise>
					</c:choose>		
				</label>
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<c:choose>
							<c:when test="${!empty element.functionBelongTo  && element.functionBelongTo eq '元素'}">								
								<c:if test="${!empty element.newFunctionName}">							
									<c:choose>										
										<c:when test="${element.newFieldDataType eq 'Date' }">
											<input type="text" id="${element.fieldName }" name="${element.fieldName }" class="form-control-body" value="${model.bizTable[element.fieldName]}" onclick="${element.newFunctionName}">											
										</c:when>
									</c:choose>							
								</c:if>
							</c:when>
							<c:otherwise>
								<input type="text" id="${element.fieldName }" name="${element.fieldName }" class="form-control-body" value="${model.bizTable[element.fieldName]}">
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select id="${element.fieldName }" name="${element.fieldName }" class="form-control-body">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq model.bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
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
		<c:forEach items="${model.footLst}" varStatus="i" step="1" var="element" >
			<c:if test="${cols > 1 }">
			<c:if test="${i.count % 2 == 1}">
			<div class="row">
			</c:if>		
			</c:if>	
			<c:if test="${cols == 1 }">	
			<div class="row">
			</c:if>		
			<div class="col-6-left">																			  
				<c:choose>
					<c:when test="${!empty element.newFunctionName}">
						<a>${element.newLabelName }</a>
					</c:when>
					<c:when test="${empty element.newFunctionName}">
						${element.newLabelName }
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${element.newFieldType eq '输入框' }">
						<input type="text" id="${element.fieldName }" name="${element.fieldName }" class="form-control-head" value="${model.bizTable[element.fieldName]}">
					</c:when>
					<c:when test="${element.newFieldType eq '下拉框' }">
						<select id="${element.fieldName }" name="${element.fieldName }" class="form-control-head">
							<c:forEach items="${element.newDataContent }" var="content">
								<option value="${content}" <c:if test="${content eq model.bizTable[element.fieldName]}">selected</c:if>>${content}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${element.newFieldType eq '多选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="checkbox" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
						</c:forEach>
					</c:when>
					<c:when test="${element.newFieldType eq '单选框' }">
						<c:forEach items="${element.newDataContent }" var="content">
							<input type="radio" id="${element.fieldName }" name="${element.fieldName }" class="form-element" <c:if test="${content eq model.bizTable[element.fieldName]}">checked</c:if>>${content}&nbsp;
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
