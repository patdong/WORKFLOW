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
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/sys.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/plugin.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/arrow.css" />
<!-- jquery 弹出dialog -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">



<!-- resize setting 
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
-->
</head>
<script type="text/javascript">
$( function() {
	var currentLocation = window.location;
	//设置选中菜单高亮
	var url=currentLocation.toString();
	if(url.indexOf("wf") > 0 ){
		$("#wf").css("color","white");
	}
	if(url.indexOf("tb") > 0 ){
		$("#tb").css("color","white");
	}
	if(url.indexOf("em") > 0 ){
		$("#em").css("color","white");
	}
})

</script>
<body >
    <header id="header">
    <nav class="navbar navbar-expand-md navbar-dark fixed-top" style="background:#fd7e14;">
        <a class="navbar-brand" href="/cf/center" >流程中心</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>        		
        <div class="collapse navbar-collapse" id="navbarCollapse">
          <sec:authorize access="hasRole('ROLE_ADMIN')">
	          <ul class="navbar-nav mr-auto">
	            <li class="nav-item" > <!-- active(选中变色） -->	            	
	              <a class="nav-link" href="/wf/workflowcenter" style="font-weight:bold;" id="wf">流程定义<span class="sr-only">(current)</span></a>
	            </li>
	            <li class="nav-item" >
	              <a class="nav-link" href="/tb/tablecenter" style="font-weight:bold;" id="tb">表单管理</a>
	            </li>
	            <li class="nav-item" >
	              <a class="nav-link" href="/em/elementcenter" style="font-weight:bold;" id="em">元素库</a>
	            </li>
	            <li class="nav-item" >
	              <a class="nav-link" href="/sys/syscenter" style="font-weight:bold;" id="em"><span style="font-size:15px;color:blue;">✋</span>环境配置</a>
	            </li>  
	            <li class="nav-item" >
	              <a class="nav-link" href="/app/actualcenter" style="font-weight:bold;" id="em">流程实战</a>
	            </li>                         
	          </ul>
          </sec:authorize>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/logout" style="color:#a92525;font-weight:bold">登出</a></li>	            
          </ul>          
       </div>       
    </nav>
	</header>
    <hr />
    <sitemesh:write property="body" />
    <footer class="footer">	   
        <span style="color:#b77c0cb3">@WF2.0</span>
	</footer>
</body>
</html>