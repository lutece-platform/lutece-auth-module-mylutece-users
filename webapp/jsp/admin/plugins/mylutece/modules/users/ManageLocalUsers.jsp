<jsp:useBean id="myluteceusersmanagementLocalUser" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.users.web.LocalUserJspBean" />
<% String strContent = myluteceusersmanagementLocalUser.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
