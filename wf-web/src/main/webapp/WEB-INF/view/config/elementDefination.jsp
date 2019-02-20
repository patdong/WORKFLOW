<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="container" >
	<div id="content">
		<div id="left" style="padding:0em;margin:0px">
		  	<div >
		  		<h1 class="mt-5">元素定义
		  			<span class="small-btn" style="background:#42a288;font-weight:bold;color: #ffc107;" onclick="location.href='/em/elementcenter'">&nbsp;⬅&nbsp;</span>
		  		</h1>	  		   		    
		  	</div>
		</div>	
		<div id="right" style="padding:0em;text-align:right;">
	     	<div >
	     		<h1 class="mt-5" >	     		
	     		</h1>	     		
	     	</div>		     	    
	  	</div>
	</div>
	<div class="line"></div>
	<div style="margin-top: 3%;">
		<form id="myForm" class="" method="post" modelAttribute="element" action="/em/saveElement">
			
			<!-- hidden项是本页面两个form公用项  不可轻易做变更！ -->
			
			<input type="hidden" id="emId" name="emId" value="${element.emId }">			
	  		<div class="form-group-dialog">	  			  			       
		        <label >元素名称：</label>
		        <input name="labelName" id="labelName" class="form-control-one-line mx-sm-2" required autofocus style="width:25%" value="${element.labelName }"/>
		        <label >字段名称：</label>
		        <input name="fieldName" id="fieldName" class="form-control-one-line" required style="width:25%" value="${element.fieldName }"/>
		    </div>
		  	<div class="form-group-dialog">	    		       
		        <label>字段类型：</label>
		        <select name="fieldDataType" id="fieldDataType" class="form-control-one-line mx-sm-2" required style="width:25%" > 
		        	<option value="String">字符串</option>
			        <option value="Date">日期</option>		       
		        </select>       
		        <label >操作方式：</label>
			    <select name="fieldType" id="fieldType" class="form-control-one-line" style="width:25%">
			        <option value="输入框" <c:if test="${element.fieldType eq '输入框' }"> selected </c:if>>输入框</option>
			        <option value="下拉框" <c:if test="${element.fieldType eq '下拉框' }"> selected </c:if>>下拉框</option>
			        <option value="单选框" <c:if test="${element.fieldType eq '单选框' }"> selected </c:if>>单选框</option>
			        <option value="多选框" <c:if test="${element.fieldType eq '多选框' }"> selected </c:if>>多选框</option>
			        <option value="文本框" <c:if test="${element.fieldType eq '文本框' }"> selected </c:if>>文本框</option>
			    </select>			        		       	        		       
		    </div>
		 
			<div class="form-group-dialog">				
			    <label >数据长度： </label>
		        <input name="length" id="length" class="form-control-one-line mx-sm-2" required style="width:25%" value="${element.length}"/>			
		    	<label >隐式字段：</label>
		        <input name="hiddenFieldName" id="hiddenFieldName" class="form-control-one-line" style="width:25%" value="${element.hiddenFieldName }"/>		        		        	        			        		      
		    </div>
		    <div class="form-group-dialog">	        
		        <label >事件名称：</label>
		        <input name="functionName" id="functionName" class="form-control-one-line  mx-sm-2" style="width:45%" value="${element.functionName}"/>	
		        <input name="functionBelongTo" id="functionBelongTo" type="radio" value="标签" <c:if test="${element.functionBelongTo eq '标签' }"> checked </c:if>>标签	
		        <input name="functionBelongTo" id="functionBelongTo" type="radio" value="元素" <c:if test="${element.functionBelongTo eq '元素' }"> checked </c:if>>元素        		       
		    </div>
		    <div class="form-group-dialog">		        
		        <label >级联信息：</label>
		        <input name="dataContent" id="dataContent" class="form-control-one-line  mx-sm-2" style="width:60%" value="${element.dataContent}"/>			        		       
		    </div>		    
		    <hr></hr>		        		      
	        <div style="margin-bottom:10px;margin-top:10px;">
		   	   <button class="btn btn-lg btn-primary-dialog" style="margin-right:20px;" type="submit">保存</button>
		    </div> 	   
	   </form>	   
	</div> 
</div>
