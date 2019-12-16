/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUser;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserHome;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRole;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRoleHome;
import fr.paris.lutece.plugins.mylutece.modules.users.service.LocalUserRoleService;
import fr.paris.lutece.plugins.mylutece.service.RoleResourceIdService;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage LocalUserRole features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageLocalUserRoles.jsp", controllerPath = "jsp/admin/plugins/mylutece/modules/users/", right = "MYLUTECE_USERS_MANAGEMENT" )
public class LocalUserRoleJspBean extends AbstractmyLuteceUsersManagementJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = -779120944936133468L;
    // Templates
    private static final String TEMPLATE_MANAGE_LOCALUSERROLES = "/admin/plugins/mylutece/modules/users/manage_localuserroles.html";
    // Parameters
    private static final String PARAMETER_ID_LOCALUSER_ID = "id_localuser";
    private static final String PARAMETER_ROLE_KEY = "role_key";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_LOCALUSERROLES = "module.mylutece.users.manage_localuserroles.pageTitle";
    // Markers
    private static final String MARK_LOCALUSERROLE_LIST = "localuserrole_list";
    private static final String MARK_MYLUTECE_ROLE_LIST = "mylutece_role_list";
    private static final String MARK_LOCALUSER = "localuser";
    private static final String MARK_LOCALUSER_ID = "id_localuser";
    // Views
    private static final String VIEW_MANAGE_LOCALUSERROLES = "manageLocalUserRoles";
    // Actions
    private static final String ACTION_ASSIGN_LOCALUSERROLE = "assignLocalUserRole";
    // Infos
    private static final String INFO_LOCALUSERROLE_CREATED = "module.mylutece.users.info.localuserrole.created";
    // Session variable to store working values
    private LocalUser _localUser;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_LOCALUSERROLES, defaultView = true )
    public String getManageLocalUserRoles( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_ID_LOCALUSER_ID );
        int nUserId = Integer.parseInt( strUserId );
        _localUser = LocalUserHome.findByPrimaryKey( nUserId );
        Collection<Role> myLuteceAuthorizedRoleCollection = RBACService.getAuthorizedCollection( RoleHome.findAll( ),
                RoleResourceIdService.PERMISSION_ASSIGN_ROLE, AdminUserService.getAdminUser( request ) );
        List<Role> myLuteceAuthorizedRoleList = myLuteceAuthorizedRoleCollection.stream( ).collect( Collectors.toList( ) );
        Map<String, Object> model = getModel( );
        model.put( MARK_MYLUTECE_ROLE_LIST, myLuteceAuthorizedRoleList );
        model.put( MARK_LOCALUSERROLE_LIST, getLocalUserAuthorizedRoleList( myLuteceAuthorizedRoleList, nUserId ) );
        model.put( MARK_LOCALUSER, _localUser );
        model.put( MARK_LOCALUSER_ID, strUserId );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_LOCALUSERROLES, TEMPLATE_MANAGE_LOCALUSERROLES, model );
    }

    /**
     * Process the data capture form of a new localuserrole
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_ASSIGN_LOCALUSERROLE )
    public String doAssignLocalUserRole( HttpServletRequest request )
    {
        int nLocalUserId = _localUser.getId( );
        String [ ] arrKeys = request.getParameterValues( PARAMETER_ROLE_KEY );
        List<String> myLuteceRoleKeysList = ( arrKeys != null ) ? Arrays.asList( arrKeys ) : new ArrayList<>( );
        LocalUserRoleService.doLocalUserAssignements( myLuteceRoleKeysList, nLocalUserId );
        addInfo( INFO_LOCALUSERROLE_CREATED, getLocale( ) );
        return redirect( request, VIEW_MANAGE_LOCALUSERROLES, PARAMETER_ID_LOCALUSER_ID, nLocalUserId );
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
    public Collection<Role> getLocalUserAuthorizedRoleList( List<Role> myLuteceAuthorizedRoleList, int nUserId )
    {
        List<LocalUserRole> currentlocalUserRoleList = LocalUserRoleHome.getLocalUserRolesListByUserId( nUserId );
        Collection<Role> localUserRoleList = new ArrayList<>( );
        for ( LocalUserRole localUserRole : currentlocalUserRoleList )
        {
            for ( Role role : myLuteceAuthorizedRoleList )
            {
                String roleKey = localUserRole.getRoleKey( );
                if ( role.getRole( ).equals( roleKey ) )
                {
                    localUserRoleList.add( RoleHome.findByPrimaryKey( roleKey ) );
                    break;
                }
            }
        }
        return localUserRoleList;
    }
}
