<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<stripes:layout-render name="/WEB-INF/template/default/main.jsp">
    <stripes:layout-component name="title"><fmt:message key="/Index.action.title"/></stripes:layout-component>
    <stripes:layout-component name="contents">
        <h2><fmt:message key="/Index.action.title" /></h2>
        <p><stripes:messages/></p>
        <p>
            <stripes:link beanclass="org.jotserver.web.CreateAccountActionBean"><fmt:message key="/CreateAccount.action.title" /></stripes:link>
            <br />
            <stripes:link beanclass="org.jotserver.web.LoginActionBean"><fmt:message key="/Login.action.title" /></stripes:link>
        </p>

    </stripes:layout-component>
</stripes:layout-render>