<jsp:useBean id="myluteceusersmanagementLocalUserRole" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.users.web.LocalUserRoleJspBean" />
<% String strContent = myluteceusersmanagementLocalUserRole.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
