<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/view/include.jsp"%>
<div class="container" style="width:30%; text-align:center;border:1px solid #ff3b00;margin-top:10%;box-shadow: 0 0 0 0.2rem rgba(241,9,9,.93);" >
	<form:form class="login-centered" modelAttribute="userForm" method="post" action="/login">
        <h2 class="form-signin-heading">登陆</h2>
        <label for="inputEmail" class="sr-only">授权账号</label>
        <form:input path="username" class="form-control required autofocus" placeholder="授权账号"  />
        <p></p>
        <label for="inputPassword" class="sr-only">授权密码</label>
        <form:input type="password" path="password" class="form-control required" placeholder="授权密码"  />		
        <p></p> 
        <div style="margin-bottom:30px;margin-top:30px">
	    	<button class="btn btn-lg btn-primary " style="margin-right:20px; " type="submit" >登陆</button>
	    	<button class="btn btn-lg btn-primary " type="submit">取消</button>
	   </div>  
   </form:form>
</div>