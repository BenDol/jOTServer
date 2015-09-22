<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<stripes:layout-render name="/WEB-INF/template/default/main.jsp">
    <stripes:layout-component name="title"><fmt:message key="/Login.action.title"/></stripes:layout-component>
    <stripes:layout-component name="contents">
        <h2><fmt:message key="/Login.action.title" /></h2>
        <p><stripes:messages/></p>
        <stripes:form beanclass="org.jotserver.web.LoginActionBean" method="post" focus="">
            <table>
                <tr>
                    <td colspan="2">
                        <stripes:errors />
                    </td>
                </tr>
                <tr>
                    <td><stripes:label for="number" />:</td>
                    <td><stripes:text name="number" /></td>
                </tr>
                <tr>
                    <td><stripes:label for="password" />:</td>
                    <td><stripes:password name="password" /></td>
                </tr>
                <tr>
                    <td colspan="2"><stripes:submit name="login" /></td>
                </tr>
            </table>
        </stripes:form>
        <p>
            <stripes:link beanclass="org.jotserver.web.CreateAccountActionBean"><fmt:message key="/CreateAccount.action.title" /></stripes:link>
        </p>
    </stripes:layout-component>
</stripes:layout-render>