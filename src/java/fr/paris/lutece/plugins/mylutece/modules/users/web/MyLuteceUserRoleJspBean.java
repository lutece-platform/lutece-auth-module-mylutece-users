/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mylutece.modules.users.web;

import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceSearchUserHome;
import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceUserRole;
import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceUserRoleHome;
import fr.paris.lutece.plugins.mylutece.modules.users.service.MyLuteceUserRoleService;
import fr.paris.lutece.plugins.mylutece.service.RoleResourceIdService;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class provides the user interface to manage MyLuteceUserRole features ( manage, create, modify, remove )
 */
@SessionScoped
@Named
@Controller( controllerJsp = "ManageMyLuteceUserRoles.jsp", controllerPath = "jsp/admin/plugins/mylutece/modules/users/", right = "MYLUTECE_USERS_MANAGEMENT", securityTokenEnabled = true )
public class MyLuteceUserRoleJspBean extends AbstractmyLuteceUsersManagementJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = -779120944936133468L;
    // Templates
    private static final String TEMPLATE_MANAGE_SEARCHUSERROLES = "/admin/plugins/mylutece/modules/users/manage_user_roles.html";
    // Parameters
    private static final String PARAMETER_ID_MYLUTECESEARCHUSER_ID = "id_mylutecesearchuser";
    private static final String PARAMETER_ROLE_KEY = "role_key";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SEARCHUSERROLES = "module.mylutece.users.manageRoles.pageTitle";
    // Markers
    private static final String MARK_MYLUTECESEARCHUSERROLE_LIST = "mylutece_search_user_role_list";
    private static final String MARK_MYLUTECE_ROLE_LIST = "mylutece_role_list";
    private static final String MARK_MYLUTECESEARCHUSER = "mylutece_search_user";
    private static final String MARK_MYLUTECESEARCHUSER_ID = "id_mylutecesearchuser";
    // Views
    private static final String VIEW_MANAGE_SEARCHUSERROLES = "manageMyLuteceUserRoles";
    // Actions
    private static final String ACTION_ASSIGN_SEARCHUSERROLE = "assignMyLuteceUserRole";
    // Jsp
    private static final String JSP_MANAGE_SEARCHUSERS = "ManageMyLuteceSearchUsers.jsp";
    private static final String VIEW_MANAGE_SEARCHUSERS = "manageMyLuteceSearchUsers";
    // Infos
    private static final String INFO_SEARCHUSERROLE_CREATED = "module.mylutece.users.info.roles.updated";
    private static final String ERROR_SEARCHUSER_NOT_FOUND = "module.mylutece.users.error.userNotFound";
    // Session variable to store working values
    private MyLuteceSearchUser _myLuteceSearchUser;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_SEARCHUSERROLES, defaultView = true, securityTokenAction = ACTION_ASSIGN_SEARCHUSERROLE )
    public String getManageMyLuteceUserRoles( HttpServletRequest request )
    {
        _myLuteceSearchUser = findSearchUser( request );
        if ( _myLuteceSearchUser == null )
        {
            return redirectToUserList( request );
        }
        int nUserId = _myLuteceSearchUser.getId( );
        Collection<Role> myLuteceAuthorizedRoleCollection = RBACService.getAuthorizedCollection( RoleHome.findAll( ),
                RoleResourceIdService.PERMISSION_ASSIGN_ROLE, AdminUserService.getAdminUser( request ) );
        List<Role> myLuteceAuthorizedRoleList = myLuteceAuthorizedRoleCollection.stream( ).collect( Collectors.toList( ) );
        Map<String, Object> model = getModel( );
        model.put( MARK_MYLUTECE_ROLE_LIST, myLuteceAuthorizedRoleList );
        model.put( MARK_MYLUTECESEARCHUSERROLE_LIST, getSearchUserAuthorizedRoleList( myLuteceAuthorizedRoleList, nUserId ) );
        model.put( MARK_MYLUTECESEARCHUSER, _myLuteceSearchUser );
        model.put( MARK_MYLUTECESEARCHUSER_ID, String.valueOf( nUserId ) );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SEARCHUSERROLES, TEMPLATE_MANAGE_SEARCHUSERROLES, model );
    }

    /**
     * Process the data capture form of a new searchuserrole
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_ASSIGN_SEARCHUSERROLE )
    public String doAssignMyLuteceUserRole( HttpServletRequest request )
    {
        _myLuteceSearchUser = findSearchUser( request );
        if ( _myLuteceSearchUser == null )
        {
            return redirectToUserList( request );
        }
        int nSearchUserId = _myLuteceSearchUser.getId( );
        String [ ] arrKeys = request.getParameterValues( PARAMETER_ROLE_KEY );
        List<String> myLuteceRoleKeysList = ( arrKeys != null ) ? Arrays.asList( arrKeys ) : new ArrayList<>( );
        MyLuteceUserRoleService.doSearchUserAssignements( myLuteceRoleKeysList, nSearchUserId );
        addInfo( INFO_SEARCHUSERROLE_CREATED, getLocale( ) );
        return redirect( request, VIEW_MANAGE_SEARCHUSERROLES, PARAMETER_ID_MYLUTECESEARCHUSER_ID, nSearchUserId );
    }

    /**
     * Find the user whose id is in the request
     *
     * @param request
     *            The Http request
     * @return the user, or null if the id is missing, not a number or unknown
     */
    private static MyLuteceSearchUser findSearchUser( HttpServletRequest request )
    {
        int nUserId = NumberUtils.toInt( request.getParameter( PARAMETER_ID_MYLUTECESEARCHUSER_ID ), -1 );

        return ( nUserId < 0 ) ? null : MyLuteceSearchUserHome.findByPrimaryKey( nUserId );
    }

    /**
     * Go back to the list of the users with the message that the user is not found
     *
     * @param request
     *            The Http request
     * @return The redirection result
     */
    private String redirectToUserList( HttpServletRequest request )
    {
        addError( ERROR_SEARCHUSER_NOT_FOUND, getLocale( ) );
        UrlItem url = new UrlItem( JSP_MANAGE_SEARCHUSERS );
        url.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MANAGE_SEARCHUSERS );

        return redirect( request, url.getUrl( ) );
    }

    /**
     * Get local user authorized role list from mylutece role list.
     *
     * @param myLuteceAuthorizedRoleList
     *            The mylutece role list
     * @param nUserId
     *            The user id
     * @return The Local user role list
     */
    public Collection<Role> getSearchUserAuthorizedRoleList( List<Role> myLuteceAuthorizedRoleList, int nUserId )
    {
        List<MyLuteceUserRole> currentmyLuteceSearchUserRoleList = MyLuteceUserRoleHome.getMyLuteceUserRolesListByUserId( nUserId );
        Collection<Role> myLuteceSearchUserRoleList = new ArrayList<>( );
        for ( MyLuteceUserRole myLuteceSearchUserRole : currentmyLuteceSearchUserRoleList )
        {
            for ( Role role : myLuteceAuthorizedRoleList )
            {
                String roleKey = myLuteceSearchUserRole.getRoleKey( );
                if ( role.getRole( ).equals( roleKey ) )
                {
                    myLuteceSearchUserRoleList.add( RoleHome.findByPrimaryKey( roleKey ) );
                    break;
                }
            }
        }
        return myLuteceSearchUserRoleList;
    }
}
