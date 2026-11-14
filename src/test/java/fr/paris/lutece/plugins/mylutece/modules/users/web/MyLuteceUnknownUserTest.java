/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceSearchUserHome;
import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * The screens and the actions of a user whose id is unknown, missing or not a number go back to the list of the users
 * with an error message.
 */
public class MyLuteceUnknownUserTest extends LuteceTestCase
{
    private static final String RIGHT = "MYLUTECE_USERS_MANAGEMENT";
    private static final String SERVLET_PATH_ROLES = "/jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceUserRoles.jsp";
    private static final String SERVLET_PATH_USERS = "/jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceSearchUsers.jsp";
    private static final String PARAMETER_ID_ROLES = "id_mylutecesearchuser";
    private static final String PARAMETER_ID_USERS = "id";
    private static final String UNKNOWN_ID = "999999";
    private static final String NOT_A_NUMBER = "abc";
    private static final String USERS_LIST = "ManageMyLuteceSearchUsers.jsp?view=manageMyLuteceSearchUsers";

    @Inject
    private MyLuteceUserRoleJspBean _roleJspBean;
    @Inject
    private MyLuteceSearchUserJspBean _userJspBean;
    @Inject
    private Models _models;

    /**
     * Keeps the URL of the redirection, which the mock response does not keep
     */
    private static final class RedirectResponse extends HttpServletResponseWrapper
    {
        private String _strLocation;

        RedirectResponse( )
        {
            super( new MockHttpServletResponse( ) );
        }

        @Override
        public void sendRedirect( String strLocation ) throws IOException
        {
            _strLocation = strLocation;
        }

        String getLocation( )
        {
            return _strLocation;
        }
    }

    private static MockHttpServletRequest newRequest( String strMethod, String strServletPath )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( strMethod );
        request.setServletPath( strServletPath );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RIGHT );
        return request;
    }

    /**
     * Calls the controller and checks that it redirects to the list of the users with an error message.
     */
    private void assertBackToTheListWithAnError( ControllerCall caller, MockHttpServletRequest request ) throws AccessDeniedException
    {
        RedirectResponse response = new RedirectResponse( );
        assertNull( caller.call( request, response ), "The page is shown instead of a redirection" );
        String strLocation = response.getLocation( );
        assertTrue( strLocation != null, "No redirection" );
        assertTrue( strLocation.contains( USERS_LIST ), strLocation );
        Set<?> errors = _models.get( MVCUtils.MARK_ERRORS, Set.class );
        assertTrue( errors != null && !errors.isEmpty( ), "No error message" );
    }

    @FunctionalInterface
    private interface ControllerCall
    {
        String call( MockHttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException;
    }

    private void assertRolesViewBackToTheList( String strId ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newRequest( "GET", SERVLET_PATH_ROLES );
        request.addParameter( "view", "manageMyLuteceUserRoles" );
        if ( strId != null )
        {
            request.addParameter( PARAMETER_ID_ROLES, strId );
        }
        assertBackToTheListWithAnError( _roleJspBean::processController, request );
    }

    private void assertModifyViewBackToTheList( String strId ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newRequest( "GET", SERVLET_PATH_USERS );
        request.addParameter( "view", "modifyMyLuteceSearchUser" );
        if ( strId != null )
        {
            request.addParameter( PARAMETER_ID_USERS, strId );
        }
        assertBackToTheListWithAnError( _userJspBean::processController, request );
    }

    @Test
    public void testRolesOfAnUnknownUser( ) throws AccessDeniedException
    {
        assertRolesViewBackToTheList( UNKNOWN_ID );
    }

    @Test
    public void testRolesWithAnIdThatIsNotANumber( ) throws AccessDeniedException
    {
        assertRolesViewBackToTheList( NOT_A_NUMBER );
    }

    @Test
    public void testRolesWithoutId( ) throws AccessDeniedException
    {
        assertRolesViewBackToTheList( null );
    }

    @Test
    public void testAssignRolesToAnUnknownUser( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newRequest( "POST", SERVLET_PATH_ROLES );
        request.addParameter( "action", "assignMyLuteceUserRole" );
        request.addParameter( PARAMETER_ID_ROLES, UNKNOWN_ID );
        assertBackToTheListWithAnError( _roleJspBean::processController, request );
    }

    @Test
    public void testAssignRolesToAKnownUserStillWorks( ) throws AccessDeniedException
    {
        MyLuteceSearchUser user = new MyLuteceSearchUser( );
        user.setLogin( "unknown_user_test" );
        user.setGivenName( "Given" );
        user.setLastName( "Last" );
        user.setProviderUserId( "unknown_user_test" );
        MyLuteceSearchUserHome.create( user );
        try
        {
            MockHttpServletRequest request = newRequest( "POST", SERVLET_PATH_ROLES );
            request.addParameter( "action", "assignMyLuteceUserRole" );
            request.addParameter( PARAMETER_ID_ROLES, String.valueOf( user.getId( ) ) );
            RedirectResponse response = new RedirectResponse( );
            assertNull( _roleJspBean.processController( request, response ) );
            String strLocation = response.getLocation( );
            assertTrue( strLocation != null, "No redirection" );
            assertTrue( strLocation.contains( "ManageMyLuteceUserRoles.jsp?view=manageMyLuteceUserRoles&" + PARAMETER_ID_ROLES + "=" + user.getId( ) ),
                    strLocation );
        }
        finally
        {
            MyLuteceSearchUserHome.remove( user.getId( ) );
        }
    }

    @Test
    public void testModifyAnUnknownUser( ) throws AccessDeniedException
    {
        assertModifyViewBackToTheList( UNKNOWN_ID );
    }

    @Test
    public void testModifyWithAnIdThatIsNotANumber( ) throws AccessDeniedException
    {
        assertModifyViewBackToTheList( NOT_A_NUMBER );
    }

    @Test
    public void testModifyWithoutId( ) throws AccessDeniedException
    {
        assertModifyViewBackToTheList( null );
    }

    @Test
    public void testSaveAnUnknownUser( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newRequest( "POST", SERVLET_PATH_USERS );
        request.addParameter( "action", "modifyMyLuteceSearchUser" );
        request.addParameter( PARAMETER_ID_USERS, UNKNOWN_ID );
        request.addParameter( "login", "login" );
        request.addParameter( "given_name", "Given" );
        request.addParameter( "last_name", "Last" );
        request.addParameter( "provider_user_id", "provider" );
        assertBackToTheListWithAnError( _userJspBean::processController, request );
    }
}
