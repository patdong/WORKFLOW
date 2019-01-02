<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<title>Work Flow Configuration</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/sys.css" />
</head>

<body>
	<div style="position: absolute;width: 30%;margin-left: 35%;top: 15%;font-size: 1.95rem;">
		<span style="color:#13ca3d;font-weight: bold;">W</span><span style="color:#e60e23;font-weight: bold;">F</span>
		<span style="color:#17dcfb;font-weight: bold;">2.0</span>
	</div>	
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
</body>
</html>
