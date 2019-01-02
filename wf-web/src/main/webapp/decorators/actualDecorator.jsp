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
<!-- jquery 弹出dialog -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
</head>
<script type="text/javascript">
</script>
<body>   
  <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
  <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">流程实战 <span style="cursor:pointer;color:red;font-size:1.7rem;font-weight:bold;" onclick="location.href='/cf/center'">⇪</span></a>  
  </nav>
  <div class="container-fluid">
  <div class="row" style="padding-left:0px;position:absolute;bottom:0px;height:94%;width:100%;">
    <nav class="col-md-2 d-none d-md-block sidebar" style="background-color:#249024;">
      <div class="sidebar-sticky">
        <ul class="nav flex-column">
          <c:forEach items="${wfs}" varStatus="i" step="1" var="workflow" >
          	<li class="nav-item" style="padding-top:15px;" onclick="location.href='/app/list/${workflow.wfId}'" > 
          		<span class="menu-icon" >&nbsp;✍&nbsp;</span> 
          		<label <c:if test="${wf.wfId == workflow.wfId}">class="menu-sel-font"</c:if> style="color: white;cursor:pointer">&nbsp;${workflow.wfName }</label> 
          		<hr style="margin-top: 0.5rem; margin-bottom: 0.5rem;"></hr>                            
            </li>
          </c:forEach>          
        </ul>
      </div>
    </nav>
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">   
    	<sitemesh:write property="body" />
    </main>
  </div>
  </div>
</body>
</html>