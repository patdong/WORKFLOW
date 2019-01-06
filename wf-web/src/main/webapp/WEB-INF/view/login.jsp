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
<body style="background:;">
	<div style="position: absolute;width: 30%;margin-left: 35%;top: 15%;font-size: 1.95rem;">
		<span style="color:#13ca3d;font-weight: bold;">W</span><span style="color:#e60e23;font-weight: bold;">F</span>
		<span style="color:#7b7070;font-weight: bold;font-size:18px;">2.0</span>
	</div>	
    <div class="container" style="width:30%; text-align:center;border:2px solid gray;margin-top:10%;box-shadow: 3px 4px 4px #6f6c6a;" >
	<form:form class="login-centered" modelAttribute="userForm" method="post" action="/login">
        <h2 class="form-signin-heading" style="color:#7b7070;">登陆</h2>
        <label for="inputEmail" class="sr-only">授权账号</label>
        <form:input path="username" class="form-control required autofocus" placeholder="授权账号"  />
        <p></p>
        <label for="inputPassword" class="sr-only">授权密码</label>
        <form:input type="password" path="password" class="form-control required" placeholder="授权密码"  />		
        <p></p> 
        <div style="margin-bottom:30px;margin-top:30px">
	    	<button class="btn btn-lg btn-primary " style="margin-right:20px;background:gray;border-color:gray; " type="submit" >登陆</button>
	    	<button class="btn btn-lg btn-primary " style="background:gray;border-color:gray" type="submit">取消</button>
	   </div>  
    </form:form>
	</div>
	<div style="position:absolute;right:15%;top:10px;width: 5px;height: 5px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:16%;top:15px;width: 6px;height: 6px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:15%;top:25px;width: 6px;height: 6px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:17%;top:35px;width: 10px;height:10px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:15%;top:45px;width: 18px;height: 18px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:19%;top:35px;width: 15px;height:15px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:18%;top:80px;width: 18px;height:18px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:19%;top:80px;width: 25px;height:25px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:19%;top:60px;width: 25px;height:25px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:22%;top:70px;width: 28px;height:28px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:24%;top:65px;width: 30px;height:30px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:25%;top:100px;width: 25px;height:25px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:28%;top:88px;width: 35px;height:35px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="position:absolute;right:30%;top:90px;width: 50px;height: 50px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #bdb4b4"></div>
	<div style="position:absolute;right:27%;top:150px;width: 20px;height: 20px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>	
	<div style="position:absolute;right:31%;top:140px;width: 50px;height: 50px;-webkit-border-radius: 25px;-moz-border-radius: 25px;border-radius: 25px;background: #7b7070;"></div>
	<div style="width: 338px;height: 179px;background-color:  #7b7070;border-top-left-radius: 153px;border-top-right-radius: 165px;border: 10px solid  #7b7070;border-bottom: 0;position:absolute;left:0px;bottom:0px;"></div>
	<div style="width: 180px;height: 140px;background-color: #7b7070;border-bottom-right-radius: 110px;border-top-right-radius: 110px;border: 10px solid #7b7070;border-bottom: 0;position: absolute;left: 200px;bottom: 10px;"></div>
	<div style="width: 80px; height: 10px;background-color: #7b7070;border-bottom-right-radius: 110px;border-top-right-radius: 110px;border: 10px solid #7b7070;;border-bottom: 0;position: absolute;left: 360px;bottom: 90px;"></div>
	<div style="width: 50px; height: 50px;background-color: #7b7070;border-bottom-right-radius: 110px;border-top-left-radius: 110px;border-bottom-left-radius: 110px;border-top-right-radius: 110px;border: 10px solid #7b7070;border-bottom: 0;position: absolute;left: 430px;bottom: 90px;"></div>
	<div style="width: 40px; height: 40px;background-color: #7b7070;border-bottom-right-radius: 110px;border-top-left-radius: 110px;border-bottom-left-radius: 110px;border-top-right-radius: 110px;border: 10px solid #7b7070;border-bottom: 0;position: absolute;left: 540px;bottom: 30px;"></div>
	<div style="width: 30px; height: 30px;background-color: #7b7070;border-bottom-right-radius: 110px;border-top-left-radius: 110px;border-bottom-left-radius: 110px;border-top-right-radius: 110px;border: 10px solid #7b7070;border-bottom: 0;position: absolute;right: 360px;bottom: 0px;"></div>
	<div style="width: 20px; height: 20px;background-color: #bdb4b4;border-bottom-right-radius: 110px;border-top-left-radius: 110px;border-bottom-left-radius: 110px;border-top-right-radius: 110px;border: 10px solid #bdb4b4;border-bottom: 0;position: absolute;right: 160px;bottom: 0px;"></div>
</body>
</html>
