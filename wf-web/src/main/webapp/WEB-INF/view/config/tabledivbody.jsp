<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<c:set var="cols" value="${style}"/>
<div class="line-table" ></div>
	<div id="table-div" class="draw" style="padding-left:1%;bottom:5%;height:78%;top:16%;background-image: url('/img/wf_btn11.PNG'); background-repeat: repeat;">		
		<!-- 表单标题 -->	
		<div style="text-align:center;margin-left:10%;margin-right:10%">
			<label for="tableName" class="sr-only">表单名称</label>				
        	<input type="text" name="tableName" id="tableName" class="form-control" placeholder="表单名称"  style="text-align:center;font-weight:bold;font-size:2.2rem;" value="${brief.tableName }" onkeypress="setTableName(event);" />
		</div>
		<!-- 表单头部 -->
		<div style="text-align:center;margin-left:2%;margin-right:2%;top:25%">
			<div class="table-container">					
				<c:forEach items="${headList}" varStatus="i" step="1" var="element" >								
					<c:if test="${i.count % 2 == 1}">
					<div class="row">
					</c:if>						
					<div class="col-6-left">																			  
						${element.newLabelName }
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-head">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-head">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'head');" title="编辑">⤧</span>
						</c:if>
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
							${element.newLabelName }
						</label>
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-body">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-body">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span>
						</c:if>	
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
						${element.newLabelName }
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" class="form-control-head">
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" class="form-control-head">
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" class="form-element">${content}&nbsp;
								</c:forEach>
							</c:when>
						</c:choose>	
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'foot');" title="编辑">⤧</span>
						</c:if>
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
	</div>