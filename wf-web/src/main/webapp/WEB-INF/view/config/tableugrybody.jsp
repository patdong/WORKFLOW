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
	<!-- 表单头 -->
	<div style="text-align:center;margin-top:7%;margin-left:2%;margin-right:2%">
		<div class="table-container" style="margin-left:0px">
			<table border="1" style="width:100%">
			<c:forEach items="${headList}" varStatus="i" var="elements" >
				<tr style="height:75px">
				<c:forEach items="${elements}" varStatus="j" var="element" >
					<c:if test="${!empty element.newLabelName }">					
					<td colspan="${element.cols}" rowspan="${element.rowes}">
						<label>${element.newLabelName}：</label>
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" >
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" >
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span>
						</c:if>
					</td>
					</c:if>					
				</c:forEach>
				</tr>
			</c:forEach>
			</table>				
		</div>
	</div>
	<div class="line-gap-table"></div>
	<!-- 表单体 -->
	<div style="text-align:center;margin-top:7%;margin-left:2%;margin-right:2%">
		<div class="table-container" style="margin-left:0px">
			<table border="1" style="width:100%">
			<c:forEach items="${bodyList}" varStatus="i" var="elements" >				
				<tr style="height:75px">
				<c:forEach items="${elements}" varStatus="j" var="element" >
					<c:if test="${!empty element.newLabelName }">					
					<td colspan="${element.cols}" rowspan="${element.rowes}">
						<label>${element.newLabelName}：</label>
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" >
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" >
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span>
						</c:if>
					</td>
					</c:if>					
				</c:forEach>
				</tr>				
			</c:forEach>
			</table>				
		</div>
	</div>
	<div class="line-gap-table"></div>
	<!-- 表单尾 -->
	<div style="text-align:center;margin-top:7%;margin-left:2%;margin-right:2%">
		<div class="table-container" style="margin-left:0px">
			<table border="1" style="width:100%">
			<c:forEach items="${footList}" varStatus="i" var="elements" >
				<tr style="height:75px">
				<c:forEach items="${elements}" varStatus="j" var="element" >
					<c:if test="${!empty element.newLabelName }">					
					<td colspan="${element.cols}" rowspan="${element.rowes}">
						<label>${element.newLabelName}：</label>
						<c:choose>
							<c:when test="${element.newFieldType eq '输入框' }">
								<input type="text" name="${element.fieldName }" >
							</c:when>
							<c:when test="${element.newFieldType eq '下拉框' }">
								<select name="${element.fieldName }" >
									<c:forEach items="${element.newDataContent }" var="content">
										<option>${content}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${element.newFieldType eq '多选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="checkbox" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
							<c:when test="${element.newFieldType eq '单选框' }">
								<c:forEach items="${element.newDataContent }" var="content">
									<input type="radio" name="${element.fieldName }" >${content}
								</c:forEach>
							</c:when>
						</c:choose>
						<c:if test="${!empty element.newLabelName}">
						<span id="btn-up" class="btn-edit-pointer" onclick="showPos(event,${element.emId},'body');" title="编辑">⤧</span>
						</c:if>
					</td>
					</c:if>					
				</c:forEach>
				</tr>
			</c:forEach>
			</table>				
		</div>
	</div>
	<div class="line-gap-table"></div>
</div>