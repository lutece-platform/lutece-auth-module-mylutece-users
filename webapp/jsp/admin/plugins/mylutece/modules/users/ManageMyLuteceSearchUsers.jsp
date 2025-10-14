<jsp:useBean id="myluteceusersmanagementMyLuteceSearchUser" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.users.web.MyLuteceSearchUserJspBean" />
<% String strContent = myluteceusersmanagementMyLuteceSearchUser.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
