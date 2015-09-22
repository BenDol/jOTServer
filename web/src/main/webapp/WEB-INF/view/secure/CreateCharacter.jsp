<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<stripes:layout-render name="/WEB-INF/template/default/main.jsp">
    <stripes:layout-component name="title"><fmt:message key="/secure/CreateCharacter.action.title"/></stripes:layout-component>
    <stripes:layout-component name="contents">
        <h2><fmt:message key="/secure/CreateCharacter.action.title" /></h2>
        <p><stripes:messages/></p>
        <stripes:form beanclass="org.jotserver.web.secure.CreateCharacterActionBean" method="post" focus="">
            <table>
                <tr>
                    <td colspan="2">
                        <stripes:errors />
                    </td>
                </tr>
                <tr>
                    <td><stripes:label for="name" />:</td>
                    <td><stripes:text name="name" /></td>
                </tr>
                <tr>
                    <td><stripes:label for="world" />:</td>
                    <td>
                        <stripes:select name="world">
                            <stripes:options-collection collection="${actionBean.context.config.gameWorldAccessor.gameWorlds}" value="identifier" label="name"/>
                        </stripes:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><stripes:submit name="create" /></td>
                </tr>
            </table>
        </stripes:form>
    </stripes:layout-component>
</stripes:layout-render>