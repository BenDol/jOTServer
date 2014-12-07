<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<stripes:layout-definition>

﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="<fmt:message key="/Template.meta.description" />" />
		<meta name="keywords" content="<fmt:message key="/Template.meta.keywords" />" />
		<meta name="author" content="<fmt:message key="/Template.meta.author" />" />
		<link rel="stylesheet" type="text/css" href="<stripes:url value="/template/default/style.css" />"/>
		<link rel="stylesheet" type="text/css" href="<stripes:url value="/template/default/custom.css" />"/>
		<title>
			<fmt:message key="/Template.title.prefix" />
			<stripes:layout-component name="title">
				<fmt:message key="/Template.title" />
			</stripes:layout-component>
		</title>
		<stripes:layout-component name="htmlhead"/>
	</head>
	<body style="background: #f4f4f4 url(<stripes:url value="/template/default/images/bg.gif" />) top center repeat-y;">
		<div id="wrap">
			
			<div id="header">
				<h1><fmt:message key="/Template.title" /></h1>
				<p><strong>"<fmt:message key="/Template.quote" />"</strong></p>
			</div>
			
			<img id="frontphoto" src="<stripes:url value="/template/default/images/front.jpg" />" width="760" height="175" alt="" />
			
			<div id="avmenu">
				<h2 class="hide"><fmt:message key="/Template.menu" />:</h2>
				<ul>
					<li><stripes:link href="/Index.action"><fmt:message key="/Index.action.title" /></stripes:link></li>
					<c:if test="${actionBean.context.loggedIn == false}">
						<li><stripes:link beanclass="org.jotserver.web.CreateAccountActionBean"><fmt:message key="/CreateAccount.action.title" /></stripes:link></li>
						<li><stripes:link beanclass="org.jotserver.web.LoginActionBean"><fmt:message key="/Login.action.title" /></stripes:link></li>
					</c:if>
					<c:if test="${actionBean.context.loggedIn == true}">
						<li><stripes:link beanclass="org.jotserver.web.secure.IndexActionBean"><fmt:message key="/secure/Index.action.title" /></stripes:link></li>
						<li><stripes:link beanclass="org.jotserver.web.secure.CreateCharacterActionBean"><fmt:message key="/secure/CreateCharacter.action.title" /></stripes:link></li>
						<li><stripes:link beanclass="org.jotserver.web.secure.LogoutActionBean"><fmt:message key="/secure/Logout.action.title" /></stripes:link></li>
					</c:if>
				</ul>
				
				<div class="announce">
					<h3><fmt:message key="/Template.news.latest" />:</h3>
					<p><strong>August 14, 2008:</strong><br />
						New design and layout from OSWD.</p>
					<p class="textright"><stripes:link href="/News.action"><fmt:message key="/Template.news.more" />...</stripes:link></p>
				</div>
				
			</div>
			
			<div id="extras">
				<h3><fmt:message key="/Template.serverinfo.title" />:</h3>
				<table>
					<tr>
						<th colspan="2"><fmt:message key="/Template.serverinfo.host" />: </th>
					</tr>
					<tr>
						<td colspan="2">jiddoserv.game-server.cc</td>
					</tr>
					<tr>
						<th><fmt:message key="/Template.serverinfo.port" />: </th><td>7171</td>
					</tr>
					<tr>
						<th><fmt:message key="/Template.serverinfo.client" />: </th><td>8.11</td>
					</tr>
				</table>
			</div>
			
			<div id="content">
				
				<stripes:layout-component name="contents"/>
			</div>
			<div id="footer">
				<fmt:message key="/Template.footer" />
			</div>
		</div>
	</body>
</html>
</stripes:layout-definition>
