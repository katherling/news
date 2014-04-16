<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty param.error}">
	<font color="red"> <spring:message code="loginerror"/>
		 ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
	</font>
</c:if>
<s:url var="authUrl" value="/j_spring_security_check" />
<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
	<form method="POST" class="navbar-form navbar-right" action="${authUrl}">
		<div class="form-group">
			<input type="text" id="login" name="j_username" class="form-control" placeholder="<s:message code="login" />" /> 
		</div>
		<div class="form-group">
			<input type="password" id="password" name="j_password" class="form-control" placeholder="<s:message code="password" />" /> 
		</div>
		<div class="form-group">
			<label for="remember"><s:message code="rememberMe" /></label> 
			<input type="checkbox" id="remember" name="_spring_security_remember_me" />
		</div> 
		<div class="form-group">
			<button type="submit" class="btn btn-default" >
				<span class="glyphicon glyphicon-log-in"></span>
				<s:message code="logInLabel" />
			</button>
		</div>
	</form>
</sec:authorize>
<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
	<div class="pull-right">
		<p class="navbar-text">
			<sec:authentication property="principal.username" />
		</p>
		<a class="btn btn-default navbar-btn" href=<c:url value="/logout" />>
			<span class="glyphicon glyphicon-log-out"></span>
			<s:message code="logout" />
		</a>
	</div>
</sec:authorize>