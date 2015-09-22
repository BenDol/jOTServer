<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<stripes:layout-render name="/WEB-INF/template/default/main.jsp">
    <stripes:layout-component name="title"><fmt:message key="/secure/Index.action.title"/></stripes:layout-component>
    <stripes:layout-component name="contents">
        <h2><fmt:message key="/secure/Index.action.title" /></h2>
        <p><stripes:messages/></p>
        <p>
            <ul>
                <c:if test="${empty(actionBean.players)}"><fmt:message key="/secure/Index.action.characters.none"/></c:if>
                <c:forEach items="${actionBean.players}" var="it">
                    <li><b>${it.name}</b> <i>(${it.world.name})</i></li>
                </c:forEach>
            </ul>
        </p>
        <p>
            <stripes:link beanclass="org.jotserver.web.secure.CreateCharacterActionBean"><fmt:message key="/secure/CreateCharacter.action.title" /></stripes:link>
            <br />
            <stripes:link beanclass="org.jotserver.web.secure.ChangePasswordActionBean"><fmt:message key="/secure/ChangePassword.action.title" /></stripes:link>
            <br />
            <stripes:link beanclass="org.jotserver.web.secure.LogoutActionBean"><fmt:message key="/secure/Logout.action.title" /></stripes:link>
        </p>
    </stripes:layout-component>
</stripes:layout-render>