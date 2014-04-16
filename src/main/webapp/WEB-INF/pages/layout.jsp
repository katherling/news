<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<script src='<c:url value="/resources/jquery-2.1.0.js" />'></script>
<link href='<c:url value="/resources/select2-3.4.5/select2.css" />' rel="stylesheet" />
<script src='<c:url value="/resources/select2-3.4.5/select2.js" />'></script>

<link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap-slate.css"/>" />

<link rel="stylesheet" href="<c:url value="/resources/select2-bootstrap.css"/>" />
<script src='<c:url value="/resources/bootstrap/js/bootstrap.js" />'></script>
<link href='<c:url value="/resources/site.css"/>' rel="stylesheet" />
</head>
<body>
<hr />
<div class="row">

	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<a class="navbar-brand" href="<c:url value="/"/>#"><s:message code="news" /></a>
		<div class="container">
			
			<tiles:insertAttribute name="language-select" />
			
			<sec:authorize ifAnyGranted="ROLE_ADMIN">
				<a class="btn btn-default navbar-btn" href="<c:url value="/edit" />">
					<span class="glyphicon glyphicon-plus"></span>
					<s:message code="add" />
				</a>
			</sec:authorize>
			
			<tiles:insertAttribute name="login" />
		</div>
	</nav>
</div>
<br />
<div class="row">
	<div id="content" class="col-md-10 col-md-offset-1">
	<tiles:insertAttribute name="body" />
	</div>
</div>
</body>
</html>